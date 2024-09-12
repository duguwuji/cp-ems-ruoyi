package com.cpems.system.service;

import cn.hutool.core.lang.tree.Tree;
import com.cpems.system.domain.vo.ItemTopologyVo;
import com.cpems.system.domain.bo.ItemTopologyBo;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 项目拓扑Service接口
 *
 * @author ruoyi
 * @date 2023-04-06
 */
public interface IItemTopologyService {

    /**
     * 查询项目拓扑
     */
    ItemTopologyVo queryById(Long itemId);

    /**
     * 查询项目拓扑列表
     */
    TableDataInfo<ItemTopologyVo> queryPageList(ItemTopologyBo bo, PageQuery pageQuery);

    /**
     * 查询项目拓扑列表
     */
    List<ItemTopologyVo> queryList(ItemTopologyBo bo);

    /**
     * 新增项目拓扑
     */
    Boolean insertByBo(ItemTopologyBo bo);

    /**
     * 修改项目拓扑
     */
    Boolean updateByBo(ItemTopologyBo bo);

    /**
     * 校验并批量删除项目拓扑信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 获取拓扑树
     */
    List<Tree<Long>> selectTreeList(ItemTopologyBo bo);
}
