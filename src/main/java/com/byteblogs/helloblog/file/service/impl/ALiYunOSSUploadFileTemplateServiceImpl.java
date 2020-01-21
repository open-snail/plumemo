package com.byteblogs.helloblog.file.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.internal.OSSHeaders;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.StorageClass;
import com.byteblogs.common.cache.ConfigCache;
import com.byteblogs.common.constant.Constants;
import com.byteblogs.common.util.FileUtil;
import com.byteblogs.helloblog.file.factory.UploadFileFactory;
import com.byteblogs.helloblog.file.service.UploadFileTemplateService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ALiYunOSSUploadFileTemplateServiceImpl implements UploadFileTemplateService, InitializingBean {

    @Override
    public boolean doCheck(final MultipartFile file) {
        return true;
    }

    @Override
    public String doSaveFileStore(final MultipartFile file) {
        final OSS ossClient = new OSSClientBuilder()
                .build(ConfigCache.getConfig(Constants.ALIYUN_OSS_ENDPOINT),
                        ConfigCache.getConfig(Constants.ALIYUN_OSS_ACCESS_KEY),
                        ConfigCache.getConfig(Constants.ALIYUN_OSS_SECRET_KEY));
        try {
            final String fileName = FileUtil.createSingleFilePath(ConfigCache.getConfig(Constants.ALIYUN_OSS_PATH), file.getOriginalFilename());
            final PutObjectRequest putObjectRequest = new PutObjectRequest(ConfigCache.getConfig(Constants.ALIYUN_OSS_BUCKET), fileName, file.getInputStream());
            final ObjectMetadata metadata = new ObjectMetadata();
            metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
            putObjectRequest.setMetadata(metadata);
            ossClient.putObject(putObjectRequest);
            return ConfigCache.getConfig(Constants.ALIYUN_OSS_IMAGE_DOMAIN) + fileName;
        } catch (final IOException e) {
            return "";
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        UploadFileFactory.register(Constants.ALIYUN_OSS, this);
    }
}
