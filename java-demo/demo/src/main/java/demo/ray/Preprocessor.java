package demo.ray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Preprocessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(Preprocessor.class);

    public Map<String, List<Integer>> call(Object memberIDs) {
        Map<String, List<Integer>> feature = new HashMap<>();
        // simulate feature access
        for (String id : ((String) memberIDs).split(",")) {
            feature.put(id, Arrays.asList(1, 2, 3));
        }
        LOGGER.info("Feature Map", feature);
        return feature;
    }
}
