package com.cpems.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cpems.common.utils.StringUtils;
import com.cpems.system.domain.ChargingPriceStrategy;
import com.cpems.system.domain.bo.ChargingPriceStrategyBo;
import com.cpems.system.domain.vo.ChargingPriceStrategyVo;
import com.cpems.system.mapper.ChargingPriceStrategyMapper;
import com.cpems.system.service.IChargingPriceStrategyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 充电价格策略信息Service业务层处理
 *
 * @author cpems
 * @date 2023-10-12
 */
@RequiredArgsConstructor
@Service
public class ChargingPriceStrategyServiceImpl implements IChargingPriceStrategyService {

    private final ChargingPriceStrategyMapper baseMapper;

    /**
     * 查询充电价格策略信息
     */
    @Override
    public ChargingPriceStrategyVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询充电价格策略信息列表
     */
    @Override
    public TableDataInfo<ChargingPriceStrategyVo> queryPageList(ChargingPriceStrategyBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ChargingPriceStrategy> lqw = buildQueryWrapper(bo);
        Page<ChargingPriceStrategyVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询充电价格策略信息列表
     */
    @Override
    public List<ChargingPriceStrategyVo> queryList(ChargingPriceStrategyBo bo) {
        LambdaQueryWrapper<ChargingPriceStrategy> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ChargingPriceStrategy> buildQueryWrapper(ChargingPriceStrategyBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ChargingPriceStrategy> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getStrategyName()), ChargingPriceStrategy::getStrategyName, bo.getStrategyName());
        lqw.eq(bo.getStationId() != null, ChargingPriceStrategy::getStationId, bo.getStationId());
        lqw.like(StringUtils.isNotBlank(bo.getStationName()), ChargingPriceStrategy::getStationName, bo.getStationName());
        lqw.eq(StringUtils.isNotBlank(bo.getBillModel()), ChargingPriceStrategy::getBillModel, bo.getBillModel());
        lqw.eq(StringUtils.isNotBlank(bo.getDescription()), ChargingPriceStrategy::getDescription, bo.getDescription());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), ChargingPriceStrategy::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增充电价格策略信息
     */
    @Override
    public Boolean insertByBo(ChargingPriceStrategyBo bo) {
        ChargingPriceStrategy add = BeanUtil.toBean(bo, ChargingPriceStrategy.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改充电价格策略信息
     */
    @Override
    public Boolean updateByBo(ChargingPriceStrategyBo bo) {
        ChargingPriceStrategy update = BeanUtil.toBean(bo, ChargingPriceStrategy.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ChargingPriceStrategy entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除充电价格策略信息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public Long insertPriceStrategy(ChargingPriceStrategyBo bo) {
        ChargingPriceStrategy add = BeanUtil.toBean(bo, ChargingPriceStrategy.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return add.getId();
    }
}
