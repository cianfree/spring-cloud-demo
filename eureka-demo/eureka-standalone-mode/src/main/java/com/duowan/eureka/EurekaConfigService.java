package com.duowan.eureka;

import java.util.Map;

/**
 * @author Arvin
 */
public interface EurekaConfigService {

    /**
     * @return 返回全局Zones配置
     */
    Map<String, String> getConfig();

    /**
     * 添加
     *
     * @param zoneName zone 名称
     * @param zones    zones 列表，使用英文逗号分隔，如： http://localhost:8761/eureka/,http://localhost:8762/eureka/
     * @return 返回全局Zones配置
     */
    Map<String, String> add(String zoneName, String zones);

    /**
     * 删除
     *
     * @param zoneName zone 名称
     * @param zones    zones 列表，使用英文逗号分隔，如： http://localhost:8761/eureka/,http://localhost:8762/eureka/
     * @return 返回全局Zones配置
     */
    Map<String, String> delete(String zoneName, String zones);
}
