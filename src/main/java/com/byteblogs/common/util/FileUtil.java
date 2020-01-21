package com.byteblogs.common.util;

import java.util.UUID;

/**
 * @Author:byteblogs
 * @Date:2018/09/27 12:52
 */
public class FileUtil {


    /**
     * 获取文件后缀名
     */
    public static String getSuffix(String fileName) {
        int pos = fileName.lastIndexOf(".");
        return fileName.substring(pos);
    }

    public static String createSingleFilePath(String parentPath,String fileName) {
        return parentPath+createSingleFileName(fileName);
    }

    /**
     * 根据原有文件名称，生成一个随机文件名称
     */
    public static String createSingleFileName(String fileName) { // 创建文件名称
        return UUID.randomUUID() + getSuffix(fileName);
    }

}
