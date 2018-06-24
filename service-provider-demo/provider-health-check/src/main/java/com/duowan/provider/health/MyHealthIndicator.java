package com.duowan.provider.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * spring cloud 实现了很多 HealthIndicator，默认注册了 org.springframework.boot.actuate.health.CompositeHealthIndicator
 *
 * 包含数据库，Redis，MQ 等大部分的检测实现，当然也可以自定义实现 com.netflix.appinfo.HealthCheckHandler
 *
 * @author Arvin
 */
@Component
public class MyHealthIndicator implements HealthIndicator {

    public static volatile boolean up = true;

    @Override
    public Health health() {
        return up ? Health.up().build() : Health.down().build();
    }
}
