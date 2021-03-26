package com.atguigu.guli.service.edu.entity.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "QueryTeacher对象",description = "讲师查询对象")
public class QueryTeacher {
    @ApiModelProperty(value = "讲师姓名")
    private String name;
    @ApiModelProperty(value = "头衔 1高级讲师 2首席讲师")
    private Integer level;
    @ApiModelProperty(value = "入驻起始时间")
    private String joinDateBegin;
    @ApiModelProperty(value = "入驻结束时间")
    private String joinDateEnd;
}
