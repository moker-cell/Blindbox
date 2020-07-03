package com.toher.project.module.goods.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName 商品系列实体类
 * @Author 廖亭婷
 * @Date 2020/7/3
 */
@Data
@Table(name = "mh_goods_series")
public class GoodsSeries {
    //系列编号
    @Id
    @ApiModelProperty("系列编号")
    private String seriesId;
    //系列名称
    @ApiModelProperty("系列名称")
    private String seriesName;
    //系列图片
    @ApiModelProperty("系列图片")
    private String seriesImgs;
    //详情盒子图片
    @ApiModelProperty("详情盒子图片")
    private String seriesDetailsUrl;
    //是否推荐（1：是，0：不是）
    @ApiModelProperty("是否推荐（1：是，0：不是）")
    private Integer seriesRecommended;
    //摇一摇排除数
    @ApiModelProperty("摇一摇排除数")
    private Integer seriesSeveral;
    //分类编号
    @ApiModelProperty("分类编号")
    private Integer classificationId;
    //品牌编号
    @ApiModelProperty("品牌编号")
    private String brandId;
    // 备注配置
    @ApiModelProperty(" 备注配置")
    private String remark;
    //产品价格
    @ApiModelProperty("产品价格")
    private BigDecimal seriesPrice;
    //回收价格
    @ApiModelProperty("回收价格")
    private BigDecimal seriesRecyclingPrice;
    //发布时间
    @ApiModelProperty("发布时间")
    private Date seriesTime;
    //盒子数
    @Transient
    @ApiModelProperty("盒子数")
    private Integer goodsBoxNumber;
}
