package com.thinkingdata.webui.serviceUi;

import java.util.*;

import com.thinkingdata.Application;
import com.thinkingdata.configs.Conditions;
import com.thinkingdata.lib.AnalysisSeleniumIde;
import com.thinkingdata.lib.FunctionValue;
import com.thinkingdata.lib.HandleParam;
import com.thinkingdata.webdriverImpl.PageElement;
import com.thinkingdata.webdriverImpl.WebDriverSnapshot;
import com.thinkingdata.webdriverImpl.WebDriverVerify;
import com.thinkingdata.webdriverImpl.WebUiDriver;
import com.thinkingdata.webui.daoUi.*;
import com.thinkingdata.webui.entityUi.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.thinkingdata.response.ResponseDataUtils;
import com.thinkingdata.response.ResponseData;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/21 17:45
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Service
public class WebUiCaseService {
    private static final Integer pageSize = 10;
    private Logger log = LoggerFactory.getLogger(WebUiCaseService.class);


    // 对比完整图片
    @Value(value = "${compareConfig.compareImageList}")
    private String compareImageList;

    @Autowired
    private FunctionValue functionValue;

    @Autowired
    private HandleParam handleParam;

    @Autowired
    private WebUiCaseDao caseDao;


    @Autowired
    private WebUiDriver uiDriver;


    @Autowired
    private PageElement pageElement;


    @Autowired
    private WebUiStepDao uiStepDao;

    @Autowired
    private WebUiElementDao uiElementDao;

    @Autowired
    private WebDriverSnapshot snapshot;

    @Autowired
    private WebUiResultDao resultDao;


    @Autowired
    private WebDriverVerify webDriverVerify;


    @Autowired
    private AnalysisSeleniumIde analysisSeleniumIde;

    @Autowired
    private WebUiPageDao uiPageDao;

    @Autowired
    private Conditions conditions;

    /**
     * 新增用例
     *
     * @param uiCase 用例对象
     * @return
     */
    public Object addCase(Object uiCase, List<Map<String, Object>> stepList) {
        ResponseData<Object> responseData;
        Map<String, Object> map = new HashMap<String, Object>();
        Integer caseResult = caseDao.addCase(uiCase);
        Integer stepResult = uiStepDao.batchUpdateStep(stepList);
        if (caseResult > 0 & stepResult > 0) {
            responseData = ResponseDataUtils.buildAddSuccess(map);
        } else {
            responseData = ResponseDataUtils.buildAddFail();
        }
        return responseData;
    }


    public Object insertCaseByIde(Map<String, Object> sourceMap) {
        ResponseData<Object> responseData;
        List<Integer> caseIdList = analysisSeleniumIde.initCaseBySeleniumIde(sourceMap);
        responseData = ResponseDataUtils.buildAddSuccess(caseIdList);
        return responseData;
    }

    public Object getCaseById(Integer id) {
        ResponseData<Object> responseData = null;
        WebUiCase caseDetail = caseDao.getCaseById(id);
        String sql = String.format("SELECT e.pageId,s.id,s.stepName,s.elementId,s.action,s.actionValue,s.function,s.status,s.verifyAction,s.verifyValue,s.paramKey FROM webui.element e, webui.step s where s.elementId=e.id and s.id in (%s)  ORDER BY INSTR('%s',s.id);", caseDetail.getStepIds(), caseDetail.getStepIds());
        List<Map<String, Object>> stepList = uiStepDao.getStepListBySql(sql);
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("caseDetail", caseDetail);
        dataMap.put("stepList", stepList);
        responseData = ResponseDataUtils.buildSuccess(dataMap);
        return responseData;
    }


