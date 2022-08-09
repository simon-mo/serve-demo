package demo.ray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import io.ray.api.ObjectRef;
import io.ray.api.Ray;
import io.ray.serve.api.Serve;
import io.ray.serve.deployment.Deployment;
import io.ray.serve.generated.DeploymentLanguage;
import io.ray.serve.handle.RayServeHandle;

public class App {
        public static void main(String[] args) throws InterruptedException {
                // System.setProperty("ray.job.runtime-env.jars.0",
                // "file:///Users/xmo/Desktop/ray/demo/java-demo/demo/target/demo-1.0-SNAPSHOT.jar");
                // System.setProperty("ray.job.runtime-env.jars.1",
                // "file:///Users/xmo/Desktop/ray/ray/python/ray/jars");
                // System.setProperty("ray.job.code-search-path",
                // "/Users/xmo/Desktop/ray/demo/java-demo/demo/target/demo-1.0-SNAPSHOT.jar:/Users/xmo/Desktop/ray/ray/python/ray/jars");

                System.setProperty("ray.job.code-search-path",
                                "/Users/xmo/Desktop/ray/demo/java-demo/demo/target/:/Users/xmo/Desktop/ray/ray/.jar/darwin:/Users/xmo/Desktop/ray/ray/python/ray/jars:/Users/xmo/Desktop/ray/demo/java-demo/demo");
                Ray.init();

                // ActorHandle<Preprocessor> handle = Ray.actor(Preprocessor::new).remote();
                // System.out.println(handle.task(Preprocessor::sayHi, "inp").remote().get());

                Serve.start(true, false, null);

                Deployment preprocessDeployment = Serve.deployment()
                                .setDeploymentDef(Preprocessor.class.getName())
                                .setName("preprocessor")
                                .setLanguage(DeploymentLanguage.JAVA)
                                .setNumReplicas(1)
                                .create();
                preprocessDeployment.deploy(/* blocking */true);
                RayServeHandle handle = preprocessDeployment.getHandle();

                Deployment modelDeployment = Serve.deployment()
                                .setDeploymentDef("py_model.SumModel")
                                .setName("pymodel")
                                .setLanguage(DeploymentLanguage.PYTHON)
                                .setNumReplicas(1)
                                .create();
                modelDeployment.deploy(/* blocking */true);
                RayServeHandle handle2 = modelDeployment.getHandle();

                Deployment postprocessor = Serve.deployment()
                                .setDeploymentDef(Postprocessor.class.getName())
                                .setName("postprocessor")
                                .setLanguage(DeploymentLanguage.JAVA)
                                .setNumReplicas(1)
                                .create();
                postprocessor.deploy(/* blocing */ true);
                RayServeHandle handle3 = postprocessor.getHandle();

                TimeUnit.SECONDS.sleep(4L);

                Gson gson = new Gson();
                ObjectRef ref = handle.method("call")
                                .remote(Arrays.asList("member-1", "member-2"));
                Map<String, List<Integer>> preprocessed = (Map) Ray.get(ref);
                System.out.println(String.format("Step 1: preprocessed result %s", preprocessed));

                ObjectRef ref2 = handle2.method("__call__")
                                .remote(gson.toJson(preprocessed));
                String modelPredictionRaw = (String) Ray.get(ref2);
                Map<String, Integer> modelPrediction = gson.fromJson(
                        modelPredictionRaw, new TypeToken<Map<String, Integer>>() {}.getType());
                System.out.println(String.format("Step 2: python prediction %s", modelPrediction));

                ObjectRef ref3 = handle3.method("rank")
                                .remote(modelPrediction);
                List<Pair<String, Integer>> rankedResult = (List) Ray.get(ref3);
                System.out.println(String.format("Step 3: ranked result %s", rankedResult));

                // Serve.shutdown();
                // Ray.shutdown();
        }
}
