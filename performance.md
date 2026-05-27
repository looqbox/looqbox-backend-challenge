# 🚀 Performance (foco no cache)

Este documento foca nas melhorias de performance e resiliência trazidas pelo **cache manual do catálogo** (`PokemonCatalogCache`) e pelos jobs de refresh. Complexidade de ordenação fica em [sorting-algorithm.md](sorting-algorithm.md); gargalos gerais em [bottlenecks.md](bottlenecks.md).

## 🎯 Objetivo do cache

Sem cache, cada request “puxa” a lista completa da PokéAPI e paga latência de rede + parsing JSON.
Com cache, a maioria dos requests vira leitura local e o custo passa a ser CPU (filtro/ordenação/serialização).
O ganho de performance vem de transformar “rede por request” em “rede por janela de TTL”.

## ⏱️ TTL e taxa de chamadas externas

- O TTL (`pokemon.catalog-cache-ttl-millis`) define a frequência máxima esperada de chamadas à PokéAPI por instância.
- Em termos práticos: quanto maior o TTL, **menos** chamadas externas; quanto menor, **mais** tráfego externo.

## 🧯 Resiliência: stale-while-revalidate

Quando existe cache mas ele está expirado, o serviço pode **servir dados stale** se o fetch falhar (evita 500 em instabilidades da PokéAPI). Em paralelo, o revalidate tenta atualizar o cache em background.

Isso troca “falhar por indisponibilidade externa” por “responder com dado possivelmente desatualizado”, o que é aceitável para catálogo de nomes.

## 🔁 Jobs de refresh (redução de cache misses)

Sem biblioteca de scheduler, os jobs usam `ScheduledExecutorService`:

- **Pré-refresh**: tenta atualizar o cache antes do TTL expirar .
- **Stale-while-revalidate**: se o cache está expirado e ainda há entrada, tenta revalidar em background.

Resultado: mais tempo em “cache quente”, menos requests pagando rede no caminho crítico.

## 📡 Como observar o cache em runtime

- `GET /internal/cache-catalog/metrics`: snapshot do cache (tem entrada, contagem, expirado, idade, TTL).
- Logs `[cache-metrics]`: mesma informação emitida periodicamente.
- Actuator `/actuator/metrics`: saúde/recursos do processo (CPU, memória, GC) e métricas HTTP.

Ver detalhes em [metrics.md](metrics.md).

## ✅ Resumo

O cache manual reduz drasticamente chamadas à PokéAPI e estabiliza a latência: com cache quente, o request deixa de ser limitado por rede e passa a ser limitado por CPU. Schedulers e stale-while-revalidate reduzem misses e aumentam disponibilidade sob falhas externas.
