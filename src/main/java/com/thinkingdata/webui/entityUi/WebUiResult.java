package com.thinkingdata.webui.entityUi;


import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/21 17:45
 */
@Component
public class WebUiResult {
    // 执行结果id
    private Integer id;
    // 用例id
    private Integer caseId;
    // 失败步骤id
    private Integer failStepId;
    //    // 失败步骤图片
//    private byte[] failStepImage;
// 失败步骤图片
    private String failStepImage;
    // 失败原因
    private String failReason;
    // 用例执行状态
    private Integer status;
    // 用例执行耗时
    private String useTime;
    // 添加时间
    private String addTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public Integer getFailStepId() {
        return failStepId;
    }

    public void setFailStepId(Integer failStepId) {
        this.failStepId = failStepId;
    }

//    public byte[] getFailStepImage() {
//        return failStepImage;
//    }
//
//    public void setFailStepImage(byte[] failStepImage) {
//        this.failStepImage = failStepImage;¬
//    }

    public String getFailStepImage() {
        return failStepImage;
    }

    public void setFailStepImage(String failStepImage) {
        this.failStepImage = failStepImage;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
