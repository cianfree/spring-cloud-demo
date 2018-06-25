package com.duowan.eureka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 支持动态配置 ServiceUrls, 定时维护刷新 serviceUrl 和 availabilityZones 即可
 *
 * @author Arvin
 */
//@ConfigurationProperties(EurekaClientConfigBean.PREFIX)
//@Primary
//@Component
public class DynamicEurekaClientConfigBean extends EurekaClientConfigBean {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ScheduledExecutorService taskExecutor;

    private EurekaConfigService eurekaConfigService = new DefaultEurekaConfigServiceImpl();

    @PostConstruct
    public void init() {

        // 调度更新 ServiceUrl 和 Zone
        //scheduleUpdateServiceUrlAndZones();

    }

    private void scheduleUpdateServiceUrlAndZones() {

        logger.info("调度动态更新 Eureka serviceUrl 和 Zone");

        taskExecutor = Executors.newSingleThreadScheduledExecutor(
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r, "Eureka-Dynamic-serviceUrl and zones updater");
                        thread.setDaemon(true);
                        return thread;
                    }
                }
        );

        try {
            Runnable updateTask = new Runnable() {
                @Override
                public void run() {
                    try {

                        System.out.println("更新 Zones");

                        Map<String, String> remoteConfigs = eurekaConfigService.getConfig();
                        if (null != remoteConfigs && !remoteConfigs.isEmpty()) {
                            getServiceUrl().putAll(remoteConfigs);
                        }

                        System.out.println("从远程获取到ServiceUrl：" + getServiceUrl());

                    } catch (Throwable e) {
                        logger.error("Cannot update the Dynamic-serviceUrl and zones", e);
                    }

                }
            };
            updateTask.run();

            taskExecutor.scheduleWithFixedDelay(
                    updateTask,
                    10000,
                    10000,
                    TimeUnit.MILLISECONDS
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

    }

}
