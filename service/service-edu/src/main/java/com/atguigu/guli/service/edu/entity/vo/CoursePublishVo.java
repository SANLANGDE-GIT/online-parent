package com.atguigu.guli.service.edu.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel("课程发布列表")
@Data
public class CoursePublishVo {

    @ApiModelProperty(value = "课程标题")
    private String title;

    @ApiModelProperty(value = "课程讲师名称")
    private String teacherName;

    @ApiModelProperty(value = "课程专业ID")
    private String subjectTitle;

    @ApiModelProperty(value = "课程专业父级ID")
    private String subjectParentTitle;

    @ApiModelProperty(value = "课程销售价格，设置为0则可免费观看")
    private BigDecimal price;

    @ApiModelProperty(value = "总课时")
    private Integer lessonNum;

    @ApiModelProperty(value = "课程封面图片路径")
    private String cover;

}
