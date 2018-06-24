# zuul 服务路由网关
    官方文档： http://cloud.spring.io/spring-cloud-static/Finchley.RELEASE/single/spring-cloud.html#_router_and_filter_zuul
    
    Netflix uses Zuul for the following:
    
    Authentication
    Insights
    Stress Testing
    Canary Testing
    Dynamic Routing
    Service Migration
    Load Shedding
    Security
    Static Response handling
    Active/Active traffic management
  
# Zuul中默认实现的Filter
        类型	    顺序	    过滤器	                功能
        ------------------------------------------------------------------------------
        pre	    -3	    ServletDetectionFilter	标记处理Servlet的类型
        pre	    -2	    Servlet30WrapperFilter	包装HttpServletRequest请求
        pre	    -1	    FormBodyWrapperFilter	包装请求体
        route	1	    DebugFilter	            标记调试标志
        route	5	    PreDecorationFilter	    处理请求上下文供后续使用
        route	10	    RibbonRoutingFilter	    serviceId请求转发
        route	100	    SimpleHostRoutingFilter	url请求转发
        route	500	    SendForwardFilter	    forward请求转发
        post	0	    SendErrorFilter	        处理有错误的请求响应
        post	1000	SendResponseFilter	    处理正常的请求响应
        ------------------------------------------------------------------------------
        
        禁用指定的Filter
        
        可以在application.yml中配置需要禁用的filter，格式：
        
        zuul:
        	FormBodyWrapperFilter:
        		pre:
        			disable: true

# 自定义 ZuulFilter
    可以通过自定义 Filter 实现请求的鉴权，查看示例 AuthZuulFilter
    
    通过上面这例子我们可以看出，我们可以使用“PRE”类型的Filter做很多的验证工作，在实际使用中我们可以结合shiro、oauth2.0等技术去做鉴权、验证。


# 路由熔断
    当我们的后端服务出现异常的时候，我们不希望将异常抛出给最外层，期望服务可以自动进行一降级。Zuul给我们提供了这样的支持。当某个服务出现异常时，
    直接返回我们预设的信息。
    
    我们通过自定义的fallback方法，并且将其指定给某个route来实现该route访问出问题的熔断处理。主要继承ZuulFallbackProvider接口来实现，
    ZuulFallbackProvider默认有两个方法，一个用来指明熔断拦截哪个服务，一个定制返回内容。
    
    http://cloud.spring.io/spring-cloud-static/Finchley.RELEASE/single/spring-cloud.html#_router_and_filter_zuul
        Providing Hystrix Fallbacks For Routes
        
    ********* Zuul 目前只支持服务级别的熔断，不支持具体到某个URL进行熔断。
    
    演示的时候，应该是微服务启动了（如 hello-service）, 然后启动 ZuulApplication， 先访问 hello-service, 此时正常
    
    停掉 hello-service， 通过 zuul 再次访问，会进入熔断， 但是等 ZuulApplication 重新从EurekaService读取服务列表的时候
    此时 hello-service 一个都没有，那么就会报 404， 找不到 404 而不进入熔断


# Zuul Timeouts
    If you want to configure the socket timeouts and read timeouts for requests proxied through Zuul, 
    you have two options, based on your configuration:
    
    If Zuul uses service discovery, you need to configure these timeouts with the 【ribbon.ReadTimeout】 
    and 【ribbon.SocketTimeout】 Ribbon properties.
    If you have configured Zuul routes by specifying URLs, you need to use 【zuul.host.connect-timeout-millis】
    and 【zuul.host.socket-timeout-millis】.






# 参考文章
    纯洁的微笑：  
        https://www.cnblogs.com/ityouknow/p/6944096.html
        http://www.ityouknow.com/springcloud/2018/01/20/spring-cloud-zuul.html
        
    http://tech.lede.com/2017/05/16/rd/server/SpringCloudZuul/
        
