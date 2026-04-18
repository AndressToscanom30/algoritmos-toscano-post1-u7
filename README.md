# toscano-post1-u7

**Diseño de Algoritmos y Sistemas — Unidad 7**  
**Post-Contenido 1: Algoritmos de Búsqueda y Recuperación de Información**  
Universidad Internacional de Santander (UDES) · Ingeniería de Sistemas · 2026  
Autor: Toscano

---

## Descripción del Laboratorio

El laboratorio implementa en Java 17 tres familias de algoritmos de búsqueda robusta:

1. **Búsqueda Binaria Robusta** (`BinarySearch.java`): variantes `lowerBound`, `upperBound`, `countOccurrences` y `bisectAnswer` (búsqueda sobre la respuesta).
2. **Algoritmo KMP** (`KMP.java`): construcción de la función de fallo (`buildFailure`) y búsqueda de patrón en texto (`search`) en tiempo `O(n + m)`.
3. **Suffix Array** (`SuffixArray.java`): construcción `O(n log² n)` por ordenamiento, arreglo LCP mediante el algoritmo de Kasai `O(n)`, y búsqueda de patrones en `O(m log n)`.

Se mide empíricamente la diferencia de rendimiento entre enfoques lineales y sublineales/logarítmicos mediante benchmarks.

---

## Estructura del Proyecto

```
toscano-post1-u7/
├── pom.xml                              # Configuración Maven (JMH 1.37, JUnit 5)
├── README.md
└── src/
    ├── main/java/search/
    │   ├── BinarySearch.java            # lowerBound, upperBound, bisectAnswer
    │   ├── KMP.java                     # buildFailure, search
    │   ├── SuffixArray.java             # build (SA), buildLCP (Kasai), contains
    │   └── SearchBenchmark.java         # Benchmark manual (sin dependencias externas)
    └── test/java/search/
        ├── BinarySearchTest.java        # Tests + checkpoints BinarySearch
        ├── KMPTest.java                 # Tests + checkpoints KMP
        ├── SuffixArrayTest.java         # Tests + checkpoints SuffixArray
        └── TestRunner.java              # Runner unificado de todos los tests
```

---

## Prerrequisitos

| Herramienta | Versión mínima |
|-------------|----------------|
| Java (JDK)  | 17+            |
| Maven       | 3.9+           |

---

## Compilación y Ejecución

### Con Maven (recomendado)

```bash
# Compilar el proyecto
mvn compile

# Ejecutar todos los tests
mvn test

# Ejecutar benchmark manual (sin JMH)
mvn exec:java -Dexec.mainClass="search.SearchBenchmark"

# Generar JAR con benchmarks JMH y ejecutar
mvn package
java -jar target/benchmarks.jar -wi 5 -i 10 -f 1
```

### Sin Maven (compilación manual)

```bash
# Compilar fuentes principales
javac -d out/main src/main/java/search/*.java

# Compilar tests
javac -cp out/main -d out/test src/test/java/search/*.java

# Ejecutar todos los tests
java -cp out/main:out/test search.TestRunner

# Ejecutar benchmark
java -cp out/main search.SearchBenchmark
```

---

## Descripción de Componentes

### `BinarySearch.java`

Variantes robustas de búsqueda binaria sobre arreglos ordenados. Todas operan en `O(log n)`.

| Método | Descripción | Complejidad |
|--------|-------------|-------------|
| `lowerBound(arr, key)` | Primer índice con `arr[i] >= key` | O(log n) |
| `upperBound(arr, key)` | Primer índice con `arr[i] > key` | O(log n) |
| `countOccurrences(arr, key)` | Frecuencia de `key` en `arr` | O(log n) |
| `bisectAnswer(lo, hi, predicate)` | Mínimo `x` en `[lo,hi]` con `predicate(x)==true` | O(log(hi-lo)) |
| `minMaxLoad(tasks, k)` | Distribución óptima de tareas en k workers | O(n log S) |

**Clave de implementación:** se usa el intervalo semiabierto `[lo, hi)` y el cálculo `mid = lo + (hi - lo) / 2` para evitar desbordamiento de enteros.

---

### `KMP.java`

