package com.byteblogs.helloblog.posts.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import us.codecraft.webmagic.model.annotation.ExtractBy;

import java.util.List;

/**
 * @description:
 * @author: byteblogs
 * @date: 2019/09/03 18:49
 */
@Data
@Accessors(chain = true)
public class CNBlogsVO {

    @ExtractBy("//*[@id=\"cb_post_title_url\"]/text()")
    private String title;

    @ExtractBy("//*[@id=\"cnblogs_post_body\"]")
    private List<String> content;
}
