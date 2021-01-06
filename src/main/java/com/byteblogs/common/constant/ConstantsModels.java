package com.byteblogs.common.constant;

import org.apache.commons.lang3.StringUtils;

public class ConstantsModels {

    public static String getDefaultPath(String url){
        if (StringUtils.isBlank(url)){
            if (isWindows()){
                return Constants.WIN_DEFAULT_PATH;
            } else {
                return  Constants.OS_DEFAULT_PATH;
            }
        }
        return url;
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }
}
