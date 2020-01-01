package com.byteblogs.helloblog.log.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 用户行为日志记录表 实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "hello_blog_auth_user_log")
public class AuthUserLog extends Model<AuthUserLog> {

	@TableId(value = "id",type= IdType.AUTO)
	private Long id;

	/**
	 * 记录用户id(游客取系统id：-1)
	 */
	@TableField(value = "user_id")
	private String userId;

	/**
	 * ip地址
	 */
	private String ip;

	/**
	 * 请求的url
	 */
	private String url; 

	/**
	 * 需要记录的参数
	 */
	private String parameter;

	/**
	 * 来自于哪个设备 eg 手机 型号 电脑浏览器
	 */
	private String device; 

	/**
	 * 描述
	 */
	private String description;

	@TableField(value = "create_time")
	private LocalDateTime createTime;

	/**
	 * 执行时间
	 */
	@TableField(value = "run_time")
	private Long runTime;

	/**
	 * 日志类型
	 */
	private String code;

	/**
	 * 浏览器名称
	 */
	@TableField(value = "browser_name")
	private String browserName;
	/**
	 * 浏览器版本号
	 */
	@TableField(value = "browser_version")
	private String browserVersion;
}