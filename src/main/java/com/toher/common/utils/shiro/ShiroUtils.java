package com.toher.common.utils.shiro;

import com.toher.common.utils.BeanUtils;
import com.toher.framework.shiro.realm.ToherFrameworkRealm;
import com.toher.project.system.user.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;

/**
 * shiro 工具类
 * @author 李怀明
 */
public class ShiroUtils {

    public static Subject getSubjct() {
        return SecurityUtils.getSubject();
    }

    public static Session getSession() {
        return SecurityUtils.getSubject().getSession();
    }

    public static void logout() {
        getSubjct().logout();
    }

    /**
     * 获取认证用户信息
     * @return
     */
    public static User getAuthUser() {
        User user = new User();
        BeanUtils.copyBeanProp(user, getSubjct().getPrincipal());
        return user;
    }

    /**
     * 重新设置认证用户信息
     * @param user
     */
    public static void setAuthUser(User user) {
        Subject subject = getSubjct();
        PrincipalCollection principalCollection = subject.getPrincipals();
        String realmName = principalCollection.getRealmNames().iterator().next();
        PrincipalCollection newPrincipalCollection = new SimplePrincipalCollection(user, realmName);
        // 重新加载Principal
        subject.runAs(newPrincipalCollection);
    }

    /**
     * 清除认证缓存
     */
    public static void clearCachedAuthorizationInfo() {
        RealmSecurityManager rsm = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        ToherFrameworkRealm realm = (ToherFrameworkRealm) rsm.getRealms().iterator().next();
        realm.clearCachedAuthorizationInfo();
    }

    /**
     * 获取用户主机
     * @return
     */
    public static String getHost() {
        return getSubjct().getSession().getHost();
    }

    /**
     * 获取SessionID
     * @return
     */
    public static String getSessionId() {
        return String.valueOf(getSubjct().getSession().getId());
    }
}
