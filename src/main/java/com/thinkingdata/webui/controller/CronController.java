package com.thinkingdata.webui.controller;

import com.thinkingdata.loghandle.WebLog;
import com.thinkingdata.webui.entityUi.Cron;
import com.thinkingdata.webui.serviceUi.CronService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/27 11:22
 */
@RestController
public class CronController {

    @Autowired

    private CronService cronService;


    @RequestMapping(value = "/addCron", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "【调用了任务新增接口】")
    public Object addCron(@RequestBody Cron cron) {
        return cronService.addCron(cron);
    }

    @RequestMapping(value = "/updateCron", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "【调用了任务更新接口】")
    public Object updateCron(@RequestBody Cron cron) {
        return cronService.updateCron(cron);
    }

    @RequestMapping(value = "/deleteCron", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "【调用了任务删除接口】")
    public Object delete(@RequestBody Map<String, String> map) {
        return cronService.delete(Integer.valueOf(map.get("id")));
    }

    @RequestMapping(value = "/cronList", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "【调用了任务查询接口】")
    public Object list(@RequestBody Map<String, Object> map) {
        return cronService.list(map);
    }
}
