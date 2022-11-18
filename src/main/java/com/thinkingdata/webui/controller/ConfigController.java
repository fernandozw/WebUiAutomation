package com.thinkingdata.webui.controller;

import com.thinkingdata.loghandle.WebLog;
import com.thinkingdata.webui.serviceUi.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.util.Map;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/6/8 16:02
 */
@RestController
@RequestMapping(value = "/config")
public class ConfigController {

    @Autowired
    private ConfigService configService;

    @RequestMapping(value = "/getConfig", method = {RequestMethod.GET})
    @WebLog(description = "【调用了获取浏览器运行模式接口】")
    public Object getConfig() {
        return configService.getHeadless();
    }

    @RequestMapping(value = "/updateConfig", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "【调用了更新浏览器运行模式接口】")
    public Object updateConfig(@RequestBody Map<String, String> map) {
        return configService.updateHeadless(map);
    }


    @RequestMapping(value = "/getLanguage", method = {RequestMethod.GET})
    @WebLog(description = "【调用了获取浏览器语言配置接口】")
    public Object getLanguage() {
        return configService.getLanguage();
    }

    @RequestMapping(value = "/updateLanguage", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "【调用了更新浏览器运语言配置接口】")
    public Object updateLanguage(@RequestBody Map<String, String> map) {
        return configService.updateLanguage(map);
    }


    @RequestMapping(value = "/getWindowSize", method = {RequestMethod.GET})
    @WebLog(description = "【调用了获取浏览器窗口分辨率接口】")
    public Object getWindowSize() {
        return configService.getWindowSize();
    }

    @RequestMapping(value = "/updateWindowSize", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "【调用了更新浏览器窗口分辨率接口】")
    public Object updateWindowSize(@RequestBody Map<String, String> map) {
        return configService.updateWindowSize(map);
    }


    @RequestMapping(value = "/downloadJar", method = {RequestMethod.GET})
    public Object downloadExport() throws FileNotFoundException {
        return configService.downloadExport();
    }


}
