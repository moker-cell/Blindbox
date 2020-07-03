package com.toher.project.system.role.entity;

import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/6 16:39
 */
@Table(name="th_sys_role_group")
public class RoleGroup implements Serializable {

    @Id
    @KeySql(useGeneratedKeys = true)
    @Column(insertable = false, updatable = false)
    private Integer groupRoleId;

    private String groupName;

    private String remarks;

    private Date createDate;

    private List<Role> roles;

    public Integer getGroupRoleId() {
        return groupRoleId;
    }

    public void setGroupRoleId(Integer groupRoleId) {
        this.groupRoleId = groupRoleId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
