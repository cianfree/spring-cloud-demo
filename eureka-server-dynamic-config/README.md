# Eureka Server 配置动态配置
    实现 Region,Zones 的管理
    
    提供两个接口：
        1. /eureka/configs
            获取 Eureka service url 和 zones 配置
            JSON 格式数据：
            {
                "asia-wuxi": "http://localhost:8761/eureka/",
                "asia-yatai": "http://localhost:8762/eureka/"
            }
            
        2. /eureka/add
            添加 Eureka Server 节点
            参数：
                zoneName    所属机房名称，默认是 defaultZone
                zones       zone 配置, 如 http://localhost:8761/eureka/
            添加后返回所有配置
            
        3. /eureka/delete
            删除 Eureka Server 节点
            参数：
                zoneName    所属机房名称
                zones       zone 配置, 如 http://localhost:8761/eureka/
            删除后返回所有配置