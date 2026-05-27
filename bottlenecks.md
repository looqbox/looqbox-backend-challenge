# 📊 Bottlenecks (foco em sorting)

Este documento foca no gargalo e nas melhorias possíveis ligadas ao **algoritmo de ordenação**. Após o filtro, ordenamos **k** nomes (no máximo ~**1300** quando `query` é vazio). A justificativa formal do algoritmo e Big-Θ estão em [sorting-algorithm.md](sorting-algorithm.md).

## 🎯 Quando o sort vira gargalo

Com **cache quente** (sem rede no caminho crítico), o tempo do request é dominado por CPU local:

- filtro: **O(N)** no catálogo completo;
- sort: sobre **k** itens (resultado do filtro).

O pior cenário de sort é `query` vazio, onde **k ≈ N ≈ 1300**.

## ✅ Solução atual: quicksort manual

O projeto usa `ManualQuicksort` com pivô no **meio** do intervalo.

- **Ganho:** caso médio **Θ(k log k)** em vez de **Θ(k²)** em listas grandes (ex.: insertion sort).
- **Trade-off:** pior caso ainda é **Θ(k²)**; pivô central reduz degeneração em entradas comuns.
- **Memória:** ordena in-place a cópia mutável; custo extra típico **O(log k)** (recursão).

## 💡 Melhoria possível: sort híbrido (cut-off 16)

Implementações reais de sort costumam ser híbridas: um algoritmo simples para sublistas pequenas e um algoritmo **O(k log k)** para o restante.

### Como seria

- Se **k < 16**: **insertion sort** (menor overhead em listas minúsculas).
- Se **k ≥ 16**: **quicksort** (escala melhor para dezenas/centenas/mil).

### Por que pode ajudar

- Em **k** muito pequeno, o overhead de partição/recursão do quicksort pode ser maior que o “quadrático pequeno” do insertion.
- Em **k** médio/grande, quicksort continua dominante; o híbrido preserva o ganho.

### Quando vale

- Se profiling mostrar muitas requisições com **k** pequeno e o sort aparecer no p95/p99.
- Se quisermos um cut-off “padrão de biblioteca” (16–32), documentado como heurística.

### Custo/risco

- Mais código e mais testes (dois algoritmos).
- Cut-off é heurística, não valor exato “ótimo”.

## 🔁 Alternativas (se ainda pesar)

- **Pré-ordenar** visões (ALPHABETICAL/LENGTH) quando o cache atualiza, e filtrar em cima delas.
- **Merge sort**: garante **Θ(k log k)** no pior caso, mas usa buffer auxiliar **O(k)**.

## ✅ Resumo

Hoje o sort já escala no pior caso de produto (~1300 itens) com **quicksort** manual. Se necessário, a melhoria incremental é um **híbrido insertion+quick** com cut-off ~16 para reduzir overhead quando **k** é muito pequeno.
