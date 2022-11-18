package com.thinkingdata.webui.daoUi;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.thinkingdata.webui.entityUi.WebUiCase;
import com.thinkingdata.webui.entityUi.WebUiStep;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.thinkingdata.webui.entityUi.WebUiResult;
import org.springframework.stereotype.Repository;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/21 17:45
 */

@Repository
@Mapper
public interface WebUiResultDao {
    public Integer addResult(WebUiResult result);

    public List<WebUiResult> results(Map<String, Object> map);

    public Integer total(Map<String, Object> map);

    public List<Map<String,Object>> totalGroupByDate(Map<String, Object> map);


    public WebUiResult result(@Param("id") Integer id, @Param("status") Integer status);


    public List<WebUiCase> caseList(@Param("ids") Set<Integer> ids);

    public List<WebUiStep> stepList(@Param("ids") Set<Integer> ids);

}
