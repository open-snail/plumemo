package com.byteblogs.common.cache;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: byteblogs
 * @date: 2019/09/03 11:48
 */
public class ConfigCache {

    private final static Map<String, String> configCache = new ConcurrentHashMap<>();

    public static void putConfig(String key, String value) {
        configCache.put(key, value);
    }

    public static String getConfig(String key) {
        return StringUtils.trim(configCache.get(key));
    }
}
