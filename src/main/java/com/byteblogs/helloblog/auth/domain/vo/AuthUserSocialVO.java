package com.byteblogs.helloblog.auth.domain.vo;

import com.byteblogs.common.base.domain.vo.BaseVO;
import com.byteblogs.common.validator.annotion.IntegerNotNull;
import com.byteblogs.common.validator.annotion.NotBlank;
import com.byteblogs.helloblog.auth.domain.validator.InsertSocial;
import com.byteblogs.helloblog.auth.domain.validator.UpdateSocial;
import lombok.Data;
import lombok.experimental.Accessors;
import java.time.LocalDateTime;

/**
 * 用户表社交信息表 实体类
 * @author nosum
 */
@Data
@Accessors(chain = true)
public class AuthUserSocialVO extends BaseVO<AuthUserSocialVO> {

    // columns START
	@IntegerNotNull(groups = {UpdateSocial.class})
	private Long id;

	/**
	 * qq、csdn、wechat、weibo、email等
	 */
	@NotBlank(groups = {InsertSocial.class})
	private String code;

	/**
	 * 社交账户
	 */
	private String account; 

	/**
	 * 图标
	 */
	private String icon; 

	/**
	 * 二维码
	 */
	private String qrCode;

	/**
	 * 展示类型( 1、显示二维码，2、显示账号，3、跳转链接)
	 */
	private Integer showType;

	/**
	 * 跳转链接
	 */
	private String url; 

	/**
	 * 备注
	 */
	private String remark; 

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	private LocalDateTime updateTime;

	/**
	 * 是否删除
	 */
	private Integer isEnabled;
	// columns END
}