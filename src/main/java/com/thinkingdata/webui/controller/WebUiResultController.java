package com.thinkingdata.webui.controller;

import java.util.Map;

import com.thinkingdata.loghandle.WebLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.thinkingdata.webui.serviceUi.WebUiResultService;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/21 17:45
 */
@RestController
@RequestMapping(value = "/result")
public class WebUiResultController {
    private WebUiResultService resultService;

    @Autowired
    public void setResultService(WebUiResultService resultService) {
        this.resultService = resultService;
    }

    @RequestMapping(value = "/resultList", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "【调用了执行结果列表接口】")
    public Object results(@RequestBody Map<String, Object> map) {
        return resultService.resultList(map);
    }

    @RequestMapping(value = "/failImage", method = {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "【调用了失败步骤截图接口】")
    public Object getImage(@RequestParam Integer id, @RequestParam Integer status) {
        return resultService.image(id, status);
    }

    @RequestMapping(value = "/getSearchCondition", method = {RequestMethod.GET})
    @WebLog(description = "【调用了获取测试结果查询条件接口】")
    public Object getSearchCondition() {
        return resultService.getSearchCondition();
    }


    @RequestMapping(value = "/getResultPic", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "【调用了获取测试结果趋势图接口】")
    public Object getResultPic(@RequestBody Map<String, Object> map) {
        return resultService.getResultPic(map);
    }

}
