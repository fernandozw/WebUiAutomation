package com.thinkingdata.webdriverImpl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thinkingdata.webui.daoUi.ExpectImageDao;
import com.thinkingdata.webui.daoUi.WebUiStepDao;
import com.thinkingdata.webui.entityUi.ExpectImage;
import com.thinkingdata.webui.entityUi.WebUiElement;
import com.thinkingdata.webui.entityUi.WebUiStep;
import com.thinkingdata.lib.RequestTools;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/6/5 11:07
 */
@Component
public class WebDriverVerify {


    // 对比完整图片
    @Value(value = "${compareConfig.completeUrl}")
    private String completeUrl;


    // 对比局部图片
    @Value(value = "${compareConfig.partUrl}")
    private String partUrl;


    // 对比混合图片
    @Value(value = "${compareConfig.mixUrl}")
    private String mixUrl;

    @Autowired
    private PageElement pageElement;
    @Autowired
    private ExpectImageDao expectImageDao;
    @Autowired
    private RequestTools requestTools;

    /***
     * 判断url是否正确
     * @param driver driver对象
     * @param expectUrl 期望url
     * @return
     */

    public Boolean verifyLocation(WebDriver driver, String expectUrl) {
        return driver.getCurrentUrl().equalsIgnoreCase(expectUrl);
    }


    /**
     * 判断标题是否正确
     *
     * @param driver      driver对象
     * @param expectTitle 期望标题
     * @return
     */
    public Boolean verifyTitle(WebDriver driver, String expectTitle) {
        return driver.getTitle().equalsIgnoreCase(expectTitle);
    }

    /**
     * 判断输入框value值是否正确
     *
     * @param uiElement   当前元素
     * @param expectValue 期望值
     * @return
     */
    public Boolean verifyValue(WebDriver driver, WebUiElement uiElement, String expectValue) throws Exception {
        WebElement element = pageElement.getElement(driver, uiElement);
        return element.getAttribute("value").equalsIgnoreCase(expectValue);
    }

    /**
     * 判断文本内容是否正确
     *
     * @param uiElement   当前元素
     * @param expectValue 期望文本
     * @return
     */
    public Boolean verifyText(WebDriver driver, WebUiElement uiElement, String expectValue) throws Exception {
        WebElement element = pageElement.getElement(driver, uiElement);

        return element.getText().equalsIgnoreCase(expectValue);
    }

    /**
     * 判断文本是否包含期望值
     *
     * @param uiElement   当前元素
     * @param expectValue 期望值
     * @return
     */
    public Boolean verifyContain(WebDriver driver, WebUiElement uiElement, String expectValue) throws Exception {
        WebElement element = pageElement.getElement(driver, uiElement);
        return element.getText().contains(expectValue);
    }

    /**
     * 判断元素是否被选中
     *
     * @param uiElement 当前元素
     * @return
     */
    public Boolean verifySelected(WebDriver driver, WebUiElement uiElement) throws Exception {
        WebElement element = pageElement.getElement(driver, uiElement);

        return element.isSelected();
    }

    /**
     * 判断元素是否可用
     *
     * @param uiElement 当前元素
     * @return
     */
    public Boolean verifyEnabled(WebDriver driver, WebUiElement uiElement) throws Exception {
        WebElement element = pageElement.getElement(driver, uiElement);

        return element.isEnabled();
    }


    /***
     * 判断当前元素是否可见
     * @param uiElement 当前元素
     * @return
     */
    public Boolean verifyDisplay(WebDriver driver, WebUiElement uiElement) throws Exception {
        WebElement element = pageElement.getElement(driver, uiElement);

        return element.isDisplayed();
    }

    /***
     * 校验元素是否在页面上出现
     * @param driver driver对象
     * @param uiElement 元素对象
     * @return
     */
    public Boolean verifyElementPresent(WebDriver driver, WebUiElement uiElement) {
        Boolean result = true;
        try {
            pageElement.getElement(driver, uiElement);
        } catch (Exception e) {
            result = false;
        }
        return result;
    }


