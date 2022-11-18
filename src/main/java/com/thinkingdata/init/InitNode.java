package com.thinkingdata.init;


import com.thinkingdata.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/21 17:45
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)


@Component
public class InitNode {

    private Logger log = LoggerFactory.getLogger(InitNode.class);

    @Autowired
    private ServerCommand serverCommand;
    // 操作系统名称
    private String osName = System.getProperty("os.name").toLowerCase();

    // 初始化Runtime对象
    private Runtime runtime = Runtime.getRuntime();
    // 全局Process对象
    private Process process = null;

    private String hubCommand() {

        return "java -jar selenium-server-standalone-3.141.59.jar -role hub -port 1234";
    }

    // debug 模式下启动hub集线器的命令
    private String hubDebugCommand = "java -jar selenium-server-standalone-3.141.59.jar -role hub -port 2234";


    /**
     * 启动节点命令
     *
     * @return
     */
    private String nodeCommand(String port) {
        return "java -jar selenium-server-standalone-3.141.59.jar -role node -hub http://127.0.0.1:1234/grid/register/ -port " + port;
    }

    private String getDriverPath() {
        String projectPath = "";
        String path = System.getProperty("user.dir") + "/target";
        File workSpace = new File(path);
        if (workSpace.isDirectory()) {
            projectPath = path + "/classes";
        } else {
            projectPath = System.getProperty("user.dir") + "/classes";
        }

        // driver 文件路径
        return projectPath + "/driver";
    }

    public List<String> startHub() {
        // 存放本次启动的进程号
        List<String> pidS = new ArrayList<>();
        String driverPath = getDriverPath();

        // 启动hub控制器的命令行
        String winCommand = "cmd.exe /c cd " + driverPath + " & " + hubCommand();
        String command = "cd " + driverPath + " && " + hubCommand();
        String[] linuxCommand = {"/bin/sh", "-c", command};

        try {
            if (osName.contains("windows")) {
                log.info("开始在windows启动hub控制器:" + winCommand);
                process = runtime.exec(winCommand);
            } else {
                log.info("开始在unix启动hub控制器:" + Arrays.asList(linuxCommand));
                process = runtime.exec(linuxCommand);
            }
            String jarPid = getPidOfProcess(process);
            pidS.add(jarPid);
            Thread.sleep(3000);
            String hubPidCmd = "lsof -i:1234|grep -v 'PID'|awk '{print $2}'";
            // 根据hub节点启动的端口号获取hub的pid
            List<String> hubPidList = serverCommand.execCmd(hubPidCmd);
            pidS.add(hubPidList.get(0));
            log.info("hub控制器pid:{}", hubPidList);

            if (pidS.size() > 1) {
                log.info("hub控制器启动成功!");

            } else {
                log.info("hub控制器启动失败!");

            }

        } catch (Exception e) {
            log.error("hub控制器启动失败:", e);
        }
        return pidS;
    }

    /***
     * 启动debug hub集线器
     * @return
     */
    public List<String> startDebugHub() {
        // 存放本次启动的进程号
        List<String> pidS = new ArrayList<>();
        String driverPath = getDriverPath();

        // 启动hub控制器的命令行
        String winCommand = "cmd.exe /c cd " + driverPath + " & " + hubDebugCommand;
        String command = "cd " + driverPath + " && " + hubDebugCommand;
        String[] linuxCommand = {"/bin/sh", "-c", command};

        try {
            if (osName.contains("windows")) {
                log.info("开始在windows启动hubDebug控制器:" + winCommand);
                process = runtime.exec(winCommand);
            } else {
                log.info("开始在unix启动hubDebug控制器:" + Arrays.asList(linuxCommand));
                process = runtime.exec(linuxCommand);
            }
            String jarPid = getPidOfProcess(process);
            pidS.add(jarPid);
            Thread.sleep(3000);
            String hubPidCmd = "lsof -i:2234|grep -v 'PID'|awk '{print $2}'";
            // 根据hub节点启动的端口号获取hub的pid
            List<String> hubPidList = serverCommand.execCmd(hubPidCmd);
            pidS.add(hubPidList.get(0));
            log.info("hubDebug控制器pid:{}", hubPidList);

            if (pidS.size() > 1) {
                log.info("hubDebug控制器启动成功!");

            } else {
                log.info("hubDebug控制器启动失败!");

            }

        } catch (Exception e) {
            log.error("hubDebug控制器启动失败:", e);
        }
        return pidS;
    }

