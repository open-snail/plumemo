package com.byteblogs.helloblog.posts.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byteblogs.common.base.domain.Result;
import com.byteblogs.common.base.domain.vo.BaseVO;
import com.byteblogs.common.base.domain.vo.UserSessionVO;
import com.byteblogs.common.constant.Constants;
import com.byteblogs.common.context.BeanTool;
import com.byteblogs.common.enums.ErrorEnum;
import com.byteblogs.common.enums.OperateEnum;
import com.byteblogs.common.util.*;
import com.byteblogs.helloblog.category.dao.TagsDao;
import com.byteblogs.helloblog.category.domain.po.Tags;
import com.byteblogs.helloblog.category.domain.vo.TagsVO;
import com.byteblogs.helloblog.dto.HttpResult;
import com.byteblogs.helloblog.integration.ByteBlogsClient;
import com.byteblogs.helloblog.integration.dto.PostsDTO;
import com.byteblogs.helloblog.log.dao.AuthUserLogDao;
import com.byteblogs.helloblog.log.domain.vo.AuthUserLogVO;
import com.byteblogs.helloblog.posts.dao.PostsAttributeDao;
import com.byteblogs.helloblog.posts.dao.PostsDao;
import com.byteblogs.helloblog.posts.dao.PostsTagsDao;
import com.byteblogs.helloblog.posts.domain.po.Posts;
import com.byteblogs.helloblog.posts.domain.po.PostsAttribute;
import com.byteblogs.helloblog.posts.domain.po.PostsTags;
import com.byteblogs.helloblog.posts.domain.vo.*;
import com.byteblogs.helloblog.posts.service.PostsService;
import com.byteblogs.system.enums.PlatformEnum;
import com.overzealous.remark.Remark;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.OOSpider;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author byteblogs
 * @since 2019-08-28
 */
@Service
@Slf4j
public class PostsServiceImpl extends ServiceImpl<PostsDao, Posts> implements PostsService {

    @Autowired
    private PostsDao postsDao;

    @Autowired
    private PostsAttributeDao postsAttributeDao;

    @Autowired
    private TagsDao tagsDao;

    @Autowired
    private PostsTagsDao postsTagsDao;

    @Autowired
    private AuthUserLogDao authUserLogDao;

    @Override
    public Result savePosts(PostsVO postsVO) {

        UserSessionVO userSessionInfo = SessionUtil.getUserSessionInfo();
        String html = Markdown2HtmlUtil.html(postsVO.getContent());

        Posts posts = new Posts();
        posts.setTitle(postsVO.getTitle()).setCreateTime(LocalDateTime.now()).setUpdateTime(LocalDateTime.now()).setThumbnail(postsVO.getThumbnail());
        posts.setStatus(postsVO.getStatus()).setSummary(PreviewTextUtils.getText(html, 126)).setIsComment(postsVO.getIsComment())
                .setAuthorId(userSessionInfo.getId()).setCategoryId(postsVO.getCategoryId()).setWeight(postsVO.getWeight());
        postsDao.insert(posts);
        postsVO.setId(posts.getId());

        postsAttributeDao.insert(new PostsAttribute().setContent(postsVO.getContent()).setPostsId(posts.getId()));
        List<TagsVO> tagsList = postsVO.getTagsList();
        if (!CollectionUtils.isEmpty(tagsList)) {
            tagsList.forEach(tagsVO -> {
                if (tagsVO.getId() == null) {
                    Tags tags = new Tags().setName(tagsVO.getName()).setCreateTime(LocalDateTime.now()).setUpdateTime(LocalDateTime.now());// saveMenu
                    tagsDao.insert(tags);
                    tagsVO.setId(tags.getId());
                }
                postsTagsDao.insert(new PostsTags().setPostsId(posts.getId()).setTagsId(tagsVO.getId()).setCreateTime(LocalDateTime.now()).setUpdateTime(LocalDateTime.now()));
            });
        }

        if (syncByteblogs(postsVO)) {
            return Result.createWithSuccessMessage("同步到ByteBlogs成功");
        }

        return Result.createWithSuccessMessage();
    }

