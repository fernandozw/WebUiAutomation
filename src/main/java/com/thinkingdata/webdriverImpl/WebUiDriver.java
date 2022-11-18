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
     * 初始化driver对象
     *
     * @param uiCase case对象
     * @throws Exception
     */
    public Map<String, Object> createDriver(WebUiCase uiCase, String model, String browserNode) {
        // WebDriver初始化失败的原因
        String failReason = "";
        // 失败截图
        String image = null;
        // case执行状态
        Integer resultStatus = 1;
        // WebDriver 对象
        WebDriver driver = null;
        // node节点pid列表
        List<String> nodePidList = new ArrayList<String>();
        // driver启动状态
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            // 如果是正式执行，则先启动node节点
            if (model.equalsIgnoreCase("formal")) {
                // 未被使用的selenium gird port
                String nodePort = this.serverCommand.unUsedPort();
                nodePidList = this.initNode.startNode(nodePort);
                browserNode = browserNode(nodePort);
            }
            // 获取浏览器的模式
            String browserHeadless = this.stringRedisTemplate.opsForValue().get(this.HEADLESS_KEY);
            // 获取浏览器的语言
            String browserLanguage = this.stringRedisTemplate.opsForValue().get(this.BROWSER_LANGUAGE);
            // 获取浏览器的尺寸
            String browserWindowSize = this.stringRedisTemplate.opsForValue().get(this.WINDOW_SIZE);
            String[] windowSize = {};
            if (StringUtils.isNotBlank(browserWindowSize)) {
                windowSize = browserWindowSize.split(",");
            }
            log.info("node端口号:{}", nodePidList);
            // 判断是否h5页面
            if (uiCase.getPageType().equalsIgnoreCase("H5")) {
                // 判断是否chrome浏览器
                if (uiCase.getBrowserType().equalsIgnoreCase("chrome")) {
                    DesiredCapabilities capabilities = DesiredCapabilities.chrome();
                    ChromeOptions chromeOptions = new ChromeOptions();
                    // 使用无头模式
                    if (browserHeadless.equalsIgnoreCase("1")) {
                        // 无界面模式
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
                    throw new Exception("H5页面请使用chrome浏览器!");
                }
            } else {
                if (uiCase.getBrowserType().equalsIgnoreCase("chrome")) {
                    // 使用chrome运行测试用例
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
                    // 使用无头模式
                    if (browserHeadless.equalsIgnoreCase("1")) {
                        // 无界面模式
                        chromeOptions.addArguments("--headless");
                    }
                    capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
                    driver = new RemoteWebDriver(new URL(browserNode), capabilities);

                } else if (uiCase.getBrowserType().equalsIgnoreCase("firefox")) {
                    // 使用fireFox运行测试用例
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
                        log.info("使用无头模式!");

                        firefoxOptions.addArguments("--headless");

                    }
                    DesiredCapabilities capabilities = DesiredCapabilities.firefox();
                    capabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, firefoxOptions);

                    driver = new RemoteWebDriver(new URL(browserNode), capabilities);

                } else {
                    throw new Exception("请使用chrome、firefox浏览器进行测试!");
                }

            }
            // 删除所有的缓存信息
            driver.manage().deleteAllCookies();

            if (windowSize.length == 2) {
                // 设置浏览器尺寸
                Dimension dimension = new Dimension(Integer.valueOf(windowSize[0]), Integer.valueOf(windowSize[1]));
                driver.manage().window().setSize(dimension);
            } else {
                // 最大化浏览器
                driver.manage().window().maximize();

            }
            // 打开连接
            driver.get(uiCase.getUrl());
            // 页面断言
            if (StringUtils.isNotEmpty(uiCase.getVerifyAction())) {
                if (!webDriverVerify.verifyPage(driver, uiCase.getVerifyAction(), uiCase.getVerifyValue())) {
                    if (uiCase.getVerifyAction().equalsIgnoreCase(ASSERT_TITLE)) {
                        failReason = "实际页面标题【" + driver.getTitle() + "】与期望结果【" + uiCase.getVerifyValue() + "】不一致!";
                        log.error(failReason);

                    } else if (uiCase.getVerifyAction().equalsIgnoreCase(ASSERT_LOCATION)) {
                        failReason = "实际页面url【" + driver.getCurrentUrl() + "】与期望结果【" + uiCase.getVerifyValue() + "】不一致!";
                        log.error(failReason);
                    }
                    // 对比不一致的页面截图进行截图
                    image = this.snapshot.snapshotAsString(driver);

                    // 设置状态为失败
                    resultStatus = 2;

                }
            }
        } catch (Exception e) {
            log.error("driver初始化失败:{}", e);
            // 失败原因
            failReason = "driver初始化失败:" + e;
            // 设置状态为失败
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
     * 关闭所有浏览器窗口并关闭driver进程kill node的进程号
     */
    public void quitDriver(WebDriver driver, List<String> nodePidS) {
        if (driver != null) {
            // 退出浏览器driver进程
            driver.quit();
        }
        // kill hub、node进程
        this.initNode.killPid(nodePidS);

    }

    @Test
    public void test1() {

        WebUiCase uiCase = new WebUiCase();
        createDriver(uiCase, "formal", "");
    }
}
