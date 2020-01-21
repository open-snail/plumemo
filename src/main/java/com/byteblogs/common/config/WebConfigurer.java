package com.byteblogs.common.config;

import com.byteblogs.common.cache.ConfigCache;
import com.byteblogs.common.constant.Constants;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfigurer implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/files/**").addResourceLocations("file:///" + ConfigCache.getConfig(Constants.DEFAULT_PATH));
    }
}