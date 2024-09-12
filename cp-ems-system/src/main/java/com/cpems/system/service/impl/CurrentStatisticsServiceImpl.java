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
import com.cpems.system.domain.bo.CurrentStatisticsBo;
import com.cpems.system.domain.vo.CurrentStatisticsVo;
import com.cpems.system.domain.CurrentStatistics;
import com.cpems.system.mapper.CurrentStatisticsMapper;
import com.cpems.system.service.ICurrentStatisticsService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 电流统计Service业务层处理
 *
 * @author ruoyi
 * @date 2023-10-18
 */
@RequiredArgsConstructor
@Service
public class CurrentStatisticsServiceImpl implements ICurrentStatisticsService {

    private final CurrentStatisticsMapper baseMapper;

    /**
     * 查询电流统计
     */
    @Override
    public CurrentStatisticsVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询电流统计列表
     */
    @Override
    public TableDataInfo<CurrentStatisticsVo> queryPageList(CurrentStatisticsBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<CurrentStatistics> lqw = buildQueryWrapper(bo);
        Page<CurrentStatisticsVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询电流统计列表
     */
    @Override
    public List<CurrentStatisticsVo> queryList(CurrentStatisticsBo bo) {
        LambdaQueryWrapper<CurrentStatistics> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<CurrentStatistics> buildQueryWrapper(CurrentStatisticsBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<CurrentStatistics> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getEquipmentSn()), CurrentStatistics::getEquipmentSn, bo.getEquipmentSn());
        lqw.eq(StringUtils.isNotBlank(bo.getEnergyType()), CurrentStatistics::getEnergyType, bo.getEnergyType());
        lqw.eq(bo.getTime() != null, CurrentStatistics::getTime, bo.getTime());
        lqw.eq(bo.getMin() != null, CurrentStatistics::getMin, bo.getMin());
        lqw.eq(bo.getAve() != null, CurrentStatistics::getAve, bo.getAve());
        lqw.eq(bo.getMax() != null, CurrentStatistics::getMax, bo.getMax());
        return lqw;
    }

    /**
     * 新增电流统计
     */
    @Override
    public Boolean insertByBo(CurrentStatisticsBo bo) {
        CurrentStatistics add = BeanUtil.toBean(bo, CurrentStatistics.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改电流统计
     */
    @Override
    public Boolean updateByBo(CurrentStatisticsBo bo) {
        CurrentStatistics update = BeanUtil.toBean(bo, CurrentStatistics.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(CurrentStatistics entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除电流统计
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