    /**
     * 用例列表
     *
     * @param map 查询条件
     * @return
     */
    public Object caseList(Map<String, Object> map) {
        ResponseData<Object> responseData = null;
        Map<String, Object> dataMap = new HashMap<String, Object>();
        Integer limit1 = (Integer) map.get("pageNum") * pageSize - pageSize;
        map.put("limit1", limit1);
        map.put("limit2", pageSize);
        List addTimeRange = map.get("startTime") != "" & map.get("startTime") != null ? (List) map.get("startTime") : new ArrayList();
        if (addTimeRange.size() > 0) {
            map.put("addStartTime", addTimeRange.get(0));
            map.put("addEndTime", addTimeRange.get(1));

        }
        List updateTimeRange = map.get("updateTime") != "" & map.get("updateTime") != "" ? (List) map.get("updateTime") : new ArrayList();
        if (updateTimeRange.size() > 0) {
            map.put("upStartTime", updateTimeRange.get(0));
            map.put("upEndTime", updateTimeRange.get(1));

        }
        List<Map<String, Object>> list = caseDao.cases(map);
        Integer total = caseDao.total(map);
        dataMap.put("cases", list);
        dataMap.put("total", total);
        dataMap.put("pageSize", pageSize);
        responseData = ResponseDataUtils.buildSuccess(dataMap);
        return responseData;
    }

    /**
     * 修改用例
     *
     * @param uiCase 用例对象
     * @return
     */
    public Object updateCase(Object uiCase, List<Map<String, Object>> stepList) {
        ResponseData<Object> responseData = null;
        Integer caseResult = caseDao.updateCase(uiCase);
        Integer stepResult = uiStepDao.batchUpdateStep(stepList);
        if (caseResult > 0 & stepResult > 0) {
            responseData = ResponseDataUtils.buildUpdateSuccess();
        } else {
            responseData = ResponseDataUtils.buildUpdateFail();
        }
        return responseData;
    }

    public Object deleteCase(Integer id) {
        ResponseData<Object> responseData = null;
        Integer result = caseDao.deleteCaseById(id);
        if (result > 0) {
            responseData = ResponseDataUtils.buildDeleteSuccess();
        } else {
            responseData = ResponseDataUtils.buildDeleteFail();
        }
        return responseData;
    }

