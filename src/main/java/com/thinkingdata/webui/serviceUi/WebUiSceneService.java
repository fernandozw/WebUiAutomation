package com.thinkingdata.webui.serviceUi;

import com.thinkingdata.response.ResponseDataUtils;
import com.thinkingdata.response.ResponseData;
import com.thinkingdata.webdriverImpl.PageElement;
import com.thinkingdata.webdriverImpl.WebDriverSnapshot;
import com.thinkingdata.webdriverImpl.WebUiDriver;
import com.thinkingdata.webui.daoUi.*;
import com.thinkingdata.webui.entityUi.*;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/22 16:41
 */
@Service
public class WebUiSceneService {
    private Integer stepStatus = 1;
    private static final Integer pageSize = 10;
    private Logger log = LoggerFactory.getLogger(WebUiSceneService.class);
    @Autowired
    private WebUiSceneDao uiSceneDao;

    @Autowired
    private WebUiCaseDao uiCaseDao;

    @Autowired
    WebUiDriver uiDriver;

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
    private WebUiResult result;


    public Object addScene(WebUiScene uiScene) {
        ResponseData<Object> responseData = null;
        Map<String, Object> map = new HashMap<String, Object>();
        uiSceneDao.addScene(uiScene);
        map.put("id", uiScene.getId());
        responseData = ResponseDataUtils.buildAddSuccess(map);
        return responseData;
    }

    public Object sceneList(Map<String, Object> map) {
        ResponseData<Object> responseData = null;
        Map<String, Object> dataMap = new HashMap<String, Object>();
        Integer limit1 = Integer.valueOf(map.get("pageNum").toString()) * pageSize - pageSize;
        map.put("limit1", limit1);
        map.put("limit2", pageSize);
        List<WebUiScene> list = uiSceneDao.scenes(map);
        Integer total = uiSceneDao.total(map);
        dataMap.put("scenes", list);
        dataMap.put("total", total);
        dataMap.put("pageSize", pageSize);
        responseData = ResponseDataUtils.buildSuccess(dataMap);
        return responseData;
    }

    public Object updateScene(WebUiScene uiScene) {
        ResponseData<Object> responseData = null;
        Integer result = uiSceneDao.updateScene(uiScene);
        if (result > 0) {
            responseData = ResponseDataUtils.buildUpdateSuccess();
        } else {
            responseData = ResponseDataUtils.buildUpdateFail();
        }
        return responseData;
    }

    @Async
    public void execScene(Integer sceneId) {
        // 根据场景id获取场景对象
        WebUiScene uiScene = uiSceneDao.getSceneById(sceneId);

        // 场景状态为空闲才执行
        if (uiScene.getStatus() == 0) {
            try {
                uiScene.setStatus(1);
                uiSceneDao.updateScene(uiScene);
                log.info("开始执行场景:{}", "【" + sceneId + "】【" + uiScene.getSceneName() + "】");
                // 获取场景下的用例集
                String caseIds = uiScene.getCaseIds();
                List<String> cases = Arrays.asList(caseIds.split(","));

                for (int i = 0; i < cases.size(); i++) {
                    WebUiCase uiCase = uiCaseDao.getCaseById(Integer.valueOf(cases.get(i)));
                    execCase(uiCase);
                }

            } catch (Exception e) {
                log.error("执行场景【" + uiScene.getId() + "】【" + uiScene.getSceneName() + "】异常:{}", e);
            } finally {
                uiScene.setStatus(0);
                uiSceneDao.updateScene(uiScene);
            }
        } else {
            log.error("当前场景正在执行中:{}", "【" + sceneId + "】【" + uiScene.getSceneName() + "】正在执行中~~~");
        }
    }

