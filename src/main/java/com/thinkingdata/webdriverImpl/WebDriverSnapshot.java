package com.thinkingdata.webdriverImpl;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import com.thinkingdata.lib.HandleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/21 17:45
 */
@Component
public class WebDriverSnapshot {
    private Logger log = LoggerFactory.getLogger(WebDriverSnapshot.class);
    private final static String PRE_FIX = "data:image/png;base64,";

    /**
     * 以文本的形式截图
     *
     * @param driver    WebDriver对象
     * @param imagePath 图片路径
     * @throws Exception
     */
    public void snapshotAsImage(WebDriver driver, String imagePath) throws Exception {

        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {

            FileUtils.copyFile(scrFile, new File(imagePath));
        } catch (IOException e) {
            String errorInfo = HandleUtils.handleErrInfo(e);
            log.error("截图出现错误,请检查图片路径是否正确::{}", "【" + imagePath + "】" + errorInfo);
        }
    }

    /**
     * 以二进制的形式截图
     *
     * @param driver WebDriver对象
     * @throws Exception
     */
    public byte[] snapshotAsByte(WebDriver driver) {
        byte[] blob = null;
        try {
            blob = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            log.error("截图转换成二进制时异常:{}", HandleUtils.handleErrInfo(e));
        }
        return blob;
    }

    /**
     * 将图片转换成base64
     *
     * @param driver WebDriver对象
     * @throws Exception
     */
    public String snapshotAsString(WebDriver driver) {
        String base64 = null;
        try {
            byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            base64 = PRE_FIX + Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            log.error("截图转换成base64字符串时异常:{}", HandleUtils.handleErrInfo(e));
        }
        return base64;
    }
}
