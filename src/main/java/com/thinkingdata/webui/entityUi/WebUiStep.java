package com.thinkingdata.webui.entityUi;

import com.alibaba.fastjson.JSONObject;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/21 17:45
 */
public class WebUiStep {
    // 步骤id
    private Integer id;
    // 步骤名称
    private String stepName;
    // 关联的元素id
    private Integer elementId;
    // 对元素的操作
    private String action;
    // 操作的给定值
    private String actionValue;
    // 执行的外部方法
    private String function;
    // 是否禁用
    private Integer status;
    // 校验操作
    private String verifyAction;
    // 校验值
    private String verifyValue;
    // 将输入参数保存为动态参数时的key
    private String paramKey;
    // 添加时间
    private String addTime;
    // 更新时间
    private String updateTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public Integer getElementId() {
        return elementId;
    }

    public void setElementId(Integer elementId) {
        this.elementId = elementId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActionValue() {
        return actionValue;
    }

    public void setActionValue(String actionValue) {
        this.actionValue = actionValue;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getVerifyAction() {
        return verifyAction;
    }

    public void setVerifyAction(String verifyAction) {
        this.verifyAction = verifyAction;
    }

    public String getVerifyValue() {
        return verifyValue;
    }

    public void setVerifyValue(String verifyValue) {
        this.verifyValue = verifyValue;
    }

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
