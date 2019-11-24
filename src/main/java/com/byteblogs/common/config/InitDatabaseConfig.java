package com.byteblogs.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.*;

/**
 * @author byteblogs
 * @date 2019/11/24
 */
@Component
@Slf4j
public class InitDatabaseConfig {

    @Value("${spring.datasource.driver-class-name}")
    private String driver;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String userName;

    @Value("${spring.datasource.password}")
    private String password;

    @PostConstruct
    public void init() throws SQLException, ClassNotFoundException {

        //连接数据库
        Class.forName(driver);
        //测试url中是否包含useSSL字段，没有则添加设该字段且禁用
        if (url.indexOf("?") == -1) {
            url = url + "?useSSL=false";
        } else if (url.indexOf("useSSL=false") == -1 || url.indexOf("useSSL=true") == -1) {
            url = url + "&useSSL=false";
        }

        final Connection conn = DriverManager.getConnection(url, userName, password);
        final Statement stat = conn.createStatement();

        final String[] tables = {"hello_blog_config", "hello_blog_auth_token", "hello_blog_auth_user",
                "hello_blog_category", "hello_blog_category_tags", "hello_blog_friendship_link", "hello_blog_posts"
                , "hello_blog_posts_attribute", "hello_blog_posts_comments", "hello_blog_posts_tags", "hello_blog_tags"};

        for (int i = 0; i < tables.length; i++) {
            //获取数据库表名
            final ResultSet rs = conn.getMetaData().getTables(null, null, tables[i], null);
            // 判断表是否存在，如果存在则什么都不做，否则创建表
            if (rs.next()) {
                return;
            } else {
                switch (tables[i]) {
                    case "hello_blog_config":
                        stat.executeUpdate(SqlStatement.createHelloBlogConfig());
                        stat.executeUpdate(SqlStatement.initData());
                        log.info("初始化hello_blog_config完成");
                        break;
                    case "hello_blog_auth_token":
                        stat.executeUpdate(SqlStatement.createHelloBlogAuthToken());
                        log.info("初始化hello_blog_auth_token完成");
                        break;
                    case "hello_blog_auth_user":
                        stat.executeUpdate(SqlStatement.createHelloBlogAuthUser());
                        log.info("初始化hello_blog_auth_user完成");
                        break;
                    case "hello_blog_category":
                        stat.executeUpdate(SqlStatement.createHelloBlogCategory());
                        log.info("初始化hello_blog_category完成");
                        break;
                    case "hello_blog_category_tags":
                        stat.executeUpdate(SqlStatement.createHelloBlogCategoryTags());
                        log.info("初始化hello_blog_category_tags完成");
                        break;
                    case "hello_blog_friendship_link":
                        stat.executeUpdate(SqlStatement.createHelloBlogFriendshipLink());
                        log.info("初始化hello_blog_friendship_link完成");
                        break;
                    case "hello_blog_posts":
                        stat.executeUpdate(SqlStatement.createHelloBlogPosts());
                        log.info("初始化hello_blog_posts完成");
                        break;
                    case "hello_blog_posts_attribute":
                        stat.executeUpdate(SqlStatement.createHelloBlogPostsAttribute());
                        log.info("初始化hello_blog_posts_attribute完成");
                        break;
                    case "hello_blog_posts_comments":
                        stat.executeUpdate(SqlStatement.createHelloBlogPostsComments());
                        log.info("初始化hello_blog_posts_comments完成");
                        break;
                    case "hello_blog_posts_tags":
                        stat.executeUpdate(SqlStatement.createHelloBlogPostsTags());
                        log.info("初始化hello_blog_posts_tags完成");
                        break;
                    case "hello_blog_tags":
                        stat.executeUpdate(SqlStatement.createHelloBlogTags());
                        log.info("初始化hello_blog_tags完成");
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + tables[i]);
                }
            }
        }
        // 释放资源
        stat.close();
        conn.close();
    }

    public static class SqlStatement {

        private static String createHelloBlogConfig() {
            return "CREATE TABLE `hello_blog_config`  (\n" +
                    "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
                    "  `config_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置key',\n" +
                    "  `config_value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '配置值',\n" +
                    "  `type` smallint(6) NOT NULL DEFAULT 0 COMMENT '配置类型',\n" +
                    "  PRIMARY KEY (`id`) USING BTREE,\n" +
                    "  UNIQUE INDEX `UK_99vo6d7ci4wlxruo3gd0q2jq8`(`config_key`) USING BTREE\n" +
                    ") ENGINE = InnoDB AUTO_INCREMENT = 0 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;";
        }

        private static String createHelloBlogAuthToken() {
            return "CREATE TABLE `hello_blog_auth_token`  (\n" +
                    "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
                    "  `token` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'token',\n" +
                    "  `expire_time` datetime(0) NOT NULL COMMENT '过期时间',\n" +
                    "  `user_id` bigint(20) NOT NULL COMMENT '创建人',\n" +
                    "  PRIMARY KEY (`id`) USING BTREE\n" +
                    ") ENGINE = InnoDB AUTO_INCREMENT = 0 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '标签表' ROW_FORMAT = Compact;";
        }

        private static String createHelloBlogAuthUser() {
            return "CREATE TABLE `hello_blog_auth_user`  (\n" +
                    "  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',\n" +
                    "  `social_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '社交账户ID',\n" +
                    "  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',\n" +
                    "  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '别名',\n" +
                    "  `role_id` bigint(20) NOT NULL COMMENT '角色主键 1 普通用户 2 admin',\n" +
                    "  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',\n" +
                    "  `introduction` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '个人简介',\n" +
                    "  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',\n" +
                    "  `create_time` datetime(0) NOT NULL COMMENT '注册时间',\n" +
                    "  `html_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'github个人主页',\n" +
                    "  `qq` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'qq号',\n" +
                    "  `csdn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'csdn主页',\n" +
                    "  `weibo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微博主页',\n" +
                    "  `twitter` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'twtter主页',\n" +
                    "  `facebook` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'facebook主页',\n" +
                    "  PRIMARY KEY (`id`) USING BTREE\n" +
                    ") ENGINE = InnoDB AUTO_INCREMENT = 0 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;";
        }

        private static String createHelloBlogCategory() {
            return "CREATE TABLE `hello_blog_category`  (\n" +
                    "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
                    "  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',\n" +
                    "  `sort` smallint(6) NOT NULL DEFAULT 0 COMMENT '排序',\n" +
                    "  `create_time` datetime(0) NOT NULL COMMENT '创建时间',\n" +
                    "  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建人',\n" +
                    "  `update_time` datetime(0) NOT NULL COMMENT '更新时间',\n" +
                    "  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新人',\n" +
                    "  PRIMARY KEY (`id`) USING BTREE\n" +
                    ") ENGINE = InnoDB AUTO_INCREMENT = 0 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;";
        }

        private static String createHelloBlogCategoryTags() {
            return "CREATE TABLE `hello_blog_category_tags`  (\n" +
                    "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
                    "  `tags_id` bigint(32) NOT NULL COMMENT '名称',\n" +
                    "  `category_id` bigint(20) NOT NULL COMMENT '分类的主键',\n" +
                    "  `sort` smallint(6) NOT NULL DEFAULT 0 COMMENT '排序',\n" +
                    "  `create_time` datetime(0) NOT NULL COMMENT '创建时间',\n" +
                    "  `update_time` datetime(0) NOT NULL COMMENT '更新时间',\n" +
                    "  PRIMARY KEY (`id`) USING BTREE\n" +
                    ") ENGINE = InnoDB AUTO_INCREMENT = 0 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;";
        }

        private static String createHelloBlogFriendshipLink() {
            return "CREATE TABLE `hello_blog_friendship_link`  (\n" +
                    "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
                    "  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',\n" +
                    "  `logo` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件',\n" +
                    "  `href` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '跳转的路径',\n" +
                    "  `sort` smallint(6) NOT NULL DEFAULT 0 COMMENT '排序',\n" +
                    "  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',\n" +
                    "  PRIMARY KEY (`id`) USING BTREE\n" +
                    ") ENGINE = InnoDB AUTO_INCREMENT = 0 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '友情链接表' ROW_FORMAT = Compact;";
        }

        private static String createHelloBlogPosts() {
            return "CREATE TABLE `hello_blog_posts`  (\n" +
                    "  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',\n" +
                    "  `author_id` bigint(255) NULL DEFAULT NULL COMMENT '文章创建人',\n" +
                    "  `title` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文章标题',\n" +
                    "  `thumbnail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '封面图',\n" +
                    "  `comments` int(11) NOT NULL DEFAULT 0 COMMENT '评论数',\n" +
                    "  `is_comment` smallint(6) NULL DEFAULT 1 COMMENT '是否打开评论 (0 不打开 1 打开 )',\n" +
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

        private static String createHelloBlogPostsAttribute() {
            return "CREATE TABLE `hello_blog_posts_attribute`  (\n" +
                    "  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',\n" +
                    "  `content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '内容',\n" +
                    "  `posts_id` bigint(20) NOT NULL COMMENT '文章表主键',\n" +
                    "  PRIMARY KEY (`id`) USING BTREE\n" +
                    ") ENGINE = InnoDB AUTO_INCREMENT = 0 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;";
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

        private static String createHelloBlogPostsTags() {
            return "CREATE TABLE `hello_blog_posts_tags`  (\n" +
                    "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
                    "  `tags_id` bigint(32) NOT NULL COMMENT '名称',\n" +
                    "  `posts_id` bigint(20) NOT NULL COMMENT '文章主键',\n" +
                    "  `sort` smallint(6) NOT NULL DEFAULT 0 COMMENT '排序',\n" +
                    "  `create_time` datetime(0) NOT NULL COMMENT '创建时间',\n" +
                    "  `update_time` datetime(0) NOT NULL COMMENT '更新时间',\n" +
                    "  PRIMARY KEY (`id`) USING BTREE\n" +
                    ") ENGINE = InnoDB AUTO_INCREMENT = 0 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;";
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

        private static String initData() {
            return "INSERT INTO `hello_blog_config` VALUES (1, 'name', 'helloblog', 0),(2, 'domain', 'http://127.0.0.1', 0),(3, 'keywords', 'java', 0)," +
                    "(4, 'description', 'java', 0),(5, 'metas', '1.1.1', 0),(6, 'copyright', 'Copyright @ ByteBlogs', 0)," +
                    "(7, 'icp', 'xxxx', 0),(8, 'qiniu_access_key', '', 1),(9, 'qiniu_secret_key', '', 1),(10, 'qiniu_bucket', '', 1)," +
                    "(11, 'qiniu_image_domain', '', 1);";
        }

    }
}
