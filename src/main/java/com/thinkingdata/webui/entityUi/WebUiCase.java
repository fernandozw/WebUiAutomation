package com.thinkingdata.webui.entityUi;

import com.alibaba.fastjson.JSON;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/21 17:45
 */
public class WebUiCase {
    // 用例id
    private Integer id;
    // 用例名称
    private String caseName;
    // url地址
    private String url;
    // 所属业务
    private Integer pageId;
    // 页面类型
    private String pageType;
    // 手机类型
    private String phoneType;
    // 浏览器类型
    private String browserType;
    // 步骤id集
    private String stepIds;
    // 用例执行状态
    private Integer status;
    // 用例是否启用
    private Integer canUse;
    // 校验操作
    private String verifyAction;
    // 校验值
    private String verifyValue;
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

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getPageId() {
        return pageId;
    }

    public void setPageId(Integer pageId) {
        this.pageId = pageId;
    }

    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public String getBrowserType() {
        return browserType;
    }

    public void setBrowserType(String browserType) {
        this.browserType = browserType;
    }

    public String getStepIds() {
        return stepIds;
    }

    public void setStepIds(String stepIds) {
        this.stepIds = stepIds;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCanUse() {
        return canUse;
    }

    public void setCanUse(Integer canUse) {
        this.canUse = canUse;
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


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
