package com.toher.project.common;

import com.toher.common.utils.shiro.ShiroUtils;
import com.toher.project.system.user.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/22 11:48
 */
public class BaseController {

    @Value("${toher.system.sessionUser}")
    private String SESSION_USER;

    //设置通用的日志方法
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**调用ShiroUtils 获取认证用户信息 **/
    public static User getAuthUser(){
        return ShiroUtils.getAuthUser();
    }

    /**调用ShiroUtils 修改认证用户信息 **/
    public static void setAuthUser(User user){
        ShiroUtils.setAuthUser(user);
    }

}
