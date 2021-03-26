package com.atguigu.guli.service.edu.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("课时管理")
@Data
public class VideoVo {
    @ApiModelProperty(value = "课时ID")
    private String id;

    @ApiModelProperty(value = "课时名称")
    private String title;

    @ApiModelProperty(value = "排序字段")
    private Integer sort;

    @ApiModelProperty(value = "云端视频资源")
    private String videoSourceId;

    @ApiModelProperty(value = "是否可以试听：0收费 1免费")
    private Boolean free;

    @ApiModelProperty(value = "原始文件名称")
    private String videoOriginalName;
}
