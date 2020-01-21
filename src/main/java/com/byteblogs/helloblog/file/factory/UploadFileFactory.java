package com.byteblogs.helloblog.file.factory;

import com.byteblogs.common.cache.ConfigCache;
import com.byteblogs.common.constant.Constants;
import com.byteblogs.helloblog.file.service.UploadFileService;
import com.byteblogs.helloblog.file.service.impl.COSUploadFileServiceImpl;
import com.byteblogs.helloblog.file.service.impl.ALiYunOSSUploadFileServiceImpl;
import com.byteblogs.helloblog.file.service.impl.DefaultUploadFileServiceImpl;
import com.byteblogs.helloblog.file.service.impl.QiNiuUploadFileServiceImpl;

/**
 * 文件存储实例工厂
 */
public class UploadFileFactory {

    private static UploadFileService uploadFileService;

    static {
        doCache();
    }

    public static UploadFileService getUploadFileService(){
        return uploadFileService;
    }

    public static void doCache(){
        if (ConfigCache.getConfig(Constants.STORE_TYPE).equals(Constants.QINIU)){
            uploadFileService=new QiNiuUploadFileServiceImpl();
        }
        if (ConfigCache.getConfig(Constants.STORE_TYPE).equals(Constants.COS)){
            uploadFileService=new COSUploadFileServiceImpl();
        }
        if (ConfigCache.getConfig(Constants.STORE_TYPE).equals(Constants.ALIYUN_OSS)){
            uploadFileService=new ALiYunOSSUploadFileServiceImpl();
        }
        if (ConfigCache.getConfig(Constants.STORE_TYPE).equals(Constants.DEFAULT_TYPE)){
            uploadFileService=new DefaultUploadFileServiceImpl();
        }
    }
}
