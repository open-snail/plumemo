package com.byteblogs.plumemo.posts.service;

import com.byteblogs.common.base.domain.Result;
import com.byteblogs.plumemo.posts.domain.vo.BlogMoveVO;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: zsg
 * @description:
 * @date: 2020/4/5 17:13
 * @modified:
 */
public interface BlogMoveService {

    /**
     * 通过文件导入数据
     * @param file
     * @return
     */
    Result importDataByFile(MultipartFile file);

    /**
     * 通过数据库导入数据
     * @param blogMoveVO
     * @return
     */
    Result importDataByDB(BlogMoveVO blogMoveVO);
}
