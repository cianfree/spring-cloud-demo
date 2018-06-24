package com.duowan.zuul.hystrix;

import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang.StringUtils;
import org.bouncycastle.cert.ocsp.Req;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.AbstractClientHttpResponse;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 断路器
 *
 * @author Arvin
 */
@Component
public class HelloServiceHystrix implements FallbackProvider {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 返回要进行熔断的服务，通常是注册在 Eureka 上的服务名称
     */
    @Override
    public String getRoute() {
        return "hello-service";
    }

    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {

        final RequestContext context = RequestContext.getCurrentContext();
        final HttpServletRequest request = context.getRequest();

        logger.info("Fallback: {}, {}, {}", route, request.getRequestURL().toString(), cause);

        return new AbstractClientHttpResponse() {
            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                String contentType = request.getContentType();

                if (StringUtils.isBlank(contentType)) {
                    contentType = MediaType.APPLICATION_JSON.toString();
                }

                headers.setContentType(MediaType.parseMediaType(contentType));
                return headers;
            }

            @Override
            public InputStream getBody() throws IOException {
                return new ByteArrayInputStream("The service is unavailable.".getBytes("UTF-8"));
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return HttpStatus.OK.value();
            }

            @Override
            public String getStatusText() throws IOException {
                return HttpStatus.OK.getReasonPhrase();
            }

            @Override
            public void close() {

            }
        };
    }
}
