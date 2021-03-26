package com.atguigu.guli.service.edu.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class WebCourseVo {

    @ApiModelProperty(value = "ID")
    private String id;
    @ApiModelProperty(value = "课程标题")
    private String title;
    @ApiModelProperty(value = "课程销售价格，设置为0则可免费观看")
    private BigDecimal price;
    @ApiModelProperty(value = "总课时")
    private Integer lessonNum;
    @ApiModelProperty(value = "课程封面图片路径")
    private String cover;
    @ApiModelProperty(value = "销售数量")
    private Long buyCount;
    @ApiModelProperty(value = "浏览数量")
    private Long viewCount;
    @ApiModelProperty(value = "课程简介")
    private String description;


    @ApiModelProperty(value = "课程讲师ID")
    private String teacherId;
    @ApiModelProperty(value = "课程讲师名称")
    private String teacherName;
    @ApiModelProperty(value = "头衔 1高级讲师 2首席讲师")
    private Integer level;
    @ApiModelProperty(value = "讲师头像")
    private String avatar;


    @ApiModelProperty(value = "课程专业ID")
    private String subjectId;
    @ApiModelProperty(value = "课程专业ID")
    private String subjectTitle;
    @ApiModelProperty(value = "课程专业父级ID")
    private String subjectParentId;
    @ApiModelProperty(value = "课程专业父级ID")
    private String subjectParentTitle;


    @ApiModelProperty(value = "章节、课时嵌套列表")
    private List<ChapterVo> chapterVos;
}
