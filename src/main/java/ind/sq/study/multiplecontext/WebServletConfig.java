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
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;


@Configuration
@ComponentScan("ind.sq.study.multiplecontext.web")
@EnableAutoConfiguration
@EnableWebMvc
public class WebServletConfig {
//    private final Logger logger = LoggerFactory.getLogger(getClass());
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new HandlerInterceptor() {
//            @Override
//            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//                logger.info("!!!!!!web");
//                return HandlerInterceptor.super.preHandle(request, response, handler);
//            }
//        });
//    }

//    @Bean
//    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
//        var requestMapping = new RequestMappingHandlerMapping();
//        requestMapping.setOrder(0);
//        return requestMapping;
//    }
}
