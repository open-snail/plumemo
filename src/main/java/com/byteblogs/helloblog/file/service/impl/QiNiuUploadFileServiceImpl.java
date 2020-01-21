package com.byteblogs.helloblog.file.service.impl;

import com.byteblogs.common.cache.ConfigCache;
import com.byteblogs.common.constant.Constants;
import com.byteblogs.helloblog.file.service.UploadFileService;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
public class QiNiuUploadFileServiceImpl extends UploadFileService {

    @Override
    public String doSaveFileStore(MultipartFile file) {
        Configuration cfg = new Configuration(Zone.autoZone());
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(ConfigCache.getConfig(Constants.QINIU_ACCESS_KEY), ConfigCache.getConfig(Constants.QINIU_SECRET_KEY));
        String upToken = auth.uploadToken(ConfigCache.getConfig(Constants.QINIU_BUCKET));
        try {
            Response response = uploadManager.put(file.getInputStream(), null, upToken, null, null);
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return ConfigCache.getConfig(Constants.QINIU_IMAGE_DOMAIN) + putRet.key;
        } catch (IOException e) { e.printStackTrace(); }
        return "";
    }
}
