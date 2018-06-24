package com.duowan.consumer;

import com.duowan.common.dns.CustomDnsUtil;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

/**
 * @author Arvin
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.duowan"})
@ComponentScans({
        @ComponentScan("com.duowan.service.hystrix")
})
public class ConsumerApplication {

    public static void main(String[] args) {

        CustomDnsUtil.enabled();
        CustomDnsUtil.add("wx-zone", "127.0.0.1");
        CustomDnsUtil.add("sz-zone", "127.0.0.1");
        CustomDnsUtil.add("yt-zone", "127.0.0.1");

        new SpringApplicationBuilder(ConsumerApplication.class).web(WebApplicationType.SERVLET).run(args);
    }

}
