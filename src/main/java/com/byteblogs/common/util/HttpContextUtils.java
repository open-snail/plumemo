package com.byteblogs.common.util;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.Version;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class HttpContextUtils {
	public static HttpServletRequest getHttpServletRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

	/**
	 * 获取当前网络ip
	 */
	public static String getIpAddr(HttpServletRequest request){
		String ipAddress = request.getHeader("x-forwarded-for");
		if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if(ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")){
				//根据网卡取本机配置的IP
				InetAddress inet=null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				ipAddress= inet.getHostAddress();
			}
		}
		//对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if(ipAddress!=null && ipAddress.length()>15){ //"***.***.***.***".length() = 15
			if(ipAddress.indexOf(",")>0){
				ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));
			}
		}
		return ipAddress;
	}

	/**
	 * 获取发起请求的浏览器名称
	 */
	public static String getBrowserName(HttpServletRequest request) {
		try{
			String header = request.getHeader("User-Agent");
			UserAgent userAgent = UserAgent.parseUserAgentString(header);
			Browser browser = userAgent.getBrowser();
			return browser.getName();
		}catch (Exception e){
			return "not found name";
		}
	}

	/**
	 * 获取发起请求的浏览器版本号
	 */
	public static String getBrowserVersion(HttpServletRequest request) {
		try{
			String header = request.getHeader("User-Agent");
			UserAgent userAgent = UserAgent.parseUserAgentString(header);
			Browser browser = userAgent.getBrowser();// 获取浏览器信息
			Version version = browser.getVersion(header);// 获取浏览器版本号
			return version.getVersion();
		}catch (Exception e){
			return "not found version";
		}

	}

	/**
	 * 获取发起请求的操作系统名称
	 */
	public static String getOsName(HttpServletRequest request) {
		try{
			String header = request.getHeader("User-Agent");
			UserAgent userAgent = UserAgent.parseUserAgentString(header);
			OperatingSystem operatingSystem = userAgent.getOperatingSystem();
			return operatingSystem.getName();
		}catch (Exception e){
			return "not found osName";
		}

	}
}