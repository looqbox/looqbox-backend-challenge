# ⚡ Algoritmo de ordenação

Este projeto cumpre a restrição do desafio: **nenhuma** API de ordenação da biblioteca padrão Java/Kotlin (`Comparator`, `sortedWith`, `compareBy`, `Collections.sort`, ordenação via streams, etc.).

## 📌 Escolha do algoritmo: **quicksort**

O algoritmo escolhido é o **quicksort** implementado manualmente (`ManualQuicksort`), partilhado pelos critérios **ALPHABETICAL** e **LENGTH**. Case insensitive e com critério de desempate ALFABÉTICO para LENGHT IGUAIS.

## 📌 Porque escolhi o algoritmo **quicksort**
Escolhi quicksort implementado à mão porque a listagem pode trazer até cerca de 1300 nomes de Pokémon quando o filtro não reduz o resultado — nesse volume, um algoritmo “quadrático” como o insertion sort torna-se claramente mais lento do que um que escala melhor com o tamanho da lista. O quicksort oferece, no caso típico, complexidade da ordem de Θ(k log k) (sendo k o número de nomes após o filtro), em contraste com Θ(k²) do insertion sort. No nosso código, o pivô no centro do intervalo ajuda a evitar o pior caso degenerado Θ(k²) do quicksort. Para listas muito pequenas (k baixo), um esquema híbrido (insertion abaixo de um limiar, por exemplo 16) seria uma evolução opcional, documentada em bottlenecks.md.

## 🔎 Critérios de ordenação

| Modo | Comportamento |
|------|----------------|
| **ALPHABETICAL** | Crescente **case insensitive** (`PokemonNameComparisons.compareAlphabeticalIgnoreCase`). |
| **LENGTH** | Crescente por **tamanho**; empate → **alfabético** ignore case. |

O query param `sort` é mapeado em `PokemonSortType`; o **`PokemonNameSorter`** delega na estratégia certa.

## 🧠 Ideia do quicksort

1. Escolher um **pivô** (aqui: elemento do meio do intervalo `[low, high]`).
2. **Particionar:** nomes “menores” que o pivô à esquerda, “maiores” à direita (segundo a função de comparação).
3. Ordenar **recursivamente** as partições esquerda e direita até intervalos de tamanho 0 ou 1.

Implementação: `ManualQuicksort.kt`. Comparações: `PokemonNameComparisons.kt`.

## 📈 Complexidade assintótica (Big-Θ)

Seja **k** o número de nomes **após o filtro**; **m** o comprimento médio de um nome.


Em termos simples: quanto maior a lista de resultados (k nomes após o filtro), mais importante é usar um algoritmo que não cresça em “quadrado”(como por exemplo o insertion sort) do tamanho da lista — porque isso deixa a ordenação visivelmente mais lenta quando a listagem se aproxima do máximo (~1300). Tecnicamente, o quicksort tende a ordenar em Θ(k log k) no caso médio (partições razoavelmente equilibradas), enquanto algoritmos como insertion sort crescem em Θ(k²). O quicksort ainda pode chegar a Θ(k²) no pior caso quando as partições ficam muito desbalanceadas; por isso usamos pivô central para reduzir a chance de degeneração em entradas comuns. Além disso, cada comparação de nomes pode custar até O(m) (m = tamanho médio do nome), mas como m é pequeno, o fator que mais pesa é mesmo k.

## JUSTIFICATIVA QUICK SORT VS OUTROS ALGORITMOS:
para k até ~1300, quicksort oferece Θ(k log k) no cenário usual, evitando Θ(k²) do insertion sort; face ao merge sort, mantém complexidade logarítmica com menor memória auxiliar (O(log k) vs Θ(k)).


## ❌ O que **não** entra no código de ordenação

- `Collections.sort`, `Arrays.sort`, `Comparator`, `Comparable` “de serviço”, `sortedWith`, `compareBy`, ordenação implícita em streams.

## 🔧 Extensibilidade

Evolução possível (documentada em [bottlenecks.md](bottlenecks.md)): **sort híbrido** (insertion sort para **k < 16**, quicksort para **k ≥ 16**).
