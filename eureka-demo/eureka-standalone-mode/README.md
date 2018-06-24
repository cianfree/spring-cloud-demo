# 单节点 Eureka 部署


    
# 自我保护机制
    https://blog.csdn.net/akaks0/article/details/79512680
    
    在某一些时候注册在Eureka的服务已经挂掉了，但是服务却还留在Eureka的服务列表的情况。
    
    1.Eureka服务端
    Eureka服务端的配置application.yml：
    
    server:
      port: 9501
    
    eureka:
      instance:
        hostname: 127.0.0.1
      client:
        registerWithEureka: false
        fetchRegistry: false
        serviceUrl:
          defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
      server:
        # 关闭自我保护机制
        enable-self-preservation: false
        # 每隔10s扫描服务列表，移除失效服务
        eviction-interval-timer-in-ms: 10000
        
    默认情况下，如果Eureka Server在一定时间内（默认90秒）没有接收到某个微服务实例的心跳，Eureka Server将会移除该实例。但是当网络分区故障发生时，微服务与Eureka Server之间无法正常通信，而微服务本身是正常运行的，此时不应该移除这个微服务，所以引入了自我保护机制。
    自我保护模式正是一种针对网络异常波动的安全保护措施，使用自我保护模式能使Eureka集群更加的健壮、稳定的运行。
    自我保护机制的工作机制是如果在15分钟内超过85%的客户端节点都没有正常的心跳，那么Eureka就认为客户端与注册中心出现了网络故障，Eureka Server自动进入自我保护机制，此时会出现以下几种情况：
    
        1、Eureka Server不再从注册列表中移除因为长时间没收到心跳而应该过期的服务。
        2、Eureka Server仍然能够接受新服务的注册和查询请求，但是不会被同步到其它节点上，保证当前节点依然可用。
        3、当网络稳定时，当前Eureka Server新的注册信息会被同步到其它节点中。
        
    因此Eureka Server可以很好的应对因网络故障导致部分节点失联的情况，而不会像ZK那样如果有一半不可用的情况会导致整个集群不可用而变成瘫痪。
    
        # 该配置可以移除这种自我保护机制，防止失效的服务也被一直访问 (Spring Cloud默认该配置是 true)
        eureka.server.enable-self-preservation: false
        
        # 该配置可以修改检查失效服务的时间，每隔10s检查失效服务，并移除列表 (Spring Cloud默认该配置是 60s)
        eureka.server.eviction-interval-timer-in-ms: 10
        
    2.Eureka客户端
    Eureka客户端的配置application.yml：
    
    eureka:
      instance:
        # 每隔10s发送一次心跳
        lease-renewal-interval-in-seconds: 10
        # 告知服务端30秒还未收到心跳的话，就将该服务移除列表
        lease-expiration-duration-in-seconds: 30
      client:
        serviceUrl:
          defaultZone: http://localhost:9501/eureka/
    
    server:
      port: 9502
    spring:
      application:
        name: service-hi
        
    # 该配置指示eureka客户端需要向eureka服务器发送心跳的频率  (Spring Cloud默认该配置是 30s)
    eureka.instance.lease-renewal-interval-in-seconds: 10
    
    # 该配置指示eureka服务器在接收到最后一个心跳之后等待的时间，然后才能从列表中删除此实例 (Spring Cloud默认该配置是 90s)
    eureka.instance.lease-expiration-duration-in-seconds: 30

