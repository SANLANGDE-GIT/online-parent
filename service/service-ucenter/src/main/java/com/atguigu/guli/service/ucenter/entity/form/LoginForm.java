package com.atguigu.guli.service.ucenter.entity.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginForm {
    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "密码")
    private String password;
}
