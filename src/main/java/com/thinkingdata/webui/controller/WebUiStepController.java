package com.thinkingdata.webui.controller;

import java.util.Map;

import com.thinkingdata.loghandle.WebLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.thinkingdata.webui.entityUi.WebUiStep;
import com.thinkingdata.webui.serviceUi.WebUiStepService;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/21 17:45
 */
@RestController
@RequestMapping(value = "/step")
public class WebUiStepController {
    private WebUiStepService stepService;

    @Autowired
    public void setStepService(WebUiStepService stepService) {
        this.stepService = stepService;
    }


    @RequestMapping(value = "/add", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "调用了步骤新增接口")
    public Object addStep(@RequestBody WebUiStep step) {
        return stepService.addStep(step);
    }

    @RequestMapping(value = "/steps", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "调用了步骤列表接口")
    public Object steps(@RequestBody Map<String, Object> map) {
        return stepService.stepList(map);
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "【调用了步骤更新接口】")
    public Object updateStep(@RequestBody WebUiStep step) {
        return stepService.updateStep(step);
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "【调用了步骤删除接口】")
    public Object deleteStep(@RequestBody Map<String, Integer> map) {
        return stepService.deleteStep(map.get("id"));
    }

    @RequestMapping(value = "/getStepById", method = {RequestMethod.GET})
    @WebLog(description = "【调用了根据id获取步骤详情接口】")
    public Object getStepById(@RequestParam(value = "id") Integer id) {
        return stepService.getStepById(id);
    }

    @RequestMapping(value = "/getStepDetailById", method = {RequestMethod.GET})
    @WebLog(description = "【调用了根据id获取步骤详情与页面接口】")
    public Object getStepDetailById(@RequestParam(value = "id") Integer id) {
        return stepService.getStepDetailById(id);
    }


    @RequestMapping(value = "/getStepListByPageId", method = {RequestMethod.GET})
    @WebLog(description = "【调用了根据页面id获取步骤列表接口】")
    public Object getStepListByPageId(@RequestParam(value = "pageId") Integer pageId) {
        return stepService.getStepListByPageId(pageId);
    }


    @RequestMapping(value = "/getSearchCondition", method = {RequestMethod.GET})
    @WebLog(description = "【调用了获取步骤查询条件接口】")
    public Object getSearchCondition() {
        return stepService.getSearchCondition();
    }


    @RequestMapping(value = "/getAllStep", method = {RequestMethod.GET})
    @WebLog(description = "【调用了获取全部步骤查询条件接口】")
    public Object getAllStep() {
        return stepService.allStep();
    }

    @RequestMapping(value = "/noticeList", method = {RequestMethod.GET})
    @WebLog(description = "【调用了获取内置参数列表】")
    public Object noticeList() {
        return stepService.noticeList();
    }
}
