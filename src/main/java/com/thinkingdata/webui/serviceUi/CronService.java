package com.thinkingdata.webui.serviceUi;

import com.thinkingdata.lib.AnalysisSeleniumIde;
import com.thinkingdata.response.ResponseDataUtils;
import com.thinkingdata.response.ResponseData;
import com.thinkingdata.timer.CronTaskRegistrar;
import com.thinkingdata.timer.SchedulingRunnable;
import com.thinkingdata.webdriverImpl.PageElement;
import com.thinkingdata.webdriverImpl.WebDriverSnapshot;
import com.thinkingdata.webdriverImpl.WebDriverVerify;
import com.thinkingdata.webdriverImpl.WebUiDriver;
import com.thinkingdata.webui.daoUi.*;
import com.thinkingdata.webui.entityUi.*;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/27 10:22
 */
@Service
public class CronService {
    private static final Integer pageSize = 10;
    private Logger log = LoggerFactory.getLogger(CronService.class);
    @Autowired
    private CronDao cronDao;

    @Autowired
    private CronTaskRegistrar taskRegistrar;

    @Autowired
    private WebUiCaseDao caseDao;


    @Autowired
    private WebUiDriver uiDriver;


    @Autowired
    private PageElement pageElement;


    @Autowired
    private WebUiStepDao uiStepDao;

    @Autowired
    private WebUiElementDao uiElementDao;

    @Autowired
    private WebDriverSnapshot snapshot;

    @Autowired
    private WebUiResultDao resultDao;


    @Autowired
    WebDriverVerify webDriverVerify;


    @Autowired
    WebUiPageDao uiPageDao;

    public Object addCron(Cron cron) {
        ResponseData responseData;
        try {
            // 定时任务插入数据库
            Integer cronId = cronDao.addCron(cron);
            // 当定时任务数据插入数据库成功并返回id成功后,向任务注册器中插入数据
            if (cronId > 0) {
                // 构造任务对象
                SchedulingRunnable task = new SchedulingRunnable(cron);
                // 移除已有任务
                taskRegistrar.removeCronTask(cron.getId());
                // 加入新的任务
                taskRegistrar.addCronTask(cron.getId(), task, cron.getExecTime());
                responseData = ResponseDataUtils.buildAddSuccess();
                log.info("定时任务插入任务注册器成功:{}", cronId);
            } else {
                responseData = ResponseDataUtils.buildAddFail();
                log.error("定时任务插入数据库失败:{}", cron);
            }
        } catch (Exception e) {
            responseData = ResponseDataUtils.buildError();
            log.error("新增定时任务异常:{}", e);
        }
        return responseData;
    }

    public Object updateCron(Cron cron) {
        ResponseData responseData = null;
        // 先更新数据库并返回更新结果
        try {
            Integer isSuccess = cronDao.update(cron);
            if (isSuccess > 0) {
                // 状态为1,启用job,先删除再新增
                if (cron.getStatus() == 1) {
                    taskRegistrar.removeCronTask(cron.getId());
                    SchedulingRunnable task = new SchedulingRunnable(cron);
                    taskRegistrar.addCronTask(cron.getId(), task, cron.getExecTime());
                    log.info("任务注册器中启用定时任务成功:{}", cron.getStatus());
                    // 状态为0,删除job
                } else if (cron.getStatus() == 0) {
                    taskRegistrar.removeCronTask(cron.getId());
                    log.info("任务注册器中停用定时任务成功:{}", cron.getStatus());
                }
                responseData = ResponseDataUtils.buildUpdateSuccess();
            } else if (isSuccess < 1) {
                log.error("定时任务修改失败:{}", cron);
                responseData = ResponseDataUtils.buildUpdateFail();
            }
        } catch (Exception e) {
            log.error("更新定时任务异常:{}", e);
            responseData = ResponseDataUtils.buildError();
        }
        return responseData;
    }

    public Object delete(Integer cronId) {
        ResponseData responseData = null;
        try {
            // 删除数据库中的定时任务数据
            Integer remove = cronDao.removeById(cronId);
            // 删除任务注册器中的任务数据
            if (remove > 0) {
                taskRegistrar.removeCronTask(cronId);
                log.info("任务注册器中定时任务删除成功:{}", remove);
                responseData = ResponseDataUtils.buildDeleteSuccess();
            } else {
                log.info("数据库中定时任务删除失败:{}", remove);

                responseData = ResponseDataUtils.buildDeleteFail();
            }
        } catch (Exception e) {
            log.error("删除定时任务异常:{}", e.toString());
            responseData = ResponseDataUtils.buildError();
        }
        return responseData;
    }

    public Object list(Map<String, Object> map) {
        ResponseData responseData = null;
        Map<String, Object> dataMap = new HashMap<String, Object>();
        Integer limit1 = (Integer) map.get("pageNum") * pageSize - pageSize;
        map.put("limit1", limit1);
        map.put("limit2", pageSize);
        List<Cron> cronList = cronDao.list(map);
        Integer total = cronDao.total(map);
        dataMap.put("cronList", cronList);
        dataMap.put("total", total);
        dataMap.put("pageSize", pageSize);
        responseData = ResponseDataUtils.buildSuccess();
        return responseData;
    }

    public List<Cron> allList() {
        return cronDao.allCron();
    }

    /**
     * 异步方法,无须等待用例执行完毕
     *
     * @param caseId 用例id
     */

}
