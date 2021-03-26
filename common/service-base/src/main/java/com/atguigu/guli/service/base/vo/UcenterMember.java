package com.atguigu.guli.service.base.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UcenterMember {
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "头像")
    private String avatar;

}
