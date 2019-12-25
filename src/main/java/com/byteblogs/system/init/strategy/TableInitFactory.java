package com.byteblogs.system.init.strategy;

import com.byteblogs.common.validator.annotion.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangshuguang
 * @date 2019/12/25
 */
public class TableInitFactory {
    private static final Map<String, TableInfoService> tableInfoServiceMap = new ConcurrentHashMap<>();

    public static TableInfoService getByTableName(@NotNull final String tableName) {
        return tableInfoServiceMap.get(tableName);
    }

    public static void register(@NotNull final String tableName, final TableInfoService defaultConvertService) {
        tableInfoServiceMap.put(tableName, defaultConvertService);
    }

}
