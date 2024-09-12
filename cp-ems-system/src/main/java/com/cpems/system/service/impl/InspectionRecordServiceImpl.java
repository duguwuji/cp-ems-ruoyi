package com.cpems.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cpems.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.cpems.system.domain.bo.InspectionRecordBo;
import com.cpems.system.domain.vo.InspectionRecordVo;
import com.cpems.system.domain.InspectionRecord;
import com.cpems.system.mapper.InspectionRecordMapper;
import com.cpems.system.service.IInspectionRecordService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 巡检记录Service业务层处理
 *
 * @author ruoyi
 * @date 2023-04-04
 */
@RequiredArgsConstructor
@Service
public class InspectionRecordServiceImpl implements IInspectionRecordService {

    private final InspectionRecordMapper baseMapper;

    /**
     * 查询巡检记录
     */
    @Override
    public InspectionRecordVo queryById(Long id){
        InspectionRecordVo result = baseMapper.selectVoById(id);
        if (ObjectUtil.isNotNull(result.getUserId())){
            result.setUserIds(StringUtils.splitList(result.getUserId(),","));
        }
        return result;
    }

    /**
     * 查询巡检记录列表
     */
    @Override
    public TableDataInfo<InspectionRecordVo> queryPageList(InspectionRecordBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<InspectionRecord> lqw = buildQueryWrapper(bo);
        Page<InspectionRecordVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询巡检记录列表
     */
    @Override
    public List<InspectionRecordVo> queryList(InspectionRecordBo bo) {
        LambdaQueryWrapper<InspectionRecord> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<InspectionRecord> buildQueryWrapper(InspectionRecordBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<InspectionRecord> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getPlanName()), InspectionRecord::getPlanName, bo.getPlanName());
        lqw.eq(StringUtils.isNotBlank(bo.getPlanContent()), InspectionRecord::getPlanContent, bo.getPlanContent());
        lqw.like(StringUtils.isNotBlank(bo.getProjectName()), InspectionRecord::getProjectName, bo.getProjectName());
        lqw.eq(bo.getPlanTime() != null, InspectionRecord::getPlanTime, bo.getPlanTime());
        lqw.eq(bo.getInspectionTime() != null, InspectionRecord::getInspectionTime, bo.getInspectionTime());
        lqw.eq(StringUtils.isNotBlank(bo.getInspectionPerson()), InspectionRecord::getInspectionPerson, bo.getInspectionPerson());
        lqw.eq(StringUtils.isNotBlank(bo.getInspectionStatus()), InspectionRecord::getInspectionStatus, bo.getInspectionStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getInspectionRemark()), InspectionRecord::getInspectionRemark, bo.getInspectionRemark());
        lqw.like(ObjectUtil.isNotNull(bo.getUserId()), InspectionRecord::getUserId, bo.getUserId());
        return lqw;
    }

    /**
     * 新增巡检记录
     */
    @Override
    public Boolean insertByBo(InspectionRecordBo bo) {
        InspectionRecord add = BeanUtil.toBean(bo, InspectionRecord.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改巡检记录
     */
    @Override
    public Boolean updateByBo(InspectionRecordBo bo) {
        InspectionRecord update = BeanUtil.toBean(bo, InspectionRecord.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(InspectionRecord entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除巡检记录
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
