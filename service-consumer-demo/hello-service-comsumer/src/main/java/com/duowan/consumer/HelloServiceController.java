package com.duowan.consumer;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Arvin
 */
@Controller
public class HelloServiceController implements InitializingBean {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient loadBalancer;

    @Autowired
    private DiscoveryClient discoveryClient;

    @PostConstruct
    public void init() {

        System.out.println(discoveryClient);

        System.out.println("HelloService: " + helloService.getClass());

        //testRestTemplateByLoadBalancerClient();


    }

    @ResponseBody
    @RequestMapping("/test")
    public String test(String name) {

        String responseText = "UNKNOWN";
//        responseText = testRestTemplateByAnnotation();
//
//
//        responseText = testByWebClient();

        responseText = testByHelloService(name);


        return responseText;
    }

    @ResponseBody
    @RequestMapping("/sayHello")
    public String sayHello(String name) {

        long begTime = System.currentTimeMillis();
        String responseText = helloService.sayHello(name);

        return responseText + ", 耗时 " + (System.currentTimeMillis() - begTime) + " 毫秒！";
    }

    @Autowired
    private com.duowan.service.HelloService helloService;

    public String testByHelloService(String name) {

        String responseText = helloService.sayHello(name);

        System.out.println("ByHelloService: " + responseText);


        return responseText;


    }

    @Autowired
    private WebClient.Builder webClientBuilder;

    public void testByWebClient() {

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


    }

    public void testRestTemplateByAnnotation() {
        System.out.println(restTemplate);

        //String text = this.restTemplate.getForObject("http://hello-service/sayHello?name=Arvin", String.class);

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

    }

    public void testRestTemplateByLoadBalancerClient() {
        System.out.println(restTemplate);

        ServiceInstance instance = loadBalancer.choose("hello-service");

        System.out.println("ServiceUri: " + instance.getUri().toString());

        //String text = this.restTemplate.getForObject("http://hello-service/sayHello?name=Arvin", String.class);
        String text = this.restTemplate.getForObject(instance.getUri().toString() + "/sayHello?name=Arvin", String.class);

        System.out.println("ResponseText: " + text);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //testRestTemplateByAnnotation();
    }
}
