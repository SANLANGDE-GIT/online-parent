package com.atguigu.guli.service.cms.service;

import com.atguigu.guli.service.cms.entity.AdType;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 推荐位 服务类
 * </p>
 *
 * @author atguigu
 * @since 2020-12-29
 */
public interface AdTypeService extends IService<AdType> {

    Page<AdType> getPageList(Long page, Long limit);
}
