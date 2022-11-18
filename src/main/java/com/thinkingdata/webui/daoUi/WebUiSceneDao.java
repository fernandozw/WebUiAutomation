package com.thinkingdata.webui.daoUi;

import com.thinkingdata.webui.entityUi.WebUiScene;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/22 11:36
 */
@Repository
@Mapper
public interface WebUiSceneDao {
    public Integer addScene(WebUiScene webUiScene);

    public List<WebUiScene> scenes(Map<String, Object> map);

    public Integer total(Map<String, Object> map);

    public Integer updateScene(WebUiScene webUiScene);

    public WebUiScene getSceneById(@Param("sceneId") Integer sceneId);

    public List<WebUiScene> allScene();

}
