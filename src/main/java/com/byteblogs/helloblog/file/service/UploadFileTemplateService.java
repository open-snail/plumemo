package com.byteblogs.helloblog.file.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadFileTemplateService {

    /**
     * 进行文件上传前的准备操作
     */
    boolean doCheck(MultipartFile file);

    /**
     * 进行文件的保存操作
     */
    String doSaveFileStore(MultipartFile file);

    /**
     * 上传文件
     */
    default String saveFileStore(final MultipartFile file) {
        if (doCheck(file)) {
            return doSaveFileStore(file);
        }
        return "";
    }

}
