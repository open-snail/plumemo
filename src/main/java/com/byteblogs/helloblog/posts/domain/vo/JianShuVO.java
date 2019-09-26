package com.byteblogs.helloblog.posts.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import us.codecraft.webmagic.model.annotation.ExtractBy;

import java.util.List;

/**
 * @author: zsg
 * @description:
 * @date: 2019/8/3 13:15
 * @modified:
 */

@Data
@Accessors(chain = true)
public class JianShuVO {

    @ExtractBy("//*[@id='__next']/div[1]/div/div/section[1]/h1/text()")
    private String title;

    @ExtractBy("//*[@id='__next']/div[1]/div/div/section[1]/article")
    private List<String> content;

}
