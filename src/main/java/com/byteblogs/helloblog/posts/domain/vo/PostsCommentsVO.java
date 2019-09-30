package com.byteblogs.helloblog.posts.domain.vo;

import com.byteblogs.common.base.domain.vo.BaseVO;
import com.byteblogs.common.validator.annotion.NotBlank;
import com.byteblogs.common.validator.annotion.NotNull;
import com.byteblogs.helloblog.posts.domain.validator.InsertPostsComments;
import com.byteblogs.helloblog.posts.domain.validator.QueryPostsComments;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 评论表
 * </p>
 * @author byteblogs
 * @since 2019-09-03
 */
@Data
@Accessors(chain = true)
public class PostsCommentsVO extends BaseVO<PostsCommentsVO> {

    private Long authorId;

    @NotBlank(groups = {InsertPostsComments.class})
    private String content;

    private Long parentId;

    private Integer status;

    @NotNull(groups = {InsertPostsComments.class, QueryPostsComments.class})
    private Long postsId;

    private String treePath;

    private String authorName;

    private String authorAvatar;

    private String parentUserName;

    private LocalDateTime createTime;

}
