package com.duowan.provider.health;

import com.duowan.common.dns.CustomDnsUtil;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author Arvin
 */
@SpringBootApplication
@EnableEurekaClient
public class HelloApplicationProvider {

    public static void main(String[] args) {

        CustomDnsUtil.enabled();
        CustomDnsUtil.add("wx-zone", "127.0.0.1");
        CustomDnsUtil.add("sz-zone", "127.0.0.1");
        CustomDnsUtil.add("yt-zone", "127.0.0.1");

        new SpringApplicationBuilder(HelloApplicationProvider.class).web(WebApplicationType.SERVLET).run(args);

    }
}
