package com.toher.project.system.menu.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/10 10:06
 */
@Table(name="th_sys_menu")
public class Menu implements Serializable {

    @Column(insertable = false,updatable = false)
    private Integer Id;
    @Id
    private String menuId;
    private String menuName;
    private String menuUrl;
    private String parentId;
    private String menuIcon;
    private Integer menuType;
    private String permissionCode;
    private Integer menuSort;
    private Boolean menuStatus;
    private String menuRemarks;
    private String menuTarget;
    private Date addTime;
    @JsonProperty("children")
    @JsonInclude(value= JsonInclude.Include.NON_NULL)
    private List<Menu> menus;
    @Transient
    private String DT_RowId;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    public Integer getMenuType() {
        return menuType;
    }

    public void setMenuType(Integer menuType) {
        this.menuType = menuType;
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    public Integer getMenuSort() {
        return menuSort;
    }

    public void setMenuSort(Integer menuSort) {
        this.menuSort = menuSort;
    }

    public Boolean getMenuStatus() {
        return menuStatus;
    }

    public void setMenuStatus(Boolean menuStatus) {
        this.menuStatus = menuStatus;
    }

    public String getMenuRemarks() {
        return menuRemarks;
    }

    public void setMenuRemarks(String menuRemarks) {
        this.menuRemarks = menuRemarks;
    }

    public String getMenuTarget() {
        return menuTarget;
    }

    public void setMenuTarget(String menuTarget) {
        this.menuTarget = menuTarget;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
        //在mybatis 赋值的时候调用setChildren的set 方法
//        setChildren(menus);
    }

    public String getDT_RowId() {
        return DT_RowId;
    }

    public void setDT_RowId(String DT_RowId) {
        this.DT_RowId = DT_RowId;
    }

//    public List<Menu> getChildren() {
//        return children;
//    }
//
//    public void setChildren(List<Menu> children) {
//        this.children = children;
//    }
}
