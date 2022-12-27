package github.incodelearning.functional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.groupingBy;

/**
 * Tricks on streams.
 */
public class DemoStreamOps {
    public <E> List<E> arrayToList(E[] array) {
        return Arrays.stream(array).collect(Collectors.toList());
    }

    /**
     * groupingBy Can perform SQL like operations.
     */
    public <E> Map<String, List<E>> groupBy(List<E> list, Function<E, String> getter) {
        return list.stream().collect(groupingBy(getter));
    }

    /**
     * Generate a list of integer numbers in the range specified.
     *
     * @param start left boundary, inclusive
     * @param end   right boundary, exclusive
     * @return the generated list
     */
    public static List<Integer> generateNaturalNumbers(int start, int end) {
        return IntStream.range(start, end).boxed().toList();
    }
}
