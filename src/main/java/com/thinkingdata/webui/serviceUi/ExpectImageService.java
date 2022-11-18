package com.thinkingdata.webui.serviceUi;

import com.thinkingdata.response.ResponseData;
import com.thinkingdata.response.ResponseDataUtils;
import com.thinkingdata.webui.daoUi.ExpectImageDao;
import com.thinkingdata.webui.daoUi.WebUiStepDao;
import com.thinkingdata.webui.entityUi.ExpectImage;
import com.thinkingdata.webui.entityUi.WebUiStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2022/09/27 14:41
 */

@Service
public class ExpectImageService {
    @Autowired
    private ExpectImageDao expectImageDao;


    @Autowired
    private WebUiStepDao webUiStepDao;

    /**
     * 设置图片
     *
     * @param expectImage 期望图片对象
     * @param compareType 对比的类型
     * @return
     */
    public Object setExpectImage(ExpectImage expectImage, String compareType) {
        ResponseData<Object> responseData;
        ExpectImage target = expectImageDao.getImage(expectImage.getStepId());
        WebUiStep webUiStep = webUiStepDao.getUiStepById(expectImage.getStepId());
        Integer result;
        if (target != null) {
            result = expectImageDao.updateImage(expectImage);

        } else {
            result = expectImageDao.addImage(expectImage);
        }
        if (result > 0) {
            webUiStep.setVerifyAction(compareType);
            Integer stepResult = webUiStepDao.updateStep(webUiStep);
            if (stepResult > 0) {
                responseData = ResponseDataUtils.buildSuccess("图片设置成功!");
            } else {
                responseData = ResponseDataUtils.buildSuccess("图片设置失败!");
            }

        } else {
            responseData = ResponseDataUtils.buildSuccess("图片设置失败!");

        }
        return responseData;
    }

    /**
     * 删除期望图片
     *
     * @param stepId 步骤id
     * @return
     */
    public Object deleteImage(Integer stepId) {
        ResponseData<Object> responseData;
        Integer result = expectImageDao.deleteImage(stepId);
        if (result > 0) {
            responseData = ResponseDataUtils.buildDeleteSuccess();
        } else {
            responseData = ResponseDataUtils.buildDeleteFail();
        }
        return responseData;
    }

    /***
     * 根据图片id 获取图片
     * @param stepId 步骤id
     * @return
     */
    public Object getImage(Integer stepId) {
        ResponseData<Object> responseData;
        ExpectImage expectImage = expectImageDao.getImage(stepId);
        responseData = ResponseDataUtils.buildSuccess(expectImage);
        return responseData;
    }
}
