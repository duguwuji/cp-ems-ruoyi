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
import com.cpems.system.domain.bo.PrePlanBo;
import com.cpems.system.domain.vo.PrePlanVo;
import com.cpems.system.domain.PrePlan;
import com.cpems.system.mapper.PrePlanMapper;
import com.cpems.system.service.IPrePlanService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 预案管理Service业务层处理
 *
 * @author ruoyi
 * @date 2023-09-11
 */
@RequiredArgsConstructor
@Service
public class PrePlanServiceImpl implements IPrePlanService {

    private final PrePlanMapper baseMapper;

    /**
     * 查询预案管理
     */
    @Override
    public PrePlanVo queryById(Long prePlanId){
        return baseMapper.selectVoById(prePlanId);
    }

    /**
     * 查询预案管理列表
     */
    @Override
    public TableDataInfo<PrePlanVo> queryPageList(PrePlanBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<PrePlan> lqw = buildQueryWrapper(bo);
        Page<PrePlanVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询预案管理列表
     */
    @Override
    public List<PrePlanVo> queryList(PrePlanBo bo) {
        LambdaQueryWrapper<PrePlan> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<PrePlan> buildQueryWrapper(PrePlanBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<PrePlan> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getNumber()), PrePlan::getNumber, bo.getNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getType()), PrePlan::getType, bo.getType());
        lqw.eq(StringUtils.isNotBlank(bo.getStation()), PrePlan::getStation, bo.getStation());
        lqw.eq(StringUtils.isNotBlank(bo.getDevice()), PrePlan::getDevice, bo.getDevice());
        lqw.eq(StringUtils.isNotBlank(bo.getContent()), PrePlan::getContent, bo.getContent());
        lqw.eq(StringUtils.isNotBlank(bo.getPreparedBy()), PrePlan::getPreparedBy, bo.getPreparedBy());
        lqw.eq(StringUtils.isNotBlank(bo.getKeywords()), PrePlan::getKeywords, bo.getKeywords());
        return lqw;
    }

    /**
     * 新增预案管理
     */
    @Override
    public Boolean insertByBo(PrePlanBo bo) {
        PrePlan add = BeanUtil.toBean(bo, PrePlan.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setPrePlanId(add.getPrePlanId());
        }
        return flag;
    }

    /**
     * 修改预案管理
     */
    @Override
    public Boolean updateByBo(PrePlanBo bo) {
        PrePlan update = BeanUtil.toBean(bo, PrePlan.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(PrePlan entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除预案管理
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
