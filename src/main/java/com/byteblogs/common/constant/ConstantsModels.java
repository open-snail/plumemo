package com.byteblogs.common.constant;

import com.byteblogs.system.init.InitFileConfig;
import org.apache.commons.lang3.StringUtils;

public class ConstantsModels {

    public static String getDefaultPath(String url){
        if (StringUtils.isBlank(url)){
            if (InitFileConfig.isWindows()){
                return Constants.WIN_DEFAULT_PATH;
            } else {
                return  Constants.OS_DEFAULT_PATH;
            }
        }
        return url;
    }
}
