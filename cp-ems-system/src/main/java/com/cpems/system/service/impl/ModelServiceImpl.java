package com.cpems.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cpems.common.utils.StringUtils;
import com.cpems.system.domain.Model;
import com.cpems.system.domain.bo.ModelBo;
import com.cpems.system.domain.vo.ModelVo;
import com.cpems.system.mapper.ModelMapper;
import com.cpems.system.service.IModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 型号信息Service业务层处理
 *
 * @author cpems
 * @date 2023-10-11
 */
@RequiredArgsConstructor
@Service
public class ModelServiceImpl implements IModelService {

    private final ModelMapper baseMapper;

    /**
     * 查询型号信息
     */
    @Override
    public ModelVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询型号信息列表
     */
    @Override
    public TableDataInfo<ModelVo> queryPageList(ModelBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Model> lqw = buildQueryWrapper(bo);
        Page<ModelVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询型号信息列表
     */
    @Override
    public List<ModelVo> queryList(ModelBo bo) {
        LambdaQueryWrapper<Model> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Model> buildQueryWrapper(ModelBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Model> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getModelName()), Model::getModelName, bo.getModelName());
        lqw.eq(bo.getBrandId() != null, Model::getBrandId, bo.getBrandId());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), Model::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增型号信息
     */
    @Override
    public Boolean insertByBo(ModelBo bo) {
        Model add = BeanUtil.toBean(bo, Model.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改型号信息
     */
    @Override
    public Boolean updateByBo(ModelBo bo) {
        Model update = BeanUtil.toBean(bo, Model.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Model entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除型号信息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
