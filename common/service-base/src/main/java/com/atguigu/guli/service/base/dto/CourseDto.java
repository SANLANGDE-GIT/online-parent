package com.atguigu.guli.service.base.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CourseDto {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "teacher_id")
    private String teacherId;

    @ApiModelProperty(value = "课程标题")
    private String title;

    @ApiModelProperty(value = "课程销售价格，设置为0则可免费观看")
    private BigDecimal price;

    @ApiModelProperty(value = "讲师名称")
    private String teacherName;

    @ApiModelProperty(value = "课程封面图片路径")
    private String cover;
}
