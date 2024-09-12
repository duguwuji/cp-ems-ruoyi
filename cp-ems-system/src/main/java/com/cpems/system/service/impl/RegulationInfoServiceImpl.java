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
import com.cpems.system.domain.bo.RegulationInfoBo;
import com.cpems.system.domain.vo.RegulationInfoVo;
import com.cpems.system.domain.RegulationInfo;
import com.cpems.system.mapper.RegulationInfoMapper;
import com.cpems.system.service.IRegulationInfoService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 制度管理Service业务层处理
 *
 * @author cpems
 * @date 2023-09-11
 */
@RequiredArgsConstructor
@Service
public class RegulationInfoServiceImpl implements IRegulationInfoService {

    private final RegulationInfoMapper baseMapper;

    /**
     * 查询制度管理
     */
    @Override
    public RegulationInfoVo queryById(Long regulationId){
        return baseMapper.selectVoById(regulationId);
    }

    /**
     * 查询制度管理列表
     */
    @Override
    public TableDataInfo<RegulationInfoVo> queryPageList(RegulationInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<RegulationInfo> lqw = buildQueryWrapper(bo);
        Page<RegulationInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询制度管理列表
     */
    @Override
    public List<RegulationInfoVo> queryList(RegulationInfoBo bo) {
        LambdaQueryWrapper<RegulationInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<RegulationInfo> buildQueryWrapper(RegulationInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<RegulationInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getRegulationCode()), RegulationInfo::getRegulationCode, bo.getRegulationCode());
        lqw.like(StringUtils.isNotBlank(bo.getRegulationName()), RegulationInfo::getRegulationName, bo.getRegulationName());
        lqw.eq(StringUtils.isNotBlank(bo.getRegulationDescribe()), RegulationInfo::getRegulationDescribe, bo.getRegulationDescribe());
        lqw.eq(StringUtils.isNotBlank(bo.getRegulationType()), RegulationInfo::getRegulationType, bo.getRegulationType());
        lqw.eq(StringUtils.isNotBlank(bo.getDocOssId()), RegulationInfo::getDocOssId, bo.getDocOssId());
        lqw.eq(bo.getUploadTime() != null, RegulationInfo::getUploadTime, bo.getUploadTime());
        return lqw;
    }

    /**
     * 新增制度管理
     */
    @Override
    public Boolean insertByBo(RegulationInfoBo bo) {
        RegulationInfo add = BeanUtil.toBean(bo, RegulationInfo.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setRegulationId(add.getRegulationId());
        }
        return flag;
    }

    /**
     * 修改制度管理
     */
    @Override
    public Boolean updateByBo(RegulationInfoBo bo) {
        RegulationInfo update = BeanUtil.toBean(bo, RegulationInfo.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(RegulationInfo entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除制度管理
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
