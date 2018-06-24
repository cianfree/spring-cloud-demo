package com.duowan.eureka.dynamic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Arvin
 */
@Controller
@RequestMapping("/eureka/")
public class EurekaServerConfigController {

    /**
     * ZoneName --> ZoneConfig
     */
    private Map<String, List<String>> configMap = new HashMap<>();

    /**
     * 获取 配置
     */
    @RequestMapping("/configs")
    @ResponseBody
    public Map<String, String> configs() {

        Map<String, String> resultMap = new HashMap<>();

        if (configMap.isEmpty()) {
            return resultMap;
        }

        for (Map.Entry<String, List<String>> entry : configMap.entrySet()) {

            String zoneName = entry.getKey();
            List<String> zones = entry.getValue();

            if (StringUtils.isEmpty(zoneName) || zones == null || zones.isEmpty()) {
                continue;
            }

            resultMap.put(zoneName, StringUtils.arrayToCommaDelimitedString(zones.toArray()));

        }

        return resultMap;

    }

    /**
     * 添加
     */
    @RequestMapping("/add")
    @ResponseBody
    public Map<String, String> add(String zoneName, String zones) {

        if (StringUtils.isEmpty(zoneName) || StringUtils.isEmpty(zones)) {
            return configs();
        }

        String[] zoneArray = StringUtils.delimitedListToStringArray(zones, ",");

        List<String> list = configMap.get(zoneName);
        if (null == list || list.isEmpty()) {
            list = new ArrayList<>();
            configMap.put(zoneName, list);
        }

        for (String zone : zoneArray) {
            if (StringUtils.isEmpty(zone)) {
                continue;
            }
            String trimZone = zone.trim();
            if (list.contains(trimZone)) {
                continue;
            }
            list.add(trimZone);
        }

        return configs();
    }


    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Map<String, String> delete(String zoneName, String zones) {

        if (StringUtils.isEmpty(zoneName) || StringUtils.isEmpty(zones)) {
            return configs();
        }

        String[] zoneArray = StringUtils.delimitedListToStringArray(zones, ",");

        List<String> list = configMap.get(zoneName);
        if (null == list || list.isEmpty()) {
            return configs();
        }

        for (String zone : zoneArray) {
            if (StringUtils.isEmpty(zone)) {
                continue;
            }
            String trimZone = zone.trim();
            if (list.contains(trimZone)) {
                list.remove(trimZone);
            }
        }

        return configs();
    }
}
