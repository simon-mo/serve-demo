package demo.ray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Preprocessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(Preprocessor.class);

    private Random randomState;

    public Preprocessor() {
        randomState = new Random(42);
    }

    public Map<String, List<Integer>> call(List<String> memberIDs) {
        Map<String, List<Integer>> feature = new HashMap<>();
        // simulate feature access
        for (String id : memberIDs) {
            feature.put(id, Arrays.asList(
                randomState.nextInt(10),
                randomState.nextInt(10),
                randomState.nextInt(10)));
        }
        LOGGER.info("Feature Map", feature);
        return feature;
    }
}
