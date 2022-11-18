package com.thinkingdata.webui.daoUi;


import com.thinkingdata.webui.entityUi.Cron;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/26 19:22
 */
@Repository
@Mapper
public interface CronDao {
    public Cron getCronById(@Param("cronId") Integer cronId);

    public Integer addCron(Cron cron);

    public Integer removeById(@Param("cronId") Integer cronId);

    public Integer update(Cron cron);

    public List<Cron> list(Map<String, Object> map);

    public List<Cron> allCron();

    public Integer total(Map<String, Object> map);
}
