package ind.sq.study.multiplecontext.web;

import ind.sq.study.multiplecontext.share.SharedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {
    @Autowired
    private SharedService service;

    @GetMapping("/hello")
    public String hello() {
        return "In Web: " + service.hello();
    }
}
