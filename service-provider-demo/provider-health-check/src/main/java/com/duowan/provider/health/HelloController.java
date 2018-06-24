package com.duowan.provider.health;

import com.duowan.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Arvin
 */
@RestController
public class HelloController {

    @Autowired
    private HelloService helloService;

    @GetMapping("/sayHello")
    @ResponseBody
    public String sayHello(String name) {
        return helloService.sayHello(name);
    }

    @RequestMapping("/status/up/{up}")
    @ResponseBody
    public boolean up(@PathVariable boolean up) {

        MyHealthIndicator.up = up;

        return MyHealthIndicator.up;
    }
}
