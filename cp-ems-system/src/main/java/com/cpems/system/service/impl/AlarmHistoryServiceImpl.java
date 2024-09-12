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
import com.cpems.system.domain.bo.AlarmHistoryBo;
import com.cpems.system.domain.vo.AlarmHistoryVo;
import com.cpems.system.domain.AlarmHistory;
import com.cpems.system.mapper.AlarmHistoryMapper;
import com.cpems.system.service.IAlarmHistoryService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 实时报警Service业务层处理
 *
 * @author ruoyi
 * @date 2023-04-04
 */
@RequiredArgsConstructor
@Service
public class AlarmHistoryServiceImpl implements IAlarmHistoryService {

    private final AlarmHistoryMapper baseMapper;

    /**
     * 查询实时报警
     */
    @Override
    public AlarmHistoryVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询实时报警列表
     */
    @Override
    public TableDataInfo<AlarmHistoryVo> queryPageList(AlarmHistoryBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<AlarmHistory> lqw = buildQueryWrapper(bo);
        Page<AlarmHistoryVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询实时报警列表
     */
    @Override
    public List<AlarmHistoryVo> queryList(AlarmHistoryBo bo) {
        LambdaQueryWrapper<AlarmHistory> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<AlarmHistory> buildQueryWrapper(AlarmHistoryBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<AlarmHistory> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getParamName()), AlarmHistory::getParamName, bo.getParamName());
        lqw.eq(bo.getAlarmTime() != null, AlarmHistory::getAlarmTime, bo.getAlarmTime());
        lqw.between(params.get("beginTime") != null && params.get("endTime") != null,
            AlarmHistory::getAlarmTime, params.get("beginTime"), params.get("endTime"));
        lqw.eq(StringUtils.isNotBlank(bo.getAlarmInfo()), AlarmHistory::getAlarmInfo, bo.getAlarmInfo());
        lqw.eq(StringUtils.isNotBlank(bo.getAlarmLevel()), AlarmHistory::getAlarmLevel, bo.getAlarmLevel());
        lqw.eq(StringUtils.isNotBlank(bo.getArea()), AlarmHistory::getArea, bo.getArea());
        lqw.eq(StringUtils.isNotBlank(bo.getEquipment()), AlarmHistory::getEquipment, bo.getEquipment());
        lqw.eq(bo.getAlarmVal() != null, AlarmHistory::getAlarmVal, bo.getAlarmVal());
        lqw.eq(bo.getEndTime() != null, AlarmHistory::getEndTime, bo.getEndTime());
        return lqw;
    }

    /**
     * 新增实时报警
     */
    @Override
    public Boolean insertByBo(AlarmHistoryBo bo) {
        AlarmHistory add = BeanUtil.toBean(bo, AlarmHistory.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改实时报警
     */
    @Override
    public Boolean updateByBo(AlarmHistoryBo bo) {
        AlarmHistory update = BeanUtil.toBean(bo, AlarmHistory.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(AlarmHistory entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除实时报警
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
