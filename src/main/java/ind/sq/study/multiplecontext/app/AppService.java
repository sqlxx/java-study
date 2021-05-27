package ind.sq.study.multiplecontext.app;

import org.springframework.stereotype.Service;

@Service
public class AppService {
    public String hello() {
        return "Hello in App service";
    }
}
