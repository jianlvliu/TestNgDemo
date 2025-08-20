package com.learn.testngdemo;

import com.alibaba.fastjson.JSONObject;
import com.learn.testngdemo.util.HttpHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

/**
 * ExampleTest
 *
 * @author: liujiahong
 * @date: 2025/8/13 19:46
 * @description:
 */
@SpringBootTest
public class ExampleTest extends AbstractTestNGSpringContextTests {
    @Autowired
    private HttpHelper httpHelper;


    @Test
    public void getExample(){
        String s = httpHelper.httpGet("http://httpbin.org/get?key1=value1&key2=value2");
    }

    @Test
    public void postExample(){
        String s = httpHelper.httpPost("http://httpbin.org/post","{\n" +
                "  \"name\": \"测试用户\",\n" +
                "  \"age\": 25\n" +
                "}");
    }





}
