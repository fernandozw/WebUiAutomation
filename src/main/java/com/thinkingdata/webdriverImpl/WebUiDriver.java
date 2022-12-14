package com.thinkingdata.webdriverImpl;

import java.net.URL;
import java.util.*;

import com.thinkingdata.Application;
import com.thinkingdata.init.InitNode;
import com.thinkingdata.init.ServerCommand;
import com.thinkingdata.webui.entityUi.WebUiCase;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/21 17:45
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Component
public class WebUiDriver {

    private Logger log = LoggerFactory.getLogger(WebUiDriver.class);
    private static final String HEADLESS_KEY = "browserHeadless";
    private static final String BROWSER_LANGUAGE = "browserLanguage";
    private static final String ASSERT_TITLE = "assertTitle";
    private static final String ASSERT_LOCATION = "assertLocation";
    private static final String WINDOW_SIZE = "windowSize";


    @Autowired
    @Qualifier("redis_01")
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private WebDriverVerify webDriverVerify;

    @Autowired
    private WebDriverSnapshot snapshot;


    @Autowired
    private InitNode initNode;

    @Autowired
    private ServerCommand serverCommand;

    private String browserNode(String nodePort) {
        return "http://127.0.0.1:" + nodePort + "/wd/hub";
    }

    /**
     * ?????????driver??????
     *
     * @param uiCase case??????
     * @throws Exception
     */
    public Map<String, Object> createDriver(WebUiCase uiCase, String model, String browserNode) {
        // WebDriver????????????????????????
        String failReason = "";
        // ????????????
        String image = null;
        // case????????????
        Integer resultStatus = 1;
        // WebDriver ??????
        WebDriver driver = null;
        // node??????pid??????
        List<String> nodePidList = new ArrayList<String>();
        // driver????????????
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            // ????????????????????????????????????node??????
            if (model.equalsIgnoreCase("formal")) {
                // ???????????????selenium gird port
                String nodePort = this.serverCommand.unUsedPort();
                nodePidList = this.initNode.startNode(nodePort);
                browserNode = browserNode(nodePort);
            }
            // ????????????????????????
            String browserHeadless = this.stringRedisTemplate.opsForValue().get(this.HEADLESS_KEY);
            // ????????????????????????
            String browserLanguage = this.stringRedisTemplate.opsForValue().get(this.BROWSER_LANGUAGE);
            // ????????????????????????
            String browserWindowSize = this.stringRedisTemplate.opsForValue().get(this.WINDOW_SIZE);
            String[] windowSize = {};
            if (StringUtils.isNotBlank(browserWindowSize)) {
                windowSize = browserWindowSize.split(",");
            }
            log.info("node?????????:{}", nodePidList);
            // ????????????h5??????
            if (uiCase.getPageType().equalsIgnoreCase("H5")) {
                // ????????????chrome?????????
                if (uiCase.getBrowserType().equalsIgnoreCase("chrome")) {
                    DesiredCapabilities capabilities = DesiredCapabilities.chrome();
                    ChromeOptions chromeOptions = new ChromeOptions();
                    // ??????????????????
                    if (browserHeadless.equalsIgnoreCase("1")) {
                        // ???????????????
                        chromeOptions.addArguments("--headless");
                        chromeOptions.addArguments("--disable-dev-shm-usage");
                        chromeOptions.addArguments("--no-sandbox");
                        chromeOptions.addArguments("--disable-gpu");
                        chromeOptions.addArguments("--hide-scrollbars");
                        chromeOptions.addArguments("--incognito");
                        if (browserLanguage.equalsIgnoreCase("1")) {
                            chromeOptions.addArguments("lang=zh_CN.UTF-8");

                        } else {
                            chromeOptions.addArguments("--lang=en-ca");

                        }
                    }
                    capabilities.setCapability("deviceName", uiCase.getPhoneType());
                    capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
                    driver = new RemoteWebDriver(new URL(browserNode), capabilities);
                } else {
                    throw new Exception("H5???????????????chrome?????????!");
                }
            } else {
                if (uiCase.getBrowserType().equalsIgnoreCase("chrome")) {
                    // ??????chrome??????????????????
                    DesiredCapabilities capabilities = DesiredCapabilities.chrome();
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-gpu");
//                    chromeOptions.addArguments("blink-settings=imagesEnabled=false");
                    chromeOptions.addArguments("--hide-scrollbars");
                    chromeOptions.addArguments("--incognito");
                    if (browserLanguage.equalsIgnoreCase("1")) {
                        chromeOptions.addArguments("lang=zh_CN.UTF-8");

                    } else {
                        chromeOptions.addArguments("--lang=en-ca");

                    }
                    // ??????????????????
                    if (browserHeadless.equalsIgnoreCase("1")) {
                        // ???????????????
                        chromeOptions.addArguments("--headless");
                    }
                    capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
                    driver = new RemoteWebDriver(new URL(browserNode), capabilities);

                } else if (uiCase.getBrowserType().equalsIgnoreCase("firefox")) {
                    // ??????fireFox??????????????????
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.addArguments("--disable-gpu");
                    FirefoxProfile profile = new FirefoxProfile();
                    if (browserLanguage.equalsIgnoreCase("1")) {
                        profile.setPreference("intl.accept_languages", "zh-CN");

                    } else {
                        profile.setPreference("intl.accept_languages", "en-US, en");

                    }
                    firefoxOptions.setProfile(profile);
                    if (browserHeadless.equalsIgnoreCase("1")) {
                        log.info("??????????????????!");

                        firefoxOptions.addArguments("--headless");

                    }
                    DesiredCapabilities capabilities = DesiredCapabilities.firefox();
                    capabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, firefoxOptions);

                    driver = new RemoteWebDriver(new URL(browserNode), capabilities);

                } else {
                    throw new Exception("?????????chrome???firefox?????????????????????!");
                }

            }
            // ???????????????????????????
            driver.manage().deleteAllCookies();

