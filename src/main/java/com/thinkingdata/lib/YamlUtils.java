package com.thinkingdata.lib;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/21 17:45
 */
//public class YamlUtils {
//    public Map<String, Object> getContent(String path) throws Exception {
//        Map<String, Object> map = null;
//        try {
//            Yaml yaml = new Yaml();
//            URL url = YamlUtils.class.getClassLoader().getResource(path);
//            if (url != null) {
//                // 获取test.yaml文件中的配置数据，然后转换为obj，
//                Object obj = yaml.load(new FileInputStream(url.getFile()));
//                // 也可以将值转换为Map
//                map = yaml.load(new FileInputStream(url.getFile()));
//            }
//        } catch (Exception e) {
//            throw new Exception("读取配置文件时出现错误:" + e.toString());
//        }
//        return map;
//    }
//}
public class YamlUtils {
    public Map<String, Object> getContent(String fileName) throws Exception {
        Map<String, Object> map = null;
        try {
            Yaml yaml = new Yaml();
            if (fileName != "") {
                String path = System.getProperty("user.dir") + "/target";
                File workSpace = new File(path);
                if (workSpace.isDirectory()) {
                    path = path + "/classes";
                } else {
                    path = System.getProperty("user.dir") + "/classes";
                }
                //String path = System.getProperty("os.name").toLowerCase().contains("windows") ? System.getProperty("user.dir") + "/target/classes" : System.getProperty("user.dir") + "/target/classes";
//                String path = System.getProperty("os.name").toLowerCase().contains("windows") ? System.getProperty("user.dir") + "/classes" : System.getProperty("user.dir") + "/classes";

                // 也可以将值转换为Map
                map = yaml.load(new FileInputStream(path + "/" + fileName));
            }
        } catch (Exception e) {
            throw new Exception("读取配置文件时出现错误:" + e.toString());
        }
        return map;
    }

    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));
    }
}
