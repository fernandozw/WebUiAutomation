package com.thinkingdata.function;


import java.lang.reflect.Method;

import com.thinkingdata.webui.daoUi.ElementBackupDao;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.thinkingdata.webui.entityUi.WebUiElement;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/21 17:45
 */
public class WaitElement {
    private WebDriverWait wait;
    private By by;
    @Autowired
    private ElementBackupDao elementBackupDao;

    /**
     * 构造函数,显式等待函数
     *
     * @param driver           Webdriver对象
     * @param entity           子用例集主体
     * @param timeOutInSeconds 超时时间
     * @throws Exception
     */
    public WaitElement(WebDriver driver, WebUiElement entity, Long timeOutInSeconds) throws Exception {

        this.wait = new WebDriverWait(driver, timeOutInSeconds);

        // 定位方式
        String locateType = entity.getLocateType();
        // 定位方式的关键字
        String keyword = entity.getKeyword();
        // 根据方法名称反射By类型下面的方法
        Method byMethod = By.class.getMethod(locateType, String.class);
        // 构建By对象
        Class<?> clazz = Class.forName("org.openqa.selenium.By");
        // 通过关键字调用By对象下的方法并返回By对象
        By by = (By) byMethod.invoke(clazz, keyword);
        this.by = by;
    }

    /**
     * 固定等待一段时间
     *
     * @param milliSecond
     * @throws Exception
     */
    public void fixedWait(String milliSecond) throws Exception {
        Thread.sleep(Long.parseLong(milliSecond));
    }

    /**
     * 等待元素加载到dom中,并不代表元素一定是在页面上展示的
     *
     * @param
     */
    public void waitForElementPresent() {
        this.wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }


    /**
     * 等待元素可以被选择
     *
     * @param
     */
    public void waitForElementToBeSelected() {
        this.wait.until(ExpectedConditions.elementToBeSelected(by));
    }

    /**
     * 等待元素在页面上可见
     *
     * @param
     */
    public void waitForElementVisible() {
        this.wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }


    /**
     * 等待页面元素可以点击
     *
     * @param
     */
    public void waitElementToBeClick() {
        this.wait.until(ExpectedConditions.elementToBeClickable(by));
    }

    /**
     * 等待页面元素的文本内容出现预期值
     *
     * @param
     * @param text
     */
    public void waitElementTextTobePresent(String text) {
        this.wait.until(ExpectedConditions.textToBePresentInElementLocated(by, text));
    }

    /**
     * 等待页面元素的value值出现预期值
     *
     * @param
     * @param text
     */
    public void waitElementValueTobePresent(String text) {
        this.wait.until(ExpectedConditions.textToBePresentInElementValue(by, text));
    }

}
