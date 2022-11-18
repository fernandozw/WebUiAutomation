package com.thinkingdata.webui.daoUi;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.thinkingdata.webui.entityUi.WebUiElement;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/21 17:45
 */
@Repository
@Mapper
public interface WebUiElementDao {
    /**
     * 新增元素
     *
     * @param element 元素对象
     * @return 返回元素id
     */
    public Integer addElement(WebUiElement element);

    /**
     * 查询元素列表
     *
     * @param map 查询参数
     * @return 返回元素列表
     */
    public List<WebUiElement> elements(Map<String, Object> map);

    /**
     * 查询元素总数
     *
     * @param map 元素对象
     * @return 返回查询总数
     */
    public Integer total(Map<String, Object> map);

    /**
     * 更新元素
     *
     * @param element 元素对象
     * @return 返回更新结果
     */
    public Integer updateElement(WebUiElement element);

    public WebUiElement getUiElementById(@Param("elementId") Integer elementId);

    /**
     * 根据页面id获取元素
     *
     * @param pageId 页面id
     * @return
     */
    public List<WebUiElement> getUiElementByPageId(@Param("pageId") Integer pageId);

    /**
     * 根据id删除元素
     *
     * @param id 元素id
     * @return
     */
    public Integer deleteById(@Param("id") Integer id);


    public Integer deleteElementByPageId(@Param("pageId") Integer pageId);
    /**
     * 获取所有元素
     *
     *
     * @return
     */
    public List<WebUiElement> getAllElement();


    public Integer checkDelete(Integer id);
}
