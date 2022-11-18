package com.thinkingdata.webui.controller;

import com.thinkingdata.loghandle.WebLog;
import com.thinkingdata.webui.entityUi.ExpectImage;
import com.thinkingdata.webui.serviceUi.ExpectImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2022/09/27 14:54
 */
@RestController
@RequestMapping(value = "/expectImage")
public class ExpectImageController {
    @Autowired
    private ExpectImageService expectImageService;

    @RequestMapping(value = "/setExpectImage", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "【调用了期望图片设置接口】")
    public Object setExpectImage(@RequestBody Map<String, Object> paramMap) throws Exception {
        ExpectImage expectImage = mapToBean((Map<String, Object>) paramMap.get("imageDetail"), ExpectImage.class);
        return expectImageService.setExpectImage(expectImage, (String) paramMap.get("compareType"));
    }

    /**
     * 将map集合中的数据转化为指定对象的同名属性中
     */
    private static <T> T mapToBean(Map<String, Object> map, Class<T> clazz) throws Exception {
        T bean = clazz.newInstance();
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }


    @RequestMapping(value = "/deleteImage", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "【调用了期望图片删除接口】")
    public Object deleteImage(@RequestBody Map<String, String> map) {
        return expectImageService.deleteImage(Integer.valueOf(map.get("stepId")));
    }

    @RequestMapping(value = "/getImage", method = {RequestMethod.GET})
    @WebLog(description = "【调用了获取期望图片接口】")
    public Object getImage(@RequestParam(value = "stepId") Integer stepId) {
        return expectImageService.getImage(stepId);
    }

}
