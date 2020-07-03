package com.toher.project.module.goods.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName 商品品牌实体类
 * @Author 廖亭婷
 * @Date 2020/7/3
 */
@Data
@Table(name = "mh_goods_brand")
public class GoodsBrand {
    //品牌编号
    @Id
    @ApiModelProperty("品牌编号")
    private String brandId;
    //品牌名称
    @ApiModelProperty("品牌名称")
    private String brandName;
    //品牌logo
    @ApiModelProperty("品牌logo")
    private String brandLogo;
    //品牌说明
    @ApiModelProperty("品牌说明")
    private String brandInstructions;

}
