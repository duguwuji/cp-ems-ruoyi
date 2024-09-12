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
import com.cpems.system.domain.bo.ChargingPileBo;
import com.cpems.system.domain.vo.ChargingPileVo;
import com.cpems.system.domain.ChargingPile;
import com.cpems.system.mapper.ChargingPileMapper;
import com.cpems.system.service.IChargingPileService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 充电桩信息Service业务层处理
 *
 * @author ruoyi
 * @date 2023-10-08
 */
@RequiredArgsConstructor
@Service
public class ChargingPileServiceImpl implements IChargingPileService {

    private final ChargingPileMapper baseMapper;

    /**
     * 查询充电桩信息
     */
    @Override
    public ChargingPileVo queryById(Long pileId){
        return baseMapper.selectVoById(pileId);
    }

    /**
     * 查询充电桩信息列表
     */
    @Override
    public TableDataInfo<ChargingPileVo> queryPageList(ChargingPileBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ChargingPile> lqw = buildQueryWrapper(bo);
        Page<ChargingPileVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询充电桩信息列表
     */
    @Override
    public List<ChargingPileVo> queryList(ChargingPileBo bo) {
        LambdaQueryWrapper<ChargingPile> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ChargingPile> buildQueryWrapper(ChargingPileBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ChargingPile> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getEncoding()), ChargingPile::getEncoding, bo.getEncoding());
        lqw.eq(StringUtils.isNotBlank(bo.getType()), ChargingPile::getType, bo.getType());
        lqw.like(StringUtils.isNotBlank(bo.getName()), ChargingPile::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getMerchantId()), ChargingPile::getMerchantId, bo.getMerchantId());
        lqw.like(StringUtils.isNotBlank(bo.getMerchantName()), ChargingPile::getMerchantName, bo.getMerchantName());
        lqw.eq(StringUtils.isNotBlank(bo.getStationId()), ChargingPile::getStationId, bo.getStationId());
        lqw.like(StringUtils.isNotBlank(bo.getStationName()), ChargingPile::getStationName, bo.getStationName());
        lqw.like(StringUtils.isNotBlank(bo.getBrand()), ChargingPile::getBrand, bo.getBrand());
        lqw.like(StringUtils.isNotBlank(bo.getModel()), ChargingPile::getModel, bo.getModel());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), ChargingPile::getStatus, bo.getStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getWorkStatus()), ChargingPile::getWorkStatus, bo.getWorkStatus());
        return lqw;
    }

    /**
     * 新增充电桩信息
     */
    @Override
    public Boolean insertByBo(ChargingPileBo bo) {
        ChargingPile add = BeanUtil.toBean(bo, ChargingPile.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setPileId(add.getPileId());
        }
        return flag;
    }

    /**
     * 修改充电桩信息
     */
    @Override
    public Boolean updateByBo(ChargingPileBo bo) {
        ChargingPile update = BeanUtil.toBean(bo, ChargingPile.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ChargingPile entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除充电桩信息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    /**
     * 批量启用/停用充电桩
     */
    @Override
    public Boolean openOrClose(Collection<ChargingPile> piles){
        return baseMapper.updateBatchById(piles);
    }
}
