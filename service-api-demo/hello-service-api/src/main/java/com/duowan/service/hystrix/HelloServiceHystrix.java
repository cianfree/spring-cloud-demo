package com.duowan.service.hystrix;

import com.duowan.service.HelloService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 如果已经存在 HelloService 实例该配置不生效
 *
 * @author Arvin
 */
@Component
public class HelloServiceHystrix implements HelloService {

    @Override
    public String sayHello(@RequestParam("name") String name) {
        return "Hello " + name + ", 失败的请求，Hystrix！";
    }

}