    public void execCase(WebUiCase uiCase) {
        if (uiCase.getCanUse() == 1 && uiCase.getStatus() == 0) {
            // 获取用例下的执行步骤
            String stepIds = uiCase.getStepIds();
            // 初始化webdriver对象并获取Webdriver是否启动成功
            Map<String,Object> driverMap = uiDriver.createDriver(uiCase,"formal","");
            WebDriver driver = (WebDriver)driverMap.get("driver");
            List<String> nodePidList=(List<String>)driverMap.get("nodePidList");
            // 用例执行开始时间
            Long startTime = System.currentTimeMillis();
            if (driver != null) {
                // 设置用例的执行状态为1,执行中
                uiCase.setStatus(1);
                // 更新数据库
                uiCaseDao.updateCase(uiCase);
                // 用例生命周期driver对象赋值
                // 获取case对象下的步骤集
                List<String> steps = Arrays.asList(stepIds.split(","));
                // 遍历用例步骤并执行
                Integer stepId = 0;
                // 执行结果1,成功,0失败
                Integer resultStatus = 1;
                // 失败原因
                String failReason = "";
                // 失败步骤截图
//                byte[] image = null;
                String image = null;

                log.info("开始执行用例:{}", "【" + uiCase.getId() + "】:" + uiCase.getCaseName());
                try {
                    for (int i = 0; i < steps.size(); i++) {
                        stepId = Integer.valueOf(steps.get(i));
                        // 当标志位stepStatus>0（启用）时才执行步骤
                        if (stepStatus > 0 && stepId > 0) {
                            // 根据步骤id获取WebUiStep对象
                            WebUiStep step = uiStepDao.getUiStepById(stepId);
                            // 根据元素id获取WebUiElment对象
                            WebUiElement element = uiElementDao.getUiElementById(step.getElementId());
                            if (step.getStatus() == 1) {
                                log.info("开始执行步骤:{}", "【" + step.getId() + "】:" + step.getStepName());
                                // 通过用例步骤执行元素操作
                                pageElement.elementAction(driver, step, element);
                            } else {
                                log.info("当前步骤被禁用:{}", "【" + step.getId() + "】:" + step.getStepName() + ",跳过执行");
                                continue;
                            }

                        } else if (stepStatus < 1 || stepId < 1) {
                            // 当标志位stepStatus小于1时,直接跳出循环,整个用例结束执行
                            break;
                        }
                    }
                    // 没有异常,执行结果为成功
                    resultStatus = 1;
                    log.info("用例执行成功:{}", "【" + uiCase.getId() + "】:" + uiCase.getCaseName());
                } catch (Exception e) {
                    // 执行异常,执行结果为失败
                    resultStatus = 0;
                    // 执行异常,失败原因为异常
                    failReason = e.toString();
                    // 执行异常,失败步骤截图
//                    image = snapshot.snapshotAsByte(driver);
                    image = snapshot.snapshotAsString(driver);

                    log.error("执行用例时异常,异常信息:{}", e);
                } finally {
                    // 保存执行结果
                    saveResult(startTime, System.currentTimeMillis(), stepId, image, failReason, stepStatus, resultStatus, uiCase);
                    // 退出Webdriver,kill hub、node进程
                    uiDriver.quitDriver(driver,nodePidList);
                }
            } else {
                log.error("Webdriver初始化失败!");
            }
        } else if (uiCase.getCanUse() == 0) {
            log.error("当前用例已被禁用,跳过执行:{}", "【" + uiCase.getId() + "】【" + uiCase.getCaseName() + "】");
        } else if (uiCase.getStatus() == 1) {
            log.error("当前用例正在执行,跳过执行:{}", "【" + uiCase.getId() + "】【" + uiCase.getCaseName() + "】");
        }
    }

    /**
     * 保存用例执行结果
     *
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param stepId     步骤id
     * @param image      图片
     * @param stepStatus 步骤可执行状态
     * @param status     用例执行状态
     * @param uiCase     case对象
     */
//    private void saveResult(Long startTime, Long endTime, Integer stepId, byte[] image, String failReason, Integer stepStatus, Integer status, WebUiCase uiCase) {
    private void saveResult(Long startTime, Long endTime, Integer stepId, String image, String failReason, Integer stepStatus, Integer status, WebUiCase uiCase) {

        // 为result设置caseId
        result.setCaseId(uiCase.getId());
        if (status < 1) {
            // 设置失败步骤的截图
            result.setFailStepImage(image);
            // 设置失败步骤
            result.setFailStepId(stepId);
            // 设置失败原因
            result.setFailReason(failReason);
        }
        // 设置用例结果状态
        result.setStatus(status);
        // 设置用例的耗时
        result.setUseTime(String.valueOf(endTime - startTime));
        // 插入用例执行结果
        resultDao.addResult(result);
        // 设置步骤的执行状态为0
        this.stepStatus = stepStatus;
        // 设置用例的执行状态为0,可执行
        uiCase.setStatus(0);
        // 更新数据库,解除占用
        uiCaseDao.updateCase(uiCase);
    }


    /***
     * 判断当前场景是否可执行
     * @param sceneId 请求参数
     * @return 是否可执行标志位
     */
    public Boolean canExec(Integer sceneId) {
        Boolean status = false;
        // 根据用例id获取case对象
        WebUiScene uiScene = uiSceneDao.getSceneById(sceneId);
        if (uiScene.getStatus() == 0) {
            status = true;
        } else {
            log.warn("当前场景正在执行中:{}", "【" + uiScene.getId() + "】【" + uiScene.getSceneName() + "】");
            status = false;
        }
        return status;
    }

}

