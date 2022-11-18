package com.thinkingdata.webui.daoUi;

import com.thinkingdata.webui.entityUi.ElementBackup;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2022/10/27 18:33
 */
@Repository
public interface ElementBackupDao {

    public Integer batchAdd(@Param("elementBackupList") List<ElementBackup> elementBackupList);

    public Integer singleAdd(ElementBackup elementBackup);

    public Integer updateElementBackup(ElementBackup elementBackup);

    public Integer deleteById(Integer id);

    public Integer deleteByElementId(Integer elementId);

    public Integer deleteBackupByPageId(Integer pageId);


    public List<ElementBackup> getElementBackupByElementId(Integer elementId);

    public List<ElementBackup> elementbackupList(Map<String, Object> map);
}
