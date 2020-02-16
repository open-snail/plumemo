package com.byteblogs.common.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
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
        if (pos == -1) {
            return ".png";
        }
        return fileName.substring(pos);
    }

    public static String createSingleFilePath(String parentPath, String fileName) {
        return parentPath + createSingleFileName(fileName);
    }

    /**
     * 根据原有文件名称，生成一个随机文件名称
     */
    public static String createSingleFileName(String fileName) { // 创建文件名称
        return UUID.randomUUID() + getSuffix(fileName);
    }

    /**
     * 根据文件路径将文件转为字节数组
     * @param filePath 文件路径
     * @return 字节数组
     */
    public static byte[] tranToBytes(String filePath) {
        byte[] data = null;
        URL url = null;
        InputStream input = null;
        ByteArrayOutputStream output=null;
        try{
            url = new URL(filePath);
            HttpURLConnection httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            httpUrl.getInputStream();
            input = httpUrl.getInputStream();
            output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (output!=null){ try { output.close(); } catch (IOException e) { e.printStackTrace(); } }
            if (input!=null){ try { input.close(); } catch (IOException e) { e.printStackTrace(); } }
        }
        return data;
    }

}
