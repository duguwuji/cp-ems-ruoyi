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
import com.cpems.system.domain.bo.ProcessInfoBo;
import com.cpems.system.domain.vo.ProcessInfoVo;
import com.cpems.system.domain.ProcessInfo;
import com.cpems.system.mapper.ProcessInfoMapper;
import com.cpems.system.service.IProcessInfoService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 流程管理Service业务层处理
 *
 * @author cpems
 * @date 2023-09-11
 */
@RequiredArgsConstructor
@Service
public class ProcessInfoServiceImpl implements IProcessInfoService {

    private final ProcessInfoMapper baseMapper;

    /**
     * 查询流程管理
     */
    @Override
    public ProcessInfoVo queryById(Long processId){
        return baseMapper.selectVoById(processId);
    }

    /**
     * 查询流程管理列表
     */
    @Override
    public TableDataInfo<ProcessInfoVo> queryPageList(ProcessInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ProcessInfo> lqw = buildQueryWrapper(bo);
        Page<ProcessInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询流程管理列表
     */
    @Override
    public List<ProcessInfoVo> queryList(ProcessInfoBo bo) {
        LambdaQueryWrapper<ProcessInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ProcessInfo> buildQueryWrapper(ProcessInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ProcessInfo> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getEventName()), ProcessInfo::getEventName, bo.getEventName());
        lqw.eq(StringUtils.isNotBlank(bo.getEventType()), ProcessInfo::getEventType, bo.getEventType());
        lqw.eq(bo.getTouchTime() != null, ProcessInfo::getTouchTime, bo.getTouchTime());
        lqw.eq(StringUtils.isNotBlank(bo.getHandleResult()), ProcessInfo::getHandleResult, bo.getHandleResult());
        lqw.eq(bo.getHandlePerson() != null, ProcessInfo::getHandlePerson, bo.getHandlePerson());
        return lqw;
    }

    /**
     * 新增流程管理
     */
    @Override
    public Boolean insertByBo(ProcessInfoBo bo) {
        ProcessInfo add = BeanUtil.toBean(bo, ProcessInfo.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setProcessId(add.getProcessId());
        }
        return flag;
    }

    /**
     * 修改流程管理
     */
    @Override
    public Boolean updateByBo(ProcessInfoBo bo) {
        ProcessInfo update = BeanUtil.toBean(bo, ProcessInfo.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ProcessInfo entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除流程管理
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
