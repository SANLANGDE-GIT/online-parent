package com.atguigu.guli.service.cms.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AdVo {
    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "排序")
    private Integer sort;
}
