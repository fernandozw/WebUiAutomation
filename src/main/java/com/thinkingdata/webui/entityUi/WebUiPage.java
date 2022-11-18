package com.thinkingdata.webui.entityUi;

import com.alibaba.fastjson.JSON;
/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/21 17:45
 */
public class WebUiPage {
	// 页面的id
	private Integer id;
	// 页面的名称
	private String pageName;
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

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
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
