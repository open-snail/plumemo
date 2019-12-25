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
public class HelloBlogPostsCommentsTable implements TableInfoService, InitializingBean {

    @Override
    public void builderTable(final Statement stat) {
        try {
            stat.execute(createHelloBlogPostsComments());
            log.info("初始化hello_blog_posts_comments完成");
        } catch (final SQLException e) {
            log.error("初始化hello_blog_posts_comments失败", e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        TableInitFactory.register("hello_blog_posts_comments", this);
    }

    private static String createHelloBlogPostsComments() {
        return "CREATE TABLE `hello_blog_posts_comments`  (\n" +
                "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
                "  `author_id` bigint(20) NOT NULL,\n" +
                "  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,\n" +
                "  `parent_id` bigint(20) NOT NULL DEFAULT 0,\n" +
                "  `status` int(11) NOT NULL DEFAULT 0,\n" +
                "  `posts_id` bigint(20) NOT NULL,\n" +
                "  `tree_path` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '层级结构',\n" +
                "  `create_time` datetime(0) NULL DEFAULT NULL,\n" +
                "  PRIMARY KEY (`id`) USING BTREE\n" +
                ") ENGINE = InnoDB AUTO_INCREMENT = 0 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '评论表' ROW_FORMAT = Compact;";
    }
}
