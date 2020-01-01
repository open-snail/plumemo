package com.byteblogs.common.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byteblogs.common.base.domain.PageInfo;
import com.byteblogs.common.base.domain.vo.BaseVO;
import com.byteblogs.common.constant.Constants;

/**
 * @author: byteblogs
 * @date: 2019/8/3 14:57
 */
public class PageUtil {

    public static Page checkAndInitPage(BaseVO baseVO) {
        if (baseVO.getPage() == null) {
             baseVO.setPage(Constants.DEFAULT_PAGE_INDEX);
        }
        if (baseVO.getSize() == null){
            baseVO.setSize(Constants.DEFAULT_PAGE_SIZE);
        }

        return new Page(baseVO.getPage(), baseVO.getSize());
    }

    public static Page initPage() {
        return new Page(Constants.DEFAULT_PAGE_INDEX, Constants.DEFAULT_PAGE_SIZE);
    }

    public static PageInfo initPageInfo(Page page) {
        if (page != null) {
            return new PageInfo().setPage(page.getCurrent()).setSize(page.getSize()).setTotal(page.getTotal());
        }
        return null;
    }
}
