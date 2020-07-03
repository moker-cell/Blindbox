package com.toher.project.system.dict.entity;

import com.toher.common.dto.BaseEntity;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/20 16:03
 */
@Table(name="th_sys_dict_type")
public class DictType extends BaseEntity {

    /** 字典主键 */
    @Id
    @KeySql(useGeneratedKeys = true)
    @Column(insertable = false, updatable = false)
    private Integer dictId;

    /** 字典名称 */
    private String dictName;

    /** 字典类型 */
    private String dictType;

    /** 状态（true正常 false停用） */
    private Boolean status;

    public Integer getDictId() {
        return dictId;
    }

    public void setDictId(Integer dictId) {
        this.dictId = dictId;
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
