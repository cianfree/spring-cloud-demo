# RestTemplate 有点坑
    不能直接在 @PostConstruct 和 InitializingBean 接口上用，必须等整个应用起来了才行
    
    @Configuration
    public class MyConfiguration {
    
        @Bean
        @LoadBalanced
        public WebClient.Builder loadBalancedWebClientBuilder() {
            return WebClient.builder();
        }
    
    }
    
    Map<String, Object> paramMap = new HashMap<String, Object>() {
        {
            put("name", "Arvin");
        }
    };

    // 一定要用占位符, 下面两种写法都可以
    String text = this.restTemplate.getForObject("http://hello-service/sayHello?name={name}", String.class, paramMap);
    System.out.println("ResponseText1: " + text);
    text = this.restTemplate.getForObject("http://hello-service/sayHello?name={name}", String.class, "Arvin");
    System.out.println("ResponseText2: " + text);
    
# WebClient 使用
    <!--
    不是必须的，用 WebClient 才需要
    http://cloud.spring.io/spring-cloud-static/Finchley.RELEASE/single/spring-cloud.html#_spring_webclient_as_a_load_balancer_client -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-webflux</artifactId>
    </dependency>
    
    @Configuration
    public class MyConfiguration {
    
        @Bean
        @LoadBalanced
        public WebClient.Builder loadBalancedWebClientBuilder() {
            return WebClient.builder();
        }
    }
    
    Map<String, Object> paramMap = new HashMap<String, Object>() {
        {
            put("name", "Arvin");
        }
    };

    // 下面两种形式都可以调用
    Mono<String> mono = webClientBuilder.build().get().uri("http://hello-service/sayHello?name={name}", "Arvin")
            .retrieve().bodyToMono(String.class);

    System.out.println("WebClient ResponseText1: " + mono.block());

    mono = webClientBuilder.build().get().uri("http://hello-service/sayHello?name={name}", paramMap)
            .retrieve().bodyToMono(String.class);

    System.out.println("WebClient ResponseText2: " + mono.block());
    
# Feign 使用
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-feign</artifactId>
    </dependency>

    @SpringBootApplication
    @EnableDiscoveryClient
    @EnableFeignClients(basePackages = "com.duowan.service")    # 指定service扫描包
    public class ConsumerApplication {
    
    @FeignClient(value = "hello-service")
    public interface HelloService {
    
        @RequestMapping(value = "/sayHello")
        String sayHello(@RequestParam("name") String name);
    }
    
    @Autowired
    private HelloService helloService;

    public void testByHelloService() {
        System.out.println("ByHelloService: " + helloService.sayHello("Arvin"));
    }


# Hystrix 断路器
    参考链接： 
        http://cloud.spring.io/spring-cloud-static/Finchley.RELEASE/single/spring-cloud.html#spring-cloud-feign-hystrix
        http://www.ityouknow.com/springcloud/2017/05/16/springcloud-hystrix.html
        http://skaka.me/blog/2016/09/04/springcloud5/
        
## Hystrix特性
### 1.断路器机制
    断路器很好理解, 当Hystrix Command请求后端服务失败数量超过一定比例(默认50%), 断路器会切换到开路状态(Open). 这时所有请求会直接失败而不会发送到后端服务. 
    断路器保持在开路状态一段时间后(默认5秒), 自动切换到半开路状态(HALF-OPEN). 这时会判断下一次请求的返回情况, 如果请求成功, 断路器切回闭路状态(CLOSED), 
    否则重新切换到开路状态(OPEN). Hystrix的断路器就像我们家庭电路中的保险丝, 一旦后端服务不可用, 断路器会直接切断请求链, 避免发送大量无效请求影响系统吞吐量, 
    并且断路器有自我检测并恢复的能力.

### 2.Fallback
    Fallback相当于是降级操作. 对于查询操作, 我们可以实现一个fallback方法, 当请求后端服务出现异常的时候, 可以使用fallback方法返回的值. 
    fallback方法的返回值一般是设置的默认值或者来自缓存.

### 3.资源隔离
    在Hystrix中, 主要通过线程池来实现资源隔离. 通常在使用的时候我们会根据调用的远程服务划分出多个线程池. 例如调用产品服务的Command放入A线程池, 
    调用账户服务的Command放入B线程池. 这样做的主要优点是运行环境被隔离开了. 这样就算调用服务的代码存在bug或者由于其他原因导致自己所在线程池被耗尽时,
    不会对系统的其他服务造成影响. 但是带来的代价就是维护多个线程池会对系统带来额外的性能开销. 如果是对性能有严格要求而且确信自己调用服务的客户端代码不会
    出问题的话, 可以使用Hystrix的信号模式(Semaphores)来隔离资源.






















