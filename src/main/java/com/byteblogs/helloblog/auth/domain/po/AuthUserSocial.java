package com.byteblogs.helloblog.auth.domain.po;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户表社交信息表 实体类
 * @author nosum
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "hello_blog_auth_user_social")
public class AuthUserSocial extends Model<AuthUserSocial> {

	private static final long serialVersionUID = 1L;

    // columns START

	@TableId(value = "id",type= IdType.AUTO)
	private Long id;

	/**
	 * qq、csdn、wechat、weibo、email等
	 */
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
	@TableField(value = "qr_code")
	private String qrCode;

	/**
	 * 展示类型( 1、显示二维码，2、显示账号，3、跳转链接)
	 */
	@TableField(value = "show_type")
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
	@TableField(value = "create_time")
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@TableField(value = "update_time")
	private LocalDateTime updateTime;

	/**
	 * 是否删除
	 */
	@TableField(value = "is_enabled")
	private Integer isEnabled;
	// columns END

	@Override
	protected Serializable pkVal() {
		return id;
	}
}