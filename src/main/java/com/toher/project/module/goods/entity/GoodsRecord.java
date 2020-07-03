package com.toher.project.module.goods.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName 摇一摇使用记录实体类
 * @Author 廖亭婷
 * @Date 2020/7/3
 */
@Data
@Table(name = "mh_goods_record")
public class GoodsRecord {
    //使用编号
    @Id
    @ApiModelProperty("使用编号")
    private Integer recordId;
    //用户编号
    @ApiModelProperty("用户编号")
    private String userId;
    //格子位置
    @ApiModelProperty("格子位置")
    private Integer orderLocation;
    //盒子编号
    @ApiModelProperty("盒子编号")
    private Integer boxId;

}
