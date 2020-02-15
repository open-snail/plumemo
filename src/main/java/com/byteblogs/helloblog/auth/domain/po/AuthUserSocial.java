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
	 * 内容
	 */
	@TableField(value = "content")
	private String content;

	/**
	 * 展示类型( 1、图片，2、文本，3、链接)
	 */
	@TableField(value = "show_type")
	private Integer showType;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 图标
	 */
	private String icon;

	/**
	 * 是否启用
	 */
	@TableField(value = "is_enabled")
	private Integer isEnabled;

	/**
	 * 是否主页社交信息
	 */
	@TableField(value = "is_home")
	private Integer isHome;

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
	// columns END

	@Override
	protected Serializable pkVal() {
		return id;
	}
}