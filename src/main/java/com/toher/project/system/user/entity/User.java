package com.toher.project.system.user.entity;

import com.github.crab2died.annotation.ExcelField;
import com.toher.common.execl.BooleanStatusConverter;
import com.toher.common.execl.DateByYYYYMMDDConverter;
import com.toher.project.system.role.entity.Role;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Table(name = "th_sys_user")
public class User implements Serializable {

    @Column(insertable = false, updatable = false)
    private Integer id;
    @Id
    private String userId;
    //用户名
    @ExcelField(title = "登录名", order = 1)
    private String username;
    //密码
    private String password;
    //真实姓名
    @ExcelField(title = "姓名", order = 2)
    private String realName;
    //登录时间
    @ExcelField(title = "最后登录时间", order = 6)
    private String lastLogin;
    //登录IP
    @ExcelField(title = "最后登录IP", order = 7)
    private String ip;
    //邮箱
    @ExcelField(title = "邮箱", order = 3)
    private String email;
    //手机号码
    @ExcelField(title = "手机", order = 4)
    private String phone;
    //启用禁用
    @ExcelField(title = "是否启用", order = 5, writeConverter = BooleanStatusConverter.class)
    private Boolean status;
    //备注
    private String remarks;
    //用户头像
    private String avatar;
    //创建时间
    @ExcelField(title = "创建时间", order = 8, writeConverter = DateByYYYYMMDDConverter.class)
    private Date createDate;
    //是否开发者
    private Boolean isDeveloper;
    //角色集合
    private List<Role> roles;

    @Transient
    private List<String> roleIds;
    @Transient
    private String DT_RowId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getDeveloper() {
        return isDeveloper;
    }

    public void setDeveloper(Boolean developer) {
        isDeveloper = developer;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
        //在mybatis 赋值的时候调用DT_RowId 的set 方法
        setDT_RowId(userId);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin == null ? null : lastLogin.trim();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDT_RowId() {
        return DT_RowId;
    }

    public void setDT_RowId(String DT_RowId) {
        this.DT_RowId = DT_RowId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Boolean getIsDeveloper() {
        return isDeveloper;
    }

    public void setIsDeveloper(Boolean isDeveloper) {
        this.isDeveloper = isDeveloper;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<String> getRoleIds() {
        //获取List<String> 用户角色组ID集合 作用于option选中
        List<String> ids = new ArrayList();
        if(roles!=null && roles.size()>0) {
            for (Role r : roles) {
                ids.add(r.getRoleId());
            }
        }
        return ids;
    }

    public void setRoleIds(List<String> roleIds) {
        this.roleIds = roleIds;
    }

}
