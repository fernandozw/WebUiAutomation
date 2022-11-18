package com.thinkingdata.webdriverImpl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thinkingdata.webui.daoUi.ElementBackupDao;
import com.thinkingdata.webui.entityUi.ElementBackup;
import com.thinkingdata.webui.entityUi.WebUiElement;
import com.thinkingdata.webui.entityUi.WebUiStep;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.thinkingdata.function.JavaScriptExecute;
import com.thinkingdata.function.WaitElement;
import com.thinkingdata.lib.HandleUtils;
import com.thinkingdata.lib.ReflectionUtils;
import org.springframework.stereotype.Component;


/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/21 17:45
 */

@Component
public class PageElement {
    private Logger log = LoggerFactory.getLogger(PageElement.class);

    @Autowired
    private ElementBackupDao elementBackupDao;

    /**
     * 根据WebUiElement 对象获取Webdriver Element对象
     *
     * @param driver    全局Webdriver对象
     * @param uiElement WebUiElement对象
     * @return 返回Element对象
     * @throws Exception 异常抛出在执行case时捕捉
     */
    public WebElement getElement(WebDriver driver, WebUiElement uiElement) {

        // 用于存放获取的WebElment对象
        WebElement element;
        // 定位方式
        String locateType = uiElement.getLocateType();
        // 定位方式的关键字
        String keyword = uiElement.getKeyword();
        try {
            // 根据方法名称反射By类型下面的方法
            Method method = By.class.getMethod(locateType, String.class);
            // 构建By对象
            Class<?> clazz = Class.forName("org.openqa.selenium.By");
            // 通过关键字调用By对象下的方法并返回By对象
            By by = (By) method.invoke(clazz, keyword);
            // 默认等待5秒元素出现
            WebDriverWait wait = new WebDriverWait(driver, 5L);
            wait.until(ExpectedConditions.presenceOfElementLocated(by));
            // 通过By对象对页面元素进行定位
            element = driver.findElement(by);
        } catch (Exception e) {
            log.error("定位元素出现异常:{}", e);
            element = null;
        }
        return element;

    }

    /**
     * 根据元素列表获取WebElement对象
     *
     * @param driver    WebDriver
     * @param uiElement 对象
     * @return
     */
    public Map<String, Object> getElementByList(WebDriver driver, WebUiElement uiElement) {
        WebElement targetElement = null;
        Boolean status = true;
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<WebUiElement> elementList = new ArrayList<>();
        List<String> errorList = new ArrayList<>();
        elementList.add(uiElement);
        List<ElementBackup> elementBackupList = elementBackupDao.getElementBackupByElementId(uiElement.getId());
        for (ElementBackup elementBackup : elementBackupList) {
            WebUiElement element = new WebUiElement();
            element.setId(uiElement.getId());
            element.setIframe(uiElement.getIframe());
            element.setPageId(uiElement.getPageId());
            element.setLocateType(elementBackup.getLocateType());
            element.setKeyword(elementBackup.getKeyword());
            element.setElementName(uiElement.getElementName());
            elementList.add(element);
        }

        for (WebUiElement listElement : elementList) {

            targetElement = this.getElement(driver, listElement);
            if (targetElement != null) {
                status = true;
                errorList.add("通过定位方式【%s】和关键字【%s】定位到了元素");
                break;
            } else {
                status = false;
                String errorString = String.format("无法通过定位方式【%s】和关键字【%s】定位到元素", listElement.getLocateType(), listElement.getKeyword());
                errorList.add(errorString);
            }
        }
        resultMap.put("status", status);
        resultMap.put("msg", errorList);
        resultMap.put("element", targetElement);
        return resultMap;
    }

