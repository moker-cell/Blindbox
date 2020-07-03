
package com.toher.project.system.role.service;


import com.toher.common.service.BaseService;
import com.toher.project.system.role.entity.Role;
import com.toher.project.system.role.entity.RolePermission;

import java.util.List;

/**
 * @author liuzh
 * @since 2016-01-31 21:42
 */

public interface RoleService extends BaseService<Role,String> {

    Role getRoleAndPermission(String roleId);

    int savePermission(List<RolePermission> rolePermissions);


}
