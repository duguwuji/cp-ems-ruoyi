package com.cpems.system.service;

import com.cpems.system.domain.bo.OccupancyOrderBo;
import com.cpems.system.domain.vo.OccupancyOrderVo;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 占位订单信息Service接口
 *
 * @author cpems
 * @date 2023-10-08
 */
public interface IOccupancyOrderService {

    /**
     * 查询占位订单信息
     */
    OccupancyOrderVo queryById(Long id);

    /**
     * 查询占位订单信息列表
     */
    TableDataInfo<OccupancyOrderVo> queryPageList(OccupancyOrderBo bo, PageQuery pageQuery);

    /**
     * 查询占位订单信息列表
     */
    List<OccupancyOrderVo> queryList(OccupancyOrderBo bo);

    /**
     * 新增占位订单信息
     */
    Boolean insertByBo(OccupancyOrderBo bo);

    /**
     * 修改占位订单信息
     */
    Boolean updateByBo(OccupancyOrderBo bo);

    /**
     * 校验并批量删除占位订单信息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
