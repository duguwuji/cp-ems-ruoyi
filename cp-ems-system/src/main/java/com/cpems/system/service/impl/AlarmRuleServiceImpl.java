package com.cpems.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cpems.common.core.domain.entity.SysUser;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cpems.common.utils.StringUtils;
import com.cpems.system.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.cpems.system.domain.bo.AlarmRuleBo;
import com.cpems.system.domain.vo.AlarmRuleVo;
import com.cpems.system.domain.AlarmRule;
import com.cpems.system.mapper.AlarmRuleMapper;
import com.cpems.system.service.IAlarmRuleService;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 报警规则Service业务层处理
 *
 * @author ruoyi
 * @date 2023-04-06
 */
@RequiredArgsConstructor
@Service
public class AlarmRuleServiceImpl implements IAlarmRuleService {

    private final AlarmRuleMapper baseMapper;
    private final SysUserMapper sysUserMapper;

    /**
     * 查询报警规则
     */
    @Override
    public AlarmRuleVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询报警规则列表
     */
    @Override
    public TableDataInfo<AlarmRuleVo> queryPageList(AlarmRuleBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<AlarmRule> lqw = buildQueryWrapper(bo);
        Page<AlarmRuleVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);

        //获取用户列表
        List<SysUser> sysUsers = sysUserMapper.selectUserList(new QueryWrapper<>());
        for(AlarmRuleVo alarmRuleVo:result.getRecords()){
            if(StringUtils.isBlank(alarmRuleVo.getUserId())) {
                continue;
            }
            List<SysUser> users = sysUsers.stream().filter(u->u.getUserId().toString().equals(alarmRuleVo.getUserId())).collect(Collectors.toList());
            if(ObjectUtil.isNotEmpty(users)){
                alarmRuleVo.setNickName(users.get(0).getNickName());
            }
        }
        return TableDataInfo.build(result);
    }

    /**
     * 查询报警规则列表
     */
    @Override
    public List<AlarmRuleVo> queryList(AlarmRuleBo bo) {
        LambdaQueryWrapper<AlarmRule> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<AlarmRule> buildQueryWrapper(AlarmRuleBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<AlarmRule> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getParamId() != null, AlarmRule::getParamId, bo.getParamId());
        lqw.like(StringUtils.isNotBlank(bo.getParamName()), AlarmRule::getParamName, bo.getParamName());
        lqw.eq(StringUtils.isNotBlank(bo.getAlarmType()), AlarmRule::getAlarmType, bo.getAlarmType());
        lqw.eq(StringUtils.isNotBlank(bo.getAlarmLevel()), AlarmRule::getAlarmLevel, bo.getAlarmLevel());
        lqw.eq(StringUtils.isNotBlank(bo.getEventType()), AlarmRule::getEventType, bo.getEventType());
        lqw.eq(StringUtils.isNotBlank(bo.getCondition1()), AlarmRule::getCondition1, bo.getCondition1());
        lqw.eq(bo.getThresholdValue1() != null, AlarmRule::getThresholdValue1, bo.getThresholdValue1());
        lqw.eq(StringUtils.isNotBlank(bo.getCondition2()), AlarmRule::getCondition2, bo.getCondition2());
        lqw.eq(bo.getThresholdValue2() != null, AlarmRule::getThresholdValue2, bo.getThresholdValue2());
        lqw.eq(StringUtils.isNotBlank(bo.getAlarmSwitch()), AlarmRule::getAlarmSwitch, bo.getAlarmSwitch());
        lqw.eq(StringUtils.isNotBlank(bo.getUserId()), AlarmRule::getUserId, bo.getUserId());
        return lqw;
    }

    /**
     * 新增报警规则
     */
    @Override
    public Boolean insertByBo(AlarmRuleBo bo) {
        AlarmRule add = BeanUtil.toBean(bo, AlarmRule.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改报警规则
     */
    @Override
    public Boolean updateByBo(AlarmRuleBo bo) {
        AlarmRule update = BeanUtil.toBean(bo, AlarmRule.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(AlarmRule entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除报警规则
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
