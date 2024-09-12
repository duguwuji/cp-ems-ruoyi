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
import com.cpems.system.domain.bo.DutyBo;
import com.cpems.system.domain.vo.DutyVo;
import com.cpems.system.domain.Duty;
import com.cpems.system.mapper.DutyMapper;
import com.cpems.system.service.IDutyService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 值班管理Service业务层处理
 *
 * @author ruoyi
 * @date 2023-09-11
 */
@RequiredArgsConstructor
@Service
public class DutyServiceImpl implements IDutyService {

    private final DutyMapper baseMapper;

    /**
     * 查询值班管理
     */
    @Override
    public DutyVo queryById(Long dutyId){
        return baseMapper.selectVoById(dutyId);
    }

    /**
     * 查询值班管理列表
     */
    @Override
    public TableDataInfo<DutyVo> queryPageList(DutyBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Duty> lqw = buildQueryWrapper(bo);
        Page<DutyVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询值班管理列表
     */
    @Override
    public List<DutyVo> queryList(DutyBo bo) {
        LambdaQueryWrapper<Duty> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Duty> buildQueryWrapper(DutyBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Duty> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getDutyDate()), Duty::getDutyDate, bo.getDutyDate());
        lqw.eq(StringUtils.isNotBlank(bo.getOnDuty()), Duty::getOnDuty, bo.getOnDuty());
        lqw.eq(StringUtils.isNotBlank(bo.getDutyPeriod()), Duty::getDutyPeriod, bo.getDutyPeriod());
        lqw.eq(StringUtils.isNotBlank(bo.getType()), Duty::getType, bo.getType());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), Duty::getStatus, bo.getStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getRelief()), Duty::getRelief, bo.getRelief());
        return lqw;
    }

    /**
     * 新增值班管理
     */
    @Override
    public Boolean insertByBo(DutyBo bo) {
        Duty add = BeanUtil.toBean(bo, Duty.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setDutyId(add.getDutyId());
        }
        return flag;
    }

    /**
     * 修改值班管理
     */
    @Override
    public Boolean updateByBo(DutyBo bo) {
        Duty update = BeanUtil.toBean(bo, Duty.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Duty entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除值班管理
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
