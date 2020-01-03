package com.byteblogs.system.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.byteblogs.common.annotation.OperateLog;
import com.byteblogs.common.constant.Constants;
import com.byteblogs.common.enums.OperateEnum;
import com.byteblogs.common.util.HttpContextUtils;
import com.byteblogs.common.util.JsonUtil;
import com.byteblogs.helloblog.auth.domain.po.AuthUser;
import com.byteblogs.helloblog.log.domain.vo.AuthUserLogVO;
import com.byteblogs.system.sync.LogSyncTask;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.Map;
import java.util.Optional;

@Aspect
@Component
public class LogAspect {
    @Autowired
    private LogSyncTask logSyncTask;
    @Pointcut(value = "@annotation(com.byteblogs.common.annotation.OperateLog)")
    public void logsPointCut() {}// 配置切点

    /**
     * 配置环绕通知
     */
    @Around("logsPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis(); // 开始时间
        Object result = point.proceed(); // 执行方法
        long time = System.currentTimeMillis() - beginTime;// 结束时间
        this.saveLog(point, time);
        return result;
    }

    private void saveLog(ProceedingJoinPoint joinPoint, long time){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        AuthUserLogVO authUserLogVO = new AuthUserLogVO();
        OperateLog operateLog = method.getAnnotation(OperateLog.class);
        if (operateLog != null) {
            authUserLogVO.setDescription(operateLog.module());//  获取注解上的描述
            authUserLogVO.setCode(operateLog.code().getCode());// 获取主街上定义的类型
        }

        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        JSONObject parameter =new JSONObject();// 保存参数
        if (authUserLogVO.getCode().equals(OperateEnum.GET_POSTS_DETAIL.getCode())){
            Map<String,String> pathVariables = (Map<String,String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            for (Map.Entry<String,String> map: pathVariables.entrySet()) {
                parameter.put(map.getKey(),map.getValue());
            }
        }else{
            // 请求的参数
            Enumeration<String> enums = request.getParameterNames();
            while(enums.hasMoreElements()) {
                String paramName = enums.nextElement();
                String paramValue = request.getParameter(String.valueOf(paramName));
                parameter.put(paramName,paramValue);
            }
        }

        authUserLogVO.setParameter(parameter.toJSONString());
        authUserLogVO.setIp(HttpContextUtils.getIpAddr(request));
        authUserLogVO.setUrl(request.getRequestURI());
        authUserLogVO.setDevice(HttpContextUtils.getOsName(request));
        authUserLogVO.setBrowserName(HttpContextUtils.getBrowserName(request));
        authUserLogVO.setBrowserVersion(HttpContextUtils.getBrowserVersion(request));

        // 取得用户信息
        String token = Optional.ofNullable(request.getHeader(Constants.AUTHENTICATION)).orElse(null);
        if (!StringUtils.isBlank(token)) {
            AuthUser authUser= JsonUtil.parseObject(JWT.decode(token).getAudience().get(0), AuthUser.class);
            authUserLogVO.setUserId(authUser.getId().toString());
        }else{
            authUserLogVO.setUserId(Constants.DEFAULT_USER_ID);
        }
        // 统计时间
        authUserLogVO.setRunTime(time);
        authUserLogVO.setCreateTime(LocalDateTime.now());
        // 保存系统日志
        logSyncTask.addLog(authUserLogVO);
    }
}