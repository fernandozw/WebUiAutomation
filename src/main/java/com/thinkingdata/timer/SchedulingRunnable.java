package com.thinkingdata.timer;


import com.thinkingdata.webui.entityUi.Cron;
import com.thinkingdata.webui.serviceUi.CronService;
import com.thinkingdata.webui.serviceUi.WebUiSceneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/26 09:22
 */

@Component
public class SchedulingRunnable implements Runnable {
    private Logger log = LoggerFactory.getLogger(SchedulingRunnable.class);
    public static SchedulingRunnable schedulingRunnable;
    private Cron cron;

    private CronService cronService;

    @Autowired
    public void setCronService(CronService cronService) {
        this.cronService = cronService;
    }

    private CronTaskRegistrar taskRegistrar;

    @Autowired
    public void setTaskRegistrar(CronTaskRegistrar taskRegistrar) {
        this.taskRegistrar = taskRegistrar;
    }

    private WebUiSceneService uiSceneService;

    @Autowired
    public void setUiSceneService(WebUiSceneService uiSceneService) {
        this.uiSceneService = uiSceneService;
    }


    public SchedulingRunnable() {

    }

    @PostConstruct
    public void init() {
        schedulingRunnable = this;
        schedulingRunnable.uiSceneService = this.uiSceneService;
        schedulingRunnable.cronService = this.cronService;
        schedulingRunnable.taskRegistrar = this.taskRegistrar;
    }


    public SchedulingRunnable(Cron cron) {
        this.cron = cron;
    }

    @Override
    public void run() {
        if (cron.getId() > 0) {
            log.info("开始执行定时任务:{}", "【" + cron.getId() + "】【" + cron.getCronName() + "】");
            schedulingRunnable.uiSceneService.execScene(Integer.valueOf(cron.getArgs()));
        } else {
            log.info("开始执行兜底任务:{}", "【" + cron.getId() + "】【" + cron.getCronName() + "】");
            List<Cron> cronList = schedulingRunnable.cronService.allList();
            handleDifference(cronList);
            handleIntersection(cronList);
            handleRegistrar(cronList);
        }
    }

    /**
     * 处理原始数据多余部分
     *
     * @param cronList 原始数据
     */
    public void handleDifference(List<Cron> cronList) {
        // 获取注册器map
        Map<Integer, ScheduledTask> map = schedulingRunnable.taskRegistrar.getScheduledTasks();
        // 数据库原始数据map,去除停用的
        Map<Integer, Cron> cronMap = cronList.stream().collect(Collectors.toMap(Cron::getId, cron -> cron));
        // 注册器keySet
        List<Integer> taskIds = new ArrayList<Integer>(map.keySet());
        // 数据库原始数据的id列表
        List<Integer> cronIds = cronList.stream().map(Cron::getId).collect(Collectors.toList());
        // 数据库原始数据比注册器中多出的部分数据列表
        List<Integer> cronIdMore = cronIds.stream().filter(item -> !taskIds.contains(item)).collect(toList());
        // 原始数据多出部分转化为map
        Map<Integer, Cron> cronMoreMap = new HashMap<Integer, Cron>();
        cronIdMore.forEach(item -> cronMoreMap.put(item, cronMap.get(item)));
        log.info("原始数据多出部分:{}", cronMoreMap);
        // 遍历原始数据多出部分map
        cronMoreMap.forEach((k, v) -> {
            // 注册器中没有这个任务,新增
            if (map.get(k) == null) {
                if (v.getStatus() != 0) {
                    log.info("注册器中没有这个任务:{},新增任务到注册器中", v);
                    schedulingRunnable.taskRegistrar.addCronTask(k, new SchedulingRunnable(v), v.getExecTime());
                }
            }
        });
    }

    /**
     * 处理注册器中多余的任务
     *
     * @param cronList 原始数据
     */
    public void handleRegistrar(List<Cron> cronList) {
        // 获取注册器map
        Map<Integer, ScheduledTask> map = schedulingRunnable.taskRegistrar.getScheduledTasks();
        // 注册器keySet
        List<Integer> taskIds = new ArrayList<Integer>(map.keySet());
        // 数据库原始数据的id列表
        List<Integer> cronIds = cronList.stream().map(Cron::getId).collect(Collectors.toList());
        // 注册器比中数据库原始数据多出的部分数据列表
        List<Integer> taskIdMore = taskIds.stream().filter(item -> !cronIds.contains(item)).collect(toList());
        log.info("注册器比原始数据多出部分:{}", taskIdMore);
        // 遍历并移除注册器中多余的任务
        taskIdMore.forEach(item -> {
            if (item != 0) {
                log.info("删除注册器中多余的任务:{}", item);
                schedulingRunnable.taskRegistrar.removeCronTask(item);
            }
        });
    }

    /**
     * 处理交集
     *
     * @param cronList 原始数据
     */
    public void handleIntersection(List<Cron> cronList) {
        // 获取注册器map
        Map<Integer, ScheduledTask> map = schedulingRunnable.taskRegistrar.getScheduledTasks();
        // 数据库原始数据map
        Map<Integer, Cron> cronMap = cronList.stream().collect(Collectors.toMap(Cron::getId, cron -> cron));
        // 注册器keySet
        List<Integer> taskIds = new ArrayList<Integer>(map.keySet());
        // 数据库原始数据的id列表
        List<Integer> cronIds = cronList.stream().map(Cron::getId).collect(Collectors.toList());
        // 原始数据与注册器中相同的部分
        List<Integer> idIntersection = cronIds.stream().collect(Collectors.toList());
        // 交集转化为map
        Map<Integer, Cron> intersectionMap = new HashMap<Integer, Cron>();
        idIntersection.forEach(item -> intersectionMap.put(item, cronMap.get(item)));
        log.info("原始数据与注册器的交集:{}", intersectionMap);
        intersectionMap.forEach((k, v) -> {
            if (map.get(k) != null) {
                if (v.getStatus() == 0) {
                    log.info("原始数据中任务已停用:{},删除注册器中的该任务", k);
                    schedulingRunnable.taskRegistrar.removeCronTask(k);
                } else if (v.getStatus() == 1) {
                    if (v.getExecTime().equalsIgnoreCase(map.get(k).getExpression())) {
                        log.info("原始数据与注册器中调度时间一致,无须更新:{}", k);
                    } else if (!v.getExecTime().equalsIgnoreCase(map.get(k).getExpression())) {
                        log.info("原始数据与注册器中调度时间不一致,更新注册器:{}", k);
                        schedulingRunnable.taskRegistrar.removeCronTask(k);
                        schedulingRunnable.taskRegistrar.addCronTask(k, new SchedulingRunnable(v), v.getExecTime());
                    }
                }
            }
        });
    }
}
