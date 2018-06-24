package com.duowan.provider.health.impl;

import com.duowan.service.HelloService;
import org.springframework.stereotype.Service;

/**
 * @author Arvin
 */
@Service
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "Hello, " + name;
    }
}
