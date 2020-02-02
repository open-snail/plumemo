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
public class HelloBlogBlackListTable implements TableInfoService {

    @Override
    public void builderTable(final Statement stat) {
        try {
            stat.execute(createHelloBlogAuthUserLog());
            log.info("初始化hello_blog_black_list完成");
        } catch (final SQLException e) {
            log.info("初始化hello_blog_black_list失败", e);
        }
    }

    @PostConstruct
    public void beforePropertiesSet() throws Exception {
        TableInitFactory.register("hello_blog_black_list", this);
    }

    private static String createHelloBlogAuthUserLog() {
        return "CREATE TABLE `hello_blog_black_list` (\n" +
                "  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',\n" +
                "  `ip_range` varchar(150) DEFAULT NULL COMMENT 'ip范围',\n" +
                "  `is_enable` int(1) DEFAULT '0' COMMENT '是否启用 0 启用，1不启用',\n" +
                "  `create_user` varchar(255) DEFAULT NULL COMMENT '创建者',\n" +
                "  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
                "  `update_user` datetime DEFAULT NULL COMMENT '更新者',\n" +
                "  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='黑名单拦截表'";
    }
}