# Eureka serviceUrl 配置
    org.springframework.cloud.netflix.eureka.EurekaClientConfigBean.getEurekaServerServiceUrls
    com.netflix.eureka.cluster.PeerEurekaNodes.start
    com.netflix.eureka.cluster.PeerEurekaNodes.updatePeerEurekaNodes
    
    // 刷新 Eureka 配置的方法
    // 更新频率，默认是10分钟：org.springframework.cloud.netflix.eureka.server.EurekaServerConfigBean.peerEurekaNodesUpdateIntervalMs
    //        更新频率配置项：eureka.server.peer-eureka-nodes-update-interval-ms
    com.netflix.eureka.cluster.PeerEurekaNodes.resolvePeerUrls
    
    因此如要要进行动态的 Eureka 配置，需要改造这个方法实现或者内部的方法实现
    
    com.netflix.appinfo.ApplicationInfoManager.getInfo
    com.netflix.discovery.EurekaClientConfig.getRegion
    com.netflix.discovery.EurekaClientConfig.getAvailabilityZones
    com.netflix.discovery.endpoint.EndpointUtils.getDiscoveryServiceUrls
    
    protected List<String> resolvePeerUrls() {
        InstanceInfo myInfo = applicationInfoManager.getInfo();
        String zone = InstanceInfo.getZone(clientConfig.getAvailabilityZones(clientConfig.getRegion()), myInfo);
        List<String> replicaUrls = EndpointUtils
                .getDiscoveryServiceUrls(clientConfig, zone, new EndpointUtils.InstanceInfoBasedUrlRandomizer(myInfo));

        int idx = 0;
        while (idx < replicaUrls.size()) {
            if (isThisMyUrl(replicaUrls.get(idx))) {
                replicaUrls.remove(idx);
            } else {
                idx++;
            }
        }
        return replicaUrls;
    }
    
    要实现动态配置，至少需要提供两组参数：
        新服务器 Region -> zone, 
            即维护：
                org.springframework.cloud.netflix.eureka.EurekaClientConfigBean.serviceUrl
                org.springframework.cloud.netflix.eureka.EurekaClientConfigBean.availabilityZones
        
        这个 serviceUrl 的配置格式是：
            zone 机房名称 --> Eureka 服务列表，使用英文逗号分隔如： 
                            http://localhost:8761/eureka/,http://localhost:8762/eureka/
                            
        这个 availabilityZones 的配置格式是：
            [region -> zone] --> 表示当前启动的 Eureka Server 所属地区，以及本 Eureka Server 需要优先使用的 Zone
            
        配置实例：
            eureka:
              client:
                region: wuxi
                availability-zones:
                  wuxi: myzone
                  yatai: ytzone
                service-url:
                  # 设置与Eureka Server交互的地址，查询服务和注册服务都需要依赖这个地址。默认是http://localhost:8761/eureka ；多个地址可使用 , 分隔。
                  # 注意不要用 default-zone, 不然不可以，被坑过了
                  defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
                  myzone: http://${eureka.instance.hostname}:${server.port}/eureka/
                  ytzone: http://${eureka.instance.hostname}:${server.port}/eureka/

        思路：
            1. 设置一个配置中心，配置 Region 和 zone
            2. 自定义 EurekaClientConfigBean， 调度任务，定时刷新从配置中心同步 region 和 zone的配置
            3. 启动 Eureka server 的时候，自动注册到配置中心，配置中心加入新节点
            

# 多区域多机房测试实践
    一、背景
        用户量比较大或者用户地理位置分布范围很广的项目，一般都会有多个机房。这个时候如果上线springCloud服务的话，我们希望一个机房内的服务优先调用同一个
        机房内的服务，当同一个机房的服务不可用的时候，再去调用其它机房的服务，以达到减少延时的作用。
        
    二、概念
        eureka提供了region和zone两个概念来进行分区，这两个概念均来自于亚马逊的AWS：
        （1）region：可以简单理解为地理上的分区，比如亚洲地区，或者华北地区，再或者北京等等，没有具体大小的限制。根据项目具体的情况，可以自行合理划分region。
        （2）zone：可以简单理解为region内的具体机房，比如说region划分为北京，然后北京有两个机房，就可以在此region之下划分出zone1,zone2两个zone。
    
    
    测试场景假定为在一个地区的多个机房：
    region: china
    wx-zone: 无锡机房
    yt-zone: 亚太机房
    sz-zone: 深圳机房


























