package com.thinkingdata.webui.controller;

import java.util.Map;

import com.thinkingdata.loghandle.WebLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.thinkingdata.webui.entityUi.WebUiPage;
import com.thinkingdata.webui.serviceUi.WebUiPageService;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/21 17:45
 */
@RestController
@RequestMapping(value = "/page")
public class WebUiPageController {
    private WebUiPageService pageService;

    @Autowired
    public void setPageService(WebUiPageService pageService) {
        this.pageService = pageService;
    }


    @RequestMapping(value = "/add", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "【调用了页面新增接口】")
    public Object addPage(@RequestBody WebUiPage page) {
        return pageService.addPage(page);
    }

    @RequestMapping(value = "/pages", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "【调用了页面列表接口】")
    public Object pageList(@RequestBody Map<String, Object> param) {
        return pageService.pages(param);
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "【调用了页面更新接口】")
    public Object pageList(@RequestBody WebUiPage page) {
        return pageService.updatePage(page);
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "【调用了页面删除接口】")
    public Object deletePageById(@RequestBody Map<String, Integer> map) {
        return pageService.deletePageById(map.get("id"));
    }


    @RequestMapping(value = "/deleteAll", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "【调用了页面全量删除接口】")
    public Object deleteAllById(@RequestBody Map<String, Integer> map) {
        return pageService.deleteAllById(map.get("id"));
    }

    @RequestMapping(value = "/allPage", method = {RequestMethod.GET})
    @WebLog(description = "【调用了获取所有页面接口】")
    public Object allPage() {
        return pageService.allPage();
    }

    @RequestMapping(value = "/getPageById", method = {RequestMethod.GET})
    @WebLog(description = "【调用了根据页面id接口】")
    public Object getPageById(@RequestParam(value = "id") Integer id) {
        return pageService.getPageById(id);
    }
}

