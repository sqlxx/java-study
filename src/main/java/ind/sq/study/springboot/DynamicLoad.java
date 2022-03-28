package ind.sq.study.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.util.ClassUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

@SpringBootApplication
public class DynamicLoad {
    public static void main(String[] args) throws MalformedURLException {

        SpringApplication app = new SpringApplication(DynamicLoad.class);
        var resourceLoader = new DefaultResourceLoader();
        resourceLoader.setClassLoader(loadClassFromMaven());
        app.setResourceLoader(resourceLoader);
        app.run();
    }

    public static ClassLoader loadClassFromMaven() throws MalformedURLException {
        URLClassLoader ucl = new URLClassLoader("Custom", new URL[]{new URL("file:///Users/sqlxx/Projects/mock-jar/target/mock-jar-1.0-SNAPSHOT.jar")}, ClassUtils.getDefaultClassLoader());
        return ucl;
    }
}
