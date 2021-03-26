package com.atguigu.guli.service.ucenter.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.ucenter.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-12-30
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/admin/ucenter/member")
public class AdminMemberController {

    @Autowired
    MemberService memberService;

    @ApiOperation("用户注册数量统计")
    @GetMapping("/count-register/{day}")
    public R countRegisterNum(@ApiParam(value = "统计日期：yyyy-MM-dd",required = true)@PathVariable String day){
        Long num = memberService.countRegisterNumByDay(day);
        return R.ok().data("registerNum",num);
    }

}

