package com.thinkingdata.webui.serviceUi;

import com.thinkingdata.Application;
import com.thinkingdata.init.InitNode;
import com.thinkingdata.lib.HandleUtils;
import com.thinkingdata.response.ResponseData;
import com.thinkingdata.response.ResponseDataUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2022/11/07 14:03
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Service
public class DebugService {
    private Logger log = LoggerFactory.getLogger(DebugService.class);

    private final String DEBUG_HUB_PID = "debugHubPid";
    @Autowired
    private InitNode initNode;
    @Autowired
    @Qualifier("redis_01")
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 开启debug模式
     *
     * @return
     */
    public Object startDebugHub() {
        ResponseData<Object> responseData;
        log.info("开始启动debugHub集线器!");
        try {
            List<String> pidList = initNode.startDebugHub();
            if (pidList.size() > 1) {
                Map<String, String> pidMap = new HashMap<String, String>();
                pidMap.put("debugHubPid", StringUtils.join(pidList, ","));
                stringRedisTemplate.opsForValue().set(DEBUG_HUB_PID, StringUtils.join(pidList, ","));
                responseData = ResponseDataUtils.buildSuccess(pidMap);
                log.info("debugHub集线器启动成功:{}", pidList);
            } else {
                responseData = ResponseDataUtils.buildError("debugHub集线器失败!");

            }
        } catch (Exception e) {
            log.error("debugHub集线器启动失败:{}", HandleUtils.handleErrInfo(e));
            stringRedisTemplate.opsForValue().set(DEBUG_HUB_PID, "");
            responseData = ResponseDataUtils.buildError("debugHub集线器失败!");

        }

        return responseData;
    }

    /**
     * 关闭debug模式
     *
     * @return
     */
    public Object stopDebugHub() {
        ResponseData<Object> responseData;

        String debugHubPid = stringRedisTemplate.opsForValue().get(DEBUG_HUB_PID);
        List<String> pidList = Arrays.asList(debugHubPid.split(","));

        if (StringUtils.isNotBlank(debugHubPid) && pidList.size() > 1) {
            Boolean status = initNode.killPid(pidList);
            if (status) {
                log.info("KILL debugHub集线器成功!");
                responseData = ResponseDataUtils.buildSuccess("KILL debugHub集线器成功!");
                stringRedisTemplate.opsForValue().set(DEBUG_HUB_PID, "");
            } else {
                log.info("KILL debugHub集线器失败,debugHubPid:{}", debugHubPid);

                responseData = ResponseDataUtils.buildError("KILL debugHub集线器失败,debugHubPid:{}", debugHubPid);
            }
        } else {
            log.error("debugHub集线器为空!");

            responseData = ResponseDataUtils.buildError("debugHub集线器为空!");

        }

        return responseData;
    }

    /***
     * 更新debugHub集线器状态
     * @param status
     * @return
     */
    public Object updateDebugHub(String status) {
        if (status.equalsIgnoreCase("1")) {
            return startDebugHub();
        } else {
            return stopDebugHub();
        }

    }

    /**
     * 获取debugHub集线器的状态
     *
     * @return
     */
    public Object getDebugStatus() {
        ResponseData<Object> responseData;

        String debugHubPid = stringRedisTemplate.opsForValue().get(DEBUG_HUB_PID);
        List<String> pidList = Arrays.asList(debugHubPid.split(","));
        Map<String, Boolean> statusMap = new HashMap<String, Boolean>();
        if (StringUtils.isNotBlank(debugHubPid) && pidList.size() > 1) {
            statusMap.put("status", true);
            responseData = ResponseDataUtils.buildSuccess(statusMap);
        } else {
            statusMap.put("status", false);
            responseData = ResponseDataUtils.buildSuccess(statusMap);

        }
        return responseData;
    }

    /**
     * 检查是否开启了debug模式
     *
     * @return
     */
    public Boolean checkDebugHub() {
        Boolean status = true;
        String debugHubPid = stringRedisTemplate.opsForValue().get(DEBUG_HUB_PID);
        List<String> pidList = Arrays.asList(debugHubPid.split(","));
        if (StringUtils.isBlank(debugHubPid) && pidList.size() < 2) {
            status = false;
        } else if (StringUtils.isNotBlank(debugHubPid) && pidList.size() > 1) {
            status = true;
        }
        return status;
    }

    public Object initDebugHub() {
        Object responseData = null;

        String debugHubPid = stringRedisTemplate.opsForValue().get(DEBUG_HUB_PID);
        List<String> pidList = Arrays.asList(debugHubPid.split(","));
        if (StringUtils.isBlank(debugHubPid) && pidList.size() < 2) {
            responseData = stopDebugHub();
        } else if (StringUtils.isNotBlank(debugHubPid) && pidList.size() > 1) {
            responseData = startDebugHub();
        }
        return responseData;
    }

    @Test
    public void test1() {
        List<String> list = Arrays.asList("1,2".split(","));
        System.out.println(list);
    }

}
