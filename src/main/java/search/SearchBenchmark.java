package search;

import org.openjdk.jmh.annotations.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 5)
@Measurement(iterations = 20)
@Fork(1)
@State(Scope.Thread)
public class SearchBenchmark {

    @Param({ "10000", "100000", "1000000" })
    private int n;

    private int[] sortedArr;
    private String text;
    private String patternPresent;
    private String patternAbsent;

    // ─── setup ─────────────────────────────────────────

    @Setup(Level.Trial)
    public void setup() {
        sortedArr = new int[n];
        for (int i = 0; i < n; i++)
            sortedArr[i] = i * 2;

        text = "a".repeat(n);
        patternPresent = "a".repeat(20);
        patternAbsent = "a".repeat(20) + "b";
    }

    // ─── benchmarks ────────────────────────────────────

    @Benchmark
    public int linearSearch() {
        int target = n - 1;
        for (int i = 0; i < sortedArr.length; i++) {
            if (sortedArr[i] == target)
                return i;
        }
        return -1;
    }

    @Benchmark
    public int binarySearch() {
        return BinarySearch.lowerBound(sortedArr, n - 1);
    }

    @Benchmark
    public int indexOfSearch() {
        return text.indexOf(patternAbsent);
    }

    @Benchmark
    public int kmpAbsent() {
        List<Integer> result = KMP.search(text, patternAbsent);
        return result.size(); // evitar eliminación por JIT
    }

    @Benchmark
    public int kmpPresent() {
        List<Integer> result = KMP.search(text, patternPresent);
        return result.size();
    }
}