    /**
     * 异步方法,无须等待用例执行完毕
     *
     * @param caseId 用例id
     */
    @Async
    public void execCase(Integer caseId, String model, String browserNode) {
        // 存放动态参数的map
        Map<String, String> paramMap = functionValue.initValue();
        log.info("生成动态参数:{}", paramMap);
        // case执行失败原因
        String failReason = "";
        // case是否执行成功
        Integer resultStatus = 1;
        // 失败截图
        String image = null;
        // 失败步骤的id
        Integer failStepId = null;
        // 用例执行开始时间
        Long startTime = System.currentTimeMillis();
        // 根据用例id获取case对象
        WebUiCase uiCase = caseDao.getCaseById(caseId);
        // 设置用例的运行状态为2,执行中
        uiCase.setStatus(2);
        // 更新数据库
        caseDao.updateCase(uiCase);
        // 启动WebDriver
        Map<String, Object> driverMap = this.uiDriver.createDriver(uiCase, model, browserNode);
        // driver对象
        WebDriver driver = (WebDriver) driverMap.get("driver");
        // node节点相关的pid列表
        List<String> nodePidList = (List<String>) driverMap.get("nodePidList");
        // webDriver启动成功才执行用例
        if ((Integer) driverMap.get("resultStatus") == 1 && driverMap.get("driver") != null) {

            try {
                this.log.info("开始执行用例:{}", "【" + uiCase.getId() + "】:" + uiCase.getCaseName());
                // 获取case对象下的步骤集
                List<String> steps = StringUtils.isNotBlank(uiCase.getStepIds()) ? Arrays.asList(uiCase.getStepIds().split(",")) : new ArrayList<>();
                // 遍历步骤并执行
                for (String stepId : steps) {
                    try {
                        // 根据步骤id获取WebUiStep对象(源)
                        WebUiStep stepSource = this.uiStepDao.getUiStepById(Integer.valueOf(stepId));
                        // 将源步骤赋值给新的对象
                        WebUiStep step = stepSource;
                        // 步骤未被禁用才执行
                        if (step.getStatus() == 1) {
                            // 根据元素id获取WebUiElment对象(源)
                            WebUiElement elementSource = this.uiElementDao.getUiElementById(step.getElementId());
                            // 将源元素赋值给新的对象
                            WebUiElement element = elementSource;
                            // 替换元素关键字中的动态参数
                            element.setKeyword(handleParam.setValue(paramMap, element.getKeyword()));
                            // 判断是否替换参数
                            if (step.getAction() != null && step.getAction().equals("sendKeys") && step.getActionValue() != null && StringUtils.isNotBlank(step.getActionValue())) {
                                step.setActionValue(handleParam.setValue(paramMap, step.getActionValue()));
                                log.info("替换后的参数:{}", step.getActionValue());

                            }
                            // 判断是否替换期望结果
                            if (step.getVerifyAction() != null && conditions.getNeedValueVerifyList().contains(step.getVerifyAction()) && step.getVerifyValue() != null) {
                                step.setVerifyValue(handleParam.setValue(paramMap, step.getVerifyValue()));
                                log.info("替换后的期望结果:{}", step.getVerifyValue());
                            }
                            // 通过用例步骤执行元素操作
                            this.log.info("开始执行步骤:{}", "【" + step.getId() + "】:" + step.getStepName());
                            Map<String, Object> actionMap = this.pageElement.elementAction(driver, step, element);
                            Boolean actionStatus = (Boolean) actionMap.get("status");
                            String actionMsg = actionMap.get("msg").toString();
                            // 步骤执行成功
                            if (actionStatus) {
                                // 步骤断言
                                if (StringUtils.isNotEmpty(step.getVerifyAction())) {
                                    String[] verifyImageArray = compareImageList.split(",");
                                    List<String> verifyImageList = Arrays.asList(verifyImageArray);
                                    if (!verifyImageList.contains(step.getVerifyAction())) {
                                        if (!this.webDriverVerify.verifyElement(driver, element, step.getVerifyAction(), step.getVerifyValue())) {
                                            log.error("实际结果与期望结果不一致,期望结果为:【" + step.getVerifyValue() + "】");
                                            // 失败步骤id
                                            failStepId = Integer.valueOf(stepId);
                                            // 设置失败原因
                                            failReason = "实际结果与期望结果不一致,期望结果为:【" + step.getVerifyValue() + "】";
                                            // 用例执行结果为失败
                                            resultStatus = 2;
                                            // 失败步骤进行截图
                                            image = this.snapshot.snapshotAsString(driver);
                                            // 终止用例的执行
                                            break;
                                        } else {
                                            // 用例执行结果为成功
                                            resultStatus = 1;
                                            log.info("文字、action校验通过,当前步骤执行成功:{}", "【" + step.getId() + "】:" + step.getStepName());
                                        }
                                    } else {
                                        Map<String, Object> imageResult = this.webDriverVerify.verifyImage(step, this.snapshot.snapshotAsString(driver));
                                        if ((Integer) imageResult.get("code") == 201) {
                                            // 失败步骤id
                                            failStepId = Integer.valueOf(stepId);
                                            // 设置失败原因
                                            failReason = (String) imageResult.get("msg");
                                            // 用例执行结果为失败
                                            resultStatus = 2;
                                            // 失败步骤图片为对比后处理的图片
                                            image = (String) imageResult.get("differentAreas");
                                            break;
                                        } else {
                                            // 用例执行结果为成功
                                            resultStatus = 1;
                                            log.info("图片校验通过,当前步骤执行成功:{}", "【" + step.getId() + "】:" + step.getStepName());
                                            // 判断是否需要保存参数值
                                            if (step.getAction() != null && step.getAction().equals("sendKeys") && step.getParamKey() != null && StringUtils.isNotBlank(step.getParamKey())) {
                                                paramMap.put(step.getParamKey(), step.getActionValue());
                                                log.info("保存后的参数:【{}】", step.getActionValue());

                                            }
                                        }
                                    }

                                }
                            } else {

                                // 失败步骤id
                                failStepId = Integer.valueOf(stepId);
                                // 设置失败原因
                                failReason = actionMsg;
                                // 用例执行结果为失败
                                resultStatus = 2;
                                // 截取失败图片
                                image = this.snapshot.snapshotAsString(driver);

                                break;
                            }

                        } else {
                            this.log.info("当前步骤被禁用:{}", "【" + step.getId() + "】:" + step.getStepName() + ",跳过执行");
                            continue;
                        }
                    } catch (Exception e) {
                        this.log.error("执行步骤时异常,异常信息:{}", e);
                        // 出现异常,设置用例执行结果为失败
                        resultStatus = 2;
                        // 异常作为失败步骤的原因
                        failReason = e.toString();
                        // 失败步骤
                        failStepId = Integer.valueOf(stepId);
                        // 出现异常进行截图
                        image = this.snapshot.snapshotAsString(driver);

                        break;
                    }
                }
            } catch (Exception e) {
                this.log.error("执行步骤之前发生异常,异常信息:{}", e);
                // 出现异常,设置用例执行结果为失败
                resultStatus = 2;
                // 异常作为失败步骤的原因
                failReason = e.toString();

            } finally {
                // 设置用例的执行状态为1,可执行
                uiCase.setStatus(1);
                // 更新数据库,解除占用
                caseDao.updateCase(uiCase);
                // 保存结果
                saveResult(startTime, System.currentTimeMillis(), uiCase, resultStatus, image, failStepId, failReason);
                // 退出Webdriver,kill node进程
                this.uiDriver.quitDriver(driver, nodePidList);
            }

        } else {

            // 设置用例的执行状态为1,可执行
            uiCase.setStatus(1);
            // 更新数据库,解除占用
            caseDao.updateCase(uiCase);
            // 保存结果
            saveResult(startTime, System.currentTimeMillis(), uiCase, (Integer) driverMap.get("resultStatus"), (String) driverMap.get("image"), failStepId, driverMap.get("failReason").toString());
            // 退出Webdriver,kill node进程
            this.uiDriver.quitDriver(driver, nodePidList);

        }
    }


