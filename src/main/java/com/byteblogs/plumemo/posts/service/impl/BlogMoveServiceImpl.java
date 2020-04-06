package com.byteblogs.plumemo.posts.service.impl;

import cn.hutool.core.io.FileUtil;
import com.byteblogs.common.base.domain.Result;
import com.byteblogs.common.constant.Constants;
import com.byteblogs.common.enums.ErrorEnum;
import com.byteblogs.common.enums.PostsStatusEnum;
import com.byteblogs.common.util.ExceptionUtil;
import com.byteblogs.plumemo.posts.domain.vo.BlogMoveVO;
import com.byteblogs.plumemo.posts.domain.vo.PostsVO;
import com.byteblogs.plumemo.posts.factory.BlogPlatformFactory;
import com.byteblogs.plumemo.posts.service.BlogMoveService;
import com.byteblogs.plumemo.posts.service.BlogPlatformService;
import com.byteblogs.plumemo.posts.service.PostsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.*;

/**
 * @author: zsg
 * @description:
 * @date: 2020/4/5 17:13
 * @modified:
 */
@Service
@Slf4j
public class BlogMoveServiceImpl implements BlogMoveService {

    @Autowired
    private PostsService postsService;

    /**
     * 通过文件导入数据
     *
     * @param file
     * @return
     */
    @Override
    public Result importDataByFile(MultipartFile file) {
        if (file == null) {
            ExceptionUtil.rollback(ErrorEnum.PARAM_ERROR.getZhMsg(), ErrorEnum.PARAM_ERROR.getCode());
        }

        String originalFilename = file.getOriginalFilename();
        if (StringUtils.isBlank(originalFilename)) {
            ExceptionUtil.rollback(ErrorEnum.PARAM_ERROR.getZhMsg(), ErrorEnum.PARAM_ERROR.getCode());
        }

        String suffix = FileUtil.extName(originalFilename);
        if (!Constants.MARKDOWN_FILE_SUFFIX.equals(suffix)) {
            ExceptionUtil.rollback(ErrorEnum.FILE_TYPE_ERROR.getZhMsg(), ErrorEnum.FILE_TYPE_ERROR.getCode());
        }

        String title = FileUtil.mainName(originalFilename);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            int read;
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } catch (IOException e) {
            ExceptionUtil.rollback(ErrorEnum.IMPORT_FILE_ERROR.getZhMsg(), ErrorEnum.IMPORT_FILE_ERROR.getCode());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    ExceptionUtil.rollback(ErrorEnum.IMPORT_FILE_ERROR.getZhMsg(), ErrorEnum.IMPORT_FILE_ERROR.getCode());
                }
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                ExceptionUtil.rollback(ErrorEnum.IMPORT_FILE_ERROR.getZhMsg(), ErrorEnum.IMPORT_FILE_ERROR.getCode());
            }
        }

        try {
            saveOrUpdatePosts(title, outputStream.toString("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            ExceptionUtil.rollback(ErrorEnum.ERROR.getZhMsg(), ErrorEnum.ERROR.getCode());
        }

        return Result.createWithSuccessMessage();
    }

    @Override
    public Result importDataByDB(BlogMoveVO blogMoveVO) {

        doImportData(blogMoveVO);

        return Result.createWithSuccessMessage();
    }

    /**
     * 执行导入
     *
     * @param blogMoveVO
     */
    private void doImportData(BlogMoveVO blogMoveVO) {

        BlogPlatformService uploadFileService = BlogPlatformFactory.getUploadFileService(blogMoveVO.getBlogType());

        String url = uploadFileService.getUrl(blogMoveVO);
        //连接数据库
        try {
            Class.forName(uploadFileService.getDriver());
        } catch (final ClassNotFoundException e) {
            log.error("连接数据库异常 {}", e.getMessage());
        }
        //测试url中是否包含useSSL字段，没有则添加设该字段且禁用
        if (url.indexOf("?") == -1) {
            url = url + "?useSSL=false";
        } else if (url.indexOf("useSSL=false") == -1 || url.indexOf("useSSL=true") == -1) {
            url = url + "&useSSL=false";
        }

        Connection conn = null;
        Statement stat = null;
        try {
            conn = DriverManager.getConnection(url, blogMoveVO.getUsername(), blogMoveVO.getPassword());
            stat = conn.createStatement();
        } catch (final SQLException e) {
            log.error("数据库解析异常 ", e);
            ExceptionUtil.rollback(ErrorEnum.DATABASE_SQL_PARSE_ERROR);
        }

        try {
            ResultSet resultSet1 = stat.executeQuery(uploadFileService.getCountSql(blogMoveVO));
            int count = 0;
            if (resultSet1.next()) {
                count = resultSet1.getInt(1);
                log.warn("当前总数量 {} ", count);
            }

            int pageIndex = 1;
            int pageSize = 10;
            do {
                ResultSet resultSet = stat.executeQuery(String.format(uploadFileService.getQuerySql(blogMoveVO), pageIndex, pageSize));
                while (resultSet.next()) {
                    String title = resultSet.getString(1);
                    String content = resultSet.getString(2);
                    saveOrUpdatePosts(title, content);
                }

                pageIndex++;
            } while (count >= pageIndex * pageSize);


        } catch (final SQLException e) {
            log.error("释放 Statement 失败 ", e);
            ExceptionUtil.rollback(ErrorEnum.DATABASE_SQL_PARSE_ERROR);
        } finally {
            // 释放资源
            try {
                stat.close();
            } catch (final SQLException e) {
                log.error("释放 Statement 失败 ", e);
                ExceptionUtil.rollback(ErrorEnum.DATABASE_SQL_PARSE_ERROR);
            }
            try {
                conn.close();
            } catch (final SQLException e) {
                log.error("释放 Connection 失败 {}", e.getMessage());
                ExceptionUtil.rollback(ErrorEnum.DATABASE_SQL_PARSE_ERROR);
            }
        }
    }

    /**
     * 更新或者新增文章
     *
     * @param title
     * @param content
     */
    private void saveOrUpdatePosts(String title, String content) {
        PostsVO postsVO = new PostsVO();
        postsVO.setTitle(title);
        postsVO.setContent(content);
        postsVO.setStatus(PostsStatusEnum.DRAFT.getStatus());
        postsVO.setIsPublishByteBlogs(Constants.YES);
        postsService.savePosts(postsVO);
    }

}
