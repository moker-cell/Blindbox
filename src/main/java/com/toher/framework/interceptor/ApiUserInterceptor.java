package com.toher.framework.interceptor;

import com.toher.common.constants.ConstantsCommon;
import com.toher.common.dto.Result;
import com.toher.common.utils.GsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 接口用户拦截
 * @author lzh
 * @date 2019/9/7 10:09
 */
@Component
public class ApiUserInterceptor implements HandlerInterceptor {

    /*@Resource
    UserWechatService userWechatServiceImpl;

    @Resource
    UserLoginService userLoginServiceImpl;*/

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 设置编码格式
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        // 获取请求头信息，安卓手机不能有带有下划线的头部
        String token = request.getHeader(ConstantsCommon.HEADER_KEY);
        if(StringUtils.isBlank(token)) {
            response.getWriter().print(GsonUtil.GsonString(Result.error("Token失效")));
            return false;
        }
        /*// 用户校验
        UserWechat userWechat1 = new UserWechat();
        userWechat1.setLoginUserId(token);
        UserWechat userWechat = userWechatServiceImpl.findObjectByEntity(userWechat1);
        UserLogin userLogin = userLoginServiceImpl.findObjectByPrimaryKey(userWechat.getLoginUserId());
        if(userLogin == null){
            response.getWriter().print(GsonUtil.GsonString(Result.error("未知用户")));
            return false;
        }

        request.setAttribute(ConstantsCommon.HEADER_KEY,userLogin);*/
        return true;
    }

}
