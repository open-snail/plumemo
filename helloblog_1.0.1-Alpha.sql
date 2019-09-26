
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for hello_blog_auth_token
-- ----------------------------
DROP TABLE IF EXISTS `hello_blog_auth_token`;
CREATE TABLE `hello_blog_auth_token`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `token` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'token',
  `expire_time` datetime(0) NOT NULL COMMENT '过期时间',
  `user_id` bigint(20) NOT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 287 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '标签表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for hello_blog_auth_user
-- ----------------------------
DROP TABLE IF EXISTS `hello_blog_auth_user`;
CREATE TABLE `hello_blog_auth_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `social_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '社交账户ID',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '别名',
  `role_id` bigint(20) NOT NULL COMMENT '角色主键 1 普通用户 2 admin',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `introduction` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '个人简介',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `create_time` datetime(0) NOT NULL COMMENT '注册时间',
  `html_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'github个人主页',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for hello_blog_category
-- ----------------------------
DROP TABLE IF EXISTS `hello_blog_category`;
CREATE TABLE `hello_blog_category`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `sort` smallint(6) NOT NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for hello_blog_category_tags
-- ----------------------------
DROP TABLE IF EXISTS `hello_blog_category_tags`;
CREATE TABLE `hello_blog_category_tags`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tags_id` bigint(32) NOT NULL COMMENT '名称',
  `category_id` bigint(20) NOT NULL COMMENT '分类的主键',
  `sort` smallint(6) NOT NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 58 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for hello_blog_config
-- ----------------------------
DROP TABLE IF EXISTS `hello_blog_config`;
CREATE TABLE `hello_blog_config`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `config_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置key',
  `config_value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '配置值',
  `type` smallint(6) NOT NULL DEFAULT 0 COMMENT '配置类型',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK_99vo6d7ci4wlxruo3gd0q2jq8`(`config_key`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of hello_blog_config
-- ----------------------------
INSERT INTO `hello_blog_config` VALUES (1, 'name', 'helloblog', 0);
INSERT INTO `hello_blog_config` VALUES (2, 'domain', 'http://127.0.0.1', 0);
INSERT INTO `hello_blog_config` VALUES (3, 'keywords', 'java', 0);
INSERT INTO `hello_blog_config` VALUES (4, 'description', 'java', 0);
INSERT INTO `hello_blog_config` VALUES (5, 'metas', '1.1.1', 0);
INSERT INTO `hello_blog_config` VALUES (6, 'copyright', 'Copyright @ ByteBlogs', 0);
INSERT INTO `hello_blog_config` VALUES (7, 'icp', 'xxxx', 0);
INSERT INTO `hello_blog_config` VALUES (8, 'qiniu_access_key', '', 1);
INSERT INTO `hello_blog_config` VALUES (9, 'qiniu_secret_key', '', 1);
INSERT INTO `hello_blog_config` VALUES (10, 'qiniu_bucket', '', 1);
INSERT INTO `hello_blog_config` VALUES (11, 'qiniu_image_domain', '', 1);

-- ----------------------------
-- Table structure for hello_blog_friendship_link
-- ----------------------------
DROP TABLE IF EXISTS `hello_blog_friendship_link`;
CREATE TABLE `hello_blog_friendship_link`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',
  `logo` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件',
  `href` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '跳转的路径',
  `sort` smallint(6) NOT NULL DEFAULT 0 COMMENT '排序',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '友情链接表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for hello_blog_posts
-- ----------------------------
DROP TABLE IF EXISTS `hello_blog_posts`;
CREATE TABLE `hello_blog_posts`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `author_id` bigint(255) NULL DEFAULT NULL COMMENT '文章创建人',
  `title` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文章标题',
  `thumbnail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '封面图',
  `comments` int(11) NOT NULL DEFAULT 0 COMMENT '评论数',
  `is_comment` smallint(6) NULL DEFAULT 1 COMMENT '是否打开评论 (0 不打开 1 打开 )',
  `sync_status` smallint(6) NOT NULL DEFAULT 0 COMMENT '同步到byteblogs状态',
  `status` int(11) NOT NULL DEFAULT 1 COMMENT '状态 1 草稿 2 发布',
  `summary` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '摘要',
  `views` int(11) NOT NULL DEFAULT 0 COMMENT '浏览次数',
  `weight` int(11) NOT NULL DEFAULT 0 COMMENT '文章权重',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for hello_blog_posts_attribute
-- ----------------------------
DROP TABLE IF EXISTS `hello_blog_posts_attribute`;
CREATE TABLE `hello_blog_posts_attribute`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '内容',
  `posts_id` bigint(20) NOT NULL COMMENT '文章表主键',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1168883007018885123 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for hello_blog_posts_comments
-- ----------------------------
DROP TABLE IF EXISTS `hello_blog_posts_comments`;
CREATE TABLE `hello_blog_posts_comments`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `author_id` bigint(20) NOT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `parent_id` bigint(20) NOT NULL DEFAULT 0,
  `status` int(11) NOT NULL DEFAULT 0,
  `posts_id` bigint(20) NOT NULL,
  `tree_path` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '层级结构',
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '评论表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for hello_blog_posts_tags
-- ----------------------------
DROP TABLE IF EXISTS `hello_blog_posts_tags`;
CREATE TABLE `hello_blog_posts_tags`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tags_id` bigint(32) NOT NULL COMMENT '名称',
  `posts_id` bigint(20) NOT NULL COMMENT '文章主键',
  `sort` smallint(6) NOT NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for hello_blog_tags
-- ----------------------------
DROP TABLE IF EXISTS `hello_blog_tags`;
CREATE TABLE `hello_blog_tags`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `sort` smallint(6) NOT NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 269 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '标签表' ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
