package com.duowan.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 认证鉴权， 当请求中带有某个值的时候允许请求
 *
 * @author Arvin
 */
@Component
public class AuthZuulFilter extends ZuulFilter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String filterType() {
        // 指定在说明时候调用
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        // 指定顺序，数字越小优先级越高
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        // 表示是否需要执行该filter，true表示执行，false表示不执行
        return true;
    }

    @Override
    public Object run() throws ZuulException {

        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();

        logger.info("--->> AuthFilter {}, {}", request.getMethod(), request.getRequestURL().toString());

        String token = request.getParameter("token");
        // 此处简单是校验是否有token参数，有才允许继续路由
        if (StringUtils.isNotBlank(token)) {
            context.setSendZuulResponse(true);
        } else {
            // 不进行路由，立即返回
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(400);
            context.setResponseBody("Auth failed");
            context.set("isSuccess", false);
        }

        return null;
    }
}
