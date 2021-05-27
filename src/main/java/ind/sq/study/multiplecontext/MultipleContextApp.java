package ind.sq.study.multiplecontext;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletPath;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@SpringBootApplication
@ComponentScan(basePackages = "ind.sq.study.multiplecontext.share")
public class MultipleContextApp {
    public static void main(String[] args) {
        SpringApplication.run(MultipleContextApp.class, args);
    }

    @Bean
    public ServletRegistrationBean webApi(ApplicationContext parent) {
        return createChildContextServlet(parent, "/web/*", "web-servlet", WebServletConfig.class);
    }

    @Bean
    public ServletRegistrationBean appApi(ApplicationContext parent) {
        return createChildContextServlet(parent, "/app/*", "app-servlet", AppServletConfig.class);
    }

    private ServletRegistrationBean createChildContextServlet(ApplicationContext parent, String path, String name, Class<?> configClass) {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();

        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.setParent(parent);
        ctx.register(configClass);
        ctx.refresh();
        dispatcherServlet.setApplicationContext(ctx);

        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(dispatcherServlet, path);
        servletRegistrationBean.setName(name);

        return servletRegistrationBean;
    }

}
