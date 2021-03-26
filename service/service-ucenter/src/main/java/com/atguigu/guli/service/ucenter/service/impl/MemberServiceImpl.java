package com.atguigu.guli.service.ucenter.service.impl;

import com.atguigu.guli.common.util.FormUtils;
import com.atguigu.guli.common.util.MD5;
import com.atguigu.guli.service.base.constant.ServiceConstant;
import com.atguigu.guli.service.base.dto.MemberDto;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.helper.JwtHelper;
import com.atguigu.guli.service.base.helper.JwtInfo;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.ucenter.entity.Member;
import com.atguigu.guli.service.ucenter.entity.form.LoginForm;
import com.atguigu.guli.service.ucenter.entity.form.RegisterMemberForm;
import com.atguigu.guli.service.ucenter.mapper.MemberMapper;
import com.atguigu.guli.service.ucenter.service.MemberService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-12-30
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public void regiterMember(RegisterMemberForm registerMember) {

        //验证手机号是否正确
        String mobile = registerMember.getMobile();
        if(StringUtils.isEmpty(mobile) ||!FormUtils.isMobile(mobile)){
            //return R.setResult(ResultCodeEnum.LOGIN_MOBILE_ERROR);
            throw new GuliException(ResultCodeEnum.LOGIN_PHONE_ERROR);
        }
        //验证参数是否为空
        String nickname = registerMember.getNickname();
        String password = registerMember.getPassword();
        String code = registerMember.getCode();
        if (StringUtils.isEmpty(code)||
                StringUtils.isEmpty(nickname)||
                StringUtils.isEmpty(password)){
            //return R.setResult(ResultCodeEnum.PARAM_ERROR);
            throw new GuliException(ResultCodeEnum.PARAM_ERROR);
        }
        //验证码是否正确
        String key = ServiceConstant.REGISTER_CODE + mobile;
        Object redisCode = redisTemplate.opsForValue().get(key);
        if(!code.equals(redisCode.toString())){
            throw new GuliException(ResultCodeEnum.CODE_ERROR);
        }

        Member member = new Member();
        BeanUtils.copyProperties(registerMember,member);

        QueryWrapper<Member> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",member.getMobile());
        int integer = baseMapper.selectCount(wrapper);
        if(integer>0){
            throw new GuliException(ResultCodeEnum.REGISTER_MOBLE_ERROR);
        }
        member.setPassword((MD5.encrypt(member.getPassword())));
        baseMapper.insert(member);
        //删除redis缓存
        redisTemplate.delete(key);

    }

    @Override
    public String login(LoginForm loginForm) {

        String mobile = loginForm.getMobile();
        String password = loginForm.getPassword();
        if(StringUtils.isEmpty(mobile)||
                !FormUtils.isMobile(mobile)||
                StringUtils.isEmpty(password)){
            throw new GuliException(ResultCodeEnum.PARAM_ERROR);
        }

        QueryWrapper<Member> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        Member member = baseMapper.selectOne(wrapper);
        if(member==null){
            throw new GuliException(ResultCodeEnum.LOGIN_MOBILE_ERROR);
        }

        if(member.getDisabled()){
            throw new GuliException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }

        if(!MD5.encrypt(password).equals(member.getPassword())){
            throw new GuliException(ResultCodeEnum.LOGIN_PASSWORD_ERROR);
        }

        JwtInfo jwtInfo=new JwtInfo();
        jwtInfo.setNickname(member.getNickname());
        jwtInfo.setId(member.getId());
        jwtInfo.setAvatar(member.getAvatar()==null?"http://cr-0821-guli-file.oss-cn-shanghai.aliyuncs.com/avatar/2020/12/28/3800400e34124f1c80cd404bcf1fd187.jpg".toString():member.getAvatar());

        return JwtHelper.createToken(jwtInfo);
    }

    @Override
    public MemberDto getMemberDtoById(String memberId) {

        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id","nickname","mobile");
        queryWrapper.eq("id",memberId);
        Member member = baseMapper.selectOne(queryWrapper);

        MemberDto memberDto = new MemberDto();

        BeanUtils.copyProperties(member,memberDto);

        return memberDto;
    }

    @Override
    public Long countRegisterNumByDay(String day) {
        return baseMapper.countRegisterNumByDay(day);
    }
}
