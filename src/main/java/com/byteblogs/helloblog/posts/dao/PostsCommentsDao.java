package com.byteblogs.helloblog.posts.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byteblogs.common.base.dao.BaseDao;
import com.byteblogs.helloblog.posts.domain.po.PostsComments;
import com.byteblogs.helloblog.posts.domain.vo.PostsCommentsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 评论表 Mapper 接口
 * </p>
 * @author byteblogs
 * @since 2019-09-03
 */
public interface PostsCommentsDao extends BaseDao<PostsComments> {

    /**
     * 查询评论列表
     * @param page
     * @param postsId
     * @return
     */
    List<PostsCommentsVO> selectPostsCommentsByPostsIdList(Page<PostsCommentsVO> page, @Param("postsId") Long postsId);

    /**
     * 查询评论后台评论管理列表
     * @param page
     * @param keywords
     * @return
     */
    List<PostsCommentsVO> selectPostsCommentsList(Page<PostsCommentsVO> page, @Param("keywords") String keywords);
}
