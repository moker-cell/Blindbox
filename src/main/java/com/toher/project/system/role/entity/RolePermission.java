package com.toher.project.system.role.entity;

import com.toher.project.system.menu.entity.Menu;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/13 10:04
 */
@Table(name="th_sys_role_permission")
public class RolePermission implements Serializable {

    @Id
    @Column(insertable = false, updatable = false)
    @KeySql(useGeneratedKeys = true)
    private Integer permissionId;
    private String roleId;
    private String menuId;
    private Menu menu;

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }
}

