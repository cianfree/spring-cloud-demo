package com.duowan.eureka.dynamic;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author Arvin
 */
@SpringBootApplication
public class DynamicEurekaServerConfigApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(DynamicEurekaServerConfigApplication.class).web(WebApplicationType.SERVLET).run(args);
    }
}
