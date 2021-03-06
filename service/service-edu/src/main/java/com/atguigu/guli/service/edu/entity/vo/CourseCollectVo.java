package com.atguigu.guli.service.edu.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CourseCollectVo {
    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "courseId")
    private String courseId;

    @ApiModelProperty(value = "课程标题")
    private String title;

    @ApiModelProperty(value = "课程讲师名称")
    private String teacherName;

    @ApiModelProperty(value = "课程销售价格，设置为0则可免费观看")
    private BigDecimal price;

    @ApiModelProperty(value = "课程封面图片路径")
    private String cover;

    @ApiModelProperty(value = "更新时间")
    private Date gmtCreate;
}
