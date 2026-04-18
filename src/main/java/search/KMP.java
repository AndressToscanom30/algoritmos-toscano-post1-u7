package search;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del algoritmo Knuth-Morris-Pratt (KMP) para
 * emparejamiento de patrones en cadenas de texto.
 *
 * <p>KMP evita retroceder en el texto al explotar la estructura interna del
 * patrón a través de la <em>función de fallo</em> (también llamada tabla de
 * prefijos). Esto permite alcanzar complejidad {@code O(n + m)} en tiempo
 * y {@code O(m)} en espacio adicional, donde {@code n} es la longitud del
 * texto y {@code m} es la longitud del patrón.</p>
 *
 * <p>Definición de la función de fallo:<br>
 * {@code failure[j]} = longitud del prefijo propio más largo de
 * {@code pattern[0..j]} que también es sufijo de {@code pattern[0..j]}.</p>
 *
 * @author Toscano
 * @version 1.0
 */
public class KMP {

    /**
     * Construye la función de fallo (tabla de prefijos) para el patrón dado.
     *
     * <p>Precondición: {@code pattern} no es {@code null}.</p>
     * <p>Postcondición: retorna un arreglo {@code f} de longitud
     * {@code pattern.length()} donde {@code f[j]} es la longitud del prefijo
     * propio más largo de {@code pattern[0..j]} que también es sufijo.</p>
     *
     * <p>Complejidad: {@code O(m)} tiempo y espacio, donde {@code m = pattern.length()}.</p>
     *
     * @param pattern cadena de patrón para la que se construye la función de fallo
     * @return arreglo de función de fallo de longitud {@code pattern.length()}
     */
    public static int[] buildFailure(String pattern) {
        int m = pattern.length();
        int[] f = new int[m];
        f[0] = 0; // el prefijo vacío es el único prefijo-sufijo propio de longitud 1
        int k = 0; // longitud del prefijo-sufijo actual

        for (int i = 1; i < m; i++) {
            // Retroceder mientras haya mismatch
            while (k > 0 && pattern.charAt(k) != pattern.charAt(i)) {
                k = f[k - 1];
            }
            // Si hay match, extender el prefijo-sufijo
            if (pattern.charAt(k) == pattern.charAt(i)) {
                k++;
            }
            f[i] = k;
        }
        return f;
    }

    /**
     * Busca todas las ocurrencias (posiblemente solapadas) de {@code pattern}
     * en {@code text} y retorna sus posiciones de inicio (0-indexed).
     *
     * <p>Precondición: {@code text} y {@code pattern} no son {@code null}.</p>
     * <p>Postcondición: retorna una lista con todos los índices {@code i} tales
     * que {@code text.substring(i, i + pattern.length()).equals(pattern)},
     * en orden creciente. Si {@code pattern} es vacío, retorna lista vacía.</p>
     *
     * <p>Complejidad: {@code O(n + m)} tiempo, {@code O(m)} espacio adicional,
     * donde {@code n = text.length()} y {@code m = pattern.length()}.</p>
     *
     * @param text    texto donde se realiza la búsqueda
     * @param pattern patrón a buscar
     * @return lista de posiciones de inicio (0-indexed) donde ocurre el patrón
     */
    public static List<Integer> search(String text, String pattern) {
        List<Integer> results = new ArrayList<>();
        if (pattern.isEmpty()) return results;

        int[] f = buildFailure(pattern);
        int n = text.length(), m = pattern.length();
        int q = 0; // número de caracteres del patrón ya emparejados

        for (int i = 0; i < n; i++) {
            // Retroceder mientras haya mismatch
            while (q > 0 && pattern.charAt(q) != text.charAt(i)) {
                q = f[q - 1];
            }
            // Si hay match, avanzar en el patrón
            if (pattern.charAt(q) == text.charAt(i)) {
                q++;
            }
            // Si se completó el patrón, registrar ocurrencia
            if (q == m) {
                results.add(i - m + 1); // posición de inicio
                q = f[q - 1];           // continuar buscando solapamientos
            }
        }
        return results;
    }
}
