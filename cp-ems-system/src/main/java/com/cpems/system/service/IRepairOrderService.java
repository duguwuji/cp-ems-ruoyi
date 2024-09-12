package com.cpems.system.service;

import com.cpems.system.domain.vo.RepairOrderVo;
import com.cpems.system.domain.bo.RepairOrderBo;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 维修工单Service接口
 *
 * @author ruoyi
 * @date 2023-04-04
 */
public interface IRepairOrderService {

    /**
     * 查询维修工单
     */
    RepairOrderVo queryById(Long id);

    /**
     * 查询维修工单列表
     */
    TableDataInfo<RepairOrderVo> queryPageList(RepairOrderBo bo, PageQuery pageQuery);

    /**
     * 查询维修工单列表
     */
    List<RepairOrderVo> queryList(RepairOrderBo bo);

    /**
     * 新增维修工单
     */
    Boolean insertByBo(RepairOrderBo bo);

    /**
     * 修改维修工单
     */
    Boolean updateByBo(RepairOrderBo bo);

    /**
     * 校验并批量删除维修工单信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
