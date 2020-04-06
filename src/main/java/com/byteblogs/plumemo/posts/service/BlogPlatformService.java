package com.byteblogs.plumemo.posts.service;

import com.byteblogs.plumemo.posts.domain.vo.BlogMoveVO;

/**
 * @author: zsg
 * @description:
 * @date: 2020/4/5 21:29
 * @modified:
 */
public interface BlogPlatformService {

    String URL = "jdbc:mysql://%s:%s/%s?useSSL=false&characterEncoding=utf8";

    String MYSQL_DRIVER = "com.mysql.jdbc.Driver";

    /**
     * 获取url
     *
     * @param blogMoveVO
     * @return
     */
    String getUrl(BlogMoveVO blogMoveVO);

    /**
     * 获取统计sql
     *
     * @param blogMoveVO
     * @return
     */
    String getCountSql(BlogMoveVO blogMoveVO);

    /**
     * 获取查询接口
     *
     * @param blogMoveVO
     * @return
     */
    String getQuerySql(BlogMoveVO blogMoveVO);

    /**
     * 获取驱动
     *
     * @return
     */
    String getDriver();
}
