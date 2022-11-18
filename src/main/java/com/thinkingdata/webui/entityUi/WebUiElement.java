package com.thinkingdata.webui.entityUi;

import com.alibaba.fastjson.JSON;
/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/21 17:45
 */
public class WebUiElement {
    // 用例id
    private Integer id;
    // 元素名称
    private String elementName;
    // 元素所属iframe
    private String iframe;
    // 定位方式
    private String locateType;
    // 元素关键字
    private String keyword;
    // 所属页面的id
    private Integer pageId;
    // 新增时间
    private String addTime;
    // 更新时间
    private String updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public String getIframe() {
        return iframe;
    }

    public void setIframe(String iframe) {
        this.iframe = iframe;
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

    public Integer getPageId() {
        return pageId;
    }

    public void setPageId(Integer pageId) {
        this.pageId = pageId;
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