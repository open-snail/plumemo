package com.byteblogs.helloblog.log.domain.vo;

import com.byteblogs.common.base.domain.vo.BaseVO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 用户行为日志记录表 实体类
 */
@Data
@Accessors(chain = true)
public class AuthUserLogVO extends BaseVO<AuthUserLogVO> {

	private static final long serialVersionUID = 1L;

    // columns START


	private Long id; 

	/**
	 * 记录用户id(游客取系统id：-1)
	 */

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

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

	/**
	 * 执行时间
	 */
	private Long runTime;

	/**
	 * 日志类型
	 */
	private String code;
	private String codeName;

	/**
	 * 浏览器名称
	 */
	private String browserName;
	/**
	 * 浏览器版本号
	 */
	private String browserVersion;

	/**
	 * 统计个数
	 */
	private Integer count;

	private Integer userTotal;

	private Integer viewTotal;

	private String title;

	private LocalDateTime startTime;

	private LocalDateTime endTime;

	private Integer index;

	private String type;
}