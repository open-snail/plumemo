package com.byteblogs.system.init;

import com.byteblogs.system.init.strategy.TableInfoService;
import com.byteblogs.system.init.strategy.TableInitFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.sql.*;

/**
 * @author byteblogs
 * @date 2019/11/24
 */
@Component
@Slf4j
public class InitDatabaseConfig implements ApplicationListener<ApplicationContextEvent>, Ordered {

    @Value("${spring.datasource.driver-class-name}")
    private String driver;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String userName;

    @Value("${spring.datasource.password}")
    private String password;

    public void init() {

        //连接数据库
        try {
            Class.forName(driver);
        } catch (final ClassNotFoundException e) {
            log.error("连接数据库异常 {}", e.getMessage());
        }
        //测试url中是否包含useSSL字段，没有则添加设该字段且禁用
        if (url.indexOf("?") == -1) {
            url = url + "?useSSL=false";
        } else if (url.indexOf("useSSL=false") == -1 || url.indexOf("useSSL=true") == -1) {
            url = url + "&useSSL=false";
        }

        Connection conn = null;
        Statement stat = null;
        try {
            conn = DriverManager.getConnection(url, userName, password);
            stat = conn.createStatement();
        } catch (final SQLException e) {
            log.error("数据库解析异常 {}", e.getMessage());
        }

        final String[] tables = {"hello_blog_config", "hello_blog_auth_token", "hello_blog_auth_user",
                "hello_blog_category", "hello_blog_category_tags", "hello_blog_friendship_link", "hello_blog_posts"
                , "hello_blog_posts_attribute", "hello_blog_posts_comments", "hello_blog_posts_tags", "hello_blog_tags"
                , "hello_blog_auth_user_log","hello_blog_menu","hello_blog_auth_user_social","hello_blog_black_list"};

        try {
            for (int i = 0; i < tables.length; i++) {
                //获取数据库表名
                final ResultSet rs = conn.getMetaData().getTables(null, null, tables[i], null);
                // 判断表是否存在，如果存在则什么都不做，否则创建表
                if (!rs.next()) {
                    final TableInfoService byTableName = TableInitFactory.getByTableName(tables[i]);
                    byTableName.builderTable(stat);
                }
            }
        } catch (final SQLException e) {
            log.error("数据库解析异常 {}", e.getMessage());
        } finally {
            // 释放资源
            try {
                stat.close();
            } catch (final SQLException e) {
                log.error("释放 Statement 失败 {}", e.getMessage());
            }
            try {
                conn.close();
            } catch (final SQLException e) {
                log.error("释放 Connection 失败 {}", e.getMessage());
            }
        }

    }

    @Override
    public int getOrder() {
        return -41;
    }

    @Override
    public void onApplicationEvent(final ApplicationContextEvent applicationContextEvent) {
        init();
    }
}