    /**
     * 执行元素的action
     *
     * @param driver    全局Webdriver对象
     * @param step      WebUiStep对象
     * @param uiElement WebUiElement对象
     * @throws Exception 抛出异常在执行case时捕捉
     */
    public Map<String, Object> elementAction(WebDriver driver, WebUiStep step, WebUiElement uiElement) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Boolean status = true;
        Object msg = "";
        String caseFunction = step.getFunction();
        // 对页面元素的操作方法
        String action = step.getAction();
        // 操作方法的值
        String value = step.getActionValue();
        // 初始化操作,进入默认的iframe
        driver.switchTo().defaultContent();
        // 如果iframe不为空,切换至此iframe
        if (StringUtils.isNotBlank(uiElement.getIframe())) {
            driver.switchTo().frame(uiElement.getIframe());
        }
        // 如果要执行的用例函数不为空或者null,执行excuteFunction函数
        if (StringUtils.isNotBlank(caseFunction)) {
            Map<String, Object> funcMap = execFunction(driver, step, uiElement);
            Boolean funcStatus = (Boolean) funcMap.get("status");
            Object funcMsg = funcMap.get("msg");
            // 如果函数执行成功
            if (!funcStatus) {
                // 函数执行失败
                status = funcStatus;
                msg = funcMsg;
                resultMap.put("status", status);
                resultMap.put("msg", msg);
                return resultMap;
            }
        }
        Map<String, Object> elementMap = getElementByList(driver, uiElement);
        WebElement element = (WebElement) elementMap.get("element");
        Boolean elementStatus = (Boolean) elementMap.get("status");
        Object elementMsg = elementMap.get("msg");
        // 如果定位到了元素
        if (elementStatus) {
            // 元素的操作不为空,执行操作
            if (StringUtils.isNotBlank(action)) {
                Map<String, Object> actionMap = execAction(driver, element, action, value);
                Boolean actionStatus = (Boolean) actionMap.get("status");
                Object actionMsg = actionMap.get("msg");
                if (!actionStatus) {
                    status = actionStatus;
                    msg = actionMsg;
                }
            }

        } else {
            // 没有找到元素
            status = elementStatus;
            msg = elementMsg;
        }


