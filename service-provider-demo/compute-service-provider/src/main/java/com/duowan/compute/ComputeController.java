package com.duowan.compute;

import com.duowan.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Arvin
 */
@RestController
public class ComputeController {

    @Autowired
    private HelloService helloService;

    @RequestMapping("/sum/{a}/{b}")
    public Integer sum(@PathVariable Integer a, @PathVariable Integer b) {
        a = a == null ? 0 : a;
        b = b == null ? 0 : b;

        return a + b;
    }

    @RequestMapping("/sayHello")
    public String sayHello(String name) {
        return helloService.sayHello(name);
    }
}
