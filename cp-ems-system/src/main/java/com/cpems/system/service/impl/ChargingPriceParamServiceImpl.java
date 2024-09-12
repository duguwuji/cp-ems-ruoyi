package com.cpems.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cpems.common.utils.StringUtils;
import com.cpems.system.domain.ChargingPriceParam;
import com.cpems.system.domain.bo.ChargingPriceParamBo;
import com.cpems.system.domain.vo.ChargingPriceParamVo;
import com.cpems.system.mapper.ChargingPriceParamMapper;
import com.cpems.system.service.IChargingPriceParamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 充电策略参数信息Service业务层处理
 *
 * @author cpems
 * @date 2023-10-12
 */
@RequiredArgsConstructor
@Service
public class ChargingPriceParamServiceImpl implements IChargingPriceParamService {

    private final ChargingPriceParamMapper baseMapper;

    /**
     * 查询充电策略参数信息
     */
    @Override
    public ChargingPriceParamVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询充电策略参数信息列表
     */
    @Override
    public TableDataInfo<ChargingPriceParamVo> queryPageList(ChargingPriceParamBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ChargingPriceParam> lqw = buildQueryWrapper(bo);
        lqw.orderByAsc(ChargingPriceParam::getStartTime);
        Page<ChargingPriceParamVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询充电策略参数信息列表
     */
    @Override
    public List<ChargingPriceParamVo> queryList(ChargingPriceParamBo bo) {
        LambdaQueryWrapper<ChargingPriceParam> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ChargingPriceParam> buildQueryWrapper(ChargingPriceParamBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ChargingPriceParam> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getStrategyId() != null, ChargingPriceParam::getStrategyId, bo.getStrategyId());
        lqw.eq(StringUtils.isNotBlank(bo.getStartTime()), ChargingPriceParam::getStartTime, bo.getStartTime());
        lqw.eq(StringUtils.isNotBlank(bo.getEndTime()), ChargingPriceParam::getEndTime, bo.getEndTime());
        lqw.eq(StringUtils.isNotBlank(bo.getMark()), ChargingPriceParam::getMark, bo.getMark());
        lqw.eq(bo.getElecPrice() != null, ChargingPriceParam::getElecPrice, bo.getElecPrice());
        lqw.eq(bo.getServicePrice() != null, ChargingPriceParam::getServicePrice, bo.getServicePrice());
        return lqw;
    }

    /**
     * 新增充电策略参数信息
     */
    @Override
    public Boolean insertByBo(ChargingPriceParamBo bo) {
        ChargingPriceParam add = BeanUtil.toBean(bo, ChargingPriceParam.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改充电策略参数信息
     */
    @Override
    public Boolean updateByBo(ChargingPriceParamBo bo) {
        ChargingPriceParam update = BeanUtil.toBean(bo, ChargingPriceParam.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ChargingPriceParam entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除充电策略参数信息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public Boolean updateParamList(Collection<ChargingPriceParam> paramList) {
        List<ChargingPriceParam> params = paramList.stream().collect(Collectors.toList());
        // 查询充电价格策略原本的价格参数
        ChargingPriceParamBo bo = new ChargingPriceParamBo();
        bo.setStrategyId(params.get(0).getStrategyId());
        List<ChargingPriceParam> originList = BeanUtil.copyToList(queryList(bo), ChargingPriceParam.class);

        // 前端传来的数据中，有id的更新，没有的插入
//        List<ChargingPriceParam> addList = params.stream().filter(p -> p.getId() == null).collect(Collectors.toList());
        List<ChargingPriceParam> updateList = params.stream().filter(p -> p.getId() != null).collect(Collectors.toList());

        // 数据库中存在但前端数据没有的删除
        List<ChargingPriceParam> deleteList = new ArrayList<>();
        for (ChargingPriceParam param : originList) {
            List<ChargingPriceParam> paramBoList = updateList.stream().filter(p -> p.getId().equals(param.getId())).collect(Collectors.toList());
            if(ObjectUtil.isEmpty(paramBoList)) {
                deleteList.add(param);
            }
        }
        List<Long> deleteIds = deleteList.stream().map(ChargingPriceParam::getId).collect(Collectors.toList());
        if(ObjectUtil.isEmpty(deleteIds)) {
            return baseMapper.insertOrUpdateBatch(paramList);
        }

        return baseMapper.insertOrUpdateBatch(paramList) && baseMapper.deleteBatchIds(deleteIds) > 0;
    }
}
