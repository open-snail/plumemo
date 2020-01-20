package com.byteblogs.helloblog.file.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.*;
import com.aliyun.oss.internal.OSSHeaders;
import com.aliyun.oss.model.*;
import com.byteblogs.common.cache.ConfigCache;
import com.byteblogs.common.constant.Constants;
import com.byteblogs.common.util.FileUtil;
import com.byteblogs.helloblog.file.service.UploadFileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ALiYunOSSUploadFileServiceImpl implements UploadFileService {

    @Override
    public String saveFileStore(MultipartFile file){
        OSS ossClient = new OSSClientBuilder()
                .build(ConfigCache.getConfig(Constants.ALIYUN_OSS_ENDPOINT),
                        ConfigCache.getConfig(Constants.ALIYUN_OSS_ACCESS_KEY),
                        ConfigCache.getConfig(Constants.ALIYUN_OSS_SECRET_KEY));
        try{
            String fileName = FileUtil.createSingleFilePath(ConfigCache.getConfig(Constants.ALIYUN_OSS_PATH),file.getOriginalFilename());
            PutObjectRequest putObjectRequest = new PutObjectRequest(ConfigCache.getConfig(Constants.ALIYUN_OSS_BUCKET), fileName, file.getInputStream());
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
            putObjectRequest.setMetadata(metadata);
            PutObjectResult putObjectResult=ossClient.putObject(putObjectRequest);
            System.err.println(JSONObject.toJSON(putObjectResult));
            return ConfigCache.getConfig(Constants.ALIYUN_OSS_IMAGE_DOMAIN)+fileName;
        } catch (IOException e) {
            return "";
        } finally {
            if (ossClient!=null){ossClient.shutdown();}
        }
    }
}
