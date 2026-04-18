package search;

import java.util.function.LongPredicate;

/**
 * Variantes robustas de búsqueda binaria sobre arreglos ordenados.
 *
 * <p>Invariante {@code lowerBound}: {@code arr[result] >= key} para todo
 * {@code result} en {@code [0, n]}. Si todos los elementos son menores que
 * {@code key}, retorna {@code arr.length}.</p>
 *
 * <p>Invariante {@code upperBound}: {@code arr[result] > key} para todo
 * {@code result} en {@code [0, n]}. Si todos los elementos son menores o
 * iguales que {@code key}, retorna {@code arr.length}.</p>
 *
 * <p>Todas las operaciones se realizan en {@code O(log n)} tiempo y
 * {@code O(1)} espacio adicional.</p>
 *
 * @author Toscano
 * @version 1.0
 */
public class BinarySearch {

    /**
     * Retorna el índice del primer elemento {@code >= key} en el arreglo ordenado.
     *
     * <p>Precondición: {@code arr} está ordenado en orden no decreciente.</p>
     * <p>Postcondición: retorna el índice más pequeño {@code i} tal que
     * {@code arr[i] >= key}, o {@code arr.length} si no existe tal índice.</p>
     *
     * @param arr arreglo de enteros ordenado en orden no decreciente
     * @param key valor a buscar
     * @return índice del primer elemento {@code >= key}, o {@code arr.length}
     */
    public static int lowerBound(int[] arr, int key) {
        int lo = 0, hi = arr.length; // intervalo semiabierto [lo, hi)
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2; // evita desbordamiento de enteros
            if (arr[mid] < key) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

    /**
     * Retorna el índice del primer elemento {@code > key} en el arreglo ordenado.
     *
     * <p>Precondición: {@code arr} está ordenado en orden no decreciente.</p>
     * <p>Postcondición: retorna el índice más pequeño {@code i} tal que
     * {@code arr[i] > key}, o {@code arr.length} si no existe tal índice.</p>
     *
     * @param arr arreglo de enteros ordenado en orden no decreciente
     * @param key valor a buscar
     * @return índice del primer elemento {@code > key}, o {@code arr.length}
     */
    public static int upperBound(int[] arr, int key) {
        int lo = 0, hi = arr.length; // intervalo semiabierto [lo, hi)
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2; // evita desbordamiento de enteros
            if (arr[mid] <= key) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

    /**
     * Cuenta cuántas veces aparece {@code key} en el arreglo ordenado.
     *
     * <p>Precondición: {@code arr} está ordenado en orden no decreciente.</p>
     * <p>Postcondición: retorna {@code upperBound(arr, key) - lowerBound(arr, key)}.</p>
     *
     * @param arr arreglo de enteros ordenado en orden no decreciente
     * @param key valor cuya frecuencia se cuenta
     * @return número de ocurrencias de {@code key} en {@code arr}
     */
    public static int countOccurrences(int[] arr, int key) {
        return upperBound(arr, key) - lowerBound(arr, key);
    }

    /**
     * Búsqueda sobre la respuesta: encuentra el mínimo {@code x} en {@code [lo, hi]}
     * tal que {@code predicate.test(x) == true}.
     *
     * <p>Precondición: {@code predicate} es monótona no decreciente sobre {@code [lo, hi]},
     * es decir, existe un umbral tal que todos los valores menores retornan {@code false}
     * y todos los valores mayores o iguales retornan {@code true}. Debe existir al menos
     * un valor en el rango que satisfaga el predicado.</p>
     *
     * <p>Postcondición: retorna el mínimo {@code x} en {@code [lo, hi]} tal que
     * {@code predicate.test(x) == true}.</p>
     *
     * <p>Complejidad: {@code O(log(hi - lo))} evaluaciones del predicado.</p>
     *
     * @param lo     cota inferior del rango de búsqueda (inclusive)
     * @param hi     cota superior del rango de búsqueda (inclusive)
     * @param predicate predicado monótono (false...false, true...true)
     * @return el mínimo valor en {@code [lo, hi]} que satisface {@code predicate}
     */
    public static long bisectAnswer(long lo, long hi, LongPredicate predicate) {
        while (lo < hi) {
            long mid = lo + (hi - lo) / 2; // evita desbordamiento de long
            if (predicate.test(mid)) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }
        return lo;
    }

    /**
     * Ejemplo de uso de {@code bisectAnswer}: distribución óptima de tareas.
     *
     * <p>Dado un arreglo de duraciones de tareas y un número de trabajadores {@code k},
     * calcula la mínima carga máxima posible al distribuir las tareas en {@code k}
     * trabajadores (las tareas deben asignarse en bloques contiguos).</p>
     *
     * @param tasks arreglo de duraciones de tareas (positivas)
     * @param k     número de trabajadores disponibles
     * @return la mínima carga máxima posible
     */
    public static long minMaxLoad(long[] tasks, int k) {
        LongPredicate canDistribute = maxLoad -> {
            int workers = 1;
            long current = 0;
            for (long task : tasks) {
                if (task > maxLoad) return false;
                if (current + task > maxLoad) {
                    workers++;
                    current = task;
                } else {
                    current += task;
                }
            }
            return workers <= k;
        };

        long lo = 0;
        long hi = 0;
        for (long t : tasks) {
            lo = Math.max(lo, t);
            hi += t;
        }
        return bisectAnswer(lo, hi, canDistribute);
    }
}
