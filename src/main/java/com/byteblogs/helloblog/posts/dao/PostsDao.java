package com.byteblogs.helloblog.posts.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byteblogs.common.base.dao.BaseDao;
import com.byteblogs.helloblog.posts.domain.po.Posts;
import com.byteblogs.helloblog.posts.domain.vo.PostsVO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author byteblogs
 * @since 2019-08-28
 */
public interface PostsDao extends BaseDao<Posts> {

    /**
     * 查询文章列表
     * @param page
     * @param localDateTime
     * @param keywords
     * @return
     */
    List<PostsVO> selectPostsList(Page<PostsVO> page, @Param("archiveDate") LocalDateTime localDateTime, @Param("keywords") String keywords);

    /**
     * 看板统计
     * @return
     */
    PostsVO selectPostsTotal();

    /**
     * 按照时间进行归档
     * @param page
     * @return
     */
    List<PostsVO> selectArchiveTotalGroupDateList(Page<PostsVO> page);

    /**
     * 自增浏览量
     * @param id
     * @return
     */
    int incrementView(@Param("id") Long id);

    /**
     * 自增评论量
     * @param id
     * @return
     */
    int incrementComments(@Param("id") Long id);
}
