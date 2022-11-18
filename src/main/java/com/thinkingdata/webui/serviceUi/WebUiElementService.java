package com.thinkingdata.webui.serviceUi;

import java.util.*;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.thinkingdata.configs.Conditions;
import com.thinkingdata.webui.daoUi.WebUiPageDao;
import com.thinkingdata.webui.daoUi.WebUiStepDao;
import com.thinkingdata.webui.entityUi.WebUiPage;
import com.thinkingdata.webui.entityUi.WebUiStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.thinkingdata.response.ResponseDataUtils;
import com.thinkingdata.response.ResponseData;
import com.thinkingdata.webui.daoUi.WebUiElementDao;
import com.thinkingdata.webui.entityUi.WebUiElement;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/21 17:45
 */
@Service
public class WebUiElementService {

    private static final Integer pageSize = 10;
    //    private final static Map<String, Object> ID = new HashMap<String, Object>() {
//        {
//            put("label", "id");
//            put("value", "id");
//        }
//    };
//    private final static Map<String, Object> NAME = new HashMap<String, Object>() {
//        {
//            put("label", "name");
//            put("value", "name");
//        }
//    };
//    private final static Map<String, Object> CLASSNAME = new HashMap<String, Object>() {
//        {
//            put("label", "className");
//            put("value", "className");
//        }
//    };
//    private final static Map<String, Object> CSSSELETEOR = new HashMap<String, Object>() {
//        {
//            put("label", "cssSelector");
//            put("value", "cssSelector");
//        }
//    };
//    private final static Map<String, Object> LINKTEXT = new HashMap<String, Object>() {
//        {
//            put("label", "linkText");
//            put("value", "linkText");
//        }
//    };
//
//    private final static Map<String, Object> PARTIALLINKTEXT = new HashMap<String, Object>() {
//        {
//            put("label", "partialLinkText");
//            put("value", "partialLinkText");
//        }
//    };
//    private final static Map<String, Object> TAGNAME = new HashMap<String, Object>() {
//        {
//            put("label", "tagName");
//            put("value", "tagName");
//        }
//    };
//    private final static Map<String, Object> XPATH = new HashMap<String, Object>() {
//        {
//            put("label", "xpath");
//            put("value", "xpath");
//        }
//    };
//
//    private final static List<Object> LOCATION_LIST = Arrays.asList(ID, NAME, CLASSNAME, CSSSELETEOR, LINKTEXT, PARTIALLINKTEXT, TAGNAME, XPATH);
    @Autowired
    private Conditions conditions;
    @Autowired
    private WebUiElementDao elementDao;


    @Autowired
    private WebUiPageDao uiPageDao;


    @Autowired
    private WebUiStepDao uiStepDao;

    public Object addElement(WebUiElement element) {
        ResponseData<Object> responseData = null;
        Integer stepIdNum = 0;
        Map<String, Object> map = new HashMap<String, Object>();
        elementDao.addElement(element);
        if (element.getId() > 0) {
            stepIdNum = uiStepDao.batchInsertStep(initStep(element));
        }
        map.put("id", element.getId());
        map.put("stepIdNum", stepIdNum);
        responseData = ResponseDataUtils.buildAddSuccess(map);
        return responseData;
    }

    public Object elementList(Map<String, Object> map) {
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
        List<WebUiElement> list = elementDao.elements(map);
        Integer total = elementDao.total(map);
        dataMap.put("elements", list);
        dataMap.put("total", total);
        dataMap.put("pageSize", pageSize);
        responseData = ResponseDataUtils.buildSuccess(dataMap);
        return responseData;
    }

    public Object updateElement(WebUiElement element) {
        ResponseData<Object> responseData = null;
        Integer result = elementDao.updateElement(element);
        if (result > 0) {
            responseData = ResponseDataUtils.buildUpdateSuccess();
        } else {
            responseData = ResponseDataUtils.buildUpdateFail();
        }
        return responseData;
    }


    public Object deleteElement(Integer id) {
        ResponseData<Object> responseData;
        WebUiElement element = elementDao.getUiElementById(id);
        if (elementDao.checkDelete(id) > 0) {
            responseData = ResponseDataUtils.buildDeleteFail("元素【" + element.getElementName() + "】下存在步骤,无法删除!");
        } else {
            Integer result = elementDao.deleteById(id);
            uiStepDao.deleteStepByElementId(id);
            if (result > 0) {
                responseData = ResponseDataUtils.buildDeleteSuccess();
            } else {
                responseData = ResponseDataUtils.buildDeleteFail();
            }
        }
        return responseData;
    }

    public Object getElementByPageId(Integer pageId) {
        ResponseData<Object> responseData = null;
        List<WebUiElement> list = elementDao.getUiElementByPageId(pageId);
        responseData = ResponseDataUtils.buildSuccess(list);
        return responseData;
    }


    public Object getUiElementById(Integer elementId) {
        ResponseData<Object> responseData = null;
        WebUiElement uiElement = elementDao.getUiElementById(elementId);
        responseData = ResponseDataUtils.buildSuccess(uiElement);
        return responseData;
    }


    public Object getSearchCondition() {
        ResponseData<Object> responseData;
        List<WebUiPage> pageList = uiPageDao.allPage();
        List<Map> uiPageList = pageList.stream().map(webUiPage -> {
            // 将JavaBean 转换为Map
            Map map = JSONObject.parseObject(JSON.toJSONString(webUiPage), Map.class);
            return map;
        }).collect(Collectors.toList());
        Map<Integer, Object> uiPageMap = pageList.stream().collect(Collectors.toMap(WebUiPage::getId, t -> t));


        Map<String, Object> searchMap = new HashMap<>();
        searchMap.put("locateList", conditions.getLocationList());
        searchMap.put("pageList", uiPageList);
        responseData = ResponseDataUtils.buildSuccess(searchMap);
        return responseData;
    }


    /**
     * 为元素批量生成步骤
     *
     * @param element 元素对象
     * @return
     */

    public List<WebUiStep> initStep(WebUiElement element) {
        // webdriver api提供的元素操作的方法
        String[] actionArray = new String[]{"clear", "click", "sendKeys", "submit"};
        List<WebUiStep> uiStepList = new ArrayList<>();

        for (String action : actionArray) {
            WebUiStep uiStep = new WebUiStep();
            uiStep.setElementId(element.getId());
            uiStep.setStepName(element.getElementName() + "-" + action);
            uiStep.setAction(action);
            uiStep.setStatus(1);
            uiStepList.add(uiStep);
        }

        return uiStepList;
    }

}
