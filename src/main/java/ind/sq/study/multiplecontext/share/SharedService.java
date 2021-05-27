package ind.sq.study.multiplecontext.share;

import org.springframework.stereotype.Service;

@Service
public class SharedService {

    public String  hello() {
        return "I'm a happy shared service";
    }
}