            if (windowSize.length == 2) {
                // ?????????????????????
                Dimension dimension = new Dimension(Integer.valueOf(windowSize[0]), Integer.valueOf(windowSize[1]));
                driver.manage().window().setSize(dimension);
            } else {
                // ??????????????????
                driver.manage().window().maximize();

            }
            // ????????????
            driver.get(uiCase.getUrl());
            // ????????????
            if (StringUtils.isNotEmpty(uiCase.getVerifyAction())) {
                if (!webDriverVerify.verifyPage(driver, uiCase.getVerifyAction(), uiCase.getVerifyValue())) {
                    if (uiCase.getVerifyAction().equalsIgnoreCase(ASSERT_TITLE)) {
                        failReason = "?????????????????????" + driver.getTitle() + "?????????????????????" + uiCase.getVerifyValue() + "????????????!";
                        log.error(failReason);

                    } else if (uiCase.getVerifyAction().equalsIgnoreCase(ASSERT_LOCATION)) {
                        failReason = "????????????url???" + driver.getCurrentUrl() + "?????????????????????" + uiCase.getVerifyValue() + "????????????!";
                        log.error(failReason);
                    }
                    // ??????????????????????????????????????????
                    image = this.snapshot.snapshotAsString(driver);

                    // ?????????????????????
                    resultStatus = 2;

                }
            }
        } catch (Exception e) {
            log.error("driver???????????????:{}", e);
            // ????????????
            failReason = "driver???????????????:" + e;
            // ?????????????????????
            resultStatus = 2;
            if (driver != null) {
                image = this.snapshot.snapshotAsString(driver);


            }
        }

        resultMap.put("driver", driver);
        resultMap.put("failReason", failReason);
        resultMap.put("image", image);
        resultMap.put("resultStatus", resultStatus);
        resultMap.put("nodePidList", nodePidList);
        return resultMap;
    }


    /**
     * ????????????????????????????????????driver??????kill node????????????
     */
    public void quitDriver(WebDriver driver, List<String> nodePidS) {
        if (driver != null) {
            // ???????????????driver??????
            driver.quit();
        }
        // kill hub???node??????
        this.initNode.killPid(nodePidS);

    }

    @Test
    public void test1() {

        WebUiCase uiCase = new WebUiCase();
        createDriver(uiCase, "formal", "");
    }
}
