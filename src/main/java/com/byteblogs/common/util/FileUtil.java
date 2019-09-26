package com.byteblogs.common.util;

/**
 * @Author:byteblogs
 * @Date:2018/09/27 12:52
 */
public class FileUtil {

    /**
     * 获取文件后缀名
     * @param name
     * @return
     */
    public static String getSuffix(String name) {
        int pos = name.lastIndexOf(".");
        return name.substring(pos);
    }

}
