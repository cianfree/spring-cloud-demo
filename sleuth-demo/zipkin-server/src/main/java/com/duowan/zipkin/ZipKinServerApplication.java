package com.duowan.zipkin;

import com.duowan.common.dns.CustomDnsUtil;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import zipkin.server.internal.EnableZipkinServer;
import zipkin2.storage.mysql.v1.MySQLStorage;

import javax.sql.DataSource;

/**
 * @author Arvin
 */
@SpringBootApplication
@EnableZipkinServer
@EnableDiscoveryClient
public class ZipKinServerApplication {

    public static void main(String[] args) {
        CustomDnsUtil.enabled();
        CustomDnsUtil.add("wx-zone", "127.0.0.1");
        CustomDnsUtil.add("sz-zone", "127.0.0.1");
        CustomDnsUtil.add("yt-zone", "127.0.0.1");

        new SpringApplicationBuilder(ZipKinServerApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }

    @Bean
    public MySQLStorage mySQLStorage(DataSource dataSource) {

        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        return MySQLStorage.newBuilder().datasource(dataSource).executor(Runnable::run).build();
    }

}
