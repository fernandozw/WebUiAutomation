package com.thinkingdata.webui.serviceUi;

import java.util.*;
import java.util.stream.Collectors;

import com.thinkingdata.Application;
import com.thinkingdata.lib.SplitTime;
import com.thinkingdata.webui.daoUi.WebUiPageDao;
import com.thinkingdata.webui.entityUi.WebUiPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import com.thinkingdata.response.ResponseDataUtils;
import com.thinkingdata.response.ResponseData;
import com.thinkingdata.webui.daoUi.WebUiResultDao;
import com.thinkingdata.webui.entityUi.WebUiResult;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/21 17:45
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Service
public class WebUiResultService {
    @Autowired
    private WebUiPageDao uiPageDao;
    @Autowired
    private WebUiResultDao resultDao;
    @Autowired
    private SplitTime splitTime;
    private static final Integer pageSize = 10;
    private Logger log = LoggerFactory.getLogger(WebUiResultService.class);


    private final static Map<String, Object> SUCCESS = new HashMap<String, Object>() {
        {
            put("label", "成功");
            put("value", 1);
        }
    };
    private final static Map<String, Object> FAIlED = new HashMap<String, Object>() {
        {
            put("label", "失败");
            put("value", 2);
        }
    };

    private final static List<Object> STATUS_LIST = Arrays.asList(SUCCESS, FAIlED);


    private final static Map<Object, Object> STATUS_MAP = new HashMap<Object, Object>() {
        {
            put(1, "成功");
            put(2, "失败");
        }
    };


