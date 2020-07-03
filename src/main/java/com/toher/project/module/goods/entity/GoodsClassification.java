package com.toher.project.module.goods.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName 商品分类实体类
 * @Author 廖亭婷
 * @Date 2020/7/3
 */
@Data
@Table(name = "mh_goods_classification")
public class GoodsClassification {
    //商品分类Id
    @Id
    @ApiModelProperty("商品分类Id")
    private Integer classificationId;
    //分类名称
    @ApiModelProperty("分类名称")
    private String classificationName;

}
