package com.toher.project.module.operlog.entity;

import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/11 18:08
 */
@Table(name = "th_oper_log")
public class OperLog implements Serializable {

    @Id
    @KeySql(useGeneratedKeys = true)
    @Column(insertable = false, updatable = false)
    private Integer id;
    //请求类型
    private String action;
    //操作模块
    private String title;
    //请求方法
    private String method;
    //请求地址
    private String operUrl;
    //操作地址
    private String operIp;
    //操作地点
    private String operLocation;
    //请求参数
    private String operParam;
    //操作人员
    private String operName;
    //状态
    private String status;
    //错误消息
    private String errorMsg;
    //操作时间
    private java.util.Date operTime;


    public String getAction() {
        return this.action;
    }

    public void setAction(String action) {

        this.action = action;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String method) {

        this.method = method;
    }

    public String getOperUrl() {
        return this.operUrl;
    }

    public void setOperUrl(String operUrl) {

        this.operUrl = operUrl;
    }

    public String getOperIp() {
        return this.operIp;
    }

    public void setOperIp(String operIp) {

        this.operIp = operIp;
    }

    public String getOperLocation() {
        return this.operLocation;
    }

    public void setOperLocation(String operLocation) {

        this.operLocation = operLocation;
    }

    public String getOperParam() {
        return this.operParam;
    }

    public void setOperParam(String operParam) {

        this.operParam = operParam;
    }

    public String getOperName() {
        return this.operName;
    }

    public void setOperName(String operName) {

        this.operName = operName;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {

        this.status = status;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public void setErrorMsg(String errorMsg) {

        this.errorMsg = errorMsg;
    }

    public java.util.Date getOperTime() {
        return this.operTime;
    }

    public void setOperTime(java.util.Date operTime) {

        this.operTime = operTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
