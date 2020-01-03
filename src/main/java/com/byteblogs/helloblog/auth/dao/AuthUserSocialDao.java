package com.byteblogs.helloblog.auth.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byteblogs.common.base.dao.BaseDao;
import com.byteblogs.helloblog.auth.domain.po.AuthUserSocial;
import com.byteblogs.helloblog.auth.domain.vo.AuthUserSocialVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 用户表社交信息表:数据层
 * @author generator
 * @date 2020-01-01 17:34:27
 * @since 1.0
 */
public interface AuthUserSocialDao extends BaseDao<AuthUserSocial> {

    List<AuthUserSocialVO> selectSocialList(@Param("page") Page<AuthUserSocial> page, @Param("condition") AuthUserSocialVO authUserSocialVO);

}
