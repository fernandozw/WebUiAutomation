package com.thinkingdata.timer;

import java.util.concurrent.ScheduledFuture;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/26 12:22
 */
public final class ScheduledTask {

    volatile ScheduledFuture<?> future;
    private String expression;

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }


    /**
     * 取消定时任务
     */
    public void cancel() {
        ScheduledFuture<?> future = this.future;
        if (future != null) {
            future.cancel(true);
        }
    }
}
