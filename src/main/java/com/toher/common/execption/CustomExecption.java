package com.toher.common.execption;

import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/25 17:03
 */
@ControllerAdvice
public class CustomExecption {
    /**
     * 全局权限错误控制
     *
     * @param e
     * @return
     */
    @ExceptionHandler({UnauthorizedException.class})
    public String unauthenticatedException(UnauthorizedException e, HttpServletRequest request) {
        request.setAttribute("code", "UnauthenticatedException");
        request.setAttribute("message", e.toString());
        request.setAttribute("title", "权限不足");
        //判断如果是ajax请求 返回JSON 而非错误页
        if (request.getHeader("x-requested-with") != null && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
            return "forward:/common/error.json";
        }
        return "error/error";
    }

    /**
     * 全局Exception 错误处理
     *
     * @param ex
     * @return
     */
    @ExceptionHandler({Exception.class})
    public String Exception(Exception ex ,HttpServletRequest request) {
        ex.printStackTrace();
        request.setAttribute("code", "500");
        request.setAttribute("message", ex.toString());
        request.setAttribute("title", "系统错误");
        //判断如果是ajax请求 返回JSON 而非错误页
        if (request.getHeader("x-requested-with") != null && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
            return "forward:/common/error.json";
        }
        return "error/error";
    }
}
