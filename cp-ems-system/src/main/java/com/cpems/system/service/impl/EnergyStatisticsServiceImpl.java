package com.cpems.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cpems.common.utils.StringUtils;
import com.cpems.system.domain.EnergyStatistics;
import com.cpems.system.domain.bo.EnergyStatisticsBo;
import com.cpems.system.domain.vo.EnergyStatisticsVo;
import com.cpems.system.mapper.EnergyStatisticsMapper;
import com.cpems.system.service.IEnergyStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Collection;

/**
 * 能耗统计Service业务层处理
 *
 * @author ruoyi
 * @date 2023-08-24
 */
@RequiredArgsConstructor
@Service
public class EnergyStatisticsServiceImpl implements IEnergyStatisticsService {

    private final EnergyStatisticsMapper baseMapper;

    /**
     * 查询能耗统计
     */
    @Override
    public EnergyStatisticsVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询能耗统计列表
     */
    @Override
    public TableDataInfo<EnergyStatisticsVo> queryPageList(EnergyStatisticsBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<EnergyStatistics> lqw = buildQueryWrapper(bo);
        Page<EnergyStatisticsVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询能耗统计列表
     */
    @Override
    public List<EnergyStatisticsVo> queryList(EnergyStatisticsBo bo) {
        LambdaQueryWrapper<EnergyStatistics> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<EnergyStatistics> buildQueryWrapper(EnergyStatisticsBo bo) {
        LambdaQueryWrapper<EnergyStatistics> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getEquipmentSn() != null, EnergyStatistics::getEquipmentSn, bo.getEquipmentSn());
        lqw.eq(StringUtils.isNotBlank(bo.getEnergyType()), EnergyStatistics::getEnergyType, bo.getEnergyType());
        lqw.eq(bo.getTime() != null, EnergyStatistics::getTime, bo.getTime());
        lqw.eq(bo.getStatistics() != null, EnergyStatistics::getStatistics, bo.getStatistics());
        return lqw;
    }

    /**
     * 新增能耗统计
     */
    @Override
    public Boolean insertByBo(EnergyStatisticsBo bo) {
        EnergyStatistics add = BeanUtil.toBean(bo, EnergyStatistics.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改能耗统计
     */
    @Override
    public Boolean updateByBo(EnergyStatisticsBo bo) {
        EnergyStatistics update = BeanUtil.toBean(bo, EnergyStatistics.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(EnergyStatistics entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除能耗统计
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}