    /***
     * 判断当前用例是否可执行
     * @param caseId 用例id
     * @return 是否可执行标志位
     */
    public Boolean canExec(Integer caseId) {
        Boolean status = false;
        // 根据用例id获取case对象
        WebUiCase uiCase = caseDao.getCaseById(caseId);
        if (uiCase.getCanUse() == 1) {
            status = true;
        } else {
            status = false;
        }
        return status;
    }

    /***
     * 保存用例执行结果
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param uiCase case对象
     * @param resultStatus 执行状态
     * @param image 图片
     * @param failStepId 失败步骤id
     * @param failReason 失败原因
     */
    private void saveResult(Long startTime, Long endTime, WebUiCase uiCase, Integer resultStatus, String image, Integer failStepId, String failReason) {

        WebUiResult result = new WebUiResult();
        // 为result设置caseId
        result.setCaseId(uiCase.getId());
        // 设置失败步骤的截图
        result.setFailStepImage(image);
        if (resultStatus > 1) {
            // 设置失败图片
            result.setFailStepImage(image);
            // 设置失败步骤
            result.setFailStepId(failStepId);
            // 设置失败原因
            result.setFailReason(failReason);
        }
        // 设置用例结果状态
        result.setStatus(resultStatus);
        double useTime = Math.round(endTime - startTime) / 1000.0;
        // 设置用例的耗时
        result.setUseTime(useTime + "秒");
        // 插入用例执行结果
        resultDao.addResult(result);
    }


    /**
     * 查询条件初始化
     *
     * @return
     */
    public Object getSearchCondition() {
        ResponseData<Object> responseData;
        Map<String, Object> searchMap = new HashMap<>();
        List<WebUiPage> pageList = uiPageDao.allPage();
        searchMap.put("actionList", conditions.getActionList());
        searchMap.put("pageList", pageList);
        searchMap.put("pageTypeList", conditions.getPageTypeList());
        searchMap.put("phoneTypeList", conditions.getPhoneTypeList());
        searchMap.put("browserTypeList", conditions.getBrowserTypeList());
        searchMap.put("canUseList", conditions.getCanUseList());
        searchMap.put("canUseMap", conditions.getCanUseMap());
        searchMap.put("statusList", conditions.getCaseStatusList());
        searchMap.put("statusMap", conditions.getCaseStatusMap());
        searchMap.put("pageVerifyList", conditions.getPageVerifyList());
        responseData = ResponseDataUtils.buildSuccess(searchMap);
        return responseData;
    }

    @Test
    public void test1() {

        System.out.println(StringUtils.isNotBlank(null));
    }
}
