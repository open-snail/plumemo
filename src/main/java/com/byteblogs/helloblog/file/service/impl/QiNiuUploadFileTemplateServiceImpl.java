package com.byteblogs.helloblog.file.service.impl;

import com.byteblogs.common.cache.ConfigCache;
import com.byteblogs.common.constant.Constants;
import com.byteblogs.common.util.FileUtil;
import com.byteblogs.helloblog.file.factory.UploadFileFactory;
import com.byteblogs.helloblog.file.service.UploadFileTemplateService;
import com.google.gson.Gson;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author Administrator
 */
@Service
public class QiNiuUploadFileTemplateServiceImpl implements UploadFileTemplateService, InitializingBean {

    @Override
    public boolean doCheck(final MultipartFile file) {
        return true;
    }

    @Override
    public String doSaveFileStore(final MultipartFile file) {
        final Configuration cfg = new Configuration(Zone.autoZone());
        final UploadManager uploadManager = new UploadManager(cfg);
        final Auth auth = Auth.create(ConfigCache.getConfig(Constants.QINIU_ACCESS_KEY), ConfigCache.getConfig(Constants.QINIU_SECRET_KEY));
        final String upToken = auth.uploadToken(ConfigCache.getConfig(Constants.QINIU_BUCKET));
        try {
            final Response response = uploadManager.put(file.getInputStream(), FileUtil.createSingleFileName(file.getOriginalFilename()), upToken, null, null);
            final DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return ConfigCache.getConfig(Constants.QINIU_IMAGE_DOMAIN) + putRet.key;
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 注册到工厂
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        UploadFileFactory.register(Constants.QINIU, this);
    }
}
