package com.byteblogs.helloblog.dashboard.domain.vo;

import com.byteblogs.helloblog.log.domain.vo.AuthUserLogVO;
import com.byteblogs.helloblog.posts.domain.vo.PostsVO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: byteblogs
 * @date: 2020/1/16 21:32
 */
@Data
@Accessors(chain = true)
public class ViewChartVO {

    private List<ViewChartSpotVO> viewRecordList;

}
