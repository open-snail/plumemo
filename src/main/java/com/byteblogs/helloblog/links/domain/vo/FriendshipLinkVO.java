package com.byteblogs.helloblog.links.domain.vo;

import com.byteblogs.common.base.domain.vo.BaseVO;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class FriendshipLinkVO extends BaseVO<FriendshipLinkVO> {

    private String name;

    private String logo;

    private Integer sort;

    private String description;

    private String href;
}
