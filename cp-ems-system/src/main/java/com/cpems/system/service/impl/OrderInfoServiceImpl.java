package com.cpems.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cpems.common.utils.StringUtils;
import com.cpems.system.domain.OrderInfo;
import com.cpems.system.domain.bo.OrderInfoBo;
import com.cpems.system.domain.vo.OrderInfoVo;
import com.cpems.system.mapper.OrderInfoMapper;
import com.cpems.system.service.IOrderInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 订单信息Service业务层处理
 *
 * @author ruoyi
 * @date 2023-10-07
 */
@RequiredArgsConstructor
@Service
public class OrderInfoServiceImpl implements IOrderInfoService {

    private final OrderInfoMapper baseMapper;
    private final MerchantServiceImpl merchantService;

    /**
     * 查询订单信息
     */
    @Override
    public OrderInfoVo queryById(Long id){
        OrderInfoVo result = baseMapper.selectVoById(id);
        // 查询商户信息
        if(result.getMerchantId() != null) {
            result.setMerchant(merchantService.queryById(result.getMerchantId()));
        }
        return result;
    }

    /**
     * 查询订单信息列表
     */
    @Override
    public TableDataInfo<OrderInfoVo> queryPageList(OrderInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<OrderInfo> lqw = buildQueryWrapper(bo);
        Page<OrderInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询订单信息列表
     */
    @Override
    public List<OrderInfoVo> queryList(OrderInfoBo bo) {
        LambdaQueryWrapper<OrderInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<OrderInfo> buildQueryWrapper(OrderInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<OrderInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getOrderNo() != null, OrderInfo::getOrderNo, bo.getOrderNo());
        lqw.eq(bo.getUserId() != null, OrderInfo::getUserId, bo.getUserId());
        lqw.like(StringUtils.isNotBlank(bo.getUserName()), OrderInfo::getUserName, bo.getUserName());
        lqw.eq(StringUtils.isNotBlank(bo.getPhone()), OrderInfo::getPhone, bo.getPhone());
        lqw.eq(bo.getMerchantId() != null, OrderInfo::getMerchantId, bo.getMerchantId());
        lqw.like(StringUtils.isNotBlank(bo.getMerchantName()), OrderInfo::getMerchantName, bo.getMerchantName());
        lqw.eq(bo.getStationId() != null, OrderInfo::getStationId, bo.getStationId());
        lqw.like(StringUtils.isNotBlank(bo.getStationName()), OrderInfo::getStationName, bo.getStationName());
        lqw.eq(bo.getPileId() != null, OrderInfo::getPileId, bo.getPileId());
        lqw.like(StringUtils.isNotBlank(bo.getPileName()), OrderInfo::getPileName, bo.getPileName());
        lqw.eq(bo.getStartTime() != null, OrderInfo::getStartTime, bo.getStartTime());
        lqw.eq(bo.getEndTime() != null, OrderInfo::getEndTime, bo.getEndTime());
        lqw.eq(bo.getCarId() != null, OrderInfo::getCarId, bo.getCarId());
        lqw.eq(StringUtils.isNotBlank(bo.getCarNo()), OrderInfo::getCarNo, bo.getCarNo());
        lqw.eq(StringUtils.isNotBlank(bo.getCarVin()), OrderInfo::getCarVin, bo.getCarVin());
        lqw.eq(StringUtils.isNotBlank(bo.getChargeMethod()), OrderInfo::getChargeMethod, bo.getChargeMethod());
        lqw.eq(StringUtils.isNotBlank(bo.getSettleType()), OrderInfo::getSettleType, bo.getSettleType());
        lqw.eq(StringUtils.isNotBlank(bo.getPayType()), OrderInfo::getPayType, bo.getPayType());
        lqw.eq(bo.getSettleTime() != null, OrderInfo::getSettleTime, bo.getSettleTime());
        lqw.eq(bo.getSettlePrice() != null, OrderInfo::getSettlePrice, bo.getSettlePrice());
        lqw.eq(bo.getPaidPrice() != null, OrderInfo::getPaidPrice, bo.getPaidPrice());
        lqw.eq(bo.getDiscountAmt() != null, OrderInfo::getDiscountAmt, bo.getDiscountAmt());
        lqw.eq(bo.getElecAmt() != null, OrderInfo::getElecAmt, bo.getElecAmt());
        lqw.eq(bo.getServeAmt() != null, OrderInfo::getServeAmt, bo.getServeAmt());
        lqw.eq(StringUtils.isNotBlank(bo.getOrderStatus()), OrderInfo::getOrderStatus, bo.getOrderStatus());
        lqw.eq(bo.getChargeDuration() != null, OrderInfo::getChargeDuration, bo.getChargeDuration());
        lqw.eq(bo.getEnergy() != null, OrderInfo::getEnergy, bo.getEnergy());
        lqw.eq(StringUtils.isNotBlank(bo.getOrderSource()), OrderInfo::getOrderSource, bo.getOrderSource());
        lqw.eq(bo.getSettleBalance() != null, OrderInfo::getSettleBalance, bo.getSettleBalance());
        return lqw;
    }

    /**
     * 新增订单信息
     */
    @Override
    public Boolean insertByBo(OrderInfoBo bo) {
        OrderInfo add = BeanUtil.toBean(bo, OrderInfo.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改订单信息
     */
    @Override
    public Boolean updateByBo(OrderInfoBo bo) {
        OrderInfo update = BeanUtil.toBean(bo, OrderInfo.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(OrderInfo entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除订单信息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
