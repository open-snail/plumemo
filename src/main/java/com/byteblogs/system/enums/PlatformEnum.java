package com.byteblogs.system.enums;

import com.byteblogs.helloblog.posts.domain.vo.CNBlogsVO;
import com.byteblogs.helloblog.posts.domain.vo.CSDNVO;
import com.byteblogs.helloblog.posts.domain.vo.JianShuVO;
import com.byteblogs.helloblog.posts.domain.vo.JueJinVO;
import com.byteblogs.helloblog.posts.domain.vo.SegmentFaultVO;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: byteblogs
 * @date: 2019/09/03 14:33
 */
public enum PlatformEnum {

    /**
     * 掘金
     */
    JUE_JIN(1, JueJinVO.class),

    /**
     * 简书
     */
    JIAN_SHU(2, JianShuVO.class),

    /**
     * CSDN
     */
    CSDN(3, CSDNVO.class),

    /**
     * 思否
     */
    SEGMENT_FAULT(4, SegmentFaultVO.class),

    /**
     * 博客园
     */
    CN_BLOGS(5, CNBlogsVO.class);

    private static final Map<Integer, PlatformEnum> enumTypeMap = new HashMap<>();

    static {
        for (PlatformEnum platformEnum : PlatformEnum.values()) {
            enumTypeMap.put(platformEnum.type, platformEnum);
        }
    }

    private final Integer type;
    private final Class platformClass;

    PlatformEnum(Integer type, Class platformClass) {
        this.type = type;
        this.platformClass = platformClass;
    }

    public Integer getType() {
        return type;
    }

    public Class getPlatformClass() {
        return platformClass;
    }

    public static Map<Integer, PlatformEnum> getEnumTypeMap() {
        return enumTypeMap;
    }
}