Algoritmo Knuth-Morris-Pratt para emparejamiento de patrones. Evita retroceder en el texto explotando la *función de fallo* (tabla de prefijos).

| Método | Descripción | Complejidad |
|--------|-------------|-------------|
| `buildFailure(pattern)` | Construye la tabla de prefijos del patrón | O(m) tiempo, O(m) espacio |
| `search(text, pattern)` | Retorna todas las posiciones de inicio (solapadas) | O(n + m) tiempo, O(m) espacio |

**Definición de la función de fallo:**  
`failure[j]` = longitud del prefijo propio más largo de `pattern[0..j]` que también es sufijo.

Ejemplo para `"ABABCABAB"`:

| j | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 |
|---|---|---|---|---|---|---|---|---|---|
| c | A | B | A | B | C | A | B | A | B |
| f | 0 | 0 | 1 | 2 | 0 | 1 | 2 | 3 | 4 |

---

### `SuffixArray.java`

Suffix Array con construcción por ordenamiento y LCP de Kasai.

| Campo / Método | Descripción | Complejidad |
|----------------|-------------|-------------|
| `sa[i]` | Índice de inicio del i-ésimo sufijo menor | — |
| `lcp[i]` | LCP entre sufijos `sa[i-1]` y `sa[i]` | — |
| Constructor `SuffixArray(s)` | Construye SA + LCP | O(n log² n) + O(n) |
| `contains(pattern)` | Búsqueda de patrón por bisección del SA | O(m log n) |

Para `"banana"`:
```
Sufijos ordenados:    SA:       LCP:
  "a"        → 5     [5,       [0,
  "ana"      → 3      3,        1,
  "anana"    → 1      1,        3,
  "banana"   → 0      0,        0,
  "na"       → 4      4,        0,
  "nana"     → 2      2]        2]
```

---

### `SearchBenchmark.java`

Benchmark manual (sin dependencias externas) que compara:
- **Búsqueda lineal** vs. **búsqueda binaria** (`lowerBound`) sobre arreglos ordenados.
- **`String.indexOf`** vs. **KMP** para búsqueda de patrones en texto.

Se usan calentamiento (warmup) e iteraciones repetidas para estabilizar la JVM.


## Resultados de Benchmark

Los siguientes resultados fueron obtenidos usando JMH.
Las unidades están en microsegundos por operación (µs/op).

> **Cómo reproducir con JMH:** `mvn package && java -jar target/benchmarks.jar`

### Búsqueda Lineal vs. Búsqueda Binaria (arreglo ordenado, peor caso)

| n         | Lineal (µs/op) | Binaria (µs/op) | Speedup aprox. |
| --------- | -------------- | --------------- | -------------- |
| 10 000    | 1.730          | 0.012           | ~144×          |
| 100 000   | 17.056         | 0.016           | ~1066×         |
| 1 000 000 | 182.776        | 0.033           | ~5538×         |

La búsqueda binaria crece como `O(log n)` — solo 1 comparación adicional al duplicar `n`.  
La búsqueda lineal crece como `O(n)`, haciendo que la brecha aumente indefinidamente.

### String.indexOf vs. KMP (patrón ausente, peor caso)

| n         | indexOf (µs/op) | KMP (µs/op) |
| --------- | --------------- | ----------- |
| 10 000    | 87.184          | 121.947     |
| 100 000   | 941.363         | 656.387     |
| 1 000 000 | 20220.004       | 3280.393    |

### Observaciones:

* Para tamaños pequeños, indexOf puede ser competitivo debido a optimizaciones internas del JDK.
* Para tamaños grandes, KMP supera claramente a indexOf en el peor caso.

### Patrón Presente

| n       | KMP (µs/op) |
| ------- | ----------- |
| 10 000  | 67.621      |
| 100 000 | 1360.217    |

### Análisis de Complejidad Teórica

| Algoritmo | Preprocesamiento | Búsqueda | Espacio |
|-----------|-----------------|----------|---------|
| Lineal | O(1) | O(n) | O(1) |
| Binaria (`lowerBound`) | O(n log n) ordenar | O(log n) | O(1) |
| KMP | O(m) | O(n + m) | O(m) |
| Suffix Array (build) | O(n log² n) | O(m log n) | O(n) |