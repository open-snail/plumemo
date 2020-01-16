package com.byteblogs.helloblog.dashboard.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author: zsg
 * @description:
 * @date: 2020/1/16 22:01
 * @modified:
 */
@Data
@Accessors(chain = true)
public class ViewChartSpotVO {

    private Integer todayCount;

    private Integer yesterdayCount;

    private LocalDateTime xAxis;
}
