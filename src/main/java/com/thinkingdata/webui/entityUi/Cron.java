package com.thinkingdata.webui.entityUi;

import com.alibaba.fastjson.JSON;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/25 10:32
 */
public class Cron {

    // 主键id
    private Integer id;
    // 任务名称
    private String cronName;
    // 任务状态
    private Integer status;
    // 执行时间
    private String execTime;
    // 执行时的参数
    private String args;
    // 创建时间
    private String addTime;
    // 更新时间
    private String updateTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCronName() {
        return cronName;
    }

    public void setCronName(String cronName) {
        this.cronName = cronName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getExecTime() {
        return execTime;
    }

    public void setExecTime(String execTime) {
        this.execTime = execTime;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}