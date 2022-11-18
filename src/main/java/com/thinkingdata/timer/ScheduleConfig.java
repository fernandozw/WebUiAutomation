package com.thinkingdata.timer;

import com.thinkingdata.webui.daoUi.CronDao;
import com.thinkingdata.webui.entityUi.Cron;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;


/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/26 11:22
 */
@Component
public class ScheduleConfig implements CommandLineRunner {
    // 兜底任务,每10分钟执行一次
    private static final String HANDLE_TASK_TIME = "0 */10 * * * ?";
    // 兜底任务id
    private static final Integer HANDLE_TASK_ID = 0;
    // 兜底任务名称
    private static final String HANDLE_TASK_NAME = "兜底任务-处理遗漏数据";
    @Autowired
    private CronDao cronDao;

    public void setCronDao(CronDao cronDao) {
        this.cronDao = cronDao;
    }

    @Autowired
    private CronTaskRegistrar taskRegistrar;

    public void setTaskRegistrar(CronTaskRegistrar taskRegistrar) {
        this.taskRegistrar = taskRegistrar;
    }

    /**
     * 项目启动时初始化task
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        List<Cron> cronList = cronDao.allCron();
        for (Cron cron : cronList) {
            //状态为1时为正常定时任务
            if (cron.getStatus() == 1) {
                SchedulingRunnable task = new SchedulingRunnable(cron);
                taskRegistrar.addCronTask(cron.getId(), task, cron.getExecTime());
            }
        }
        Cron cron = new Cron();
        cron.setId(HANDLE_TASK_ID);
        cron.setExecTime(HANDLE_TASK_TIME);
        cron.setCronName(HANDLE_TASK_NAME);
        taskRegistrar.addCronTask(cron.getId(), new SchedulingRunnable(cron), cron.getExecTime());
    }
}

