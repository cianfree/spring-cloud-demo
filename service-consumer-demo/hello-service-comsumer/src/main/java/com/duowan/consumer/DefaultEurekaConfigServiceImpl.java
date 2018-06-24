package com.duowan.consumer;

import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Arvin
 */
public class DefaultEurekaConfigServiceImpl implements EurekaConfigService {

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public Map<String, String> getConfig() {

        Map resultMap = restTemplate.getForObject("http://localhost:9090/eureka/configs", Map.class);

        return toStringMap(resultMap);
    }

    protected Map<String, String> toStringMap(Map resultMap) {
        if (resultMap.isEmpty()) {
            return null;
        }

        Map<String, String> map = new HashMap<>();
        for (Object key : resultMap.keySet()) {
            map.put(String.valueOf(key), String.valueOf(resultMap.get(key)));
        }

        return map;
    }

    @Override
    public Map<String, String> add(String zoneName, String zones) {
        Map resultMap = restTemplate.getForObject("http://localhost:9090/eureka/add?zoneName={zoneName}&zones={zones}",
                Map.class, zoneName, zones);

        return toStringMap(resultMap);
    }

    @Override
    public Map<String, String> delete(String zoneName, String zones) {
        Map resultMap = restTemplate.getForObject("http://localhost:9090/eureka/delete?zoneName={zoneName}&zones={zones}",
                Map.class, zoneName, zones);
        return toStringMap(resultMap);
    }
}
