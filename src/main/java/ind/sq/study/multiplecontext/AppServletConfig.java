package ind.sq.study.multiplecontext;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
@ComponentScan(basePackages = {"ind.sq.study.multiplecontext.app"})
@EnableAutoConfiguration
public class AppServletConfig implements WebMvcConfigurer {

    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        var handlerMapping = new RequestMappingHandlerMapping();
        handlerMapping.setOrder(0);
        return handlerMapping;
    }
}
