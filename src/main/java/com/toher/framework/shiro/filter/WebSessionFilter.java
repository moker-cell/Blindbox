package com.toher.framework.shiro.filter;

import org.apache.shiro.web.filter.authc.UserFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义拦截器 判断Session过期
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/15 10:18
 */

public class WebSessionFilter extends UserFilter {

    protected static final Logger logger = LoggerFactory.getLogger(WebSessionFilter.class);


//    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
//        ShiroHttpServletRequest shiroRequest = (ShiroHttpServletRequest)request;
//        String url = getPathWithinApplication(request);
//        Subject subject = getSubject(request,response);
//        logger.info("当前用户正在访问的 url => " + url);
//        logger.info("subject.isPermitted(url);"+subject.isPermitted(url));
//        HttpSession session = shiroRequest.getSession();
//
//
//        String userAgent = shiroRequest.getHeader("User-Agent");
//        String sid = session.getId();
//        String a = rrr.getRequestedSessionId();
//        String b = rrr.getQueryString();
//        String c = rrr.getPathInfo();
//        Cookie d[] = rrr.getCookies();
//
//        //是APP访问
//        if (userAgent.indexOf("Android-APP") >= 0 || userAgent.indexOf("IOS-APP") >= 0) {
//
//            response.getWriter().write("{\"code\":\"101\",\"message\":\"token已失效，请重新登录\"}");
//            response.getWriter().flush();
//            response.getWriter().close();
//        }
//        else {//是Web访问
//            super.redirectToLogin (request, response);
//            //super.isLoginRequest(request,response)
//        }
//
//        return false;
//    }


}

