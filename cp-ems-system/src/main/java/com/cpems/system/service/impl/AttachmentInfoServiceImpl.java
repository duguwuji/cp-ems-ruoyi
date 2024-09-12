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
import com.cpems.system.domain.bo.AttachmentInfoBo;
import com.cpems.system.domain.vo.AttachmentInfoVo;
import com.cpems.system.domain.AttachmentInfo;
import com.cpems.system.mapper.AttachmentInfoMapper;
import com.cpems.system.service.IAttachmentInfoService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 备件库管理Service业务层处理
 *
 * @author ruoyi
 * @date 2023-09-11
 */
@RequiredArgsConstructor
@Service
public class AttachmentInfoServiceImpl implements IAttachmentInfoService {

    private final AttachmentInfoMapper baseMapper;

    /**
     * 查询备件库管理
     */
    @Override
    public AttachmentInfoVo queryById(Long attachmentId){
        return baseMapper.selectVoById(attachmentId);
    }

    /**
     * 查询备件库管理列表
     */
    @Override
    public TableDataInfo<AttachmentInfoVo> queryPageList(AttachmentInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<AttachmentInfo> lqw = buildQueryWrapper(bo);
        Page<AttachmentInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询备件库管理列表
     */
    @Override
    public List<AttachmentInfoVo> queryList(AttachmentInfoBo bo) {
        LambdaQueryWrapper<AttachmentInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<AttachmentInfo> buildQueryWrapper(AttachmentInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<AttachmentInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getAttachmentCode()), AttachmentInfo::getAttachmentCode, bo.getAttachmentCode());
        lqw.like(StringUtils.isNotBlank(bo.getAttachmentName()), AttachmentInfo::getAttachmentName, bo.getAttachmentName());
        lqw.eq(StringUtils.isNotBlank(bo.getModel()), AttachmentInfo::getModel, bo.getModel());
        lqw.eq(StringUtils.isNotBlank(bo.getUnit()), AttachmentInfo::getUnit, bo.getUnit());
        lqw.eq(StringUtils.isNotBlank(bo.getPurveyorId()), AttachmentInfo::getPurveyorId, bo.getPurveyorId());
        lqw.like(StringUtils.isNotBlank(bo.getPurveyorName()), AttachmentInfo::getPurveyorName, bo.getPurveyorName());
        return lqw;
    }

    /**
     * 新增备件库管理
     */
    @Override
    public Boolean insertByBo(AttachmentInfoBo bo) {
        AttachmentInfo add = BeanUtil.toBean(bo, AttachmentInfo.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setAttachmentId(add.getAttachmentId());
        }
        return flag;
    }

    /**
     * 修改备件库管理
     */
    @Override
    public Boolean updateByBo(AttachmentInfoBo bo) {
        AttachmentInfo update = BeanUtil.toBean(bo, AttachmentInfo.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(AttachmentInfo entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除备件库管理
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}

