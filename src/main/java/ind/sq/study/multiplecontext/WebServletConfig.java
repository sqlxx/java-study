package ind.sq.study.multiplecontext;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
@ComponentScan("ind.sq.study.multiplecontext.web")
@EnableAutoConfiguration
public class WebServletConfig implements WebMvcConfigurer {
    @Bean
    public RequestMappingHandlerAdapter requestMappingHandler() {
        return new RequestMappingHandlerAdapter();
    }

    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        var requestMapping = new RequestMappingHandlerMapping();
        requestMapping.setOrder(0);
        return requestMapping;
    }
}
