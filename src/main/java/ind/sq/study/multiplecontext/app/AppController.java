package ind.sq.study.multiplecontext.app;

import ind.sq.study.multiplecontext.share.SharedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

    @Autowired
    private SharedService service;
    @Autowired
    private AppService appService;

    @GetMapping("/hello")
    public String hello() {
        return "In App: " + service.hello() + appService.hello();
    }
}
