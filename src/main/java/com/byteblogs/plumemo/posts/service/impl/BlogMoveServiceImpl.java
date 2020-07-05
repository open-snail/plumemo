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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

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
     *
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

        String title = null;
        String date = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
            if (!reader.ready()) {
                ExceptionUtil.rollback(ErrorEnum.IMPORT_FILE_ERROR.getZhMsg(), ErrorEnum.IMPORT_FILE_ERROR.getCode());
            }

            boolean isExistedTitle = false;
            boolean isExistedDate = false;
            String line;
            while ((line = reader.readLine()) != null) {
                if (StringUtils.isNotBlank(line)) {
                    if (line.contains("title:") && !isExistedTitle) {
                        title = line.replaceFirst("title:", "").trim();
                        isExistedTitle = true;
                    }

                    if (line.contains("date:") && !isExistedDate) {
                        date = line.replaceFirst("date:", "").trim();
                        isExistedDate = true;
                    }
                }
                stringBuilder.append(line + "\n");
            }

            int begin = stringBuilder.indexOf("---");
            int end = stringBuilder.indexOf("---", begin + 3);

            if (begin != -1 && end != -1) {
                stringBuilder.replace(begin, end + 3, "");
            }

        } catch (IOException e) {
            ExceptionUtil.rollback(ErrorEnum.IMPORT_FILE_ERROR.getZhMsg(), ErrorEnum.IMPORT_FILE_ERROR.getCode());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                ExceptionUtil.rollback(ErrorEnum.IMPORT_FILE_ERROR.getZhMsg(), ErrorEnum.IMPORT_FILE_ERROR.getCode());
            }
        }

        LocalDateTime dateTime = resolveDate(date);
        String content = stringBuilder.toString();
        if (StringUtils.isBlank(title)) {
            title = FileUtil.mainName(originalFilename);
        }

        saveOrUpdatePosts(title, content, dateTime);

        return Result.createWithSuccessMessage();
    }

    private LocalDateTime resolveDate(String date) {
        LocalDateTime dateTime = null;
        try {
            dateTime = LocalDateTime.of(LocalDate.parse(date), LocalTime.MIN);
        } catch (Exception e) {
            try {
                dateTime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } catch (Exception e1) {
                log.error("文件导入时间格式失败，格式为：2020-03-01或2020-03-01 00:00:00");
            }
        }

        return dateTime;
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

            int pageIndex = 0;
            int pageSize = 10;
            do {
                ResultSet resultSet = stat.executeQuery(String.format(uploadFileService.getQuerySql(blogMoveVO), pageIndex, pageSize));
                while (resultSet.next()) {
                    String title = resultSet.getString(1);
                    String content = resultSet.getString(2);
                    LocalDateTime localDateTime = null;
                    try {
                        Timestamp date = resultSet.getTimestamp(3, Calendar.getInstance());
                        Instant instant = date.toInstant();
                        ZoneId zoneId = ZoneId.systemDefault();
                        localDateTime = instant.atZone(zoneId).toLocalDateTime();
                        saveOrUpdatePosts(title, content, localDateTime);
                    } catch (Exception e) {
                        log.error("时间格式解析失败 ", e);
                    }
                }

                pageIndex += pageSize;
            } while (count >= pageIndex);


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
    private void saveOrUpdatePosts(String title, String content, LocalDateTime dateTime) {
        PostsVO postsVO = new PostsVO();
        postsVO.setTitle(title);
        postsVO.setContent(content);
        postsVO.setStatus(PostsStatusEnum.DRAFT.getStatus());
        postsVO.setIsPublishByteBlogs(Constants.NO);

        if (dateTime != null) {
            postsVO.setCreateTime(dateTime);
        }
        postsService.savePosts(postsVO);
    }

}
