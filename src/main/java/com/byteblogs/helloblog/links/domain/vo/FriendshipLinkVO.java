package com.byteblogs.helloblog.links.domain.vo;

import com.byteblogs.common.base.domain.vo.BaseVO;
import com.byteblogs.common.validator.annotion.NotBlank;
import com.byteblogs.helloblog.links.domain.validator.InsertLink;
import com.byteblogs.helloblog.links.domain.validator.UpdateLink;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class FriendshipLinkVO extends BaseVO<FriendshipLinkVO> {

    @NotBlank(groups = {UpdateLink.class})
    private Long id;

    private String title;

    @NotBlank(groups = {InsertLink.class, UpdateLink.class})
    private String name;

    @NotBlank(groups = {InsertLink.class, UpdateLink.class})
    private String href;

    private String logo;

    private Integer sort;

    private String description;


}
