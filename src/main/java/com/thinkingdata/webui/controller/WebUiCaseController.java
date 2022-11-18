package com.thinkingdata.webui.controller;

import java.util.List;
import java.util.Map;

import com.thinkingdata.loghandle.WebLog;
import com.thinkingdata.response.ResponseDataUtils;
import com.thinkingdata.response.ResponseData;
import com.thinkingdata.webui.daoUi.WebUiCaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.thinkingdata.webui.entityUi.WebUiCase;
import com.thinkingdata.webui.serviceUi.WebUiCaseService;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/21 17:45
 */
@RestController
@RequestMapping(value = "/case")
public class WebUiCaseController {
    @Autowired
    private WebUiCaseService webUiCaseService;


    @Autowired
    private WebUiCaseDao caseDao;

    @RequestMapping(value = "/add", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "【调用了用例新增接口】")
    public Object addCase(@RequestBody Map<String, Object> map) {
        return webUiCaseService.addCase(map.get("uiCase"), (List<Map<String, Object>>) map.get("stepList"));
    }


    @RequestMapping(value = "/insertCaseByIde", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "【调用了用例导入接口】")
    public Object insertCaseByIde(@RequestBody Map<String, Object> map) {

        return webUiCaseService.insertCaseByIde(map);
    }

    @RequestMapping(value = "/cases", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "【调用了用例查询接口】")
    public Object cases(@RequestBody Map<String, Object> map) {
        return webUiCaseService.caseList(map);
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "【调用了用例更新接口】")
    public Object updateCase(@RequestBody Map<String, Object> map) {

        return webUiCaseService.updateCase(map.get("uiCase"), (List<Map<String, Object>>) map.get("stepList"));
    }


    @RequestMapping(value = "/delete", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "调用了删除用例接口")
    public Object deleteCase(@RequestBody Map<String, Integer> map) {

        return webUiCaseService.deleteCase(map.get("id"));
    }

    @RequestMapping(value = "/execCase", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "调用了用例执行接口")
    public Object execCase(@RequestBody Map<String, Integer> map) {
        String successMsg = "开始执行用例,请稍后查看执行结果～～～";
        String model = "formal";
        String browserNode = "";
        ResponseData<Object> responseData = ResponseDataUtils.buildSuccess(successMsg);
        if (webUiCaseService.canExec(map.get("id"))) {
            if (caseDao.getCaseById(map.get("id")).getStatus() == 1) {
                webUiCaseService.execCase(Integer.valueOf(map.get("id")), model, browserNode);
            } else {
                responseData = ResponseDataUtils.buildError("用例正在执行中,请稍后再来执行～～～");
            }
        } else {
            responseData = ResponseDataUtils.buildError("用例已被禁用,请开启后再执行!");
        }
        return responseData;
    }

    @RequestMapping(value = "/getSearchCondition", method = {RequestMethod.GET})
    @WebLog(description = "【调用了获取用例查询条件接口】")
    public Object getSearchCondition() {
        return webUiCaseService.getSearchCondition();
    }


    @RequestMapping(value = "/getCaseById", method = {RequestMethod.GET})
    @WebLog(description = "调用了用例详情接口")
    public Object getCaseById(@RequestParam(value = "id") Integer id) {

        return webUiCaseService.getCaseById(id);
    }

}