    @Override
    public Result updatePosts(PostsVO postsVO) {

        UserSessionVO userSessionInfo = SessionUtil.getUserSessionInfo();
        String html = Markdown2HtmlUtil.html(postsVO.getContent());

        Posts posts1 = postsDao.selectOne(new LambdaQueryWrapper<Posts>().eq(Posts::getId, postsVO.getId()));
        if (posts1 == null) {
            ExceptionUtil.rollback(ErrorEnum.DATA_NO_EXIST);
        }

        posts1.setTitle(postsVO.getTitle()).setUpdateTime(LocalDateTime.now()).setThumbnail(postsVO.getThumbnail());
        posts1.setStatus(postsVO.getStatus()).setSyncStatus(Constants.NO).setSummary(PreviewTextUtils.getText(html, 126)).setIsComment(postsVO.getIsComment())
                .setAuthorId(userSessionInfo.getId()).setCategoryId(postsVO.getCategoryId()).setWeight(postsVO.getWeight());

        postsDao.updateById(posts1);
        Wrapper<PostsAttribute> wrapper = new LambdaUpdateWrapper<PostsAttribute>().eq(PostsAttribute::getPostsId, posts1.getId());
        if (postsAttributeDao.selectCount(wrapper) > 0) {
            postsAttributeDao.update(new PostsAttribute().setContent(postsVO.getContent()), wrapper);
        } else {
            postsAttributeDao.insert(new PostsAttribute().setContent(postsVO.getContent()).setPostsId(posts1.getId()));
        }

        List<TagsVO> tagsList = postsVO.getTagsList();

        if (!CollectionUtils.isEmpty(tagsList)) {
            List<PostsTags> originalList = postsTagsDao.selectList(new LambdaQueryWrapper<PostsTags>().eq(PostsTags::getPostsId, posts1.getId()));
            List<PostsTags> categoryTagsList = originalList.stream().filter(postsTags -> !postsVO.getTagsList().stream().map(BaseVO::getId).collect(Collectors.toList())
                    .contains(postsTags.getTagsId())).collect(Collectors.toList());

            if (!CollectionUtils.isEmpty(categoryTagsList)) {
                categoryTagsList.forEach(categoryTags -> {
                    postsTagsDao.deleteById(categoryTags.getId());
                });
            }

            tagsList.forEach(tagsVO -> {
                if (tagsVO.getId() == null) {
                    // saveMenu
                    Tags tags = new Tags().setName(tagsVO.getName()).setCreateTime(LocalDateTime.now()).setUpdateTime(LocalDateTime.now());
                    tagsDao.insert(tags);
                    tagsVO.setId(tags.getId());
                    postsTagsDao.insert(new PostsTags().setPostsId(posts1.getId()).setTagsId(tagsVO.getId()).setCreateTime(LocalDateTime.now()).setUpdateTime(LocalDateTime.now()));
                } else {
                    PostsTags postsTags = postsTagsDao.selectOne(new LambdaQueryWrapper<PostsTags>().eq(PostsTags::getPostsId, posts1.getId()).eq(PostsTags::getTagsId, tagsVO.getId()));
                    if (postsTags == null) {
                        postsTagsDao.insert(new PostsTags().setPostsId(posts1.getId()).setTagsId(tagsVO.getId()).setCreateTime(LocalDateTime.now()).setUpdateTime(LocalDateTime.now()));
                    }
                }
            });
        } else {

            postsTagsDao.delete(new LambdaQueryWrapper<PostsTags>().eq(PostsTags::getPostsId, posts1.getId()));
        }

        if (syncByteblogs(postsVO)) {
            return Result.createWithSuccessMessage("同步到ByteBlogs成功");
        }

        return Result.createWithSuccessMessage();
    }

    /**
     * 同步数据到社区
     *
     * @param postsVO
     * @return
     */
    private boolean syncByteblogs(PostsVO postsVO) {
        if (postsVO.getIsPublishByteBlogs().equals(Constants.YES)) {
            ByteBlogsClient byteBlogsClient = BeanTool.getBean(ByteBlogsClient.class);

            HttpResult result = byteBlogsClient.syncPosts(new PostsDTO().setTitle(postsVO.getTitle()).setContent(postsVO.getContent()));
            log.warn("保存到ByteBlogs草稿箱 {}", result);
            if (result.getSuccess() == Constants.YES) {
                postsDao.update(new Posts().setSyncStatus(Constants.YES), new LambdaUpdateWrapper<Posts>().eq(Posts::getId, postsVO.getId()));
                return true;
            }
        }
        return false;
    }

