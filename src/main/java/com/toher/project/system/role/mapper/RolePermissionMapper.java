package com.toher.project.system.role.mapper;

import com.toher.framework.mapper.MyMapper;
import com.toher.project.system.role.entity.RolePermission;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/13 10:14
 */
public interface RolePermissionMapper extends MyMapper<RolePermission> {

    @Select("select * from th_sys_role_permission where role_id = #{roleId}")
    @Results({
            @Result(property = "permissionId", column = "permission_id"),
            @Result(property = "roleId", column = "role_id"),
            @Result(property = "menuId", column = "menu_id"),
            //查询关联角色组对象
            @Result(property = "menu",
                    column = "menu_id",
                    one = @One(select = "com.toher.project.system.menu.mapper.MenuMapper.selectByPrimaryKey"))
    })
    List<RolePermission> selectRolePermissionByRoleId(String roleId);
}
