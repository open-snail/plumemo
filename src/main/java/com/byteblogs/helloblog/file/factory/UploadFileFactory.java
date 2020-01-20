package com.byteblogs.helloblog.file.factory;

import com.byteblogs.common.cache.ConfigCache;
import com.byteblogs.common.constant.Constants;
import com.byteblogs.helloblog.file.service.UploadFileService;
import com.byteblogs.helloblog.file.service.impl.COSUploadFileServiceImpl;
import com.byteblogs.helloblog.file.service.impl.OSSUploadFileServiceImpl;
import com.byteblogs.helloblog.file.service.impl.QiNiuUploadFileServiceImpl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 文件存储实例工厂
 */
public class UploadFileFactory {
    private static final Map<String, UploadFileService> uploadFileServiceMap = new ConcurrentHashMap<>();

    static {
        if (ConfigCache.getConfig(Constants.STORE_TYPE).equals(Constants.QINIU)){
            uploadFileServiceMap.put(Constants.STORE_TYPE,new QiNiuUploadFileServiceImpl());
        }
        if (ConfigCache.getConfig(Constants.STORE_TYPE).equals(Constants.COS)){
            uploadFileServiceMap.put(Constants.STORE_TYPE,new COSUploadFileServiceImpl());
        }
        if (ConfigCache.getConfig(Constants.STORE_TYPE).equals(Constants.OSS)){
            uploadFileServiceMap.put(Constants.STORE_TYPE,new OSSUploadFileServiceImpl());
        }
    }

    public static UploadFileService getUploadFileService(){
        return uploadFileServiceMap.get(Constants.STORE_TYPE);
    }
}
