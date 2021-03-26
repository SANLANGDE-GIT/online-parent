package com.atguigu.guli.service.edu.entity.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WebCourseQuery {

    @ApiModelProperty(value = "课程专业ID")
    private String subjectId;

    @ApiModelProperty(value = "课程专业父级ID")
    private String subjectParentId;

    @ApiModelProperty(value = "销售数量")
    private String buyCountSort;

    @ApiModelProperty(value = "课程发布时间")
    private String publishTimeSort;

    @ApiModelProperty(value = "课程销售价格")
    private String priceSort;

    @ApiModelProperty(value = "价格排序方式")
    private Integer type; //价格正序：1，价格倒序：2

}
