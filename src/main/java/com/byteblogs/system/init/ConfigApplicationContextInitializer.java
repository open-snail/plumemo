package com.byteblogs.system.init;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @Author:byteblogs
 * @Date:2018/09/27 12:52
 */
@Slf4j
@Component
public class ConfigApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(final ConfigurableApplicationContext configurableApplicationContext) {
        log.debug("==================初始化配置==================");
    }
}
