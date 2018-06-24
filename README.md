# 条件注解

    @ConditionalOnClass：表示classpath存在相关的类才生效；
    
    @ConditionalOnMissingClass：表示classpath存在相关的类不生效；
    
    @ConditionalOnBean：表示如果存在相关Bean则该自动配置生效；
    
    @ConditionalOnMissingBean：表示如果存在相关Bean则该自动配置不生效；
    
    @ConditionalOnProperty：根据property条件约束；
    
    @ConditionalOnResource：根据Resource条件约束；
    
    @ConditionalOnExpression：根据SpEL表达式来约束；
    
# 分布式链路跟踪
    https://ai.google/research/pubs/pub36356
    http://www.ityouknow.com/springcloud/2018/02/02/spring-cloud-sleuth-zipkin.html