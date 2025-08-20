package com.learn.testngdemo.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * HttpHelper
 *
 * @author: liujiahong
 * @date: 2025/7/10 16:51
 * @description:
 */
@Slf4j
@Component
public class HttpHelper {

    @Autowired
    private CloseableHttpClient closeableHttpClient;


    /**
     * get 请求
     *
     * @param url
     * @return null
     */
    public String httpGet(String url){
        log.info("请求地址：" + url);
        SSLContext sslContext = null;
        try {
            sslContext = SSLContexts.custom()
                    .loadTrustMaterial(null, (certificate, authType) -> true).build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            e.printStackTrace();
        }
        int defaultTimeout = 20000;
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setConnectTimeout(defaultTimeout)
                .setSocketTimeout(defaultTimeout)
                .setConnectionRequestTimeout(defaultTimeout)
                .build();
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLContext(sslContext)
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .setDefaultRequestConfig(defaultRequestConfig)
                .build();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        httpGet.setConfig(defaultRequestConfig);
        httpGet.addHeader("Content-Type", "application/json");

        try {
            response = httpClient.execute(httpGet, new BasicHttpContext());
            if (response.getStatusLine().getStatusCode() != 200) {

                log.error("request url failed, http code=" + response.getStatusLine().getStatusCode()
                        + ", url=" + url);
                return null;
            }

            // 如果接口接通，状态码为200。返回 响应
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String resultStr = EntityUtils.toString(entity, "utf-8");
                log.info("响应内容：{}", resultStr);
                return resultStr;
            }

        } catch (IOException e) {
            log.error("request url=" + url + ", exception, msg=" + e.getMessage());
        } finally {
            if (response != null) try {
                response.close();
            } catch (IOException e) {
                log.error(String.valueOf(e));
            }
        }
        return null;
    }

    /**
     * post 请求
     *
     * @param url
     * @return null
     */
    public String httpPost(String url,String map){
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        httpPost.addHeader("Content-Type", "application/json;charset=utf-8");

        String body = "";
        HttpEntity entity = null;

        try {
            JSONObject request = JSON.parseObject(map);
            JSONObject jsonObject = new JSONObject(request);
            //加utf-8解决乱码
            StringEntity stringEntity = new StringEntity(jsonObject.toString(), "utf-8");

            //设置参数到请求对象中
            httpPost.setEntity(stringEntity);

            log.info("请求地址：" + url);
            log.info("请求参数：" + jsonObject);


            // 执行Post请求操作，并拿到结果
            response = closeableHttpClient.execute(httpPost);

            // 如果响应状态码不等于200，说明接口没调通
            if (response.getStatusLine().getStatusCode() != 200) {
                log.error("响应码：：" + response.getStatusLine() + "响应内容：" + response.getEntity());
                return "请求失败，失败信息" + response.getStatusLine() + response.getEntity();
            }

            //获取结果实体
            entity = response.getEntity();
            // 换新的entity 处理逻辑
            if (entity != null) {
                String responseBody = EntityUtils.toString(entity, "utf-8");
                log.info("响应内容：{}", responseBody);
                return responseBody;
            }
        } catch (Exception e) {
            System.out.println("request url=" + url + ", exception, msg=" + e.getMessage());
            log.error("错误信息：" + e);
        } finally {
            if (response != null) try {
                response.close();
            } catch (IOException e) {
                log.error("错误信息：" + e);
            }
        }
        return "请求失败，失败信息：" + response.getEntity();
    }

}

