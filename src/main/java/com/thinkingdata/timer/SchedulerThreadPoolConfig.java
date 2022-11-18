package com.thinkingdata.timer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/26 11:24
 */
@Configuration
public class SchedulerThreadPoolConfig {
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        // 定时任务执行线程池核心线程数
        taskScheduler.setPoolSize(10);
        //设置任务注册器的调度器
        taskScheduler.setRemoveOnCancelPolicy(true);
        //设置线程名称前缀
        taskScheduler.setThreadNamePrefix("SchedulerThreadPool-");
        return taskScheduler;
    }
}
