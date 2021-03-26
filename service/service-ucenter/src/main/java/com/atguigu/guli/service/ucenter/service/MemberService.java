package com.atguigu.guli.service.ucenter.service;

import com.atguigu.guli.service.base.dto.MemberDto;
import com.atguigu.guli.service.ucenter.entity.Member;
import com.atguigu.guli.service.ucenter.entity.form.LoginForm;
import com.atguigu.guli.service.ucenter.entity.form.RegisterMemberForm;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author atguigu
 * @since 2020-12-30
 */
public interface MemberService extends IService<Member> {

    void regiterMember(RegisterMemberForm registerMember);

    String login(LoginForm loginForm);

    MemberDto getMemberDtoById(String memberId);

    Long countRegisterNumByDay(String day);
}
