package com.atguigu.guli.service.base.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MemberDto {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "手机号")
    private String mobile;

}
