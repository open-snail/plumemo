package com.byteblogs.helloblog.file.service.impl;

import cn.hutool.core.util.IdUtil;
import com.byteblogs.common.cache.ConfigCache;
import com.byteblogs.common.constant.Constants;
import com.byteblogs.common.util.FileUtil;
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

/**
 * @author: zsg
 * @description:
 * @date: 2019/1/13 10:55
 * @modified:
 */
@Service
public class UploadFileServiceImpl implements UploadFileService {

    @Override
    public String qiniuyunStore(MultipartFile file) {

        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);

        //默认不指定key的情况下，以文件内容的hash值作为文件名
        Auth auth = Auth.create(ConfigCache.getConfig(Constants.QINIU_ACCESS_KEY), ConfigCache.getConfig(Constants.QINIU_SECRET_KEY));
        String upToken = auth.uploadToken(ConfigCache.getConfig(Constants.QINIU_BUCKET));
        try {
            Response response = uploadManager.put(file.getInputStream(), IdUtil.simpleUUID() + FileUtil.getSuffix(file.getOriginalFilename()), upToken, null, null);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);

            return ConfigCache.getConfig(Constants.QINIU_IMAGE_DOMAIN) + putRet.key;
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}
