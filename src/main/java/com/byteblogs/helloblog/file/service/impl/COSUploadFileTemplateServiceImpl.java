package com.byteblogs.helloblog.file.service.impl;

import com.byteblogs.common.cache.ConfigCache;
import com.byteblogs.common.constant.Constants;
import com.byteblogs.common.util.FileUtil;
import com.byteblogs.helloblog.file.factory.UploadFileFactory;
import com.byteblogs.helloblog.file.service.UploadFileTemplateService;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class COSUploadFileTemplateServiceImpl implements UploadFileTemplateService, InitializingBean {

    @Override
    public boolean doCheck(final MultipartFile file) {
        return true;
    }

    @Override
    public String doSaveFileStore(final MultipartFile file) {
        COSClient cosClient = null;
        try {
            final COSCredentials cred = new BasicCOSCredentials(ConfigCache.getConfig(Constants.COS_ACCESS_KEY), ConfigCache.getConfig(Constants.COS_SECRET_KEY));
            final Region region = new Region(ConfigCache.getConfig(Constants.COS_REGION));
            final ClientConfig clientConfig = new ClientConfig(region);
            cosClient = new COSClient(cred, clientConfig);
            final String fileName = FileUtil.createSingleFilePath(ConfigCache.getConfig(Constants.COS_PATH), file.getOriginalFilename());
            final PutObjectRequest putObjectRequest = new PutObjectRequest(ConfigCache.getConfig(Constants.COS_BUCKET), fileName, file.getInputStream(), null);
            cosClient.putObject(putObjectRequest);
            return ConfigCache.getConfig(Constants.COS_IMAGE_DOMAIN) + fileName;
        } catch (final IOException e) {
            return "";
        } finally {
            if (cosClient != null) {
                cosClient.shutdown();
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        UploadFileFactory.register(Constants.COS, this);
    }
}
