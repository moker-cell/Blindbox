package com.toher.framework.aspectj;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toher.common.utils.AddressUtils;
import com.toher.common.utils.shiro.ShiroUtils;
import com.toher.framework.annotation.Log;
import com.toher.project.module.operlog.entity.OperLog;
import com.toher.project.module.operlog.service.OperLogService;
import com.toher.project.system.user.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;

import static com.toher.common.utils.spring.SpringContextHolder.getRequest;


/**
 * 操作日志记录处理
 *
 * @author 李怀明
 */
@Aspect
@Component
@EnableAsync
public class LogAspect {

    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);
    
    @Autowired
    private OperLogService operLogServiceImpl;
    
    // 配置织入点
    @Pointcut("@annotation(com.toher.framework.annotation.Log)")
    public void logPointCut() {
    }

    /**
     * 前置通知 用于拦截操作
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "logPointCut()")
    public void doBefore(JoinPoint joinPoint) {
        handleLog(joinPoint, null);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfter(JoinPoint joinPoint, Exception e) {
        handleLog(joinPoint, e);
    }

    @Async
    protected void handleLog(final JoinPoint joinPoint, final Exception e) {
        try {
            // 获得注解
            Log controllerLog = getAnnotationLog(joinPoint);
            if (controllerLog == null) {
                return;
            }
            // 获取当前的用户
            User currentUser = ShiroUtils.getAuthUser();
            // *========数据库日志=========*//
            OperLog operLog = new OperLog();
            operLog.setStatus("success");
            // 请求的地址
            String ip = ShiroUtils.getHost();
            if(ip.equals("0:0:0:0:0:0:0:1")){
                ip = "127.0.0.1";
            }
            operLog.setOperIp(ip);
            // 操作地点
            operLog.setOperLocation(AddressUtils.getRealAddressByIP(ip));
            HttpServletRequest request = getRequest();
            operLog.setOperUrl(request.getRequestURI());
            if (currentUser != null) {
                operLog.setOperName(currentUser.getUsername());
            }
            
            if (e != null) {
                operLog.setStatus("error");
                operLog.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 2000));
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operLog.setMethod(className + "." + methodName + "()");
            // 处理设置注解上的参数
            getControllerMethodDescription(controllerLog, operLog);
            // 保存数据库
            operLogServiceImpl.saveObjectSelective(operLog);
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("==前置通知异常==");
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     * @param log annotation  获取注解的值
     * @param operLog 日志对象
     * @return 方法描述
     * @throws Exception
     */
    public void getControllerMethodDescription(Log log, OperLog operLog) throws Exception {
        // 设置action动作
        operLog.setAction(log.action());
        // 设置标题
        operLog.setTitle(log.title());
        // 是否需要保存request，参数和值
        if (log.isSaveRequestData()) {
            // 获取参数的信息，传入到数据库中。
            setRequestValue(operLog);
        }
    }

    /**
     * 获取请求的参数，放到log中
     * @param operLog
     */
    private void setRequestValue(OperLog operLog) {
        HttpServletRequest request = getRequest();
        Map<String, String[]> map = request.getParameterMap();
        ObjectMapper mapper = new ObjectMapper();
        String params = null;
        try {
            params = mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if(!StringUtils.isBlank(params)) {
            operLog.setOperParam(params);
        }
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private Log getAnnotationLog(JoinPoint joinPoint) throws Exception {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(Log.class);
        }
        return null;
    }
}
