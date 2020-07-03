package com.toher.project.module.job.utils;


import com.toher.common.utils.spring.SpringContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * spring工具类通过beanName获取实体类 反射置行对应的方法 执行定时任务
 *
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/20 11:06
 */
public class ScheduleCallable implements Callable<Object> {
    private Object target;
    private Method method;
    private String params;

    public ScheduleCallable(String beanName, String methodName, String params) throws NoSuchMethodException, SecurityException {
        this.target = SpringContextHolder.getBean(beanName);
        this.params = params;

        if (StringUtils.isNotBlank(params)) {
            this.method = target.getClass().getDeclaredMethod(methodName, String.class);
        } else {
            this.method = target.getClass().getDeclaredMethod(methodName);
        }
    }

    @Override
    public Object call() throws Exception {
        Object call= null;
        try {
            ReflectionUtils.makeAccessible(method);
            if (StringUtils.isNotEmpty(params)) {
                call = method.invoke(target, params);
            } else {
                call = method.invoke(target);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return call;
    }
}
