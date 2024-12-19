package ind.sq.study.multiplecontext.share;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;


public class SharedConfig {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                logger.info("!!!!!!----");
                return HandlerInterceptor.super.preHandle(request, response, handler);
            }
        });
    }
}
