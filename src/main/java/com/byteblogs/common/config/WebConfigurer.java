package com.byteblogs.common.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.byteblogs.common.cache.ConfigCache;
import com.byteblogs.common.constant.Constants;
import com.byteblogs.common.context.BeanTool;
import com.byteblogs.helloblog.config.dao.ConfigDao;
import com.byteblogs.helloblog.config.domain.po.Config;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Component
@DependsOn(value = "configDao")
class WebConfigurer implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        final ConfigDao configDao = BeanTool.getBean(ConfigDao.class);
        final Config config=configDao.selectOne(new LambdaQueryWrapper<Config>().eq(Config::getConfigKey,Constants.DEFAULT_PATH));
        registry.addResourceHandler("/files/**").addResourceLocations("file:///"+ config.getConfigValue());
    }
}