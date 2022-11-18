package com.thinkingdata.webui.controller;

import com.thinkingdata.loghandle.WebLog;
import com.thinkingdata.response.ResponseData;
import com.thinkingdata.response.ResponseDataUtils;
import com.thinkingdata.webui.daoUi.WebUiCaseDao;
import com.thinkingdata.webui.serviceUi.DebugService;
import com.thinkingdata.webui.serviceUi.WebUiCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2022/11/07 13:59
 */
@RestController
@RequestMapping(value = "/debugCase")
public class DebugCaseController {
    @Autowired
    private DebugService debugService;

    @Autowired
    private WebUiCaseService webUiCaseService;

    @Autowired
    private WebUiCaseDao caseDao;

    @RequestMapping(value = "/updateDebugHub", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "【调用了更新debugHub接口】")
    public Object updateDebugHub(@RequestBody Map<String, Integer> map) {
        return debugService.updateDebugHub(map.get("status").toString());
    }

    @RequestMapping(value = "/getStatus", method = {RequestMethod.GET})
    @WebLog(description = "【调用了获取debugHub状态接口】")
    public Object getStatus() {
        return debugService.getDebugStatus();
    }

    @RequestMapping(value = "/execCase", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "调用了调试用例接口")
    public Object execCase(@RequestBody Map<String, Object> map) {
        String successMsg = "开始调试用例,请仔细查看整个执行过程～～～";
        ResponseData<Object> responseData = ResponseDataUtils.buildSuccess(successMsg);
        if (debugService.checkDebugHub()) {
            if (webUiCaseService.canExec((Integer) map.get("id"))) {
                if (caseDao.getCaseById((Integer) map.get("id")).getStatus() == 1) {
                    String model = "debug";
                    String browserNode = map.get("browserNode").toString();
                    webUiCaseService.execCase((Integer) map.get("id"), model, browserNode);
                } else {
                    responseData = ResponseDataUtils.buildWait("用例正在调试中,请稍后再来调试～～～");
                }
            } else {
                responseData = ResponseDataUtils.buildError("用例已被禁用,请开启后再调试!");
            }
        }else{
            responseData = ResponseDataUtils.buildError("未开启调试模式!");

        }
        return responseData;
    }

}
