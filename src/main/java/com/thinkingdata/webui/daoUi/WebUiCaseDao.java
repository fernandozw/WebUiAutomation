package com.thinkingdata.webui.daoUi;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.thinkingdata.webui.entityUi.WebUiCase;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/21 17:45
 */
@Repository
@Mapper
public interface WebUiCaseDao {
    public Integer addCase(Object uiCase);

    public List<Map<String,Object>> cases(Map<String, Object> map);

    public Integer total(Map<String, Object> map);

    public Integer updateCase(Object uiCase);

    public WebUiCase getCaseById(@Param("id") Integer caseId);

    public Integer deleteCaseById(@Param("id") Integer caseId);

    public Integer deleteCaseByPageId(@Param("pageId") Integer pageId);


}
