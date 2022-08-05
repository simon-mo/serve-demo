package demo.ray;

import io.ray.api.ObjectRef;
import io.ray.api.Ray;
import io.ray.serve.api.Serve;
import io.ray.serve.deployment.Deployment;
import io.ray.serve.generated.DeploymentLanguage;
import io.ray.serve.handle.RayServeHandle;

public class App {
    public static void main(String[] args) {
//        System.setProperty("ray.job.runtime-env.jars.0", "file:///Users/xmo/Desktop/ray/demo/java-demo/demo/target/demo-1.0-SNAPSHOT.jar");
//        System.setProperty("ray.job.runtime-env.jars.1", "file:///Users/xmo/Desktop/ray/ray/python/ray/jars");
//        System.setProperty("ray.job.code-search-path", "/Users/xmo/Desktop/ray/demo/java-demo/demo/target/demo-1.0-SNAPSHOT.jar:/Users/xmo/Desktop/ray/ray/python/ray/jars");
        Ray.init();
        Serve.start(true, false, null);

        Deployment preprocessDeployment = Serve.deployment()
                .setDeploymentDef(Preprocessor.class.getName())
                .setName("preprocessor")
                .setLanguage(DeploymentLanguage.JAVA)
                .setNumReplicas(1)
                .create();
        preprocessDeployment.deploy(/* blocking */true);
        RayServeHandle handle = preprocessDeployment.getHandle();
        ObjectRef ref = handle.method("call").remote("id-1,id-2");
        System.out.println(Ray.get(ref));
    }
}
