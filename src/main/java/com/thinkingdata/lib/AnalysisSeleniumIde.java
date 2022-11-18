package com.thinkingdata.lib;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thinkingdata.Application;
import com.thinkingdata.webui.daoUi.*;
import com.thinkingdata.webui.entityUi.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2022/08/15 11:00
 * 解析selenium ide 脚本内容
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Component
public class AnalysisSeleniumIde {

    @Autowired
    private WebUiCaseDao caseDao;
    @Autowired
    private WebUiPageDao pageDao;
    @Autowired
    private WebUiElementDao elementDao;
    @Autowired
    private WebUiStepDao stepDao;
    @Autowired
    private ElementBackupDao elementBackupDao;

    // 元素所有comand列表(包括元素本身action、verify、wait)
    private List<String> comandList = Stream.of("type", "sendKeys", "click", "submit", "clear", "mouseOver", "waitForElementVisible", "waitForElementPresent", "assertText", "assertContains", "assertDisplay", "assertEnabled", "assertNotChecked", "assertChecked", "assertNotSelected", "assertSelected", "assertValue", "assertElementPresent", "assertElementNotPresent", "assertTitle", "assertLocation", "verifyText", "verifyContains", "verifyDisplay", "verifyEnabled", "verifyNotChecked", "verifyChecked", "verifyNotSelected", "verifySelected", "verifyValue", "verifyElementPresent", "verifyElementNotPresent", "verifyTitle", "verifyLocation").collect(toList());
    // 元素操作(action)列表
    private List<String> actionList = Stream.of("type", "sendKeys", "click", "submit", "clear", "mouseOver").collect(toList());
    // 元素外部函数(funtion)列表
    private List<String> functionList = Stream.of("waitForElementVisible", "waitForElementPresent").collect(toList());
    // 校验操作(verify)列表
    private List<String> verifyList = Stream.of("assertText", "assertContains", "assertDisplay", "assertEnabled", "assertNotChecked", "assertChecked", "assertNotSelected", "assertSelected", "assertValue", "assertElementPresent", "assertElementNotPresent", "assertTitle", "assertLocation", "verifyText", "verifyContains", "verifyDisplay", "verifyEnabled", "verifyNotChecked", "verifyChecked", "verifyNotSelected", "verifySelected", "verifyValue", "verifyElementPresent", "verifyElementNotPresent", "verifyTitle", "verifyLocation").collect(toList());
    // 页面校验操作列表
    private List<String> pageVerifyList = Stream.of("assertTitle", "assertLocation", "verifyTitle", "verifyLocation").collect(toList());
    // 元素所有操作对应关系
    private HashMap<String, String> commandMap = new HashMap<String, String>() {{
        put("type", "sendKeys");
        put("sendKeys", "sendKeys");
        put("click", "click");
        put("submit", "submit");
        put("clear", "clear");
        put("mouseOver", "mouseOver");
        put("waitForElementVisible", "waitForElementVisible");
        put("waitForElementPresent", "waitForElementPresent");
        put("assertText", "assertText");
        put("assertContains", "assertContains");
        put("assertDisplay", "assertDisplay");
        put("assertEnabled", "assertEnabled");
        put("assertNotChecked", "assertNotChecked");
        put("assertChecked", "assertChecked");
        put("assertNotSelected", "assertNotSelected");
        put("assertSelected", "assertSelected");
        put("assertValue", "assertValue");
        put("assertElementPresent", "assertElementPresent");
        put("assertElementNotPresent", "assertElementNotPresent");
        put("assertTitle", "assertTitle");
        put("assertLocation", "assertLocation");
        put("verifyText", "assertText");
        put("verifyContains", "assertContains");
        put("verifyDisplay", "assertDisplay");
        put("verifyEnabled", "assertEnabled");
        put("verifyNotChecked", "assertNotChecked");
        put("verifyChecked", "assertChecked");
        put("verifyNotSelected", "assertNotSelected");
        put("verifySelected", "assertSelected");
        put("verifyValue", "assertValue");
        put("verifyElementPresent", "assertElementPresent");
        put("verifyElementNotPresent", "assertElementNotPresent");
        put("verifyTitle", "assertTitle");
        put("verifyLocation", "assertLocation");


    }};

