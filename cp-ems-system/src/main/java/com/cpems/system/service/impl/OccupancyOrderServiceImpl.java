package com.cpems.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cpems.common.utils.StringUtils;
import com.cpems.system.domain.OccupancyOrder;
import com.cpems.system.domain.OrderInfo;
import com.cpems.system.domain.bo.OccupancyOrderBo;
import com.cpems.system.domain.vo.OccupancyOrderVo;
import com.cpems.system.domain.vo.OrderInfoVo;
import com.cpems.system.mapper.OccupancyOrderMapper;
import com.cpems.system.mapper.OrderInfoMapper;
import com.cpems.system.service.IOccupancyOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 占位订单信息Service业务层处理
 *
 * @author cpems
 * @date 2023-10-08
 */
@RequiredArgsConstructor
@Service
public class OccupancyOrderServiceImpl implements IOccupancyOrderService {

    private final OccupancyOrderMapper baseMapper;
    private final OrderInfoMapper orderInfoMapper;

    /**
     * 查询占位订单信息
     */
    @Override
    public OccupancyOrderVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询占位订单信息列表
     */
    @Override
    public TableDataInfo<OccupancyOrderVo> queryPageList(OccupancyOrderBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<OccupancyOrder> lqw = buildQueryWrapper(bo);
        Page<OccupancyOrderVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);

        List<Long> orderNos = result.getRecords().stream().map(OccupancyOrderVo::getOrderNo).collect(Collectors.toList());

        if(ObjectUtil.isEmpty(orderNos)) {
            return TableDataInfo.build(result);
        }
        LambdaQueryWrapper<OrderInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(OrderInfo::getOrderNo, orderNos);
        List<OrderInfoVo> orderInfoVos = orderInfoMapper.selectVoList(lambdaQueryWrapper);

        for(OccupancyOrderVo vo : result.getRecords()) {
            List<OrderInfoVo> orders = orderInfoVos.stream().filter(o -> o.getOrderNo().equals(vo.getOrderNo())).collect(Collectors.toList());
            if(ObjectUtil.isNotNull(orders)) {
                vo.setOrderInfo(orders.get(0));
            }
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询占位订单信息列表
     */
    @Override
    public List<OccupancyOrderVo> queryList(OccupancyOrderBo bo) {
        LambdaQueryWrapper<OccupancyOrder> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<OccupancyOrder> buildQueryWrapper(OccupancyOrderBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<OccupancyOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getOccupancyNo() != null, OccupancyOrder::getOccupancyNo, bo.getOccupancyNo());
        lqw.eq(bo.getOrderNo() != null, OccupancyOrder::getOrderNo, bo.getOrderNo());
        lqw.eq(bo.getDuration() != null, OccupancyOrder::getDuration, bo.getDuration());
        lqw.eq(bo.getFee() != null, OccupancyOrder::getFee, bo.getFee());
        lqw.eq(StringUtils.isNotBlank(bo.getIsFee()), OccupancyOrder::getIsFee, bo.getIsFee());
        lqw.eq(bo.getPayTime() != null, OccupancyOrder::getPayTime, bo.getPayTime());
        lqw.eq(StringUtils.isNotBlank(bo.getSettleStatus()), OccupancyOrder::getSettleStatus, bo.getSettleStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getOrderStatus()), OccupancyOrder::getOrderStatus, bo.getOrderStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getEndReason()), OccupancyOrder::getEndReason, bo.getEndReason());
        return lqw;
    }

    /**
     * 新增占位订单信息
     */
    @Override
    public Boolean insertByBo(OccupancyOrderBo bo) {
        OccupancyOrder add = BeanUtil.toBean(bo, OccupancyOrder.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改占位订单信息
     */
    @Override
    public Boolean updateByBo(OccupancyOrderBo bo) {
        OccupancyOrder update = BeanUtil.toBean(bo, OccupancyOrder.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(OccupancyOrder entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除占位订单信息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
