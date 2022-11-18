package com.thinkingdata.webui.entityUi;

import com.alibaba.fastjson.JSON;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/22 11:16
 */
public class WebUiScene {
    // 场景id
    private Integer id;
    // 场景名称
    private String sceneName;
    // 场景下的用例集
    private String caseIds;
    // 场景的执行状态
    private Integer status;
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

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public String getCaseIds() {
        return caseIds;
    }

    public void setCaseIds(String caseIds) {
        this.caseIds = caseIds;
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

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
