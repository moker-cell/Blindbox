
package com.toher.project.system.role.mapper;


import com.toher.framework.mapper.MyMapper;
import com.toher.project.system.role.entity.Role;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/4 15:19
 */

public interface RoleMapper extends MyMapper<Role> {
    @Select("<script>"
            + "select * from th_sys_role where 1=1 "
            + "<if test='groupRoleId!=null'>"
            + "and group_role_id = #{groupRoleId} "
            + "</if>"
            + "order by create_date desc"
            + "</script>")
    @Results({
            @Result(property = "roleId", column = "role_id"),
            @Result(property = "roleName", column = "role_name"),
            @Result(property = "remarks", column = "remarks"),
            @Result(property = "createDate", column = "create_date"),
            @Result(property = "groupRoleId", column = "group_role_id"),
            //查询关联角色组对象
            @Result(property = "roleGroup",
                    column = "group_role_id",
                    one = @One(select = "com.toher.project.system.role.mapper.RoleGroupMapper.selectByPrimaryKey"))
    })
    List<Role> getObjects(Map<String, Object> map);

    Role getRoleAndPermission(String roleId);
}
