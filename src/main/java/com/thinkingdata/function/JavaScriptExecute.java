package com.thinkingdata.function;


import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.thinkingdata.webui.entityUi.WebUiElement;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/21 17:45
 */
public class JavaScriptExecute {

    private JavascriptExecutor js;
    private WebUiElement entity;
    private String locateType;
    private String keyword;

    public JavaScriptExecute() {

    }

    /**
     * 构造函数,js脚本执行器
     *
     * @param driver Webdriver对象
     * @param entity UI子用例集主体
     */
    public JavaScriptExecute(WebDriver driver, WebUiElement entity) {
        this.js = (JavascriptExecutor) driver;
        this.entity = entity;
        this.locateType = entity.getLocateType();
        this.keyword = entity.getKeyword();
    }

    /**
     * 通过js改变页面元素的样式
     *
     * @param style style样式的key
     * @param value style样式的value
     */
    public void changeStyle(String style, String value) {
        if (this.locateType.equalsIgnoreCase("id")) {
            this.js.executeScript("document.getElementById('" + this.keyword + "').style." + style + "='" + value + "';");
        } else if (this.locateType.equalsIgnoreCase("name")) {
            this.js.executeScript("document.getElementsByName('" + this.keyword + "').style." + style + "='" + value + "';");
        } else if (this.locateType.equalsIgnoreCase("className")) {
            this.js.executeScript("var array=document.getElementsByClassName('" + this.keyword + "');array[0].style." + style
                    + "='" + value + "';");
        } else if (this.locateType.equalsIgnoreCase("tagName")) {
            this.js.executeScript("var array=document.getElementsByTagName('" + this.keyword + "');array[0].style." + style + "='"
                    + value + "';");
        } else if (this.locateType.equalsIgnoreCase("xpath")) {
            this.js.executeScript("var result = document.evaluate(\"" + this.keyword
                    + "\",document.documentElement);var node = result.iterateNext();node.style." + style + "='" + value
                    + "';");
        }
    }

}
