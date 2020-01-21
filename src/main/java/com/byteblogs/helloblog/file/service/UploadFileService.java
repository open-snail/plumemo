package com.byteblogs.helloblog.file.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public abstract class UploadFileService {

    /**
     * 进行文件上传前的准备操作
     */
    protected  boolean doCheck(MultipartFile file){
        return true;
    }

    /**
     * 进行文件的保存操作
     */
    protected abstract String doSaveFileStore(MultipartFile file);

    /**
     * 上传文件
     */
    public String saveFileStore(MultipartFile file){
        if (doCheck(file)){
            return doSaveFileStore(file);
        }
        return "";
    }


}
