package com.byteblogs.plumemo.posts.service.impl;

import com.byteblogs.plumemo.posts.domain.vo.BlogMoveVO;
import com.byteblogs.plumemo.posts.factory.BlogPlatformFactory;
import com.byteblogs.plumemo.posts.service.BlogPlatformService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

/**
 * @author: zsg
 * @description:
 * @date: 2020/4/5 21:39
 * @modified:
 */
@Service
public class WordPressServiceImpl implements BlogPlatformService, InitializingBean {

    @Override
    public String getUrl(BlogMoveVO blogMoveVO) {
        return String.format(URL, blogMoveVO.getIp(), blogMoveVO.getPort(), blogMoveVO.getDatabase());
    }

    @Override
    public String getCountSql(BlogMoveVO blogMoveVO) {
        return "SELECT count(*) FROM wp_posts";
    }

    @Override
    public String getQuerySql(BlogMoveVO blogMoveVO) {
        return "SELECT post_title,post_content,post_date FROM wp_posts limit %s,%s";
    }

    @Override
    public String getDriver() {
        return MYSQL_DRIVER;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        BlogPlatformFactory.register("wordpress", this);
    }
}
