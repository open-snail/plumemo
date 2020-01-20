package com.byteblogs.helloblog.file.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author: zsg
 * @description:
 * @date: 2019/1/13 10:55
 * @modified:
 */
public interface UploadFileService {

    /**
     * 上传文件到七牛云
     */
    String saveFileStore(MultipartFile file) throws IOException;
}
