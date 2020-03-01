package com.byteblogs.system.init.strategy;

import com.byteblogs.system.init.InitFileConfig;
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
public class HelloBlogConfigTable implements TableInfoService {

    @Override
    public void builderTable(final Statement stat) {
        try {
            stat.execute(createHelloBlogConfig());
            stat.execute(initData());
            log.info("初始化hello_blog_config完成");
        } catch (final SQLException e) {
            log.error("初始化hello_blog_config失败", e);
        }
    }

    @PostConstruct
    public void beforePropertiesSet() throws Exception {
        TableInitFactory.register("hello_blog_config", this);
    }

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

    private static String initData() {
        return "INSERT INTO `hello_blog_config` VALUES " +
                "(1, 'name', 'helloblog', 0)," +
                "(2, 'domain', 'http://127.0.0.1', 0)," +
                "(3, 'keywords', 'java', 0)," +
                "(4, 'description', 'java', 0)," +
                "(5, 'metas', '1.1.1', 0)," +
                "(6, 'copyright', 'Copyright @ ByteBlogs', 0)," +
                "(7, 'icp', 'xxxx', 0)," +
                "(8, 'qiniu_access_key', '', 1)," +
                "(9, 'qiniu_secret_key', '', 1)," +
                "(10, 'qiniu_bucket', '', 1)," +
                "(11, 'qiniu_image_domain', '', 1)," +
                "(12, 'cloud_music_id', '', 2)," +
                "(14, 'store_type', 'default', 3)," +
                "(15, 'aliyun_oss_access_key', '', 4)," +
                "(16, 'aliyun_oss_secret_key', '', 4)," +
                "(17, 'aliyun_oss_bucket', '', 4)," +
                "(18, 'aliyun_oss_endpoint', '', 4)," +
                "(19, 'aliyun_oss_path', '', 4)," +
                "(20, 'aliyun_oss_image_domain', '', 4)," +
                "(21, 'cos_access_key', '', 5)," +
                "(22, 'cos_secret_key', '', 5)," +
                "(23, 'cos_bucket', '', 5)," +
                "(24, 'cos_region', '', 5)," +
                "(25, 'cos_image_domain', '', 5)," +
                "(26, 'cos_path', '', 5)," +
                "(27, 'default_path', '', 6)," +
                "(28, 'default_image_domain', 'http://127.0.0.1:8086/', 6)" +
                ";";
    }
}
