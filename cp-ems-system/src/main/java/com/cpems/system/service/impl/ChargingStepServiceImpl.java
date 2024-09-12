package com.cpems.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.cpems.system.domain.bo.ChargingStepBo;
import com.cpems.system.domain.vo.ChargingStepVo;
import com.cpems.system.domain.ChargingStep;
import com.cpems.system.mapper.ChargingStepMapper;
import com.cpems.system.service.IChargingStepService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 阶梯计费参数信息Service业务层处理
 *
 * @author ruoyi
 * @date 2023-10-31
 */
@RequiredArgsConstructor
@Service
public class ChargingStepServiceImpl implements IChargingStepService {

    private final ChargingStepMapper baseMapper;

    /**
     * 查询阶梯计费参数信息
     */
    @Override
    public ChargingStepVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询阶梯计费参数信息列表
     */
    @Override
    public TableDataInfo<ChargingStepVo> queryPageList(ChargingStepBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ChargingStep> lqw = buildQueryWrapper(bo);
        Page<ChargingStepVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询阶梯计费参数信息列表
     */
    @Override
    public List<ChargingStepVo> queryList(ChargingStepBo bo) {
        LambdaQueryWrapper<ChargingStep> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ChargingStep> buildQueryWrapper(ChargingStepBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ChargingStep> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getChargingId() != null, ChargingStep::getChargingId, bo.getChargingId());
        lqw.eq(bo.getStartStep() != null, ChargingStep::getStartStep, bo.getStartStep());
        lqw.eq(bo.getEndStep() != null, ChargingStep::getEndStep, bo.getEndStep());
        lqw.eq(bo.getPriceDifference() != null, ChargingStep::getPriceDifference, bo.getPriceDifference());
        return lqw;
    }

    /**
     * 新增阶梯计费参数信息
     */
    @Override
    public Boolean insertByBo(ChargingStepBo bo) {
        ChargingStep add = BeanUtil.toBean(bo, ChargingStep.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改阶梯计费参数信息
     */
    @Override
    public Boolean updateByBo(ChargingStepBo bo) {
        ChargingStep update = BeanUtil.toBean(bo, ChargingStep.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ChargingStep entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除阶梯计费参数信息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public Boolean updateChargingStepList(Collection<ChargingStep> paramList) {
        List<ChargingStep> params = paramList.stream().collect(Collectors.toList());
        // 查询充电价格策略原本的价格参数
        ChargingStepBo bo = new ChargingStepBo();
        bo.setChargingId(params.get(0).getChargingId());
        List<ChargingStep> originList = BeanUtil.copyToList(queryList(bo), ChargingStep.class);

        // 前端传来的数据中，有id的更新，没有的插入
//        List<ChargingPriceParam> addList = params.stream().filter(p -> p.getId() == null).collect(Collectors.toList());
        List<ChargingStep> updateList = params.stream().filter(p -> p.getId() != null).collect(Collectors.toList());

        // 数据库中存在但前端数据没有的删除
        List<ChargingStep> deleteList = new ArrayList<>();
        for (ChargingStep param : originList) {
            List<ChargingStep> paramBoList = updateList.stream().filter(p -> p.getId().equals(param.getId())).collect(Collectors.toList());
            if(ObjectUtil.isEmpty(paramBoList)) {
                deleteList.add(param);
            }
        }
        List<Long> deleteIds = deleteList.stream().map(ChargingStep::getId).collect(Collectors.toList());
        if(ObjectUtil.isEmpty(deleteIds)) {
            return baseMapper.insertOrUpdateBatch(paramList);
        }

        return baseMapper.insertOrUpdateBatch(paramList) && baseMapper.deleteBatchIds(deleteIds) > 0;
    }
}
