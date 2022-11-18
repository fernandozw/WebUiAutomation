package com.thinkingdata.webui.daoUi;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import com.thinkingdata.webui.entityUi.WebUiPage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/21 17:45
 */
@Repository
@Mapper
public interface WebUiPageDao {
    public Integer addPage(WebUiPage page);

    public List<WebUiPage> pages(Map<String, Object> map);

    public Integer total(Map<String, Object> map);

    public Integer updatePage(WebUiPage page);

    public List<WebUiPage> allPage();

    public Integer deletePageById(Integer id);

    public WebUiPage getPageById(Integer id);


    public Integer checkPage(@Param("id") Integer id, @Param("pageName") String pageName);


    public Integer checkDelete(Integer id);
};
