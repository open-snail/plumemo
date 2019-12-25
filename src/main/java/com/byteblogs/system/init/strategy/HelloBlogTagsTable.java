package com.byteblogs.system.init.strategy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author zhangshuguang
 * @date 2019/12/25
 */
@Service
@Slf4j
public class HelloBlogTagsTable implements TableInfoService, InitializingBean {

    @Override
    public void builderTable(final Statement stat) {
        try {
            stat.execute(createHelloBlogTags());
            log.info("初始化hello_blog_tags完成");
        } catch (final SQLException e) {
            log.error("初始化hello_blog_tags失败", e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        TableInitFactory.register("hello_blog_tags", this);
    }


    private static String createHelloBlogTags() {
        return "CREATE TABLE `hello_blog_tags`  (\n" +
                "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
                "  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',\n" +
                "  `sort` smallint(6) NOT NULL DEFAULT 0 COMMENT '排序',\n" +
                "  `create_time` datetime(0) NOT NULL COMMENT '创建时间',\n" +
                "  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建人',\n" +
                "  `update_time` datetime(0) NOT NULL COMMENT '更新时间',\n" +
                "  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新人',\n" +
                "  PRIMARY KEY (`id`) USING BTREE\n" +
                ") ENGINE = InnoDB AUTO_INCREMENT = 0 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '标签表' ROW_FORMAT = Compact;";
    }
}
