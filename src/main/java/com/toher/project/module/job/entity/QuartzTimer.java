package com.toher.project.module.job.entity;

import com.toher.common.dto.BaseEntity;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/18 16:57
 */
@Table(name = "th_sys_job")
public class QuartzTimer extends BaseEntity {
    @Id
    @KeySql(useGeneratedKeys = true)
    @Column(insertable = false, updatable = false)
    private Integer jobId;
    private String jobName;
    private String jobGroup;
    private String methodName;
    private String methodParams;
    private String cronExpression;
    private String timeExplain;
    private Boolean status;


    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodParams() {
        return methodParams;
    }

    public void setMethodParams(String methodParams) {
        this.methodParams = methodParams;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getTimeExplain() {
        return timeExplain;
    }

    public void setTimeExplain(String timeExplain) {
        this.timeExplain = timeExplain;
    }

    public Boolean getStatus() { return status; }

    public void setStatus(Boolean status) { this.status = status; }

}
