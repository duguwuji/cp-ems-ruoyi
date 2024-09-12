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
import com.cpems.system.domain.bo.ElectricityIBo;
import com.cpems.system.domain.vo.ElectricityIVo;
import com.cpems.system.domain.ElectricityI;
import com.cpems.system.mapper.ElectricityIMapper;
import com.cpems.system.service.IElectricityIService;

import java.util.List;
import java.util.Collection;

/**
 * 电流值Service业务层处理
 *
 * @author cpems
 * @date 2023-04-21
 */
@RequiredArgsConstructor
@Service
public class ElectricityIServiceImpl implements IElectricityIService {

    private final ElectricityIMapper baseMapper;

    /**
     * 查询电流值
     */
    @Override
    public ElectricityIVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询电流值列表
     */
    @Override
    public TableDataInfo<ElectricityIVo> queryPageList(ElectricityIBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ElectricityI> lqw = buildQueryWrapper(bo);
        Page<ElectricityIVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询电流值列表
     */
    @Override
    public List<ElectricityIVo> queryList(ElectricityIBo bo) {
        LambdaQueryWrapper<ElectricityI> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ElectricityI> buildQueryWrapper(ElectricityIBo bo) {
        LambdaQueryWrapper<ElectricityI> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getClientId()), ElectricityI::getClientId, bo.getClientId());
        lqw.eq(StringUtils.isNotBlank(bo.getValue()), ElectricityI::getValue, bo.getValue());
        return lqw;
    }

    /**
     * 新增电流值
     */
    @Override
    public Boolean insertByBo(ElectricityIBo bo) {
        ElectricityI add = BeanUtil.toBean(bo, ElectricityI.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改电流值
     */
    @Override
    public Boolean updateByBo(ElectricityIBo bo) {
        ElectricityI update = BeanUtil.toBean(bo, ElectricityI.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ElectricityI entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除电流值
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