    // 元素所有操作是否有值
    private HashMap<String, Boolean> commandValue = new HashMap<String, Boolean>() {{
        put("type", true);
        put("sendKeys", true);
        put("click", false);
        put("submit", false);
        put("clear", false);
        put("mouseOver", false);
        put("waitForElementVisible", false);
        put("waitForElementPresent", false);
        put("assertText", true);
        put("assertContains", true);
        put("assertDisplay", false);
        put("assertEnabled", false);
        put("assertNotChecked", false);
        put("assertChecked", false);
        put("assertNotSelected", false);
        put("assertSelected", false);
        put("assertValue", true);
        put("assertElementPresent", true);
        put("assertElementNotPresent", true);
        put("assertTitle", true);
        put("assertLocation", true);
    }};


    // 元素定位方式
    private HashMap<String, String> locationMap = new HashMap<String, String>() {{
        put("id", "id");
        put("name", "name");
        put("className", "className");
        put("css", "cssSelector");
        put("linkText", "linkText");
        put("partialLinkText", "partialLinkText");
        put("tagName", "tagName");
        put("xpath", "xpath");

    }};

    public List<Integer> initCaseBySeleniumIde(Map<String, Object> sourceMap) {
        List<Integer> caseIdList = new ArrayList<Integer>();
        // 新增页面
        String pageName = sourceMap.get("name").toString();

        Integer pageId = handlePage(pageName);

        // 获取url
        String url = sourceMap.get("url").toString();

        List<Map<String, Object>> caseList = (List<Map<String, Object>>) sourceMap.get("tests");

        for (Map<String, Object> caseInfo : caseList) {
            List<String> stepIdList = new ArrayList<String>();

            String caseName = caseInfo.get("name").toString();
            // 新增用例
            Integer caseId = handleCase(url, caseName, pageId);
            // 获取步骤集
            List<Map<String, String>> stepList = (List<Map<String, String>>) caseInfo.get("commands");
            // 移除非相关步骤
            Iterator iterator = stepList.iterator();
            while (iterator.hasNext()) {
                Map<String, String> cur = (Map<String, String>) iterator.next();
                if (!comandList.contains(cur.get("command"))) {
                    iterator.remove();
                }
            }

            for (Map<String, String> step : stepList) {
                String stepId = handleElementAndStep(pageId, step).toString();
                stepIdList.add(stepId);
            }
            WebUiCase uiCase = caseDao.getCaseById(caseId);
            uiCase.setStepIds(StringUtils.join(stepIdList, ","));
            caseDao.updateCase(uiCase);
            caseIdList.add(caseId);

        }
        return caseIdList;
    }


    /**
     * 新增元素和步骤
     *
     * @param pageId 页面id
     * @param param  元素详情
     * @return 步骤id
     */
    private Integer handleElementAndStep(Integer pageId, Map<String, String> param) {
        String paramStr = JSON.toJSONString(param);
        Map<String, Object> paramMap = JSONObject.parseObject(paramStr);
        Map<String, String> elementInfo =
                paramMap.entrySet().stream()
                        .filter(map -> !"targets".equals(map.getKey()))
                        .collect(Collectors.toMap(Map.Entry::getKey, e -> (String) e.getValue()));
        Integer result = 0;
        WebUiElement element = new WebUiElement();
        element.setPageId(pageId);
        if (StringUtils.isNotBlank(elementInfo.get("comment"))) {
            element.setElementName(elementInfo.get("comment"));
        } else {
            element.setElementName(elementInfo.get("target"));
        }
        // 获取定位方式
        String[] locateAndKeyword = elementInfo.get("target").split("=", 2);
        // 特殊处理页面校验,直接定位到dom根节点
        if (pageVerifyList.contains(elementInfo.get("command"))) {
            element.setLocateType("xpath");
            element.setKeyword("/html/body");
            // 有明确的定位方式时
        } else if (locateAndKeyword.length == 2) {
            element.setLocateType(locationMap.get(locateAndKeyword[0]));
            element.setKeyword(locateAndKeyword[1]);
            // 没有明确的定位方式时直接使用xpath
        } else {
            element.setLocateType("xpath");
            element.setKeyword(locateAndKeyword[0]);
        }
        // 添加元素
        elementDao.addElement(element);
        if (element.getId() > 0) {
            // 解析元素备份操作方式
            List<List<String>> elementBackupList = (List<List<String>>) paramMap.get("targets");

            if (elementBackupList.size() > 0) {
                handleElementBackup(element.getId(), elementBackupList);
            }
            WebUiStep step = new WebUiStep();
            step.setStatus(1);
            if (StringUtils.isNotBlank(elementInfo.get("comment"))) {
                step.setStepName(elementInfo.get("comment"));

            } else {
                step.setStepName(elementInfo.get("target") + "-" + elementInfo.get("command"));
            }
            step.setElementId(element.getId());
            // 设置步骤的操作
            if (actionList.contains(elementInfo.get("command")) && !pageVerifyList.contains(elementInfo.get("command"))) {
                step.setAction(commandMap.get(elementInfo.get("command")));
                if (commandValue.get(elementInfo.get("command"))) {
                    step.setActionValue(elementInfo.get("value"));
                }
            }
            // 设置步骤的外部函数
            if (functionList.contains(elementInfo.get("command"))) {
                step.setFunction("WaitElement" + "(" + elementInfo.get("value") + ")." + commandMap.get(elementInfo.get("command")) + "()");
            }
            // 设置元素的校验
            if (verifyList.contains(elementInfo.get("command"))) {
                step.setVerifyAction(commandMap.get(elementInfo.get("command")));
                if (commandValue.get(commandMap.get(elementInfo.get("command"))) && !pageVerifyList.contains(elementInfo.get("command"))) {
                    step.setVerifyValue(elementInfo.get("value"));
                } else if (commandValue.get(elementInfo.get("command")) && pageVerifyList.contains(elementInfo.get("command"))) {
                    step.setVerifyValue(elementInfo.get("target"));
                }
            }
            // 添加步骤
            stepDao.addStep(step);
            result = step.getId();
        }
        return result;
    }


