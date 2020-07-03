
package com.toher.project.system.user.service;

import com.toher.common.service.BaseService;
import com.toher.project.system.user.entity.User;
import com.toher.project.system.user.entity.UserRole;

/**
 * @author liuzh
 * @since 2016-01-31 21:42
 */

public interface UserService extends BaseService<User,String> {

    User login(String username,String password);

    int insertUserRole(UserRole userRole);

    int deleteUserRole(String userId);

    /**
     * 判断用户名或手机号是否唯一
     * @param username
     * @param phone
     * @return
     */
    int selectCountByOnly(String username, String phone);

    /**
     * 根据用户名或手机号查询唯一
     * @param username
     * @param phone
     * @return
     */
    User selectOneByOnly(String username, String phone);
}