        resultMap.put("status", status);
        resultMap.put("msg", msg);
        return resultMap;
    }

    /**
     * 元素执行的外部函数
     *
     * @param driver    全局Webdriver对象
     * @param step      WebUiStep 对象
     * @param uiElement WebUiElement 对象
     * @throws Exception 抛出异常在执行case时捕捉
     */

    private Map<String, Object> execFunction(WebDriver driver, WebUiStep step, WebUiElement uiElement) {
        // 获取用例要执行的函数
        String caseFunction = step.getFunction();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Boolean status = true;
        Object msg = "";
        try {
            // 根据.将字符串切割
            String[] functionArray = caseFunction.split("\\.");
            // 类名称和参数
            String classNameAndParam = functionArray[0];
            //类名称和参数数组
            String[] classArray = classNameAndParam.split("\\(");
            //类名称
            String className = classArray[0];
            // 方法名称和参数字符串
            String methodNameAndParam = functionArray[1];
            // 方法名称和参数数组
            String[] methodArray = methodNameAndParam.split("\\(");
            // 方法名称
            String methodName = methodArray[0];
            // 方法参数（）形式
            String paramForm = methodArray[1].substring(methodArray[1].indexOf("(") + 1, methodArray[1].indexOf(")"));
            // 方法的参数值
            String[] paramValues = StringUtils.isBlank(paramForm) ? new String[0] : paramForm.split(",");
            String[] paramTypes = null;
            if (paramValues.length > 0) {
                paramTypes = new String[paramValues.length];
                for (int i = 0; i < paramTypes.length; i++) {
                    paramTypes[i] = "java.lang.String";
                }
            } else {
                paramTypes = new String[0];
            }
            // 执行JavaScript函数
            if (className.equalsIgnoreCase("JavaScriptExecute")) {
                try {
                    JavaScriptExecute javaScriptExecute = new JavaScriptExecute(driver, uiElement);
                    ReflectionUtils.initLoadClass(javaScriptExecute, methodName, paramTypes, paramValues);
                } catch (Exception e) {
                    status = false;
                    msg = String.format("外部函数:【%s】执行失败:%s", caseFunction, HandleUtils.handleErrInfo(e));
                }

                // 执行页面元素等待
            } else if (className.equalsIgnoreCase("WaitElement")) {
                try {
                    // 类参数
                    Integer classParam = Integer.valueOf(classArray[1].substring(classArray[1].indexOf("(") + 1, classArray[1].indexOf(")")));
                    // 处理超时时间
                    Integer timeout = classParam / 1000;
                    if (timeout < 1) {
                        timeout = 1;
                    }
                    Map<String, Object> waitMap = waitElementByList(driver, uiElement, Long.parseLong(timeout.toString()), methodName, paramTypes, paramValues);
                    Boolean waitStatus = (Boolean) waitMap.get("status");
                    Object waitMsg = waitMap.get("msg");
                    if (!waitStatus) {
                        status = waitStatus;
                        msg = waitMsg;
                    }
                } catch (Exception e) {
                    status = false;
                    msg = String.format("外部函数:【%s】执行失败:%s", caseFunction, HandleUtils.handleErrInfo(e));
                }
            } else {
                status = false;
                msg = String.format("执行外部函数异常,不存在的类:%s", className);
            }
        } catch (Exception e) {
            status = false;
            msg = String.format("执行外部函数【%s】反射时异常:%s", caseFunction, HandleUtils.handleErrInfo(e));
        }
        resultMap.put("status", status);
        resultMap.put("msg", msg);
        return resultMap;
    }


    /**
     * 执行元素的操作
     *
     * @param driver  Webdriver 对象
     * @param element WebElement对象
     * @param action  元素操作
     * @param value   操作的值
     * @return
     */
    public Map<String, Object> execAction(WebDriver driver, WebElement element, String action, String value) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Boolean status = true;
        String msg = "";
        // 用于存放根据action获取的方法名称
        Method method;
        // 有参数的action
        try {
            if (StringUtils.isNotBlank(value)) {
                if (action.equalsIgnoreCase("sendKeys")) {
                    // sendKeys方法的参数为CharSequence[]需要特殊处理
                    method = WebElement.class.getMethod(action, CharSequence[].class);
                    method.invoke(element, (Object) HandleUtils.handleStrTocs(value));
                } else {
                    method = WebElement.class.getMethod(action, String.class);
                    method.invoke(element, (Object) value);
                }
            } else if (action.equalsIgnoreCase("mouseOver")) {// mouseOver不属于页面元素本身的action
                new Actions(driver).moveToElement(element).perform();
            } else {
                method = WebElement.class.getMethod(action);
                method.invoke(element);
            }
        } catch (Exception e) {
            status = false;
            msg = String.format("元素操作【%s】执行失败:%s", action, HandleUtils.handleErrInfo(e));
        }
        resultMap.put("status", status);
        resultMap.put("msg", msg);
        return resultMap;
    }

    /**
     * 等待元素根据元素列表
     *
     * @param driver      Webdriver 对象
     * @param uiElement   WebUiElement对象
     * @param timeout     超时时间
     * @param methodName  方法名称
     * @param paramTypes  参数类型列表
     * @param paramValues 参数值列表
     * @return
     */
    public Map<String, Object> waitElementByList(WebDriver driver, WebUiElement uiElement, Long timeout, String methodName, String[] paramTypes, String[] paramValues) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Boolean status = true;
        List<WebUiElement> elementList = new ArrayList<WebUiElement>();
        elementList.add(uiElement);
        List<ElementBackup> elementBackupList = elementBackupDao.getElementBackupByElementId(uiElement.getId());
        for (ElementBackup elementBackup : elementBackupList) {
            WebUiElement element = new WebUiElement();
            element.setId(uiElement.getId());
            element.setIframe(uiElement.getIframe());
            element.setPageId(uiElement.getPageId());
            element.setLocateType(elementBackup.getLocateType());
            element.setKeyword(elementBackup.getKeyword());
            element.setElementName(uiElement.getElementName());
            elementList.add(element);
        }
        List<String> errorList = new ArrayList<String>();
        for (WebUiElement listElement : elementList) {
            try {
                WaitElement waitElement = new WaitElement(driver, listElement, Long.parseLong(timeout.toString()));
                ReflectionUtils.initLoadClass(waitElement, methodName, paramTypes, paramValues);
                status = true;
                errorList.add(String.format("元素等待外部函数:【%s】,通过定位方式【%s】找到了元素【%s】", methodName, listElement.getLocateType(), listElement.getKeyword()));
                break;
            } catch (Exception e) {
                status = false;
                errorList.add(String.format("等待【%s】秒后,无法通过定位方式【%s】找到元素【%s】", timeout, listElement.getLocateType(), listElement.getKeyword()));
            }
        }
        resultMap.put("status", status);
        resultMap.put("msg", errorList);
        return resultMap;
    }


    public static void main(String[] args) {
        List<String> errorList = new ArrayList<>();
        errorList.add("1");
        errorList.add("2");
        System.out.println(errorList.toString());
    }
}