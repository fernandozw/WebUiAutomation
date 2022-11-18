package com.thinkingdata.webui.serviceUi;

import com.thinkingdata.response.ResponseDataUtils;
import com.thinkingdata.response.ResponseData;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/6/8 15:44
 */
@Service
public class ConfigService {
    private static final String HEADLESS_KEY = "browserHeadless";
    private static final String BROWSER_LANGUAGE = "browserLanguage";
    private static final String WINDOW_SIZE = "windowSize";
    private static final String DEBUG_PACKAGE = "debugPackage.zip";

    @Autowired
    @Qualifier("redis_01")
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取浏览器无头模式配置
     *
     * @return
     */
    public Object getHeadless() {
        ResponseData<Object> responseData = null;
        Map<String, String> dataMap = new HashMap<String, String>();
        String headless = stringRedisTemplate.opsForValue().get(HEADLESS_KEY);
        dataMap.put("headless", headless);
        responseData = ResponseDataUtils.buildSuccess(dataMap);
        return responseData;
    }


    /**
     * 更新浏览器无头模式配置
     *
     * @param map 请求参数
     * @return
     */
    public Object updateHeadless(Map<String, String> map) {
        ResponseData<Object> responseData = null;
        stringRedisTemplate.opsForValue().set(HEADLESS_KEY, map.get("headless"));
        responseData = ResponseDataUtils.buildUpdateSuccess();
        return responseData;
    }

    /**
     * 更新浏览器语言配置
     *
     * @param map 请求参数
     * @return
     */
    public Object updateLanguage(Map<String, String> map) {
        ResponseData<Object> responseData = null;
        stringRedisTemplate.opsForValue().set(BROWSER_LANGUAGE, map.get("language"));
        responseData = ResponseDataUtils.buildUpdateSuccess();
        return responseData;
    }

    /**
     * 获取浏览器语言配置
     *
     * @return
     */
    public Object getLanguage() {
        ResponseData<Object> responseData = null;
        Map<String, String> dataMap = new HashMap<String, String>();
        String language = stringRedisTemplate.opsForValue().get(BROWSER_LANGUAGE);
        dataMap.put("language", language);
        responseData = ResponseDataUtils.buildSuccess(dataMap);
        return responseData;
    }


    /***
     * 更新浏览器窗口分辨率
     * @param map
     * @return
     */
    public Object updateWindowSize(Map<String, String> map) {
        ResponseData<Object> responseData;
        stringRedisTemplate.opsForValue().set(WINDOW_SIZE, map.get("windowSize"));
        responseData = ResponseDataUtils.buildUpdateSuccess();
        return responseData;
    }


    /**
     * 获取浏览器窗口分辨率
     *
     * @return
     */
    public Object getWindowSize() {
        ResponseData<Object> responseData;
        Map<String, String> dataMap = new HashMap<String, String>();
        String windowSize = stringRedisTemplate.opsForValue().get(WINDOW_SIZE);
        dataMap.put("windowSize", windowSize);
        responseData = ResponseDataUtils.buildSuccess(dataMap);
        return responseData;
    }

    /**
     * 下载jar包
     *
     * @return
     * @throws FileNotFoundException
     */
    public Object downloadExport() throws FileNotFoundException {
        InputStreamResource isr = new InputStreamResource(new FileInputStream(getDriverPath() + DEBUG_PACKAGE));
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-disposition", "attachment; filename=" + DEBUG_PACKAGE)
                .body(isr);
    }


    private String getDriverPath() {
        String projectPath = "";
        String path = System.getProperty("user.dir") + "/target";
        File workSpace = new File(path);
        if (workSpace.isDirectory()) {
            projectPath = path + "/classes";
        } else {
            projectPath = System.getProperty("user.dir") + "/classes";
        }

        // driver 文件路径
        return projectPath + "/driver/";
    }

}
