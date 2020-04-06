package com.byteblogs.plumemo.posts.factory;

import com.byteblogs.common.cache.ConfigCache;
import com.byteblogs.common.constant.Constants;
import com.byteblogs.common.validator.annotion.NotNull;
import com.byteblogs.plumemo.file.service.UploadFileTemplateService;
import com.byteblogs.plumemo.posts.service.BlogPlatformService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 博客搬家工厂
 */
public class BlogPlatformFactory {

    private static final Map<String, BlogPlatformService> blogPlatformServiceMap = new ConcurrentHashMap<>();

    /**
     * 获取工厂BlogPlatformService
     *
     * @return
     */
    public static BlogPlatformService getUploadFileService(String blogType) {
        return blogPlatformServiceMap.get(blogType);
    }

    /**
     * 工厂注册
     *
     * @param blogType
     * @param blogPlatformService
     */
    public static void register(@NotNull final String blogType, final BlogPlatformService blogPlatformService) {
        blogPlatformServiceMap.put(blogType, blogPlatformService);
    }

}
