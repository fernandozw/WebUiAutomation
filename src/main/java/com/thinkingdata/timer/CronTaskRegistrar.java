package com.thinkingdata.timer;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/26 10:22
 */
@Component
public class CronTaskRegistrar implements DisposableBean {


    private final Map<Integer, ScheduledTask> scheduledTasks = new ConcurrentHashMap<>(16);

    public Map<Integer, ScheduledTask> getScheduledTasks() {
        return scheduledTasks;
    }

    @Autowired
    private TaskScheduler taskScheduler;

    public void setTaskScheduler(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    /**
     * 获取定时任务
     *
     * @return 返回定时任务
     */
    public TaskScheduler getScheduler() {

        return this.taskScheduler;
    }

    /**
     * 新增定时任务
     *
     * @param cronId         定时任务id
     * @param task           任务对象
     * @param cronExpression cron 表达式
     */
    public void addCronTask(Integer cronId, Runnable task, String cronExpression) {
        addCronTask(cronId, new CronTask(task, cronExpression));
    }

    public void addCronTask(Integer cronId, CronTask cronTask) {
        // 先判断是否存在这个任务,如果存在,先删除,再新增
        if (this.scheduledTasks.containsKey(cronId)) {
            removeCronTask(cronId);
        }
        this.scheduledTasks.put(cronId, scheduleCronTask(cronTask));
    }


    /**
     * 根据任务id删除任务
     *
     * @param cronId
     */
    public void removeCronTask(Integer cronId) {
        // 删除任务列表中的任务元素并返回该任务
        ScheduledTask scheduledTask = this.scheduledTasks.remove(cronId);
        // 任务列表中删除该任务后,取消这个运行的任务
        if (scheduledTask != null) {
            scheduledTask.cancel();
        }
    }

    /**
     * 构造调度任务
     *
     * @param cronTask CronTask对象
     * @return 返回ScheduledTask 对象
     */
    public ScheduledTask scheduleCronTask(CronTask cronTask) {
        ScheduledTask scheduledTask = new ScheduledTask();
        scheduledTask.setExpression(cronTask.getExpression());
        scheduledTask.future = this.taskScheduler.schedule(cronTask.getRunnable(), cronTask.getTrigger());
        return scheduledTask;
    }

    /**
     * 销毁注册器中的所有任务
     */
    @Override
    public void destroy() {
        for (ScheduledTask task : this.scheduledTasks.values()) {
            task.cancel();
        }
        this.scheduledTasks.clear();
    }
}