    /**
     * 启动node节点
     *
     * @return返回进程号
     */
    public List<String> startNode(String nodePort) {
        // 存放本次启动的进程号
        List<String> pidS = new ArrayList<String>();

        String driverPath = getDriverPath();
        // 启动node节点的命令行
        String winCommand = "cmd.exe /c cd " + driverPath + " & " + nodeCommand(nodePort);
        String command = "cd " + driverPath + " && " + nodeCommand(nodePort);
        String[] linuxCommand = {"/bin/sh", "-c", command};

        try {
            if (osName.contains("windows")) {
                log.info("windows开始启动node节点:" + winCommand);
                process = runtime.exec(winCommand);
            } else {
                log.info("unix开始启动node节点:" + Arrays.asList(linuxCommand));
                process = runtime.exec(linuxCommand);
            }
            log.info("node节点启动成功!");

            String jarPid = getPidOfProcess(process);
            pidS.add(jarPid);
            Thread.sleep(5000);
            String nodePidCmd = "lsof -i:" + nodePort + "|grep -v 'PID'|awk '{print $2}'";
            // 根据node节点启动的端口号获取node的pid
            List<String> nodePidList = serverCommand.execCmd(nodePidCmd);
            pidS.add(nodePidList.get(0));
        } catch (Exception e) {
            log.error("node节点启动失败:", e);
        }
        return pidS;
    }

    /**
     * 根据端口号关闭进程
     *
     * @param pidS 端口号list
     * @return 返回执行结果
     */

    public boolean killPid(List<String> pidS) {
        // 执行状态
        boolean status = true;
        log.info("开始根据进程号kill进程:" + pidS);
        try {
            // kill命令行
            String winCommand = "TASKKILL /PID " + pidS.stream().collect(Collectors.joining(" /PID ")) + " /F";
            String[] linuxCommand = {"/bin/sh", "-c", "kill -9 " + pidS.stream().collect(Collectors.joining(" "))};
            // 执行kill命令
            if (osName.contains("windows")) {
                process = runtime.exec(winCommand);
            } else {
                process = runtime.exec(linuxCommand);
            }
            // 等待进程结束
            process.waitFor();
            // 销毁进程
            process.destroy();
            log.info("根据进程号kill进程成功!");
        } catch (Exception e) {
            log.error("根据进程号kill进程失败:", e);
            status = false;
        }
        return status;
    }

    /***
     * 根据 Process对象获取进程号
     * @param p Process对象
     * @return
     */
    public static synchronized String getPidOfProcess(Process p) {
        String pid = "-1";
        try {
            if (p.getClass().getName().equals("java.lang.UNIXProcess") || p.getClass().getName().equals("java.lang.ProcessImpl")) {
                Field f = p.getClass().getDeclaredField("pid");
                f.setAccessible(true);
                pid = String.valueOf(f.getLong(p));
                f.setAccessible(false);
            }
        } catch (Exception e) {
            pid = "-1";
        }
        return pid;
    }


    @Test
    public void test() {

        List<String> hubPids = startHub();
        System.out.println(hubPids);
        String nodePort = "5555";
        List<String> nodePids = startNode(nodePort);
        System.out.println(nodePids);

        hubPids.addAll(nodePids);

        System.out.println(hubPids);

        killPid(hubPids);
    }

}
