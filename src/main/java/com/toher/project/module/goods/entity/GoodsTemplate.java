package com.toher.project.module.goods.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @ClassName 产品模板实体类
 * @Author 廖亭婷
 * @Date 2020/7/3
 */
@Data
@Table(name = "mh_ goods _template")
public class GoodsTemplate {
    //产品编号
    @Id
    @ApiModelProperty("产品编号")
    private String templateId;
    //系列编号
    @ApiModelProperty("系列编号")
    private String seriesId;
    //产品名称
    @ApiModelProperty("产品名称")
    private String templateName;
    //产品图片
    @ApiModelProperty("产品图片")
    private String templateImgs;
    //库存数量
    @ApiModelProperty("库存数量")
    private Integer templateNumber;
    //概率配置
    @ApiModelProperty("概率配置")
    private Integer templateProbability;
    //是否隐藏产品(1：是，0：不是)
    @ApiModelProperty("是否隐藏产品(1：是，0：不是)")
    private Integer templateStatus;
    //发布时间
    @ApiModelProperty("发布时间")
    private Date templateTime;

}
