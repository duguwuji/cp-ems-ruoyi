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
import com.cpems.system.domain.bo.ScheduleBo;
import com.cpems.system.domain.vo.ScheduleVo;
import com.cpems.system.domain.Schedule;
import com.cpems.system.mapper.ScheduleMapper;
import com.cpems.system.service.IScheduleService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 调度计划管理Service业务层处理
 *
 * @author ruoyi
 * @date 2023-09-11
 */
@RequiredArgsConstructor
@Service
public class ScheduleServiceImpl implements IScheduleService {

    private final ScheduleMapper baseMapper;

    /**
     * 查询调度计划管理
     */
    @Override
    public ScheduleVo queryById(Long scheduleId){
        return baseMapper.selectVoById(scheduleId);
    }

    /**
     * 查询调度计划管理列表
     */
    @Override
    public TableDataInfo<ScheduleVo> queryPageList(ScheduleBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Schedule> lqw = buildQueryWrapper(bo);
        Page<ScheduleVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询调度计划管理列表
     */
    @Override
    public List<ScheduleVo> queryList(ScheduleBo bo) {
        LambdaQueryWrapper<Schedule> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Schedule> buildQueryWrapper(ScheduleBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Schedule> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getNumber()), Schedule::getNumber, bo.getNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getType()), Schedule::getType, bo.getType());
        lqw.eq(StringUtils.isNotBlank(bo.getCycle()), Schedule::getCycle, bo.getCycle());
        lqw.eq(StringUtils.isNotBlank(bo.getStation()), Schedule::getStation, bo.getStation());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), Schedule::getStatus, bo.getStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getIsTimeout()), Schedule::getIsTimeout, bo.getIsTimeout());
        lqw.like(StringUtils.isNotBlank(bo.getName()), Schedule::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getSource()), Schedule::getSource, bo.getSource());
        lqw.eq(StringUtils.isNotBlank(bo.getContent()), Schedule::getContent, bo.getContent());
        lqw.eq(bo.getStartTime() != null, Schedule::getStartTime, bo.getStartTime());
        lqw.eq(bo.getEndTime() != null, Schedule::getEndTime, bo.getEndTime());
        lqw.eq(bo.getFirstReminder() != null, Schedule::getFirstReminder, bo.getFirstReminder());
        lqw.eq(bo.getSecondReminder() != null, Schedule::getSecondReminder, bo.getSecondReminder());
        lqw.eq(StringUtils.isNotBlank(bo.getOperator()), Schedule::getOperator, bo.getOperator());
        lqw.eq(bo.getCommencementTime() != null, Schedule::getCommencementTime, bo.getCommencementTime());
        lqw.eq(StringUtils.isNotBlank(bo.getTerminator()), Schedule::getTerminator, bo.getTerminator());
        lqw.eq(bo.getResolutionTime() != null, Schedule::getResolutionTime, bo.getResolutionTime());
        lqw.eq(StringUtils.isNotBlank(bo.getResolutionContent()), Schedule::getResolutionContent, bo.getResolutionContent());
        lqw.eq(StringUtils.isNotBlank(bo.getPreparedBy()), Schedule::getPreparedBy, bo.getPreparedBy());
        lqw.eq(StringUtils.isNotBlank(bo.getHead()), Schedule::getHead, bo.getHead());
        return lqw;
    }

    /**
     * 新增调度计划管理
     */
    @Override
    public Boolean insertByBo(ScheduleBo bo) {
        Schedule add = BeanUtil.toBean(bo, Schedule.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setScheduleId(add.getScheduleId());
        }
        return flag;
    }

    /**
     * 修改调度计划管理
     */
    @Override
    public Boolean updateByBo(ScheduleBo bo) {
        Schedule update = BeanUtil.toBean(bo, Schedule.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Schedule entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除调度计划管理
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