    /**
     * 新增用例
     *
     * @param url      url地址
     * @param caseName 用例名称
     * @param pageId   所属页面
     * @return 用例id
     */
    private Integer handleCase(String url, String caseName, Integer pageId) {
        WebUiCase uiCase = new WebUiCase();
        uiCase.setCaseName(caseName);
        uiCase.setUrl(url);
        uiCase.setPageId(pageId);
        uiCase.setPageType("pc_web");
        uiCase.setBrowserType("chrome");
        uiCase.setStatus(1);
        caseDao.addCase(uiCase);
        return uiCase.getId();
    }

    /**
     * 批量新增元素备份定位方式
     *
     * @param elementId  元素id
     * @param backupList 备份定位方式列表
     * @return
     */
    private Integer handleElementBackup(Integer elementId, List<List<String>> backupList) {

        List<ElementBackup> elementBackupList = new ArrayList<ElementBackup>();

        for (List<String> item : backupList) {
            String elementBackupStr = item.get(0);
            String[] locationArr = elementBackupStr.split("=", 2);
            ElementBackup elementBackup = new ElementBackup();
            elementBackup.setElementId(elementId);
            elementBackup.setLocateType(locationMap.get(locationArr[0]));
            elementBackup.setKeyword(locationArr[1]);
            elementBackupList.add(elementBackup);
        }
        Integer result = elementBackupDao.batchAdd(elementBackupList);
        return result;
    }

    /**
     * 新增页面
     *
     * @param pageName 页面名称
     * @return 页面id
     */
    private Integer handlePage(String pageName) {
        WebUiPage page = new WebUiPage();
        page.setPageName(pageName);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (pageDao.checkPage(page.getId(), pageName) > 0) {
            page.setPageName(pageName + "-" + df.format(new Date()));
        }
        pageDao.addPage(page);
        return page.getId();
    }


    @Test
    public void test1() {
//        handleCase("https://ta-preview.thinkingdata.cn/", "看板分析");
        List<String> list1 = new ArrayList<String>();
        list1.add("css=.dashbordName___3es16:nth-child(2)");
        list1.add("css:finder");
        List<String> list2 = new ArrayList<String>();
        list2.add("xpath=//div[@id='192']/div[2]");
        list2.add("xpath:idRelative");
        List<String> list3 = new ArrayList<String>();
        list3.add("xpath=//div[2]/div[2]/div/div[2]");
        list3.add("xpath:position");
        List<List<String>> source = Arrays.asList(list1, list2, list3);
//        Map<String, Object> map = (Map) JSON.parse(source);
//        System.out.println(initCaseBySeleniumIde(map));
        System.out.println(Arrays.asList(handleElementBackup(1, source)));
    }
}
