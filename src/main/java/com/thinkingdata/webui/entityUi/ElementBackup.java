package com.thinkingdata.webui.entityUi;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2022/10/27 18:28
 */
public class ElementBackup {
    private Integer id;
    private Integer elementId;
    private String locateType;
    private String keyword;
    private String addTime;
    private String updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getElementId() {
        return elementId;
    }

    public void setElementId(Integer elementId) {
        this.elementId = elementId;
    }

    public String getLocateType() {
        return locateType;
    }

    public void setLocateType(String locateType) {
        this.locateType = locateType;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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
}
