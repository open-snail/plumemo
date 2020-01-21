package com.byteblogs.helloblog.file.factory;

import com.byteblogs.common.cache.ConfigCache;
import com.byteblogs.common.constant.Constants;
import com.byteblogs.common.validator.annotion.NotNull;
import com.byteblogs.helloblog.file.service.UploadFileTemplateService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 文件存储实例工厂
 */
public class UploadFileFactory {

    private static final Map<String, UploadFileTemplateService> uploadFileServiceMap = new ConcurrentHashMap<>();

    /**
     * 获取工厂UploadFileTemplateService
     *
     * @return
     */
    public static UploadFileTemplateService getUploadFileService() {
        return uploadFileServiceMap.get(ConfigCache.getConfig(Constants.STORE_TYPE));
    }

    /**
     * 工厂注册
     *
     * @param storyType
     * @param uploadFileTemplateService
     */
    public static void register(@NotNull final String storyType, final UploadFileTemplateService uploadFileTemplateService) {
        uploadFileServiceMap.put(storyType, uploadFileTemplateService);
    }

}
