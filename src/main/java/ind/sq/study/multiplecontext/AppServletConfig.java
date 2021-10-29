package ind.sq.study.multiplecontext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
@ComponentScan(basePackages = {"ind.sq.study.multiplecontext.app"})
@EnableAutoConfiguration
@EnableWebMvc
public class AppServletConfig {

//    private static final Logger logger = LoggerFactory.getLogger(AppServletConfig.class);
//
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new HandlerInterceptor() {
//            @Override
//            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//                logger.info("!!!!!!app");
//                return HandlerInterceptor.super.preHandle(request, response, handler);
//            }
//        });
//    }

//    @Bean
//    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
//        var handlerMapping = new RequestMappingHandlerMapping();
//        handlerMapping.setOrder(0);
//        handlerMapping.set
//        return handlerMapping;
//    }
}
