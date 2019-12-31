package com.byteblogs.common.util;

public class ToolUtil {
    public static Integer getInteger(Object obj){
        try{
            if (obj==null){
                return 0;
            }
            return Integer.parseInt(obj.toString());
        }catch (Exception e){
            return 0;
        }
    }
}
