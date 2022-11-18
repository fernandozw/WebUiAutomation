package com.thinkingdata.lib;

import com.alibaba.fastjson.JSONObject;
import com.thinkingdata.Application;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2022/10/19 17:19
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Component
public class RequestTools {
    private Logger log = LoggerFactory.getLogger(RequestTools.class);


    /**
     * 发送post请求
     *
     * @param url         请求地址
     * @param requestBody 请求体
     * @return 返回结果
     */
    public Map<String, Object> doPost(String url, String requestBody) {


        JSONObject jsonResult;
        // 获取HttpClient对象
        CloseableHttpClient httpClient;
        // 发送请求获取返回结果
        CloseableHttpResponse response;
        try {
            httpClient = HttpClients.createDefault();

            // 声明Post请求
            HttpPost httpPost = new HttpPost(url);
            // 设置请求头
            httpPost.addHeader("Content-Type", "application/json; charset=utf-8");
            httpPost.addHeader("Accept", "application/json");
            httpPost.setEntity(new StringEntity(requestBody, "UTF-8"));
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity responseEntity = response.getEntity();
                // 获取返回的信息
                jsonResult = JSONObject.parseObject(EntityUtils.toString(responseEntity));
            } else {

                String errorResponse = "{\"code\":201,\"msg\":\"" + response.getStatusLine().toString() + "\"}";
                jsonResult = JSONObject.parseObject(errorResponse);
            }
            // 关闭response
            response.close();
            // 关闭HttpClient资源
            httpClient.close();
        } catch (Exception e) {
            log.error("发送请求时出现异常:{}",e);
            String errorResponse =  String.format("{\"code\":201,\"msg\":\"发送请求时出现异常:%s\"}",e);
            jsonResult = JSONObject.parseObject(errorResponse);
        }
        return jsonResult;
    }

    @Test
    public void test1() {
        System.out.println(doPost("http://127.0.0.1:5000/interface", "{}"));
    }
}