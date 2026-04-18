package search;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class KMPTest {

    @Test
    void buildFailure_cases() {
        assertArrayEquals(new int[]{0,0,1,2,0,1,2,3,4},
                KMP.buildFailure("ABABCABAB"));

        assertArrayEquals(new int[]{0,0,0,0,0},
                KMP.buildFailure("ABCDE"));

        assertArrayEquals(new int[]{0,1,2,3},
                KMP.buildFailure("AAAA"));

        assertArrayEquals(new int[]{0},
                KMP.buildFailure("A"));

        assertArrayEquals(new int[]{0,1,0,1},
                KMP.buildFailure("AABA"));
    }

    @Test
    void search_cases() {
        assertEquals(List.of(0,3,6),
                KMP.search("AABAABAABAAB", "AABA"));

        assertEquals(List.of(0,3),
                KMP.search("ABCABC", "ABC"));

        assertEquals(List.of(),
                KMP.search("AAAA", "B"));

        assertEquals(List.of(0),
                KMP.search("ABC", "ABC"));

        assertEquals(List.of(),
                KMP.search("AB", "ABC"));

        assertEquals(List.of(),
                KMP.search("ABCD", ""));

        assertEquals(List.of(0,1,2),
                KMP.search("AAAA", "AA"));

        assertEquals(List.of(3),
                KMP.search("XYZABC", "ABC"));

        assertEquals(List.of(),
                KMP.search("", "A"));

        assertEquals(List.of(0),
                KMP.search("A", "A"));
    }
}