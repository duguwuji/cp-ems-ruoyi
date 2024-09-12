package com.cpems.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cpems.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.cpems.system.domain.bo.StandardInfoBo;
import com.cpems.system.domain.vo.StandardInfoVo;
import com.cpems.system.domain.StandardInfo;
import com.cpems.system.mapper.StandardInfoMapper;
import com.cpems.system.service.IStandardInfoService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 作业规范Service业务层处理
 *
 * @author cpems
 * @date 2023-09-11
 */
@RequiredArgsConstructor
@Service
public class StandardInfoServiceImpl implements IStandardInfoService {

    private final StandardInfoMapper baseMapper;

    /**
     * 查询作业规范
     */
    @Override
    public StandardInfoVo queryById(Long standardId){
        return baseMapper.selectVoById(standardId);
    }

    /**
     * 查询作业规范列表
     */
    @Override
    public TableDataInfo<StandardInfoVo> queryPageList(StandardInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StandardInfo> lqw = buildQueryWrapper(bo);
        Page<StandardInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询作业规范列表
     */
    @Override
    public List<StandardInfoVo> queryList(StandardInfoBo bo) {
        LambdaQueryWrapper<StandardInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<StandardInfo> buildQueryWrapper(StandardInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<StandardInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getStandardCode()), StandardInfo::getStandardCode, bo.getStandardCode());
        lqw.like(StringUtils.isNotBlank(bo.getStandardName()), StandardInfo::getStandardName, bo.getStandardName());
        lqw.eq(StringUtils.isNotBlank(bo.getStandardDescribe()), StandardInfo::getStandardDescribe, bo.getStandardDescribe());
        lqw.eq(StringUtils.isNotBlank(bo.getDocOssId()), StandardInfo::getDocOssId, bo.getDocOssId());
        return lqw;
    }

    /**
     * 新增作业规范
     */
    @Override
    public Boolean insertByBo(StandardInfoBo bo) {
        StandardInfo add = BeanUtil.toBean(bo, StandardInfo.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setStandardId(add.getStandardId());
        }
        return flag;
    }

    /**
     * 修改作业规范
     */
    @Override
    public Boolean updateByBo(StandardInfoBo bo) {
        StandardInfo update = BeanUtil.toBean(bo, StandardInfo.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(StandardInfo entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除作业规范
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
