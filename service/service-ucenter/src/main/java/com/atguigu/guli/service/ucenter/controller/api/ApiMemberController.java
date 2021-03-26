package com.atguigu.guli.service.ucenter.controller.api;


import com.atguigu.guli.service.base.dto.MemberDto;
import com.atguigu.guli.service.base.helper.JwtHelper;
import com.atguigu.guli.service.base.helper.JwtInfo;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.vo.UcenterMember;
import com.atguigu.guli.service.ucenter.entity.Member;
import com.atguigu.guli.service.ucenter.entity.form.LoginForm;
import com.atguigu.guli.service.ucenter.entity.form.RegisterMemberForm;
import com.atguigu.guli.service.ucenter.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-12-30
 */
@Api(tags = "用户中心")
//@CrossOrigin
@RestController
@RequestMapping("/api/ucenter/member")
public class ApiMemberController {

    @Autowired
    MemberService memberService;

    //根据token字符串获取用户信息
    @PostMapping("getInfoUc/{id}")
    public UcenterMember getInfo(@PathVariable String id) {
        //根据用户id获取用户信息
        Member member = memberService.getById(id);
        UcenterMember ucenterMember = new UcenterMember();
        BeanUtils.copyProperties(member,ucenterMember);
        return ucenterMember;
    }

    @ApiOperation("根据会员id查询会员信息")
    @GetMapping("/get-member-dto/{memberId}")
    public R getMemberDtoById(@PathVariable String memberId){
        MemberDto memberDto = memberService.getMemberDtoById(memberId);
        return R.ok().data("memberDto",memberDto);
    }

    @ApiOperation("根据token获取用户的信息")
    @GetMapping("get-user-info")
    public R getUserInfo(HttpServletRequest request){
        HttpSession session = request.getSession();
        Object skey = session.getAttribute("skey");
        System.out.println("skey = " + skey);
        JwtInfo jwtInfo = JwtHelper.getJwtInfo(request);
        return R.ok().data("userInfo",jwtInfo);
    }

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public R register(@ApiParam("用户注册信息") @RequestBody RegisterMemberForm registerMember){

        memberService.regiterMember(registerMember);
        return R.ok();
    }

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public R login(@ApiParam("用户登录信息") @RequestBody LoginForm loginForm){

        String token = memberService.login(loginForm);
        return R.ok().data("token",token);
    }
}

