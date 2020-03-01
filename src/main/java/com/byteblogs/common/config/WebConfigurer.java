package com.byteblogs.common.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.byteblogs.common.cache.ConfigCache;
import com.byteblogs.common.constant.Constants;
import com.byteblogs.common.constant.ConstantsModels;
import com.byteblogs.helloblog.config.dao.ConfigDao;
import com.byteblogs.helloblog.config.domain.po.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfigurer implements WebMvcConfigurer {

    @Autowired
    private ConfigDao configDao;

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {

        String defaultPath;
        try {
            Config config = configDao.selectOne(new LambdaQueryWrapper<Config>().eq(Config::getConfigKey, Constants.DEFAULT_PATH));
            defaultPath = config.getConfigValue();
        } catch (Exception e) {
            defaultPath = ConstantsModels.getDefaultPath(ConfigCache.getConfig(Constants.DEFAULT_PATH));
        }
        registry.addResourceHandler("/files/**").addResourceLocations("file:///" + defaultPath);
    }
}