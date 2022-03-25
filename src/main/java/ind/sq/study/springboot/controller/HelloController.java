package ind.sq.study.springboot.controller;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;

@RestController
public class HelloController {

    private final ApplicationContext context;

    public HelloController(ApplicationContext context) {
        this.context = context;
    }

    @GetMapping("/load")
    public String loadJar() throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        JarFile jarFile = new JarFile("/Users/sqlxx/Projects/mock-jar/target/mock-jar-1.0-SNAPSHOT.jar");
        var entries = jarFile.entries();
        URLClassLoader ucl = new URLClassLoader(new URL[]{new URL("file:///Users/sqlxx/Projects/mock-jar/target/mock-jar-1.0-SNAPSHOT.jar")});
        while(entries.hasMoreElements()) {
            var jarEntry = entries.nextElement();
            var classRealName = jarEntry.getRealName();
            System.out.println(classRealName);
            if (classRealName.endsWith(".class")) {
                var clazz = ucl.loadClass(classRealName.replaceAll("/", ".").substring(0, classRealName.length() - 6));
                var annotations = clazz.getDeclaredAnnotations();
                System.out.println(annotations);
                clazz.getDeclaredConstructor().newInstance();
            }

        }
        StringBuilder sb = new StringBuilder();
        sb.append("Length: ").append(ucl.getDefinedPackages().length).append("\n");
        for (var pkg : ucl.getDefinedPackages()) {
            sb.append("pkg: ").append(pkg.toString()).append("/n");

        }
        return sb.toString();

    }

    @GetMapping("/hello")
    public String hello() {
        try {
            var bean = context.getBean("SimpleBean");
            return "Simple bean load successfully";
        } catch (NoSuchBeanDefinitionException ex) {
            return ex.getMessage();
        }
    }
}