    @Override
    public Result deletePosts(Long id) {

        Posts posts = postsDao.selectById(id);
        if (posts == null) {
            ExceptionUtil.rollback(ErrorEnum.DATA_NO_EXIST);
        }

        postsDao.deleteById(id);
        postsAttributeDao.delete(new LambdaUpdateWrapper<PostsAttribute>().eq(PostsAttribute::getPostsId, id));
        postsTagsDao.delete(new LambdaUpdateWrapper<PostsTags>().eq(PostsTags::getPostsId, id));

        return Result.createWithSuccessMessage();
    }

    @Override
    public Result getPosts(Long id) {
        Posts posts = postsDao.selectOneById(id);
        if (posts == null) {
            ExceptionUtil.rollback(ErrorEnum.DATA_NO_EXIST);
        }

        PostsVO postsVO = new PostsVO();
        postsVO.setId(posts.getId())
                .setCreateTime(posts.getCreateTime())
                .setSummary(posts.getSummary())
                .setTitle(posts.getTitle())
                .setThumbnail(posts.getThumbnail())
                .setIsComment(posts.getIsComment())
                .setViews(posts.getViews())
                .setComments(posts.getComments())
                .setCategoryId(posts.getCategoryId())
                .setWeight(posts.getWeight())
                .setCategoryName(posts.getCategoryName());

        PostsAttribute postsAttribute = postsAttributeDao.selectOne(new LambdaQueryWrapper<PostsAttribute>().eq(PostsAttribute::getPostsId, posts.getId()));
        if (postsAttribute != null) {
            postsVO.setContent(postsAttribute.getContent());
        }
        List<PostsTags> postsTagsList = postsTagsDao.selectList(new LambdaQueryWrapper<PostsTags>().eq(PostsTags::getPostsId, posts.getId()));
        List<TagsVO> tagsVOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(postsTagsList)) {
            postsTagsList.forEach(postsTags -> {
                Tags tags = tagsDao.selectById(postsTags.getTagsId());
                tagsVOList.add(new TagsVO().setId(tags.getId()).setName(tags.getName()));
            });
        }

        postsVO.setTagsList(tagsVOList);

