package com.atguigu.guli.service.edu.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@ApiModel("课程分类列表")
@Data
public class SubjectVo {
    @ApiModelProperty("课程分类ID")
    private String id;
    @ApiModelProperty("课程分类名称")
    private String title;
    @ApiModelProperty("排序")
    private Integer sort;
    @ApiModelProperty("课程二级分类列表")
    private List<SubjectVo> children = new ArrayList<>();
}
