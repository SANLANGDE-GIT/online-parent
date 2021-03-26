package com.atguigu.guli.service.cms.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.cms.entity.Ad;
import com.atguigu.guli.service.cms.entity.vo.AdVo;
import com.atguigu.guli.service.cms.service.AdService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
//@CrossOrigin
@RestController
@RequestMapping("/admin/cms/ad")
public class AdminAdController {

    @Autowired
    AdService adService;

    @GetMapping("/list/{pageNum}/{pageSize}")
    public R pageList(@PathVariable Long pageNum,@PathVariable Long pageSize){
        Page<AdVo> page =adService.selectPageList(pageNum,pageSize);
        List<AdVo> ads=page.getRecords();
        return R.ok().data("items",ads).data("total",page.getTotal());
    }

    @PostMapping("/save")
    public R save(@RequestBody Ad ad){
        boolean save = adService.save(ad);
        if (save)
            return R.ok().message("添加成功！");
        else
            return R.error().message("添加失败！");
    }

    @PutMapping("/update")
    public R update(@RequestBody Ad ad){
        boolean save = adService.updateById(ad);
        if (save)
            return R.ok().message("修改成功！");
        else
            return R.error().message("修改失败！");
    }

    @GetMapping("get/{id}")
    public R getAdById(@PathVariable String id){
        Ad ad = adService.getById(id);
        if (ad!=null)
            return R.ok().data("item",ad);
        else
            return R.error().message("数据不存在");
    }

    @DeleteMapping("delete/{id}")
    public R deleteAdById(@PathVariable String id){


        boolean b = adService.removeAdById(id);
        if (b)
            return R.ok().message("删除成功！");
        else
            return R.error().message("删除失败！");
    }

}

