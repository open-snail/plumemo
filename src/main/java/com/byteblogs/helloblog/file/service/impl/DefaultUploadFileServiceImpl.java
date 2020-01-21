package com.byteblogs.helloblog.file.service.impl;

import com.byteblogs.common.cache.ConfigCache;
import com.byteblogs.common.constant.Constants;
import com.byteblogs.common.util.FileUtil;
import com.byteblogs.helloblog.file.service.UploadFileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class DefaultUploadFileServiceImpl extends UploadFileService {
    @Override
    public String doSaveFileStore(MultipartFile file){
        String filePath= ConfigCache.getConfig(Constants.DEFAULT_PATH);
        String fileName = FileUtil.createSingleFileName(file.getOriginalFilename());
        try {
            File destFile=new File(filePath);
            if (!destFile.exists()) {
                destFile.mkdirs();
            }
            file.transferTo(new File(filePath+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
         return ConfigCache.getConfig(Constants.DEFAULT_IMAGE_DOMAIN)+Constants.FILE_URL+fileName;
    }
}
