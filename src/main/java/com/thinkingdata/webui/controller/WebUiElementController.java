package com.thinkingdata.webui.controller;

import java.util.Map;

import com.thinkingdata.loghandle.WebLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.thinkingdata.webui.entityUi.WebUiElement;
import com.thinkingdata.webui.serviceUi.WebUiElementService;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/21 17:45
 */
@RestController
@RequestMapping(value = "/element")
public class WebUiElementController {
    private WebUiElementService elementService;

    @Autowired
    public void setElementService(WebUiElementService elementService) {
        this.elementService = elementService;
    }


    @RequestMapping(value = "/add", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "【调用了元素新增接口】")
    public Object addElement(@RequestBody WebUiElement element) {
        return elementService.addElement(element);
    }

    @RequestMapping(value = "/elements", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "【调用了元素列表接口】")
    public Object elements(@RequestBody Map<String, Object> map) {
        return elementService.elementList(map);
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "【调用了元素更新接口】")
    public Object updateElement(@RequestBody WebUiElement element) {
        return elementService.updateElement(element);
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "【调用了删除元素接口】")
    public Object deleteElement(@RequestBody Map<String, Integer> map) {
        return elementService.deleteElement(map.get("id"));
    }

    @RequestMapping(value = "/getElementByPageId", method = {RequestMethod.GET})
    @WebLog(description = "【调用了根据页面id获取元素列表接口】")
    public Object getElementByPageId(@RequestParam(value = "pageId") Integer pageId) {
        return elementService.getElementByPageId(pageId);
    }

    @RequestMapping(value = "/getElementById", method = {RequestMethod.GET})
    @WebLog(description = "【调用了根据元素id获取元素详情接口】")
    public Object getElementById(@RequestParam(value = "id") Integer id) {
        return elementService.getUiElementById(id);
    }

    @RequestMapping(value = "/getSearchCondition", method = {RequestMethod.GET})
    @WebLog(description = "【调用了获取元素查询条件接口】")
    public Object getSearchCondition() {
        return elementService.getSearchCondition();
    }

}
