package com.byteblogs;

import com.byteblogs.system.init.ConfigApplicationContextInitializer;
import com.byteblogs.system.listener.LoadConfigListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author:byteblogs
 * @Date:2018/09/27 12:52
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class HelloBlogApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(HelloBlogApplication.class);
        springApplication.addInitializers(new ConfigApplicationContextInitializer());
        springApplication.addListeners(new LoadConfigListener());
        springApplication.run(args);
    }
}
