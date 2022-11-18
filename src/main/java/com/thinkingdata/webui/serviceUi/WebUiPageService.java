package com.thinkingdata.webui.serviceUi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thinkingdata.webui.daoUi.*;
import com.thinkingdata.webui.entityUi.WebUiStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.thinkingdata.response.ResponseDataUtils;
import com.thinkingdata.response.ResponseData;
import com.thinkingdata.webui.entityUi.WebUiPage;


/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/21 17:45
 */
@Service
public class WebUiPageService {
    private static final Integer pageSize = 10;
    @Autowired
    private WebUiPageDao webUiPageDao;

    @Autowired
    private WebUiCaseDao webUiCaseDao;

    @Autowired
    private WebUiStepDao webUiStepDao;

    @Autowired
    private WebUiElementDao webUiElementDao;
    @Autowired
    private ExpectImageDao expectImageDao;
    @Autowired
    private ElementBackupDao elementBackupDao;

    /**
     * 新增页面
     *
     * @param page 页面对象
     * @return 响应结果
     */
    public Object addPage(WebUiPage page) {
        ResponseData<Object> responseData;
        if (webUiPageDao.checkPage(page.getId(), page.getPageName()) > 0) {
            responseData = ResponseDataUtils.buildAddFail("业务【" + page.getPageName() + "】已存在!");
        } else {
            webUiPageDao.addPage(page);
            Integer id = page.getId();
            Map<String, Integer> map = new HashMap<String, Integer>();
            map.put("id", id);
            responseData = ResponseDataUtils.buildAddSuccess(map);
        }
        return responseData;
    }

    /**
     * 返回页面列表
     *
     * @param map 请求参数
     * @return 响应结果
     */
    public Object pages(Map<String, Object> map) {
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
        List<WebUiPage> pages = webUiPageDao.pages(map);
        Integer total = webUiPageDao.total(map);
        dataMap.put("pages", pages);
        dataMap.put("total", total);
        dataMap.put("pageSize", pageSize);
        responseData = ResponseDataUtils.buildSuccess(dataMap);
        return responseData;
    }

    /***
     * 更新页面
     *
     * @param page 页面对象
     * @return 响应结果
     */
    public Object updatePage(WebUiPage page) {
        ResponseData<Object> responseData;
        if (webUiPageDao.checkPage(page.getId(), page.getPageName()) > 0) {
            responseData = ResponseDataUtils.buildUpdateFail("业务【" + page.getPageName() + "】已存在!");
        } else {
            Integer result = webUiPageDao.updatePage(page);
            if (result > 0) {
                responseData = ResponseDataUtils.buildUpdateSuccess();
            } else {
                responseData = ResponseDataUtils.buildUpdateFail();
            }
        }
        return responseData;
    }


    /***
     * 删除页面
     *
     * @param id 页面id
     * @return 响应结果
     */
    public Object deletePageById(Integer id) {
        ResponseData<Object> responseData;
        WebUiPage page = webUiPageDao.getPageById(id);
        if (webUiPageDao.checkDelete(id) > 0) {
            responseData = ResponseDataUtils.buildDeleteFail("业务【" + page.getPageName() + "】存在元素,无法删除!");

        } else {
            Integer result = webUiPageDao.deletePageById(id);
            if (result > 0) {
                responseData = ResponseDataUtils.buildDeleteSuccess();
            } else {
                responseData = ResponseDataUtils.buildDeleteFail();
            }
        }
        return responseData;
    }


    /***
     * 获取页面
     *
     * @param id 页面id
     * @return 响应结果
     */
    public Object getPageById(Integer id) {
        ResponseData<Object> responseData = null;

        WebUiPage result = webUiPageDao.getPageById(id);
        if (result != null) {
            responseData = ResponseDataUtils.buildSuccess(result);
        } else {
            responseData = ResponseDataUtils.buildError();
        }
        return responseData;
    }


    /**
     * 获取全部页面
     *
     * @return
     */
    public Object allPage() {
        ResponseData<Object> responseData;
        List<WebUiPage> list = webUiPageDao.allPage();
        responseData = ResponseDataUtils.buildSuccess(list);
        return responseData;
    }

    /**
     * 删除与业务关联的所有子表记录
     *
     * @param pageId 业务id
     * @return
     */

    public Object deleteAllById(Integer pageId) {
        ResponseData<Object> responseData;
        WebUiPage page = webUiPageDao.getPageById(pageId);
        webUiCaseDao.deleteCaseByPageId(pageId);
        expectImageDao.deleteImageByPageId(pageId);
        webUiStepDao.deleteStepByPageId(pageId);
        elementBackupDao.deleteBackupByPageId(pageId);
        webUiElementDao.deleteElementByPageId(pageId);
        webUiPageDao.deletePageById(pageId);
        responseData = ResponseDataUtils.buildDeleteSuccess("业务【" + page.getPageName() + "】及所有关联的用例、元素、步骤、校验图片已全部删除!");
        return responseData;
    }

}
