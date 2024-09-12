package com.cpems.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cpems.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.cpems.system.domain.bo.ChargingBo;
import com.cpems.system.domain.vo.ChargingVo;
import com.cpems.system.domain.Charging;
import com.cpems.system.mapper.ChargingMapper;
import com.cpems.system.service.IChargingService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 计费方案信息Service业务层处理
 *
 * @author ruoyi
 * @date 2023-10-30
 */
@RequiredArgsConstructor
@Service
public class ChargingServiceImpl implements IChargingService {

    private final ChargingMapper baseMapper;

    /**
     * 查询计费方案信息
     */
    @Override
    public ChargingVo queryById(Long chargingId) {
        return baseMapper.selectVoById(chargingId);
    }

    /**
     * 查询计费方案信息列表
     */
    @Override
    public TableDataInfo<ChargingVo> queryPageList(ChargingBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Charging> lqw = buildQueryWrapper(bo);
        Page<ChargingVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询计费方案信息列表
     */
    @Override
    public List<ChargingVo> queryList(ChargingBo bo) {
        LambdaQueryWrapper<Charging> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Charging> buildQueryWrapper(ChargingBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Charging> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getType()), Charging::getType, bo.getType());
        lqw.eq(bo.getStartDate() != null, Charging::getStartDate, bo.getStartDate());
        lqw.eq(bo.getEndDate() != null, Charging::getEndDate, bo.getEndDate());
        lqw.eq(bo.getUnitPrice() != null, Charging::getUnitPrice, bo.getUnitPrice());
        lqw.eq(StringUtils.isNotBlank(bo.getPeriodSwitch()), Charging::getPeriodSwitch, bo.getPeriodSwitch());
        lqw.eq(bo.getSharpPrice() != null, Charging::getSharpPrice, bo.getSharpPrice());
        lqw.eq(bo.getPeekPrice() != null, Charging::getPeekPrice, bo.getPeekPrice());
        lqw.eq(bo.getOrdinaryPrice() != null, Charging::getOrdinaryPrice, bo.getOrdinaryPrice());
        lqw.eq(bo.getValleyPrice() != null, Charging::getValleyPrice, bo.getValleyPrice());
        lqw.eq(StringUtils.isNotBlank(bo.getStepSwitch()), Charging::getStepSwitch, bo.getStepSwitch());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), Charging::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增计费方案信息
     */
    @Override
    public Boolean insertByBo(ChargingBo bo) {
        Charging add = BeanUtil.toBean(bo, Charging.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setChargingId(add.getChargingId());
        }
        return flag;
    }

    /**
     * 修改计费方案信息
     */
    @Override
    public Boolean updateByBo(ChargingBo bo) {
        Charging charging = baseMapper.selectOne(new LambdaQueryWrapper<Charging>()
            .eq(Charging::getStatus, bo.getStatus())
            .eq(Charging::getType,bo.getType())
            .last("limit 1"));
        if (ObjectUtil.isNotEmpty(charging)) {
            charging.setStatus("1");
            baseMapper.updateById(charging);
        }
        Charging update = BeanUtil.toBean(bo, Charging.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Charging entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除计费方案信息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public Long insertCharging(ChargingBo bo) {
        Charging add = BeanUtil.toBean(bo, Charging.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setChargingId(add.getChargingId());
        }
        return add.getChargingId();
    }
}
