package com.thinkingdata.webui.entityUi;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2022/09/27 11:14
 */
public class ExpectImage {
    // 主键id
    private Integer id;
    // 步骤id
    private Integer stepId;
    // 期望完整图片
    private String completeImage;
    // 期望局部图片
    private String partImage;
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

    public Integer getStepId() {
        return stepId;
    }

    public void setStepId(Integer stepId) {
        this.stepId = stepId;
    }

    public String getCompleteImage() {
        return completeImage;
    }

    public void setCompleteImage(String completeImage) {
        this.completeImage = completeImage;
    }

    public String getPartImage() {
        return partImage;
    }

    public void setPartImage(String partImage) {
        this.partImage = partImage;
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