    public Object addResult(WebUiResult result) {
        ResponseData<Object> responseData = null;
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            log.info("插入结果请求参数:{}", result.toString());
            resultDao.addResult(result);
            map.put("id", result.getId());
            responseData = ResponseDataUtils.buildSuccess("200", "插入成功", map);
        } catch (Exception e) {
            log.error("插入结果请求异常:{}", e);
            responseData = ResponseDataUtils.buildError("500", "服务器内部错误");
        }
        return responseData;
    }

    public Object resultList(Map<String, Object> map) {
        ResponseData<Object> responseData = null;
        Map<String, Object> dataMap = new HashMap<String, Object>();
        Integer limit1 = (Integer) map.get("pageNum") * pageSize - pageSize;
        map.put("limit1", limit1);
        map.put("limit2", pageSize);
        List addTimeRange = map.get("addTime") != "" & map.get("addTime") != null ? (List) map.get("addTime") : new ArrayList();
        if (addTimeRange.size() > 1) {
            map.put("startTime", addTimeRange.get(0));
            map.put("endTime", addTimeRange.get(1));

        }
        List<WebUiResult> list = resultDao.results(map);
        Integer total = resultDao.total(map);
        dataMap.put("resultList", list);
        dataMap.put("total", total);
        dataMap.put("pageSize", pageSize);
        responseData = ResponseDataUtils.buildSuccess(dataMap);
        return responseData;
    }

    public Object image(Integer id, Integer status) {

        return resultDao.result(id, status);
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
        searchMap.put("pageList", pageList);

        searchMap.put("statusList", STATUS_LIST);
        searchMap.put("statusMap", STATUS_MAP);
        responseData = ResponseDataUtils.buildSuccess(searchMap);
        return responseData;
    }

    /***
     * 获取所有图形指标
     * @param paramMap 参数Map
     * @return
     */
    public Object getResultPic(Map<String, Object> paramMap) {
        ResponseData<Object> responseData;
        List<String> addTimeRange = paramMap.get("addTime") != "" & paramMap.get("addTime") != null ? (List) paramMap.get("addTime") : new ArrayList();

        if (addTimeRange.size() > 1) {
            List<String> timeRange = splitTime.sliceUpDateRange(addTimeRange.get(0), addTimeRange.get(1));
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("categories", timeRange);
            Integer successTotal = getTotalPie(1, paramMap);
            Integer failTotal = getTotalPie(2, paramMap);

            List<Integer> successSpline = getColumnAndChart(1, paramMap);
            List<Integer> failSpline = getColumnAndChart(2, paramMap);
            List<Integer> allColumn = getColumnAndChart(0, paramMap);

            dataMap.put("successSpline", successSpline);
            dataMap.put("failSpline", failSpline);
            dataMap.put("allColumn", allColumn);
            dataMap.put("successTotal", successTotal);
            dataMap.put("failTotal", failTotal);

            responseData = ResponseDataUtils.buildSuccess(dataMap);
        } else {
            responseData = ResponseDataUtils.buildSuccess("必须选择起始时间和截止时间!");

        }

        return responseData;
    }

    /***
     * 获取饼图
     * @param model 状态枚举
     * @param paramMap 参数Map
     * @return
     */
    private Integer getTotalPie(Integer model, Map<String, Object> paramMap) {
        switch (model) {
            case 0:
                paramMap.put("status", "");
                break;
            case 1:
                paramMap.put("status", 1);
                break;
            case 2:
                paramMap.put("status", 2);
                break;
            default:
                break;
        }
        List<String> addTimeRange = paramMap.get("addTime") != "" & paramMap.get("addTime") != null ? (List) paramMap.get("addTime") : new ArrayList();
        paramMap.put("startTime", addTimeRange.get(0) + " 00:00:00");
        paramMap.put("endTime", addTimeRange.get(1) + " 23:59:59");
        return resultDao.total(paramMap);
    }

    /***
     * 获取趋势图和柱状图数据
     * @param model 状态枚举
     * @param paramMap 参数Map
     * @return
     */
    private List<Integer> getColumnAndChart(Integer model, Map<String, Object> paramMap) {
        switch (model) {
            case 0:
                paramMap.put("status", "");
                break;

            case 1:
                paramMap.put("status", 1);
                break;

            case 2:
                paramMap.put("status", 2);
                break;

            default:
                break;
        }
        List<String> addTimeRange = paramMap.get("addTime") != "" & paramMap.get("addTime") != null ? (List) paramMap.get("addTime") : new ArrayList();
        paramMap.put("startTime", addTimeRange.get(0) + " 00:00:00");
        paramMap.put("endTime", addTimeRange.get(1) + " 23:59:59");
        Map<String, Integer> chartMap = new LinkedHashMap<String, Integer>();
        for (Map<String, Object> element : resultDao.totalGroupByDate(paramMap)) {
            if (!chartMap.containsKey(element.get("key"))) {
                chartMap.put(element.get("key").toString(), Integer.valueOf(element.get("value").toString()));

            } else if (chartMap.containsKey(element.get("key")) && Integer.valueOf(element.get("value").toString()) > 0) {
                chartMap.put(element.get("key").toString(), Integer.valueOf(element.get("value").toString()));
            }
        }
        List<Integer> dataList = chartMap.values().stream().collect(Collectors.toList());
        return dataList;
    }


    @Test
    public void test1() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", "");
        map.put("id", "");
        map.put("caseId", "");
        map.put("caseName", "");
        map.put("startTime", "2022-08-01");
        map.put("endTime", "2022-08-31");

        List<Map<String, Object>> list = resultDao.totalGroupByDate(map);
        Map<String, Integer> chartMap = new LinkedHashMap<String, Integer>();
        System.out.println(list);
        for (Map<String, Object> element : list) {
            if (!chartMap.containsKey(element.get("key"))) {
                chartMap.put(element.get("key").toString(), Integer.valueOf(element.get("value").toString()));

            } else if (chartMap.containsKey(element.get("key")) && Integer.valueOf(element.get("value").toString()) > 0) {
                chartMap.put(element.get("key").toString(), Integer.valueOf(element.get("value").toString()));

            }
        }
        List<Integer> dataList = chartMap.values().stream().collect(Collectors.toList());

    }
}
