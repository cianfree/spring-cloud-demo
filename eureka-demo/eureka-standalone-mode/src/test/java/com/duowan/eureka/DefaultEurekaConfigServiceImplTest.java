package com.duowan.eureka;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Arvin
 */
public class DefaultEurekaConfigServiceImplTest {

    EurekaConfigService eurekaConfigService = new DefaultEurekaConfigServiceImpl();

    @Test
    public void testGetConfig() throws Exception {
        Map<String, String> map = eurekaConfigService.getConfig();

        System.out.println(map);
    }

    @Test
    public void testAdd() throws Exception {

        String zoneName = "asia-wuxi";
        String zones = "http://127.0.0.1:8763/eureka/,http://127.0.0.1:8763/eureka/,http://127.0.0.1:8763/eureka/";

        Map<String, String> map = eurekaConfigService.add(zoneName, zones);

        System.out.println(map);
    }

    @Test
    public void testDelete() throws Exception {

        String zoneName = "asia-wuxi";
        String zones = "http://127.0.0.1:8763/eureka/,http://127.0.0.1:8763/eureka/,http://127.0.0.1:8764/eureka/";

        Map<String, String> map = eurekaConfigService.delete(zoneName, zones);

        System.out.println(map);
    }
}