package com.duowan.provider.hystrix;

import com.duowan.provider.HelloService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Arvin
 */
@Component
public class HelloServiceHystrix implements HelloService {

    @Override
    public String sayHello(@RequestParam("name") String name) {
        return "Hello " + name + ", 失败的请求，Hystrix！";
    }
}
