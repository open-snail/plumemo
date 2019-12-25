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
public class HelloBlogPostsTable implements TableInfoService, InitializingBean {

    @Override
    public void builderTable(final Statement stat) {
        try {
            stat.execute(createHelloBlogPosts());
            log.info("初始化hello_blog_posts完成");
        } catch (final SQLException e) {
            log.error("初始化hello_blog_posts失败", e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        TableInitFactory.register("hello_blog_posts", this);
    }

    private static String createHelloBlogPosts() {
        return "CREATE TABLE `hello_blog_posts`  (\n" +
                "  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',\n" +
                "  `author_id` bigint(255) NULL DEFAULT NULL COMMENT '文章创建人',\n" +
                "  `title` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文章标题',\n" +
                "  `thumbnail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '封面图',\n" +
                "  `comments` int(11) NOT NULL DEFAULT 0 COMMENT '评论数',\n" +
                "  `is_comment` smallint(6) NULL DEFAULT 1 COMMENT '是否打开评论 (0 不打开 1 打开 )',\n" +
                "  `category_id` bigint(20) DEFAULT NULL COMMENT '分类主键',\n" +
                "  `sync_status` smallint(6) NOT NULL DEFAULT 0 COMMENT '同步到byteblogs状态',\n" +
                "  `status` int(11) NOT NULL DEFAULT 1 COMMENT '状态 1 草稿 2 发布',\n" +
                "  `summary` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '摘要',\n" +
                "  `views` int(11) NOT NULL DEFAULT 0 COMMENT '浏览次数',\n" +
                "  `weight` int(11) NOT NULL DEFAULT 0 COMMENT '文章权重',\n" +
                "  `create_time` datetime(0) NOT NULL COMMENT '创建时间',\n" +
                "  `update_time` datetime(0) NOT NULL COMMENT '更新时间',\n" +
                "  PRIMARY KEY (`id`) USING BTREE\n" +
                ") ENGINE = InnoDB AUTO_INCREMENT = 0 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;";
    }
}
