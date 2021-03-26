package com.atguigu.guli.service.cms.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.cms.entity.AdType;
import com.atguigu.guli.service.cms.service.AdTypeService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 广告推荐 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-12-29
 */
@Api(tags = "广告推荐管理")
//@CrossOrigin
@RestController
@RequestMapping("/admin/cms/ad-type")
public class AdminAdTypeController {

    @Autowired
    AdTypeService adTypeService;

    @ApiOperation("广告推荐类型列表")
    @GetMapping("/list")
    public R listAdType(){
        List<AdType> adTypes = adTypeService.list();
        return R.ok().data("items",adTypes);
    }

    @ApiOperation("广告推荐类型分页列表")
    @GetMapping("list/{page}/{limit}")
    public R pageList(@PathVariable Long page,@PathVariable Long limit){
        Page<AdType> pageList = adTypeService.getPageList(page,limit);
        return R.ok().data("items",pageList.getRecords()).data("total",pageList.getTotal());
    }

    @ApiOperation("新增推荐类别")
    @PostMapping("add")
    public R addAdType(@ApiParam("类别信息") @RequestBody AdType adType){
        boolean save = adTypeService.save(adType);
        if(save)
            return R.ok().message("保存成功！");
        else
            return R.error().message("保存失败！");
    }

    @ApiOperation("根据ID获取推荐类别")
    @GetMapping("/get/{id}")
    public R adTypeInfo(@PathVariable String id){
        AdType byId = adTypeService.getById(id);
        if(byId!=null)
            return R.ok().data("item",byId);
        else
            return R.error().message("数据不存在");
    }

    @ApiOperation("修改推荐类别")
    @PutMapping("update")
    public R updateAdType(@ApiParam("类别信息") @RequestBody AdType adType){
        boolean b = adTypeService.updateById(adType);
        if(b)
            return R.ok().message("修改成功！");
        else
            return R.error().message("修改失败");
    }

    @ApiOperation("根据ID删除推荐类别")
    @DeleteMapping("/delete/{id}")
    public R delete(@PathVariable String id){
        boolean b = adTypeService.removeById(id);
        if (b)
            return R.ok().message("删除成功！");
        else
            return R.error().message("删除失败！");
    }

}

