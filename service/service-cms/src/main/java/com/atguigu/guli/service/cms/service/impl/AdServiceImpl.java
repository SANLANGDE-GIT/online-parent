package com.atguigu.guli.service.cms.service.impl;

import com.atguigu.guli.service.cms.entity.Ad;
import com.atguigu.guli.service.cms.entity.vo.AdVo;
import com.atguigu.guli.service.cms.feign.OssFileService;
import com.atguigu.guli.service.cms.mapper.AdMapper;
import com.atguigu.guli.service.cms.service.AdService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 广告推荐 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-12-29
 */
@CacheConfig(cacheNames = {"ad"})
@Service
public class AdServiceImpl extends ServiceImpl<AdMapper, Ad> implements AdService {

    @Autowired
    OssFileService ossFileService;

    @Override
    public Page<AdVo> selectPageList(Long pageNum, Long pageSize) {
        Page<AdVo> page = new Page<>(pageNum,pageSize);
        QueryWrapper wrapper = new QueryWrapper();
        List<AdVo> adVos = baseMapper.selectPageQueryWrapper(page,wrapper);
        page.setRecords(adVos);
        return page;
    }

    @Override
    public boolean removeAdById(String id) {
        Ad ad = baseMapper.selectById(id);
        String imageUrl = ad.getImageUrl();
        //删除图片
        ossFileService.removeFile(imageUrl);
        return this.removeById(id);
    }

    @Cacheable(cacheNames = {"typeId"},keyGenerator="myGenerator")
    @Override
    public List<Ad> selectListByTypeId(String typeId) {
        QueryWrapper<Ad> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("sort","id");
        wrapper.eq("type_id",typeId);
        return baseMapper.selectList(wrapper);
    }
}
