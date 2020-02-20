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
public class HelloBlogFriendshipLinkTable implements TableInfoService, InitializingBean {

    @Override
    public void builderTable(final Statement stat) {
        try {
            stat.execute(createHelloBlogFriendshipLink());
            log.info("初始化hello_blog_friendship_link完成");
        } catch (final SQLException e) {
            log.error("初始化hello_blog_friendship_link失败", e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        TableInitFactory.register("hello_blog_friendship_link", this);
    }

    private static String createHelloBlogFriendshipLink() {
        return "CREATE TABLE `hello_blog_friendship_link`  (\n" +
                "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
                "  `title` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '标题',\n " +
                "  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',\n" +
                "  `logo` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件',\n" +
                "  `href` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '跳转的路径',\n" +
                "  `sort` smallint(6) NOT NULL DEFAULT 0 COMMENT '排序',\n" +
                "  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',\n" +
                "  PRIMARY KEY (`id`) USING BTREE\n" +
                ") ENGINE = InnoDB AUTO_INCREMENT = 0 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '友情链接表' ROW_FORMAT = Compact;";
    }
}
