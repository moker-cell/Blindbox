package com.toher.framework.interceptor;

import com.toher.common.constants.ConstantsCommon;
import com.toher.framework.configurer.ReadApplicationYmlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 拦截器定义的两种方式
 * 1、类要实现 Spring 的 HandlerInterceptor 接口
 * 2、类要继承 实现 HandlerInterceptor 接口的类，例如已经提供的实现了 HandlerInterceptor 接口的抽象类 HandlerInterceptorAdapter
 * */
/**
 * 接口跨域处理
 * @Author: lzh
 * @Date: 2020/2/19 10:43
 */
@Component
public class OriginInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private ReadApplicationYmlUtil readApplicationYmlUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        //白名单，允许跨域的网址
        List<String> whiteList = readApplicationYmlUtil.getWhiteList();
        String myOrigin = request.getHeader("origin");
        System.out.println(myOrigin);
        boolean isValid = false;
        for( String ip : whiteList ) {
            if( myOrigin != null && myOrigin.equals(ip) ){
                isValid = true;
                break;
            }
        }

        //axios 自定义头 出现的跨域问题
        response.setHeader("Access-Control-Allow-Origin", isValid ? myOrigin : whiteList.get(0));
        response.setHeader("Access-Control-Allow-Methods", "PUT,POST,GET");
        response.setHeader("Access-Control-Allow-Headers", String.format("x-requested-with,openid,appid,content-type,%s", ConstantsCommon.HEADER_KEY));
        response.setHeader("Access-Control-Allow-Credentials","true");
        return true;
    }

}
