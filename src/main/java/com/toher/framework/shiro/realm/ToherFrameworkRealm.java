/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toher.framework.shiro.realm;


import com.toher.project.system.menu.entity.Menu;
import com.toher.project.system.role.entity.Role;
import com.toher.project.system.role.entity.RolePermission;
import com.toher.project.system.role.service.RoleService;
import com.toher.project.system.user.entity.User;
import com.toher.project.system.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author 李怀明 2018-01-23
 */
@Component
public class ToherFrameworkRealm extends AuthorizingRealm {

    @Value("${toher.system.sessionUser}")
    private String SESSION_USER;
    @Value("${toher.system.salt}")
    private String SALT;

    @Resource
    private UserService userServiceImpl;
    @Resource
    private RoleService roleServiceImpl;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {
        Set<String> roles = new HashSet();
        Set<String> permissions = new HashSet();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        //认证处返回的是User对象故此处 可以直接采用User接收
        User user = (User) pc.getPrimaryPrincipal();
        
        //判断是否是开发者，开发者不需要判断权限，拥有最高权限
        if (user.getIsDeveloper() == false) {
            User userRole = userServiceImpl.findObjectByPrimaryKey(user.getUserId());
            //循环用户角色，设置用户角色
            for (Role role : userRole.getRoles()) {
                roles.add(role.getRoleId());
                //通过用户角色ID 查询角色的所有权限编码
                Role r = roleServiceImpl.getRoleAndPermission(role.getRoleId());
                List<RolePermission> rolePermissions = r.getRolePermissions();
                System.out.println("rolePermissions:"+rolePermissions.size());
                for (RolePermission p : rolePermissions) {
                    Menu menu = p.getMenu();
                    if(menu!=null){
                        //如果权限编码为NULL 不添加权限列表
                        String permissionCode = menu.getPermissionCode();
                        if(StringUtils.isNotEmpty(permissionCode)){
                            permissions.add(permissionCode);
                        }
                    }
                }
            }
        } else {
            roles.add("developer");
            permissions.add("*");
        }
        System.out.println(roles.toString());
        System.out.println(permissions.toString());
        authorizationInfo.setRoles(roles);
        authorizationInfo.setStringPermissions(permissions);

        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken at) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) at;
        String username = token.getUsername();
        String password = new String(token.getPassword());
        String passwd = new SimpleHash("SHA-1", SALT, password).toString();
        System.out.println("passwd:" + passwd);
        User user = userServiceImpl.login(username,passwd);
        if (user != null) {
            if (user.getStatus() == false) {
                throw new LockedAccountException("该已帐号禁止登录");
            }
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(user, password, getName());
            this.setSession(SESSION_USER, user);
            return simpleAuthenticationInfo;
        }
        return null;
    }
    
    /**
     * 清理缓存权限
     */
    public void clearCachedAuthorizationInfo(){
        this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
    }

    /**
     * 将一些数据放到ShiroSession中,以便于其它地方使用
     * 比如Controller,使用时直接用HttpSession.getAttribute(key)就可以取到
     */
    private void setSession(Object key, Object value) {
        Subject currentUser = SecurityUtils.getSubject();
        if (null != currentUser) {
            Session session = currentUser.getSession();
            System.out.println("Session默认超时时间为[" + session.getTimeout() + "]毫秒");
            if (null != session) {
                session.setAttribute(key, value);
            }
        }
    }

}
