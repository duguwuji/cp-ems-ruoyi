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
import com.cpems.system.domain.bo.VoltageStatisticsBo;
import com.cpems.system.domain.vo.VoltageStatisticsVo;
import com.cpems.system.domain.VoltageStatistics;
import com.cpems.system.mapper.VoltageStatisticsMapper;
import com.cpems.system.service.IVoltageStatisticsService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 电压统计Service业务层处理
 *
 * @author ruoyi
 * @date 2023-10-18
 */
@RequiredArgsConstructor
@Service
public class VoltageStatisticsServiceImpl implements IVoltageStatisticsService {

    private final VoltageStatisticsMapper baseMapper;

    /**
     * 查询电压统计
     */
    @Override
    public VoltageStatisticsVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询电压统计列表
     */
    @Override
    public TableDataInfo<VoltageStatisticsVo> queryPageList(VoltageStatisticsBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<VoltageStatistics> lqw = buildQueryWrapper(bo);
        Page<VoltageStatisticsVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询电压统计列表
     */
    @Override
    public List<VoltageStatisticsVo> queryList(VoltageStatisticsBo bo) {
        LambdaQueryWrapper<VoltageStatistics> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<VoltageStatistics> buildQueryWrapper(VoltageStatisticsBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<VoltageStatistics> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getEquipmentSn()), VoltageStatistics::getEquipmentSn, bo.getEquipmentSn());
        lqw.eq(StringUtils.isNotBlank(bo.getEnergyType()), VoltageStatistics::getEnergyType, bo.getEnergyType());
        lqw.eq(bo.getTime() != null, VoltageStatistics::getTime, bo.getTime());
        lqw.eq(bo.getMin() != null, VoltageStatistics::getMin, bo.getMin());
        lqw.eq(bo.getAve() != null, VoltageStatistics::getAve, bo.getAve());
        lqw.eq(bo.getMax() != null, VoltageStatistics::getMax, bo.getMax());
        return lqw;
    }

    /**
     * 新增电压统计
     */
    @Override
    public Boolean insertByBo(VoltageStatisticsBo bo) {
        VoltageStatistics add = BeanUtil.toBean(bo, VoltageStatistics.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改电压统计
     */
    @Override
    public Boolean updateByBo(VoltageStatisticsBo bo) {
        VoltageStatistics update = BeanUtil.toBean(bo, VoltageStatistics.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(VoltageStatistics entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除电压统计
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
