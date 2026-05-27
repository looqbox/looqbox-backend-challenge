# 🏗️ Arquitetura — Pokémon API

Microserviço que expõe busca e ordenação sobre nomes vindos da PokéAPI.

## 🛠️ Tecnologias

- **Kotlin**, **Spring Boot 3**, **WebFlux** (Netty, `Mono`/`Flux`, **Reactor**), **Jackson**, **Gradle**, **Docker**, **Actuator**, **Springdoc** (OpenAPI), testes com **JUnit 5**.
- Integração externa: **WebClient**.
- Tarefas agendadas só com **`ScheduledExecutorService`**;
- **cache em RAM** manual, sem libs de cache.

## 📌 Diagrama estrutural do fluxo de informação



```
┌──────────────────────────┐
│         PokéAPI          │
│   (origem HTTP/JSON)     │
└────────────┬─────────────┘
             │ corpo JSON → lista de nomes
             ▼
┌──────────────────────────┐
│   Integração HTTP        │
│  PokeApiClient (WebFlux) │
└────────────┬─────────────┘
             │ Mono com nomes normalizados
             ▼
┌──────────────────────────┐
│   Cache manual em RAM    │
│  PokemonCatalogCache      │
└────────────┬─────────────┘
             │ lista completa (hit, miss, stale)
             ▼
┌──────────────────────────┐
│      Controllers         │
│  REST WebFlux (GET)      │
└────────────┬─────────────┘
             │ parâmetros query/sort
             ▼
┌──────────────────────────┐
│        Services          │
│  fetch → filtrar → ordenar│
└────────────┬─────────────┘
             │ lista filtrada + ordenada
             ▼
┌──────────────────────────┐
│  Algoritmos & resposta    │
│  sorting + mappers (DTO) │
└────────────┬─────────────┘
             │ JSON contrato do desafio
             ▼
        Cliente HTTP
```

- A lista nasce na **PokéAPI**; não há BD local.
- O **PokeApiClient** isola URL e parsing para `List<String>`.
- O **PokemonFetchService** lê o **PokemonCatalogCache** (hit por TTL); em miss chama o cliente, faz **put** no sucesso e pode devolver **stale** se o fetch falhar mas ainda houver snapshot.
- O cache corta latência e falhas da origem.
- Os **controllers** só recebem `GET` e devolvem `Mono` de DTO; delegam em **List/Highlight SearchService**, que encadeiam fetch, **filtro** (substring, ignore case), **ordenação** e **mappers** até ao JSON do desafio.
- Os algoritmos de ordenação estão no pacote **`sorting`**.
- **Schedulers** partilham cliente e cache em intervalos fixos ou ligados ao TTL; atualizam cache ou log sem o fluxo vertical do pedido.

## 📂 Estrutura de camadas

- **`controller`**: HTTP.
- **`service`**: casos de uso (listar, highlight, snapshot de métricas do cache).
- **`client`**, **`cache`**, **`config`**, **`schedulers`**: infra.
- **`sorting`**, **`mapper`**: regras puras e forma da resposta.
- **`exception`**: erros por domínio (mapeamento HTTP futuro).
- Pacotes pequenos favorecem testes unitários rápidos em filtro, sort e mappers.

## ✅ Correlação com os princípios SOLID

- **S**: cada peça muda por uma razão (HTTP ≠ ordenar ≠ cliente externo).
- **O**: novos critérios de sort entram como classes + ramo no `PokemonNameSorter`, não mexendo nos controllers.
- **L**: sorters e `WebClient` substituíveis desde que o comportamento observado se mantenha.
- **I**: serviços com poucos métodos, sem “god class”.
- **D**: dependências injetadas; serviços não instanciam `WebClient` nem cache.


```

## 📦 Pacotes (mapa mental)

| Pacote | Papel |
|--------|--------|
| `controller` | Endpoints WebFlux; delegação a serviços. |
| `service` | Orquestração: fetch → filtrar → ordenar → mapear resposta; snapshot de métricas do cache. |
| `client` | Chamada HTTP à PokéAPI (`WebClient`). |
| `cache` | Cache em memória, thread-safe, TTL manual. |
| `sorting` | Ordenação sem APIs de sort da stdlib (quicksort manual). |
| `mapper` | Montagem de DTOs de resposta (lista simples e highlight). |
| `schedulers` | Tarefas periódicas com `ScheduledExecutorService` (sem Spring `@Scheduled`). |
| `exception` | Exceções por domínio (mapeamento futuro para HTTP). |
| `config` | Beans (`WebClient`). |

## 🎯 Decisões relevantes (resumo)

- WebFlux evita bloquear o loop na PokéAPI;
- cache manual reduz chamadas externas e permite stale;
- injeção só por construtor (desafio).

## 📚 Documentação relacionada

- [Métricas e observabilidade](metrics.md)
- [Ordenação e complexidade](sorting-algorithm.md)
- [Gargalos e mitigações](bottlenecks.md)
- [Performance](performance.md)
