package com.byteblogs;

import com.byteblogs.system.init.ConfigApplicationContextInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author:byteblogs
 * @Date:2018/09/27 12:52
 */
@SpringBootApplication
@EnableScheduling
public class HelloBlogApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(HelloBlogApplication.class);
        springApplication.addInitializers(new ConfigApplicationContextInitializer());
        springApplication.run(args);
    }
}
