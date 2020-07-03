package com.toher.project.module.goods.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName 商品盒子实体类
 * @Author 廖亭婷
 * @Date 2020/7/3
 */
@Data
@Table(name = "mh_goods_box")
public class GoodsBox {
    //盒子编号
    @Id
    @ApiModelProperty("盒子编号")
    private Integer boxId;
    //系列编号
    @ApiModelProperty("系列编号")
    private String seriesId;
    //盒子名称
    @ApiModelProperty("盒子名称")
    private String boxName;
    //盒子状态（1：售完，0：未售完）
    @ApiModelProperty("盒子状态（1：售完，0：未售完）")
    private Integer boxStatus;

}
