package search;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class SuffixArrayTest {

    @Test
    void suffixArray_banana() {
        SuffixArray sa = new SuffixArray("banana");

        assertArrayEquals(new int[]{5,3,1,0,4,2}, sa.sa);
        assertArrayEquals(new int[]{0,1,3,0,0,2}, sa.lcp);
    }

    @Test
    void contains_banana() {
        SuffixArray sa = new SuffixArray("banana");

        assertTrue(sa.contains("ana"));
        assertFalse(sa.contains("xyz"));
        assertTrue(sa.contains("banana"));
        assertTrue(sa.contains("b"));
        assertTrue(sa.contains("a"));
        assertTrue(sa.contains("na"));
        assertTrue(sa.contains("nan"));
        assertFalse(sa.contains("bananas"));
        assertFalse(sa.contains("c"));
        assertTrue(sa.contains(""));
    }

    @Test
    void suffixArray_mississippi() {
        SuffixArray sa = new SuffixArray("mississippi");

        int[] suffixArray = sa.sa;

        assertEquals(11, suffixArray.length);

        for (int i = 1; i < suffixArray.length; i++) {
            String prev = "mississippi".substring(suffixArray[i - 1]);
            String curr = "mississippi".substring(suffixArray[i]);
            assertTrue(prev.compareTo(curr) <= 0);
        }
    }

    @Test
    void contains_mississippi() {
        SuffixArray sa = new SuffixArray("mississippi");

        assertTrue(sa.contains("issi"));
        assertTrue(sa.contains("mississippi"));
        assertFalse(sa.contains("xyz"));
    }

    @Test
    void suffixArray_singleChar() {
        SuffixArray sa = new SuffixArray("a");

        assertArrayEquals(new int[]{0}, sa.sa);
        assertArrayEquals(new int[]{0}, sa.lcp);
        assertTrue(sa.contains("a"));
        assertFalse(sa.contains("b"));
    }
}