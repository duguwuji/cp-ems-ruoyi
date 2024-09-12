package com.cpems.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.ObjectUtil;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cpems.common.utils.StringUtils;
import com.cpems.common.utils.TreeBuildUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.cpems.system.domain.bo.ItemTopologyBo;
import com.cpems.system.domain.vo.ItemTopologyVo;
import com.cpems.system.domain.ItemTopology;
import com.cpems.system.mapper.ItemTopologyMapper;
import com.cpems.system.service.IItemTopologyService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 项目拓扑Service业务层处理
 *
 * @author ruoyi
 * @date 2023-04-06
 */
@RequiredArgsConstructor
@Service
public class ItemTopologyServiceImpl implements IItemTopologyService {

    private final ItemTopologyMapper baseMapper;

    /**
     * 查询项目拓扑
     */
    @Override
    public ItemTopologyVo queryById(Long itemId) {
        ItemTopologyVo result =  baseMapper.selectVoById(itemId);
        if (ObjectUtil.isNotEmpty(result.getDeviceId())){
            result.setDeviceIds(Arrays.stream(StringUtils.split(result.getDeviceId(), ",")).collect(Collectors.toList()));
        }
        return result;
    }

    /**
     * 查询项目拓扑列表
     */
    @Override
    public TableDataInfo<ItemTopologyVo> queryPageList(ItemTopologyBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ItemTopology> lqw = buildQueryWrapper(bo);
        Page<ItemTopologyVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询项目拓扑列表
     */
    @Override
    public List<ItemTopologyVo> queryList(ItemTopologyBo bo) {
        LambdaQueryWrapper<ItemTopology> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ItemTopology> buildQueryWrapper(ItemTopologyBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ItemTopology> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getParentId() != null, ItemTopology::getParentId, bo.getParentId());
        lqw.eq(StringUtils.isNotBlank(bo.getAncestors()), ItemTopology::getAncestors, bo.getAncestors());
        lqw.like(StringUtils.isNotBlank(bo.getItemName()), ItemTopology::getItemName, bo.getItemName());
        lqw.eq(bo.getOrderNum() != null, ItemTopology::getOrderNum, bo.getOrderNum());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), ItemTopology::getStatus, bo.getStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getItemType()), ItemTopology::getItemType, bo.getItemType());
        lqw.eq(bo.getCreateTime() != null, ItemTopology::getCreateTime, bo.getCreateTime());
        return lqw;
    }

    /**
     * 新增项目拓扑
     */
    @Override
    public Boolean insertByBo(ItemTopologyBo bo) {
        ItemTopology add = BeanUtil.toBean(bo, ItemTopology.class);
        validEntityBeforeSave(add);
        add.setDeviceId("");
        //绑定设备同步修改
        if (ObjectUtil.isNotEmpty(bo.getDeviceIds())) {
            add.setDeviceId(StringUtils.join(bo.getDeviceIds(),","));
        }
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setItemId(add.getItemId());
        }
        return flag;
    }

    /**
     * 修改项目拓扑
     */
    @Override
    public Boolean updateByBo(ItemTopologyBo bo) {
        ItemTopology update = BeanUtil.toBean(bo, ItemTopology.class);
        validEntityBeforeSave(update);
        update.setDeviceId("");
        //绑定设备同步修改
        if (ObjectUtil.isNotEmpty(bo.getDeviceIds())) {
            update.setDeviceId(StringUtils.join(bo.getDeviceIds(),","));
        }
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ItemTopology entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除项目拓扑
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    /**
     * 查询拓扑树结构信息
     * @param bo
     * @return
     */
    @Override
    public List<Tree<Long>> selectTreeList(ItemTopologyBo bo) {
        LambdaQueryWrapper<ItemTopology> lqw = buildQueryWrapper(bo);
        List<ItemTopologyVo> topologyVoList = baseMapper.selectVoList(lqw);
        return buildTreeSelect(topologyVoList);
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param voList 拓扑列表
     * @return 下拉树结构列表
     */
//    @Override
    public List<Tree<Long>> buildTreeSelect(List<ItemTopologyVo> voList) {
        if (CollUtil.isEmpty(voList)) {
            return CollUtil.newArrayList();
        }
        return TreeBuildUtils.build(voList, (topologyVo, tree) ->
            tree.setId(topologyVo.getItemId())
                .setParentId(topologyVo.getParentId())
                .setName(topologyVo.getItemName())
                .setWeight(topologyVo.getOrderNum()));
    }
}