        postsDao.incrementView(posts.getId());
        return Result.createWithModel(postsVO);
    }

    @Override
    public Result<PostsVO> getPostsList(PostsVO postsVO) {
        postsVO = Optional.ofNullable(postsVO).orElse(new PostsVO());
        Page page = Optional.of(PageUtil.checkAndInitPage(postsVO)).orElse(PageUtil.initPage());
        if (StringUtils.isNotBlank(postsVO.getKeywords())) {
            postsVO.setKeywords("%" + postsVO.getKeywords() + "%");
        }
        if (StringUtils.isNoneBlank(postsVO.getTitle())) {
            postsVO.setTitle("%" + postsVO.getTitle() + "%");
        }
        List<PostsVO> postsVOList = postsDao.selectPostsList(page, postsVO);
        if (!CollectionUtils.isEmpty(postsVOList)) {
            postsVOList.forEach(postsVO1 -> {
                List<PostsTags> postsTagsList = postsTagsDao.selectList(new LambdaQueryWrapper<PostsTags>().eq(PostsTags::getPostsId, postsVO1.getId()));
                List<TagsVO> tagsVOList = new ArrayList<>();
                if (!CollectionUtils.isEmpty(postsTagsList)) {
                    postsTagsList.forEach(postsTags -> {
                        Tags tags = tagsDao.selectById(postsTags.getTagsId());
                        tagsVOList.add(new TagsVO().setId(tags.getId()).setName(tags.getName()));
                    });
                }
                postsVO1.setTagsList(tagsVOList);
            });
        }

        return Result.createWithPaging(postsVOList, PageUtil.initPageInfo(page));
    }

    @Override
    public Result getPlatformArticle(PostsVO postsVO) {
        crawler(postsVO);
        return Result.createWithModel(postsVO);
    }

    @Override
    public Result<PostsVO> getArchiveTotalByDateList(PostsVO postsVO) {
        List<PostsVO> postsVOList = postsDao.selectArchiveTotalGroupDateList();
        postsVOList.forEach(obj -> {
            // 查询每一个时间点中的文章
            obj.setArchivePosts(postsDao.selectByArchiveDate(obj.getArchiveDate()));
        });
        return Result.createWithModels(postsVOList);
    }

    @Override
    public Result getArchiveGroupYearList(PostsVO postsVO) {
        List<PostsVO> postsVOList = postsDao.selectArchiveGroupYearList();
        return Result.createWithModels(postsVOList);
    }

    @Override
    public Result updatePostsStatus(PostsVO postsVO) {
        postsDao.updateById(new Posts().setId(postsVO.getId()).setStatus(postsVO.getStatus()).setUpdateTime(LocalDateTime.now()));
        return Result.createWithSuccessMessage();
    }

    @Override
    public Result publishByteBlogs(PostsVO postsVO) {
        UserSessionVO userSessionInfo = SessionUtil.getUserSessionInfo();
        Posts posts = postsDao.selectById(postsVO.getId());
        if (posts == null) {
            ExceptionUtil.rollback(ErrorEnum.DATA_NO_EXIST);
        }

        PostsAttribute postsAttribute = postsAttributeDao.selectOne(new LambdaQueryWrapper<PostsAttribute>().eq(PostsAttribute::getPostsId, posts.getId()));
        boolean syncStatus = syncByteblogs(new PostsVO().setTitle(posts.getTitle()).setIsPublishByteBlogs(Constants.YES).setContent(postsAttribute.getContent()));
        log.debug("保存到ByteBlogs草稿箱 {}", syncStatus);
        if (syncStatus) {
            postsDao.update(new Posts().setSyncStatus(Constants.YES), new LambdaUpdateWrapper<Posts>().eq(Posts::getId, postsVO.getId()));
            return Result.createWithSuccessMessage("同步到ByteBlogs成功，请点击" + Constants.BYTE_BLOGS_URL + "/editor/posts" + "进行编辑");
        }

        return Result.createWithErrorMessage(ErrorEnum.SYNC_POSTS_ERROR);
    }

    @Override
    public Result getHotPostsList(PostsVO postsVO) {
        postsVO = Optional.ofNullable(postsVO).orElse(new PostsVO());
        Page page = Optional.of(PageUtil.checkAndInitPage(postsVO)).orElse(PageUtil.initPage());
        if (StringUtils.isNotBlank(postsVO.getKeywords())) {
            postsVO.setKeywords("%" + postsVO.getKeywords() + "%");
        }
        List<AuthUserLogVO> logVOList = authUserLogDao.selectListByCode(OperateEnum.GET_POSTS_DETAIL.getCode());
        List<Long> ids = new ArrayList<>();
        logVOList.forEach(obj -> {
            JSONObject json = JSONObject.parseObject(obj.getParameter());
            ids.add(json.getLong("id"));
        });
        postsDao.selectPage(page, new QueryWrapper<Posts>().in("id", ids));
        return Result.createWithPaging(page.getRecords(), PageUtil.initPageInfo(page));
    }

    private void crawler(PostsVO postsVO) {
        Class platformClass = PlatformEnum.getEnumTypeMap().get(postsVO.getPlatformType()).getPlatformClass();
        Spider spider = OOSpider.create(Site.me(), platformClass).setDownloader(new HttpClientDownloader());
        Object object = spider.get(postsVO.getSourceUri());

        String join = "";
        if (postsVO.getPlatformType().equals(PlatformEnum.JIAN_SHU.getType())) {
            JianShuVO jianShuVO = (JianShuVO) object;
            postsVO.setTitle(jianShuVO.getTitle());
            join = String.join("", jianShuVO.getContent());
        } else if (postsVO.getPlatformType().equals(PlatformEnum.JUE_JIN.getType())) {
            JueJinVO jueJinVO = (JueJinVO) object;
            postsVO.setTitle(jueJinVO.getTitle());
            join = String.join("", jueJinVO.getContent());
        } else if (postsVO.getPlatformType().equals(PlatformEnum.SEGMENT_FAULT.getType())) {
            SegmentFaultVO segmentFaultVO = (SegmentFaultVO) object;
            postsVO.setTitle(segmentFaultVO.getTitle());
            join = String.join("", segmentFaultVO.getContent());
        } else if (postsVO.getPlatformType().equals(PlatformEnum.CSDN.getType())) {
            CSDNVO csdnVO = (CSDNVO) object;
            postsVO.setTitle(csdnVO.getTitle());
            join = String.join("", csdnVO.getContent());
        } else if (postsVO.getPlatformType().equals(PlatformEnum.CN_BLOGS.getType())) {
            CNBlogsVO cnBlogsVO = (CNBlogsVO) object;
            postsVO.setTitle(cnBlogsVO.getTitle());
            join = String.join("", cnBlogsVO.getContent());
        } else {
            ExceptionUtil.rollback(ErrorEnum.PARAM_ERROR);
        }
        String converted = new Remark().convertFragment(join);
        postsVO.setContent(converted);
    }
}
