package com.thinkingdata.webui.controller;

import com.thinkingdata.loghandle.WebLog;
import com.thinkingdata.response.ResponseDataUtils;
import com.thinkingdata.response.ResponseData;
import com.thinkingdata.webui.entityUi.WebUiScene;
import com.thinkingdata.webui.serviceUi.WebUiSceneService;
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
 * @date 2020/5/22 16:39
 */
@RestController
@RequestMapping(value = "/scene")
public class WebUiSceneController {
    private WebUiSceneService uiSceneService;

    @Autowired
    public void setUiSceneService(WebUiSceneService uiSceneService) {
        this.uiSceneService = uiSceneService;
    }

    @RequestMapping(value = "/add", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "【调用了场景新增接口】")
    public Object addScene(@RequestBody WebUiScene webUiScene) {
        return uiSceneService.addScene(webUiScene);
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "【调用了场景更新接口】")
    public Object updateScene(@RequestBody WebUiScene webUiScene) {
        return uiSceneService.updateScene(webUiScene);
    }

    @RequestMapping(value = "/scenes", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "【调用了场景查询接口】")
    public Object cases(@RequestBody Map<String, Object> map) {
        return uiSceneService.sceneList(map);
    }

    @RequestMapping(value = "/execScene", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "【调用了场景执行接口】")
    public Object execScene(@RequestBody Map<String, String> map) {
        ResponseData<Object> responseData = ResponseDataUtils.buildSuccess();
        if (uiSceneService.canExec(Integer.valueOf(map.get("id")))) {
            uiSceneService.execScene(Integer.valueOf(map.get("id")));
        } else {
            responseData = ResponseDataUtils.buildWait();
        }
        return responseData;
    }


}
