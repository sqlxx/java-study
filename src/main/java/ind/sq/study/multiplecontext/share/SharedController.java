package ind.sq.study.multiplecontext.share;

import ind.sq.study.multiplecontext.app.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SharedController {

    @Autowired
    private SharedService service;

    @RequestMapping("/hello1")
    public String hello() {
        return "In Shared controller:" + service.hello();

    }

}
