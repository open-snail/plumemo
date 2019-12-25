package com.byteblogs.system.init.strategy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author zhangshuguang
 * @date 2019/12/25
 */
@Service
@Slf4j
public class HelloBlogAuthUserLogTable implements TableInfoService {

    @Override
    public void builderTable(final Statement stat) {
        try {
            stat.execute(createHelloBlogAuthUserLog());
            log.info("初始化hello_blog_auth_user_log完成");
        } catch (final SQLException e) {
            log.info("初始化hello_blog_auth_user_log失败", e);
        }
    }

    @PostConstruct
    public void beforePropertiesSet() throws Exception {
        TableInitFactory.register("hello_blog_auth_user_log", this);
    }

    private static String createHelloBlogAuthUserLog() {
        return "CREATE TABLE `hello_blog_auth_user_log` (\n" +
                "  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',\n" +
                "   `user_id` varchar(255) NOT NULL COMMENT '记录用户id(游客取系统id：-1)',\n" +
                "  `ip` varbinary(32) NOT NULL COMMENT 'ip地址',\n" +
                "  `url` varchar(255) NOT NULL COMMENT '请求的url',\n" +
                "  `paramter` varchar(255) DEFAULT NULL COMMENT '需要记录的参数',\n" +
                "  `device` varchar(255) DEFAULT NULL COMMENT '来自于哪个设备 eg 手机 型号 电脑浏览器',\n" +
                "  `description` varchar(255) DEFAULT NULL COMMENT '描述',\n" +
                "   `create_time` datetime NOT NULL COMMENT '创建时间',\n" +
                "  PRIMARY KEY (`id`) USING BTREE\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT COMMENT='用户行为日志记录表';";
    }
}
