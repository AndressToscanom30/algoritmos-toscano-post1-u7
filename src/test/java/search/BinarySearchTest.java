package search;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BinarySearchTest {

    @Test
    void lowerBound_basicCases() {
        int[] arr = {1,3,3,5,7};

        assertEquals(1, BinarySearch.lowerBound(arr, 3));
        assertEquals(0, BinarySearch.lowerBound(arr, 1));
        assertEquals(4, BinarySearch.lowerBound(arr, 7));
        assertEquals(5, BinarySearch.lowerBound(arr, 10));
        assertEquals(0, BinarySearch.lowerBound(arr, 0));
        assertEquals(3, BinarySearch.lowerBound(arr, 4));
    }

    @Test
    void lowerBound_edgeCases() {
        assertEquals(0, BinarySearch.lowerBound(new int[]{5}, 5));
        assertEquals(1, BinarySearch.lowerBound(new int[]{5}, 6));
        assertEquals(0, BinarySearch.lowerBound(new int[]{}, 0));
    }

    @Test
    void upperBound_cases() {
        int[] arr = {1,3,3,5,7};

        assertEquals(3, BinarySearch.upperBound(arr, 3));
        assertEquals(1, BinarySearch.upperBound(arr, 1));
        assertEquals(5, BinarySearch.upperBound(arr, 7));
        assertEquals(5, BinarySearch.upperBound(arr, 10));
        assertEquals(0, BinarySearch.upperBound(arr, 0));
        assertEquals(0, BinarySearch.upperBound(new int[]{}, 0));
    }

    @Test
    void countOccurrences_cases() {
        int[] arr = {1,3,3,5,7};

        assertEquals(2, BinarySearch.countOccurrences(arr, 3));
        assertEquals(1, BinarySearch.countOccurrences(arr, 1));
        assertEquals(0, BinarySearch.countOccurrences(arr, 4));
        assertEquals(0, BinarySearch.countOccurrences(arr, 10));
        assertEquals(4, BinarySearch.countOccurrences(new int[]{2,2,2,2}, 2));
    }

    @Test
    void minMaxLoad_cases() {
        assertEquals(10, BinarySearch.minMaxLoad(new long[]{3,5,2,8,4}, 3));
        assertEquals(15, BinarySearch.minMaxLoad(new long[]{1,2,3,4,5}, 1));
        assertEquals(5, BinarySearch.minMaxLoad(new long[]{1,2,3,4,5}, 5));
        assertEquals(7, BinarySearch.minMaxLoad(new long[]{7}, 1));
    }

    @Test
    void bisectAnswer_cases() {
        assertEquals(5, BinarySearch.bisectAnswer(1,10, x -> x >= 5));
        assertEquals(10, BinarySearch.bisectAnswer(0,100, x -> x * x >= 100));
    }
}