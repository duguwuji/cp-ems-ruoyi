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
import com.cpems.system.domain.bo.ElectricityUBo;
import com.cpems.system.domain.vo.ElectricityUVo;
import com.cpems.system.domain.ElectricityU;
import com.cpems.system.mapper.ElectricityUMapper;
import com.cpems.system.service.IElectricityUService;

import java.util.List;
import java.util.Collection;

/**
 * 电压值Service业务层处理
 *
 * @author cpems
 * @date 2023-04-20
 */
@RequiredArgsConstructor
@Service
public class ElectricityUServiceImpl implements IElectricityUService {

    private final ElectricityUMapper baseMapper;

    /**
     * 查询电压值
     */
    @Override
    public ElectricityUVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询电压值列表
     */
    @Override
    public TableDataInfo<ElectricityUVo> queryPageList(ElectricityUBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ElectricityU> lqw = buildQueryWrapper(bo);
        Page<ElectricityUVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询电压值列表
     */
    @Override
    public List<ElectricityUVo> queryList(ElectricityUBo bo) {
        LambdaQueryWrapper<ElectricityU> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ElectricityU> buildQueryWrapper(ElectricityUBo bo) {
        LambdaQueryWrapper<ElectricityU> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getClientId()), ElectricityU::getClientId, bo.getClientId());
        lqw.eq(bo.getValue() != null, ElectricityU::getValue, bo.getValue());
        return lqw;
    }

    /**
     * 新增电压值
     */
    @Override
    public Boolean insertByBo(ElectricityUBo bo) {
        ElectricityU add = BeanUtil.toBean(bo, ElectricityU.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改电压值
     */
    @Override
    public Boolean updateByBo(ElectricityUBo bo) {
        ElectricityU update = BeanUtil.toBean(bo, ElectricityU.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ElectricityU entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除电压值
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
