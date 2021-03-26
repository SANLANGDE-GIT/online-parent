package com.atguigu.guli.service.edu.controller.api;


import com.atguigu.guli.service.base.helper.JwtHelper;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.vo.UcenterMember;
import com.atguigu.guli.service.edu.entity.Comment;
import com.atguigu.guli.service.edu.feign.UcenterMemberService;
import com.atguigu.guli.service.edu.service.CommentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-12-15
 */
@Slf4j
@RestController
@RequestMapping("/api/edu/comment")
@Api(tags = "评论管理")
public class ApiCommentController {

    @Autowired
    UcenterMemberService ucenterMemberService;

    @Autowired
    CommentService commentService;
//http://127.0.0.1:9110/api/edu/comment/NaN/4?courseId=1341361174200975362
    //根据课程id查询评论列表
    @ApiOperation(value = "评论分页列表")
    @GetMapping("/list/{page}/{limit}")
    public R index(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "courseQuery", value = "查询对象", required = false)
                    String courseId) {
        Page<Comment> pageParam = new Page<>(page, limit);

        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);

        commentService.page(pageParam, wrapper);
        List<Comment> commentList = pageParam.getRecords();
        pageParam.setRecords(commentList);
//        Map<String, Object> map = new HashMap<>();
//        map.put("items", commentList);
//        map.put("current", pageParam.getCurrent());
//        map.put("pages", pageParam.getPages());
//        map.put("size", pageParam.getSize());
//        map.put("total", pageParam.getTotal());
//        map.put("hasNext", pageParam.hasNext());
//        map.put("hasPrevious", pageParam.hasPrevious());
        return R.ok().data("pages",pageParam);
    }

    @ApiOperation(value = "添加评论")
    @PostMapping("auth/save")
    public R save(@RequestBody Comment comment, HttpServletRequest request) {
        String memberId = JwtHelper.getId(request);
        if(StringUtils.isEmpty(memberId)) {
            return R.error().code(28004).message("请登录");
        }
        comment.setMemberId(memberId);

        UcenterMember ucenterMember = ucenterMemberService.getInfo(memberId);

        comment.setNickname(ucenterMember.getNickname());
        comment.setAvatar(ucenterMember.getAvatar());

        commentService.save(comment);
        return R.ok();
    }

}

