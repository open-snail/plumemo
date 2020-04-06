package com.byteblogs.plumemo.dashboard.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

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
