package com.cpems.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cpems.common.utils.StringUtils;
import com.cpems.system.domain.Brand;
import com.cpems.system.domain.bo.BrandBo;
import com.cpems.system.domain.vo.BrandVo;
import com.cpems.system.mapper.BrandMapper;
import com.cpems.system.service.IBrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 品牌信息Service业务层处理
 *
 * @author cpems
 * @date 2023-10-10
 */
@RequiredArgsConstructor
@Service
public class BrandServiceImpl implements IBrandService {

    private final BrandMapper baseMapper;

    /**
     * 查询品牌信息
     */
    @Override
    public BrandVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询品牌信息列表
     */
    @Override
    public TableDataInfo<BrandVo> queryPageList(BrandBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Brand> lqw = buildQueryWrapper(bo);
        Page<BrandVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询品牌信息列表
     */
    @Override
    public List<BrandVo> queryList(BrandBo bo) {
        LambdaQueryWrapper<Brand> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Brand> buildQueryWrapper(BrandBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Brand> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getBrandName()), Brand::getBrandName, bo.getBrandName());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), Brand::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增品牌信息
     */
    @Override
    public Boolean insertByBo(BrandBo bo) {
        Brand add = BeanUtil.toBean(bo, Brand.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改品牌信息
     */
    @Override
    public Boolean updateByBo(BrandBo bo) {
        Brand update = BeanUtil.toBean(bo, Brand.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Brand entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除品牌信息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
