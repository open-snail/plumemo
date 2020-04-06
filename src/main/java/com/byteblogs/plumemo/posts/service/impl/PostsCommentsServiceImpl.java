package com.byteblogs.plumemo.posts.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byteblogs.common.base.domain.Result;
import com.byteblogs.common.base.domain.vo.UserSessionVO;
import com.byteblogs.common.base.service.impl.BaseServiceImpl;
import com.byteblogs.common.constant.Constants;
import com.byteblogs.common.enums.ErrorEnum;
import com.byteblogs.common.util.ExceptionUtil;
import com.byteblogs.common.util.PageUtil;
import com.byteblogs.common.util.SessionUtil;
import com.byteblogs.plumemo.auth.dao.AuthUserDao;
import com.byteblogs.plumemo.auth.domain.po.AuthUser;
import com.byteblogs.plumemo.posts.dao.PostsCommentsDao;
import com.byteblogs.plumemo.posts.dao.PostsDao;
import com.byteblogs.plumemo.posts.domain.po.PostsComments;
import com.byteblogs.plumemo.posts.domain.vo.PostsCommentsVO;
import com.byteblogs.plumemo.posts.service.PostsCommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 评论表 服务实现类
 * </p>
 * @author byteblogs
 * @since 2019-09-03
 */
@Service
public class PostsCommentsServiceImpl extends BaseServiceImpl<PostsCommentsDao, PostsComments> implements PostsCommentsService {

    @Autowired
    private PostsCommentsDao postsCommentsDao;

    @Autowired
    private PostsDao postsDao;

    @Autowired
    private AuthUserDao authUserDao;

    @Override
    public Result savePostsComments(PostsCommentsVO postsCommentsVO) {
        UserSessionVO userSessionInfo = SessionUtil.getUserSessionInfo();
        PostsComments postsComments = new PostsComments();
        postsComments.setAuthorId(userSessionInfo.getId());
        postsComments.setContent(postsCommentsVO.getContent());
        postsComments.setPostsId(postsCommentsVO.getPostsId());
        postsComments.setCreateTime(LocalDateTime.now());

        String treePath;
        if (postsCommentsVO.getParentId() == null) {
            this.postsCommentsDao.insert(postsComments);
            treePath = postsComments.getId() + Constants.TREE_PATH;
        } else {
            PostsComments parentPostsComments = this.postsCommentsDao.selectById(postsCommentsVO.getParentId());
            if (parentPostsComments == null) {
                ExceptionUtil.rollback(ErrorEnum.DATA_NO_EXIST);
            }

            postsComments.setParentId(postsCommentsVO.getParentId());
            this.postsCommentsDao.insert(postsComments);

            treePath = parentPostsComments.getTreePath() + postsComments.getId() + Constants.TREE_PATH;
        }

        this.postsCommentsDao.updateById(postsComments.setTreePath(treePath));

        this.postsDao.incrementComments( postsCommentsVO.getPostsId());

        return Result.createWithSuccessMessage();
    }


    @Override
    public Result replyComments(PostsCommentsVO postsCommentsVO) {
        AuthUser authUser=authUserDao.selectAdmin();
        PostsComments postsComments=postsCommentsDao.selectById(postsCommentsVO.getParentId())
                .setParentId(postsCommentsVO.getParentId())
                .setContent(postsCommentsVO.getContent())
                .setAuthorId(authUser.getId())
                .setCreateTime(LocalDateTime.now());
        this.postsCommentsDao.insert(postsComments);
        String treePath = postsComments.getTreePath() + postsComments.getId() + Constants.TREE_PATH;
        this.postsCommentsDao.updateById(postsComments.setTreePath(treePath));
        this.postsDao.incrementComments( postsCommentsVO.getPostsId());
        return Result.createWithSuccessMessage();
    }

    @Override
    public Result getPostsCommentsByPostsIdList(PostsCommentsVO postsCommentsVO) {

        Page page = Optional.ofNullable(PageUtil.checkAndInitPage(postsCommentsVO)).orElse(PageUtil.initPage());
        List<PostsCommentsVO> postsCommentsVOLis = this.postsCommentsDao.selectPostsCommentsByPostsIdList(page, postsCommentsVO.getPostsId());

        return Result.createWithPaging(postsCommentsVOLis, PageUtil.initPageInfo(page));
    }

    @Override
    public Result getPostsCommentsList(PostsCommentsVO postsCommentsVO) {
        Page page = Optional.ofNullable(PageUtil.checkAndInitPage(postsCommentsVO)).orElse(PageUtil.initPage());
        List<PostsCommentsVO> postsCommentsVOLis = this.postsCommentsDao.selectPostsCommentsList(page, postsCommentsVO);
        return Result.createWithPaging(postsCommentsVOLis, PageUtil.initPageInfo(page));
    }

    @Override
    public Result deletePostsComments(Long id) {
        this.postsCommentsDao.deleteById(id);
        return Result.createWithSuccessMessage();
    }

    @Override
    public Result getPostsComment(Long id) {
        ExceptionUtil.isRollback(id==null,ErrorEnum.PARAM_ERROR);
        List<PostsCommentsVO> postsCommentsVOLis = this.postsCommentsDao.selectPostsCommentsList(new PostsCommentsVO().setId(id));
        if (postsCommentsVOLis!=null && postsCommentsVOLis.size()>0){
            return Result.createWithModel(postsCommentsVOLis.get(0));
        }
        return Result.createWithError();
    }
}
