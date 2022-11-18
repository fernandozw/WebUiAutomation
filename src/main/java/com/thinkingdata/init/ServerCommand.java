package com.thinkingdata.init;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2022/08/19 20:13
 */
@Component
public class ServerCommand {

    /**
     * 执行shell命令,获取命令行输出
     *
     * @param cmd
     * @return
     */
    public List<String> execCmd(String cmd) {
        StringBuilder result = new StringBuilder();
        Process process = null;
        BufferedReader bufrIn = null;
        BufferedReader bufrError = null;
        try {
            String[] command = {"/bin/sh", "-c", cmd};
            // 执行命令, 返回一个子进程对象（命令在子进程中执行）
            process = Runtime.getRuntime().exec(command);
            // 方法阻塞, 等待命令执行完成（成功会返回0）
            process.waitFor();
            // 获取命令执行结果, 有两个结果: 正常的输出 和 错误的输出（PS: 子进程的输出就是主进程的输入）
            bufrIn = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
            bufrError = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"));
            // 读取输出
            String line;
            while ((line = bufrIn.readLine()) != null) {
                result.append(line).append(",");
            }
            while ((line = bufrError.readLine()) != null) {
                result.append(line).append(",");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeStream(bufrIn);
            closeStream(bufrError);
            // 销毁子进程
            if (process != null) {
                process.destroy();
            }
            // 返回执行结果
            return Arrays.asList(result.toString().split(","));
        }
    }

    /***
     * 生成一个当前操作系统未被使用的port
     * @return
     */
    public String unUsedPort() {
        // 设置最小的port
        Integer minPort = 10000;
        // linux 最大的port是65535
        Integer maxPort = 65536;
        // 未被使用的port 初始值"65535"
        // 操作系统名称
        String serverName = System.getProperty("os.name");
        // 根据操作系统名称区分名称
        String cmd = serverName.contains("Mac OS X") ? "netstat -AaLlnW|grep -v 'Local Address'|awk '{print $4}'" : "netstat -ntlp|grep -v 'Local Address'|awk '{print $4}'";
        // 用于存放处理后的已经使用的port列表
        List<String> usedList = new ArrayList<String>();
        // 处理已经使用的port
        for (String usedPort : execCmd(cmd)) {
            String port = usedPort.substring(usedPort.lastIndexOf(".") + 1);
            if (StringUtils.isNotBlank(port) && StringUtils.isNumeric(port)) {
                usedList.add(port);
            }
        }
        // 生成未使用的port
        List<String> list = getPureList(minPort, maxPort, usedList);
        Random random = new Random();
        int n = random.nextInt(list.size());
        return list.get(n);
    }

    /***
     *生成未被使用的元素的list
     * @param start 开始位置
     * @param end 结束位置
     * @param removeList 需要删除的list
     * @return
     */
    public List<String> getPureList(Integer start, Integer end, List<String> removeList) {
        List<String> list = new ArrayList<String>();

        for (int i = start; i < end; i++) {
            list.add(String.valueOf(i));

        }
        list.remove(removeList);
        return list;
    }

    public String initRandom(Integer length) {
        Random random = new Random();
        String str = "";
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                //首位不能为0且数字取值区间为 [1,9]
                str += (random.nextInt(9) + 1);
            } else {
                //其余位的数字的取值区间为 [0,9]
                str += random.nextInt(10);
            }
        }
        return str;
    }

    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Test
    public void test1() {
        System.out.println(unUsedPort());

    }
}
