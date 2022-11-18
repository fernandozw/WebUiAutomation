package com.thinkingdata.webui.serviceUi;

import java.util.*;
import java.util.stream.Collectors;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.thinkingdata.Application;
import com.thinkingdata.configs.Conditions;
import com.thinkingdata.lib.FunctionValue;
import com.thinkingdata.webui.daoUi.ExpectImageDao;
import com.thinkingdata.webui.daoUi.WebUiElementDao;
import com.thinkingdata.webui.daoUi.WebUiPageDao;
import com.thinkingdata.webui.entityUi.WebUiElement;
import com.thinkingdata.webui.entityUi.WebUiPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import com.thinkingdata.response.ResponseDataUtils;
import com.thinkingdata.response.ResponseData;
import com.thinkingdata.webui.daoUi.WebUiStepDao;
import com.thinkingdata.webui.entityUi.WebUiStep;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/21 17:45
 */


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Service
public class WebUiStepService {
    private static final Integer pageSize = 10;
    @Autowired
    private Conditions conditions;
    @Autowired
    private FunctionValue functionValue;

    @Autowired
    private WebUiPageDao uiPageDao;

    @Autowired
    private WebUiElementDao elementDao;

    @Autowired
    private WebUiStepDao stepDao;
    @Autowired
    private ExpectImageDao expectImageDao;

    public Object addStep(WebUiStep step) {
        ResponseData<Object> responseData = null;
        Map<String, Object> map = new HashMap<String, Object>();
        stepDao.addStep(step);
        map.put("id", step.getId());
        responseData = ResponseDataUtils.buildAddSuccess(map);
        return responseData;
    }

    public Object stepList(Map<String, Object> map) {
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
        List<Map<String, Object>> list = stepDao.steps(map);

        Integer total = stepDao.total(map);

        dataMap.put("steps", list);
        dataMap.put("total", total);
        dataMap.put("pageSize", pageSize);
        responseData = ResponseDataUtils.buildSuccess(dataMap);
        return responseData;
    }

    public Object allStep() {
        ResponseData<Object> responseData = null;
        List<WebUiStep> list = stepDao.getAllStep();
        responseData = ResponseDataUtils.buildSuccess(list);
        return responseData;
    }

    public Object updateStep(WebUiStep step) {
        ResponseData<Object> responseData = null;
        Integer result = stepDao.updateStep(step);
        if (result > 0) {
            responseData = ResponseDataUtils.buildUpdateSuccess();
        } else {
            responseData = ResponseDataUtils.buildUpdateFail();
        }
        return responseData;
    }

    public Object deleteStep(Integer id) {
        ResponseData<Object> responseData;
        if (checkStepInUse(id)) {
            WebUiStep step = stepDao.getUiStepById(id);
            responseData = ResponseDataUtils.buildDeleteFail("步骤【" + step.getStepName() + "】被用例使用了,无法删除!");

        } else {
            Integer result = stepDao.deleteStepById(id);
            Integer imageResult = expectImageDao.deleteImage(id);
            if (result > -1 && imageResult > -1) {
                responseData = ResponseDataUtils.buildDeleteSuccess();
            } else {
                responseData = ResponseDataUtils.buildDeleteFail();
            }
        }
        return responseData;
    }

    private Boolean checkStepInUse(Integer id) {
        List<String> caseStepIdList = stepDao.allStepId();
        List<String> stepIdList = Arrays.asList(caseStepIdList.stream().map(String::valueOf).collect(Collectors.joining(",")).split(","));
        Set<String> stepIdSet = stepIdList.stream().map(String::valueOf).collect(Collectors.toSet());
        return stepIdSet.contains(String.valueOf(id));
    }

    public Object getStepById(Integer id) {
        ResponseData<Object> responseData = null;
        WebUiStep step = stepDao.getUiStepById(id);

        responseData = ResponseDataUtils.buildSuccess(step);

        return responseData;
    }

    public Object getStepDetailById(Integer id) {
        ResponseData<Object> responseData;
        Map<String, Object> dataMap = new HashMap<String, Object>();
        Map<String, Object> step = stepDao.getStepDetailById(id);
        List<WebUiElement> elementList = elementDao.getUiElementByPageId(Integer.valueOf(step.get("pageId").toString()));
        dataMap.put("step", step);
        dataMap.put("elementList", elementList);
        responseData = ResponseDataUtils.buildSuccess(dataMap);
        return responseData;
    }

    public Object getStepListByPageId(Integer pageId) {
        ResponseData<Object> responseData = null;
        List<WebUiStep> stepList = stepDao.getStepListByPageId(pageId);

        responseData = ResponseDataUtils.buildSuccess(stepList);

        return responseData;
    }


    /**
     * 查询条件初始化
     *
     * @return
     */
    public Object getSearchCondition() {
        ResponseData<Object> responseData;
        List<WebUiPage> pageList = uiPageDao.allPage();
        List<Map> uiPageList = pageList.stream().map(webUiPage -> {
            // 将JavaBean 转换为Map
            Map map = JSONObject.parseObject(JSON.toJSONString(webUiPage), Map.class);
            return map;
        }).collect(Collectors.toList());
        List<WebUiElement> elementList = elementDao.getAllElement();
        List<Map> uiElementList = elementList.stream().map(webUiElement -> {
            // 将JavaBean 转换为Map
            Map map = JSONObject.parseObject(JSON.toJSONString(webUiElement), Map.class);
            return map;
        }).collect(Collectors.toList());
        Map<String, Object> searchMap = new HashMap<>();
        searchMap.put("actionList", conditions.getActionList());
        searchMap.put("actionParamMap", conditions.getActionParamMap());
        searchMap.put("pageList", uiPageList);
        searchMap.put("verifyList", conditions.getVerifyList());
        searchMap.put("verifyMap", conditions.getVerifyMap());
        searchMap.put("statusList", conditions.getStatusList());
        searchMap.put("statusMap", conditions.getStatusMap());
        searchMap.put("elementList", uiElementList);
        responseData = ResponseDataUtils.buildSuccess(searchMap);
        return responseData;
    }


    public Object noticeList() {
        ResponseData<Object> responseData;
        Map<String, String> paramMap = new HashMap<String, String>();
        List<Map<String, String>> paramList = new ArrayList<Map<String, String>>();
        paramMap.putAll(functionValue.initValue());
        Set<String> keySet = paramMap.keySet();
        for (String key : keySet) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("key", key);
            map.put("value", paramMap.get(key));

            paramList.add(map);
        }
        responseData = ResponseDataUtils.buildSuccess(paramList);

        return responseData;
    }

    @Test
    public void test1() {

        System.out.println(conditions.getActionList());
        System.out.println(conditions.getStatusMap());
        System.out.println(conditions.getStatusList());
        System.out.println(conditions.getActionParamMap());
        System.out.println(conditions.getVerifyMap());
        System.out.println(conditions.getVerifyList());


    }

}
