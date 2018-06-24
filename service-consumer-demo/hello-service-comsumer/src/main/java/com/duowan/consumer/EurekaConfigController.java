package com.duowan.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Arvin
 */
@Controller
public class EurekaConfigController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @ResponseBody
    @RequestMapping("/instanceInfo")
    public Object instanceInfo() {

        System.out.println(discoveryClient);

        List<String> serviceNames = discoveryClient.getServices();

        List<ServiceInstance> serviceInstanceList = new ArrayList<>();

        for (String serviceId : serviceNames) {
            List<ServiceInstance> serviceInstances = discoveryClient.getInstances(serviceId);
            if (null != serviceInstances && !serviceInstances.isEmpty()) {
                serviceInstanceList.addAll(serviceInstances);
            }
        }

        return serviceInstanceList;
    }
}
