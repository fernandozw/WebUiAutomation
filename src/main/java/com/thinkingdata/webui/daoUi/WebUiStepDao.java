package com.thinkingdata.webui.daoUi;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.thinkingdata.webui.entityUi.WebUiCase;
import org.apache.ibatis.annotations.Mapper;

import com.thinkingdata.webui.entityUi.WebUiStep;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/21 17:45
 */
@Repository
@Mapper
public interface WebUiStepDao {

    public Integer addStep(WebUiStep step);

    public Integer batchInsertStep(@Param("stepList") List<WebUiStep> stepList);

//    public List<WebUiStep> steps(Map<String, Object> map);
    public List<Map<String,Object>> steps(Map<String, Object> map);


    public Integer updateStep(WebUiStep step);

    public WebUiStep getUiStepById(@Param("id") Integer id);

    public Integer deleteStepById(@Param("id") Integer id);

    public List<WebUiStep> getStepListByPageId(@Param("pageId") Integer pageId);

    public List<WebUiStep> getAllStep();

    public Map<String, Object> getStepDetailById(@Param("id") Integer id);

    public List<Map<String, Object>> getStepListBySql(@Param("sql") String sql);

    public Integer batchUpdateStep(@Param("stepList") List<Map<String, Object>> stepList);

    public Integer deleteStepByElementId(@Param("elementId") Integer elementId);


    public Integer total(Map<String,Object> map);

    public List<String> allStepId();

    public Integer deleteStepByPageId(@Param("pageId") Integer pageId);
}
