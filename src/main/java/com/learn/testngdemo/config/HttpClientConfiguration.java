package com.learn.testngdemo.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * HttpClientConfiguration
 *
 * @author: liujiahong
 * @date: 2025/7/10 16:38
 * @description:
 */
@Configuration
public class HttpClientConfiguration {

    @Bean
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        // 最大连接 10
        manager.setMaxTotal(10);
        // 非活动时间 10秒
        manager.setValidateAfterInactivity(10000);
        return manager;
    }

    @Bean
    public HttpClientBuilder httpClientBuilder() {
        HttpClientBuilder builder = HttpClientBuilder.create();
        builder.setConnectionManager(poolingHttpClientConnectionManager());
        return builder;
    }


    @Bean
    public CloseableHttpClient closeableHttpClient(HttpClientBuilder builder) {
        return builder.build();
    }

    @Bean
    public RequestConfig.Builder requestConfigBuilder() {
        RequestConfig.Builder builder = RequestConfig.custom();
        // 请求连接超时时间
        return builder.setConnectionRequestTimeout(10000)
                // 连接超时时间
                .setConnectTimeout(10000)
                .setSocketTimeout(10000);
    }

    @Bean
    public RequestConfig requestConfig(RequestConfig.Builder builder) {
        return builder.build();
    }
}
