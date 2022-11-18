package com.thinkingdata;

import com.thinkingdata.init.InitNode;
import com.thinkingdata.webui.serviceUi.DebugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/21 17:45
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableAsync
@ComponentScan("com.thinkingdata.*")
public class Application implements CommandLineRunner {
    @Autowired
    private InitNode initNode;
    @Autowired
    private DebugService debugService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // 服务启动后自动调用的方法
    @Override
    public void run(String... args) {
        // 跟随项目启动正式hub集线器
        initNode.startHub();
        // 根据缓存判断是否需要开启/关闭debugHub集线器
        debugService.initDebugHub();

    }

}

//@SpringBootApplication
//@ComponentScan("com.thinkingdata.*")
//public class Application {
//    public static void main(String[] args) {
//        SpringApplication.run(Application.class, args);
//    }
//}