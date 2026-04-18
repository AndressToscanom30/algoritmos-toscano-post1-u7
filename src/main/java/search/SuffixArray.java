package search;

import java.util.Arrays;

/**
 * Suffix Array con construcción por ordenamiento comparativo ({@code O(n log² n)})
 * y arreglo LCP mediante el algoritmo de Kasai ({@code O(n)}).
 *
 * <p>El <em>Suffix Array</em> es un arreglo de enteros que contiene los índices
 * de inicio de todos los sufijos de una cadena, ordenados lexicográficamente.
 * Permite búsqueda de patrones en {@code O(m log n)}, donde {@code m} es la
 * longitud del patrón y {@code n} es la longitud del texto.</p>
 *
 * <p>El arreglo LCP (<em>Longest Common Prefix</em>) almacena la longitud del
 * prefijo común más largo entre sufijos consecutivos en el Suffix Array.
 * {@code lcp[i]} es el LCP entre el sufijo {@code sa[i-1]} y el sufijo
 * {@code sa[i]}; por convención {@code lcp[0] = 0}.</p>
 *
 * @author Toscano
 * @version 1.0
 */
public class SuffixArray {

    /** Texto sobre el que se construye el Suffix Array. */
    private final String s;

    /** Longitud del texto. */
    private final int n;

    /**
     * {@code sa[i]} = índice de inicio del i-ésimo sufijo menor
     * en orden lexicográfico.
     */
    public final int[] sa;

    /**
     * {@code lcp[i]} = longitud del prefijo común más largo entre
     * los sufijos {@code sa[i-1]} y {@code sa[i]} ({@code lcp[0] = 0}).
     */
    public final int[] lcp;

    /**
     * Construye el Suffix Array y el arreglo LCP para la cadena dada.
     *
     * <p>Precondición: {@code s} no es {@code null} y no está vacío.</p>
     * <p>Postcondición: {@code sa} contiene los índices de los sufijos en orden
     * lexicográfico y {@code lcp} contiene los prefijos comunes más largos
     * entre sufijos consecutivos.</p>
     *
     * @param s cadena de texto (no nula, no vacía)
     */
    public SuffixArray(String s) {
        this.s = s;
        this.n = s.length();
        this.sa = buildSA();
        this.lcp = buildLCP();
    }

    /**
     * Construye el Suffix Array ordenando todos los sufijos lexicográficamente.
     *
     * <p>Complejidad: {@code O(n log² n)} en tiempo usando {@code Arrays.sort}
     * con comparador basado en {@code String.compareTo}.</p>
     *
     * @return arreglo {@code sa} donde {@code sa[i]} es el inicio del i-ésimo
     *         sufijo menor
     */
    private int[] buildSA() {
        Integer[] idx = new Integer[n];
        for (int i = 0; i < n; i++) idx[i] = i;

        // Ordenar por comparación de sufijos: O(n log² n)
        Arrays.sort(idx, (a, b) -> s.substring(a).compareTo(s.substring(b)));

        int[] result = new int[n];
        for (int i = 0; i < n; i++) result[i] = idx[i];
        return result;
    }

    /**
     * Construye el arreglo LCP usando el algoritmo de Kasai.
     *
     * <p>El algoritmo de Kasai explota la propiedad de que si el LCP entre
     * el sufijo en posición {@code i} y su predecesor en el SA es {@code h},
     * entonces el LCP del sufijo en posición {@code i+1} es al menos
     * {@code h - 1}. Esto amortiza el costo a {@code O(n)}.</p>
     *
     * <p>Precondición: {@code sa} ya fue construido correctamente.</p>
     * <p>Postcondición: {@code lcp[i]} contiene la longitud del prefijo común
     * más largo entre los sufijos {@code sa[i-1]} y {@code sa[i]}.</p>
     *
     * @return arreglo LCP de longitud {@code n}
     */
    private int[] buildLCP() {
        int[] rank = new int[n]; // rank[i] = posición del sufijo i en sa
        int[] lcpArr = new int[n];

        // Calcular el arreglo rank (inverso del SA)
        for (int i = 0; i < n; i++) rank[sa[i]] = i;

        int h = 0; // longitud del LCP actual (aprovecha amortización)
        for (int i = 0; i < n; i++) {
            if (rank[i] > 0) {
                int j = sa[rank[i] - 1]; // sufijo predecesor en el SA
                // Extender el LCP carácter a carácter
                while (i + h < n && j + h < n && s.charAt(i + h) == s.charAt(j + h)) {
                    h++;
                }
                lcpArr[rank[i]] = h;
                // El siguiente sufijo tiene LCP >= h - 1 (amortización)
                if (h > 0) h--;
            }
        }
        return lcpArr;
    }

    /**
     * Verifica si {@code pattern} ocurre como subcadena del texto usando
     * búsqueda binaria sobre el Suffix Array.
     *
     * <p>Precondición: {@code pattern} no es {@code null}.</p>
     * <p>Postcondición: retorna {@code true} si y solo si {@code pattern}
     * es subcadena de {@code s}.</p>
     *
     * <p>Complejidad: {@code O(m log n)} tiempo, donde {@code m = pattern.length()}.</p>
     *
     * @param pattern patrón a buscar
     * @return {@code true} si {@code pattern} ocurre en el texto
     */
    public boolean contains(String pattern) {
        if (pattern.isEmpty()) return true;
        int m = pattern.length();
        int lo = 0, hi = n - 1;

        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            // Comparar solo los primeros m caracteres del sufijo
            String suffix = s.substring(sa[mid]);
            String prefix = suffix.length() >= m
                    ? suffix.substring(0, m)
                    : suffix;
            int cmp = prefix.compareTo(pattern);
            if (cmp == 0) {
                return true;
            } else if (cmp < 0) {
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }
        return false;
    }

    /**
     * Retorna el texto asociado a este Suffix Array.
     *
     * @return texto original
     */
    public String getText() {
        return s;
    }
}
