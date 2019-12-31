package com.byteblogs.common.util;

public class ToolUtil {
    public static Integer getInteger(Object obj){
        if (obj==null){
            return 0;
        }
        return Integer.parseInt(obj.toString());
    }
}
