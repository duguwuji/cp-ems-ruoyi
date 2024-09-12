package com.cpems.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.cpems.system.domain.bo.GatewayBo;
import com.cpems.system.domain.vo.GatewayVo;
import com.cpems.system.domain.Gateway;
import com.cpems.system.mapper.GatewayMapper;
import com.cpems.system.service.IGatewayService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 网关信息Service业务层处理
 *
 * @author ruoyi
 * @date 2023-04-10
 */
@RequiredArgsConstructor
@Service
public class GatewayServiceImpl implements IGatewayService {

    private final GatewayMapper baseMapper;

    /**
     * 查询网关信息
     */
    @Override
    public GatewayVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询网关信息列表
     */
    @Override
    public TableDataInfo<GatewayVo> queryPageList(GatewayBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Gateway> lqw = buildQueryWrapper(bo);
        Page<GatewayVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询网关信息列表
     */
    @Override
    public List<GatewayVo> queryList(GatewayBo bo) {
        LambdaQueryWrapper<Gateway> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Gateway> buildQueryWrapper(GatewayBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Gateway> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), Gateway::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getSn()), Gateway::getSn, bo.getSn());
        lqw.eq(StringUtils.isNotBlank(bo.getModel()), Gateway::getModel, bo.getModel());
        lqw.eq(StringUtils.isNotBlank(bo.getDescription()), Gateway::getDescription, bo.getDescription());
        lqw.eq(StringUtils.isNotBlank(bo.getTimeZone()), Gateway::getTimeZone, bo.getTimeZone());
        lqw.eq(StringUtils.isNotBlank(bo.getQrCode()), Gateway::getQrCode, bo.getQrCode());
        lqw.eq(StringUtils.isNotBlank(bo.getFactory()), Gateway::getFactory, bo.getFactory());
        lqw.eq(bo.getAreaId() != null, Gateway::getAreaId, bo.getAreaId());
        lqw.like(StringUtils.isNotBlank(bo.getArea()), Gateway::getArea, bo.getArea());
        return lqw;
    }

    /**
     * 新增网关信息
     */
    @Override
    public Boolean insertByBo(GatewayBo bo) {
        Gateway add = BeanUtil.toBean(bo, Gateway.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改网关信息
     */
    @Override
    public Boolean updateByBo(GatewayBo bo) {
        Gateway update = BeanUtil.toBean(bo, Gateway.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Gateway entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除网关信息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
