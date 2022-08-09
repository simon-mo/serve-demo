package demo.ray;

import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

public class Postprocessor {
    public List<Pair<String, Integer>> rank(Map<String, Integer> scores) {
        return scores.entrySet().stream().sorted((a, b) -> a.getValue().compareTo(b.getValue()))
                .map(e -> Pair.of(e.getKey(), e.getValue())).collect(Collectors.toList());
    }

}
