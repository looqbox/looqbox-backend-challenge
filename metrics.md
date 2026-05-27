# 📡 Métricas e observabilidade

Neste projeto foram criados enpoints para acesso a métricas de observabilidade 

## 🔧 Spring Boot Actuator

O projeto inclui **`spring-boot-starter-actuator`**. Spring Boot Actuator expõe endpoints HTTP para verificar saúde, métricas e informação da aplicação em execução

| Endpoint | Função |
|----------|--------|
| **`GET /actuator/health`** | Indica se a aplicação está **de pé** e, Serve para **verificação automática** de saúde, não para métricas de negócio. |
| **`GET /actuator/info`** | Devolve **metadados opcionais** da aplicação (versão, git, propriedades customizadas em `info.*` no YAML). Útil para identificar **que build** está a correr. |
| **`GET /actuator/metrics`** | Lista **nomes de métricas** registadas no Micrometer (JVM, CPU, HTTP do servidor, etc.) e permite **consultar uma métrica** com `GET /actuator/metrics/{nome}` (ex.: uso de memória). Serve para **observabilidade técnica** da JVM e do stack Spring. |

Exemplos:

```http
GET http://localhost:8080/actuator/health
GET http://localhost:8080/actuator/info
GET http://localhost:8080/actuator/metrics
GET http://localhost:8080/actuator/metrics/jvm.memory.used
```

### 🌐 Métricas HTTP da app de negócio (`http.server.requests`)

A métrica **`http.server.requests`** (Micrometer) regista **cada pedido HTTP** que o servidor WebFlux trata: método, URI (normalizada), código de estado e tempos. Importante distinguir:

- **Endpoints de negócio** do desafio — **`GET /pokemons`** e **`GET /pokemons/highlight`** — **só passam a aparecer** nesta métrica **depois de alguém os chamar** (não há dados “simulados” no arranque). Se ainda não houve tráfego a esses caminhos, não verás séries para eles.
- **Outros pedidos HTTP** contam na mesma métrica: por exemplo **`GET /actuator/health`**, **`GET /actuator/metrics`** ou **`GET /internal/cache-catalog/metrics`**. O primeiro pedido que fizeres ao Actuator **já é** um pedido HTTP e pode criar ou atualizar séries para esses URIs.
- **Métricas JVM** (`jvm.*`, `process.cpu.usage`, etc.) **existem desde o arranque** e refletem o processo inteiro, **independentemente** de haver chamadas a `/pokemons`.

### 📈 Cinco métricas Micrometer prioritárias

Consulta cada uma com `GET /actuator/metrics/{nome}`. Os nomes são os habituais do Spring Boot 3 + Micrometer; a resposta inclui `availableTags` para filtrar (ex.: `uri`, `status`, `method` em HTTP).

| Métrica | Porque importa nesta API |
|---------|---------------------------|
| **`http.server.requests`** | Mede **pedidos HTTP** ao servidor (contagem, tempo total, máximo). Permite ver **tráfego** e **latência** nos endpoints (`/pokemons`, `/pokemons/highlight`, `/internal/...`). |
| **`jvm.memory.used`** | Indica **memória heap (e outras áreas)** em uso. Esta aplicação guarda a **lista completa de nomes** em cache e cria cópias para ordenação; picos ou crescimento contínuo ajudam a detetar **pressão de memória** ou fugas. |
| **`process.cpu.usage`** | Uso de **CPU do processo** (valor normalizado, ex. 0–1). Ordenação **O(k log k)** no caso médio (quicksort) e filtro sobre muitos nomes consomem CPU; valores altos sob carga indicam gargalo de processamento por pedido. |
| **`jvm.gc.pause`** | **Tempos de pausa do GC** (coleções). Com heap ocupado pelo catálogo, pausas longas ou frequentes explicam **picos de latência** mesmo com respostas corretas; útil para correlacionar com `http.server.requests`. |
| **`logback.events`** (tag `level=error`) | Conta **eventos de log a nível ERROR** (e outras tags como `logger`). Picos coincidem frequentemente com **falhas de integração** ou erros não tratados; complementa o endpoint interno do cache, que não substitui o diagnóstico de exceções. |

Exemplos de como utilizar as requisições acima:

```http
GET http://localhost:8080/actuator/metrics/http.server.requests
GET http://localhost:8080/actuator/metrics/jvm.memory.used
GET http://localhost:8080/actuator/metrics/process.cpu.usage
GET http://localhost:8080/actuator/metrics/jvm.gc.pause
GET http://localhost:8080/actuator/metrics/logback.events?tag=level:error
```

## 🗃️ Snapshot do cache (endpoint para observabilidade do cache)

O endpoint é utilizado para observabilidade do cache armazenado

| Endpoint | Função |
|----------|--------|
| **`GET /internal/cache-catalog/metrics`** | Devolve **só métricas** do cache em memória da lista completa de nomes da PokéAPI: se existe entrada, quantos nomes, se o TTL estáexpirado, idade da entrada e o valor do TTL configurado. **Não** devolve a lista de nomes. Destina-se a **operações** (debug, scripts, suporte). |

Exemplo:

```http
GET http://localhost:8080/internal/cache-catalog/metrics
```

Campos do JSON:

| Campo | Função |
|-------|--------|
| `hasEntry` | Indica se existe alguma entrada guardada no cache. |
| `nameCount` | Quantidade de nomes na entrada atual. |
| `expired` | Indica se a entrada já ultrapassou o TTL (`pokemon.catalog-cache-ttl-millis`). |
| `entryAgeMillis` | Tempo em milissegundos desde a última gravação no cache, ou `null` se não houver entrada. |
| `catalogCacheTtlMillis` | Cópia do TTL configurado, para interpretar `expired` sem consultar o YAML. |

Exemplo de corpo (valores ilustrativos):

```json
{
  "hasEntry": true,
  "nameCount": 1302,
  "expired": false,
  "entryAgeMillis": 120000,
  "catalogCacheTtlMillis": 3600000
}
```



