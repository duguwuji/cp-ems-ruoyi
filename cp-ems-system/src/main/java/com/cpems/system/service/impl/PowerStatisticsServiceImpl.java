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
import com.cpems.system.domain.bo.PowerStatisticsBo;
import com.cpems.system.domain.vo.PowerStatisticsVo;
import com.cpems.system.domain.PowerStatistics;
import com.cpems.system.mapper.PowerStatisticsMapper;
import com.cpems.system.service.IPowerStatisticsService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 功率统计Service业务层处理
 *
 * @author ruoyi
 * @date 2023-09-08
 */
@RequiredArgsConstructor
@Service
public class PowerStatisticsServiceImpl implements IPowerStatisticsService {

    private final PowerStatisticsMapper baseMapper;

    /**
     * 查询功率统计
     */
    @Override
    public PowerStatisticsVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询功率统计列表
     */
    @Override
    public TableDataInfo<PowerStatisticsVo> queryPageList(PowerStatisticsBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<PowerStatistics> lqw = buildQueryWrapper(bo);
        Page<PowerStatisticsVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询功率统计列表
     */
    @Override
    public List<PowerStatisticsVo> queryList(PowerStatisticsBo bo) {
        LambdaQueryWrapper<PowerStatistics> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<PowerStatistics> buildQueryWrapper(PowerStatisticsBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<PowerStatistics> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getEquipmentSn()), PowerStatistics::getEquipmentSn, bo.getEquipmentSn());
        lqw.eq(StringUtils.isNotBlank(bo.getEnergyType()), PowerStatistics::getEnergyType, bo.getEnergyType());
        lqw.eq(bo.getTime() != null, PowerStatistics::getTime, bo.getTime());
        lqw.eq(bo.getMin() != null, PowerStatistics::getMin, bo.getMin());
        lqw.eq(bo.getMax() != null, PowerStatistics::getMax, bo.getMax());
        return lqw;
    }

    /**
     * 新增功率统计
     */
    @Override
    public Boolean insertByBo(PowerStatisticsBo bo) {
        PowerStatistics add = BeanUtil.toBean(bo, PowerStatistics.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改功率统计
     */
    @Override
    public Boolean updateByBo(PowerStatisticsBo bo) {
        PowerStatistics update = BeanUtil.toBean(bo, PowerStatistics.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(PowerStatistics entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除功率统计
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