    /**
     * 页面校验
     *
     * @param verifyAction 校验操作
     * @param driver       driver对象
     * @param verifyValue  期望值
     * @return
     */
    public Boolean verifyPage(WebDriver driver, String verifyAction, String verifyValue) {
        Boolean result = false;
        if (verifyAction.equalsIgnoreCase("assertLocation")) {
            result = this.verifyLocation(driver, verifyValue);
        } else if (verifyAction.equalsIgnoreCase("assertTitle")) {
            result = this.verifyTitle(driver, verifyValue);
        }
        return result;
    }


    /**
     * 元素校验
     *
     * @param verifyAction 校验操作
     * @param uiElement    当前元素
     * @param verifyValue  期望值
     * @return
     */
    public Boolean verifyElement(WebDriver driver, WebUiElement uiElement, String verifyAction, String verifyValue) throws Exception {
        Boolean result = false;
        if (verifyAction.equalsIgnoreCase("assertText")) {
            result = this.verifyText(driver, uiElement, verifyValue);
        } else if (verifyAction.equalsIgnoreCase("assertContains")) {
            result = this.verifyContain(driver, uiElement, verifyValue);
        } else if (verifyAction.equalsIgnoreCase("assertDisplay")) {
            result = this.verifyDisplay(driver, uiElement);
        } else if (verifyAction.equalsIgnoreCase("assertEnabled")) {
            result = this.verifyEnabled(driver, uiElement);
        } else if (verifyAction.equalsIgnoreCase("assertNotChecked") || verifyAction.equalsIgnoreCase("assertNotSelected")) {
            result = !this.verifySelected(driver, uiElement);
        } else if (verifyAction.equalsIgnoreCase("assertChecked") || verifyAction.equalsIgnoreCase("assertSelected")) {
            result = this.verifySelected(driver, uiElement);
        } else if (verifyAction.equalsIgnoreCase("assertValue")) {
            result = this.verifyValue(driver, uiElement, verifyValue);
        } else if (verifyAction.equalsIgnoreCase("assertElementPresent")) {
            result = this.verifyElementPresent(driver, uiElement);
        } else if (verifyAction.equalsIgnoreCase("assertElementNotPresent")) {
            result = !this.verifyElementPresent(driver, uiElement);

        } else if (verifyAction.equalsIgnoreCase("assertTitle")) {
            result = this.verifyTitle(driver, verifyValue);
        } else if (verifyAction.equalsIgnoreCase("assertLocation")) {
            result = this.verifyLocation(driver, verifyValue);
        }
        return result;
    }

    /***
     * 校验图片
     * @param step 步骤对象
     * @param actualImage 实际图片
     * @return 返回对比结果
     */
    public Map<String, Object> verifyImage(WebUiStep step, String actualImage) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        // 获取步骤id
        Integer stepId = step.getId();
        // 校验类型
        String verifyType = step.getVerifyAction();
        // 期望图片对象
        ExpectImage expectImage = expectImageDao.getImage(stepId);
        if (verifyType.equalsIgnoreCase("assertCompleteOrPartialImage")) {
            // 局部、完全图片对比
            String expect = expectImage.getCompleteImage();
            String part = expectImage.getPartImage();
            JSONObject jsonMix = new JSONObject();
            jsonMix.put("expect", expect);
            jsonMix.put("part", part);
            jsonMix.put("actual", actualImage);
            resultMap = requestTools.doPost(mixUrl, jsonMix.toJSONString());

        } else if (verifyType.equalsIgnoreCase("assertCompleteImage")) {
            //完全图片对比
            String completeImage = expectImage.getCompleteImage();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("expect", completeImage);
            jsonObject.put("actual", actualImage);
            resultMap = requestTools.doPost(completeUrl, jsonObject.toJSONString());

        } else if (verifyType.equalsIgnoreCase("assertPartialImage")) {
            // 局部图片对比
            String partialImage = expectImage.getPartImage();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("expect", partialImage);
            jsonObject.put("actual", actualImage);
            System.out.println(jsonObject);
            resultMap = requestTools.doPost(partUrl, jsonObject.toJSONString());

        }
        return resultMap;
    }


}