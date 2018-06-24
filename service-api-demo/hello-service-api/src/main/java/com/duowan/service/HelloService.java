package com.duowan.service;

import com.duowan.service.hystrix.HelloServiceHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Arvin
 */
@FeignClient(value = "hello-service", fallback = HelloServiceHystrix.class)
public interface HelloService {

    @RequestMapping(value = "/sayHello")
    String sayHello(@RequestParam("name") String name);
}
