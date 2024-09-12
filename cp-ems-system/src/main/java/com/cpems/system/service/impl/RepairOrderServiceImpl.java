package com.cpems.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cpems.common.utils.StringUtils;
import com.cpems.system.mapper.SysOssMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.cpems.system.domain.bo.RepairOrderBo;
import com.cpems.system.domain.vo.RepairOrderVo;
import com.cpems.system.domain.RepairOrder;
import com.cpems.system.mapper.RepairOrderMapper;
import com.cpems.system.service.IRepairOrderService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 维修工单Service业务层处理
 *
 * @author ruoyi
 * @date 2023-04-04
 */
@RequiredArgsConstructor
@Service
public class RepairOrderServiceImpl implements IRepairOrderService {

    private final RepairOrderMapper baseMapper;
    private final SysOssMapper sysOssMapper;

    /**
     * 查询维修工单
     */
    @Override
    public RepairOrderVo queryById(Long id) {
        RepairOrderVo result = baseMapper.selectVoById(id);
        List<String> ossIds = StringUtils.splitList(result.getAnnex(), ",");
        if(ObjectUtil.isNotEmpty(ossIds)) {
            result.setOssList(sysOssMapper.selectVoBatchIds(ossIds));
        }
        return result;
    }

    /**
     * 查询维修工单列表
     */
    @Override
    public TableDataInfo<RepairOrderVo> queryPageList(RepairOrderBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<RepairOrder> lqw = buildQueryWrapper(bo);
        Page<RepairOrderVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询维修工单列表
     */
    @Override
    public List<RepairOrderVo> queryList(RepairOrderBo bo) {
        LambdaQueryWrapper<RepairOrder> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<RepairOrder> buildQueryWrapper(RepairOrderBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<RepairOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getOrderNo()), RepairOrder::getOrderNo, bo.getOrderNo());
        lqw.eq(StringUtils.isNotBlank(bo.getOrderContent()), RepairOrder::getOrderContent, bo.getOrderContent());
        lqw.like(StringUtils.isNotBlank(bo.getProjectName()), RepairOrder::getProjectName, bo.getProjectName());
        lqw.eq(bo.getAssignTime() != null, RepairOrder::getAssignTime, bo.getAssignTime());
        lqw.eq(bo.getFinishTime() != null, RepairOrder::getFinishTime, bo.getFinishTime());
        lqw.eq(StringUtils.isNotBlank(bo.getFinishBy()), RepairOrder::getFinishBy, bo.getFinishBy());
        lqw.eq(StringUtils.isNotBlank(bo.getOrderStatus()), RepairOrder::getOrderStatus, bo.getOrderStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getOrderRemark()), RepairOrder::getOrderRemark, bo.getOrderRemark());
        lqw.eq(ObjectUtil.isNotNull(bo.getUserId()), RepairOrder::getUserId, bo.getUserId());
        return lqw;
    }

    /**
     * 新增维修工单
     */
    @Override
    public Boolean insertByBo(RepairOrderBo bo) {
        RepairOrder add = BeanUtil.toBean(bo, RepairOrder.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改维修工单
     */
    @Override
    public Boolean updateByBo(RepairOrderBo bo) {
        RepairOrder update = BeanUtil.toBean(bo, RepairOrder.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(RepairOrder entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除维修工单
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
