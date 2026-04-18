package search;

import java.util.List;
import java.util.Random;

/**
 * Benchmark manual que mide empíricamente el rendimiento de:
 * <ul>
 *   <li>Búsqueda lineal vs. búsqueda binaria ({@link BinarySearch#lowerBound})</li>
 *   <li>Búsqueda con {@link String#indexOf} vs. {@link KMP#search}</li>
 * </ul>
 *
 * <p>Para cada tamaño de entrada {@code n ∈ {10 000, 100 000, 1 000 000}} se realizan
 * múltiples iteraciones de calentamiento (warmup) seguidas de iteraciones de medición,
 * y se reporta el tiempo promedio en microsegundos.</p>
 *
 * <p>Diseñado para ejecutarse sin dependencias externas (no requiere JMH).</p>
 *
 * @author Toscano
 */
public class SearchBenchmark {

    /** Número de iteraciones de calentamiento para estabilizar la JVM. */
    private static final int WARMUP = 5;

    /** Número de iteraciones de medición. */
    private static final int ITERATIONS = 20;

    // ─── datos compartidos ────────────────────────────────────────────────────

    private int[] sortedArr;
    private String text;
    private String patternPresent;   // patrón que SÍ existe (caso promedio)
    private String patternAbsent;    // patrón que NO existe (peor caso para lineal)
    private int n;

    public SearchBenchmark(int n) {
        this.n = n;

        // Arreglo ordenado de pares: 0, 2, 4, …, 2(n-1)
        sortedArr = new int[n];
        for (int i = 0; i < n; i++) sortedArr[i] = i * 2;

        // Texto: cadena de 'a' de longitud n
        text = "a".repeat(n);

        // Patrón presente en el texto: "aa...a" (20 'a') — aparece en posición 0
        patternPresent = "a".repeat(20);

        // Patrón ausente: 20 'a' + 'b' → fuerza recorrido completo en búsqueda naïve
        patternAbsent  = "a".repeat(20) + "b";
    }

    // ─── benchmarks individuales ─────────────────────────────────────────────

    /** Búsqueda lineal en el arreglo ordenado (peor caso: clave al final). */
    public long benchLinearSearch() {
        int target = n - 1; // valor impar → no existe, recorre todo el arreglo
        int result = -1;
        for (int i = 0; i < sortedArr.length; i++) {
            if (sortedArr[i] == target) { result = i; break; }
        }
        return result; // evitar que el JIT elimine el cómputo
    }

    /** Búsqueda binaria (lower_bound) en el arreglo ordenado. */
    public long benchBinarySearch() {
        return BinarySearch.lowerBound(sortedArr, n - 1);
    }

    /** Búsqueda de subcadena con {@code String.indexOf} (naïve en la JVM). */
    public int benchIndexOf() {
        return text.indexOf(patternAbsent);
    }

    /** Búsqueda KMP con patrón ausente (peor caso lineal). */
    public List<Integer> benchKmpAbsent() {
        return KMP.search(text, patternAbsent);
    }

    /** Búsqueda KMP con patrón presente. */
    public List<Integer> benchKmpPresent() {
        return KMP.search(text, patternPresent);
    }

    // ─── medición ─────────────────────────────────────────────────────────────

    @FunctionalInterface
    interface Benchmark { long run(); }

    private static double measureMicros(Benchmark b) {
        // Calentamiento
        for (int i = 0; i < WARMUP; i++) b.run();

        // Medición
        long total = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            long t0 = System.nanoTime();
            b.run();
            total += System.nanoTime() - t0;
        }
        return (total / (double) ITERATIONS) / 1_000.0; // ns → µs
    }

    // ─── ejecución ────────────────────────────────────────────────────────────

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║  Benchmark: Búsqueda Lineal vs Binaria vs KMP           ║");
        System.out.println("║  Warmup=" + WARMUP + " iter=" + ITERATIONS + " | tiempos en µs (promedio)       ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();

        int[] sizes = {10_000, 100_000, 1_000_000};

        // Encabezado de tabla
        System.out.printf("%-12s | %-15s | %-15s | %-15s | %-15s%n",
                "n", "Lineal (µs)", "Binaria (µs)", "indexOf (µs)", "KMP (µs)");
        System.out.println("-".repeat(80));

        for (int n : sizes) {
            SearchBenchmark bm = new SearchBenchmark(n);

            double linearTime  = measureMicros(bm::benchLinearSearch);
            double binaryTime  = measureMicros(bm::benchBinarySearch);
            double indexOfTime = measureMicros(bm::benchIndexOf);
            double kmpTime     = measureMicros(bm::benchKmpAbsent);

            System.out.printf("%-12d | %-15.3f | %-15.3f | %-15.3f | %-15.3f%n",
                    n, linearTime, binaryTime, indexOfTime, kmpTime);
        }

        System.out.println();
        System.out.println("Notas:");
        System.out.println("  Lineal  : recorre el arreglo hasta encontrar n-1 (impar, no existe)");
        System.out.println("  Binaria : lowerBound con key=n-1 en arreglo de pares");
        System.out.println("  indexOf : Java String.indexOf con patrón ausente (peor caso)");
        System.out.println("  KMP     : KMP.search con patrón ausente (peor caso O(n+m))");
    }
}
