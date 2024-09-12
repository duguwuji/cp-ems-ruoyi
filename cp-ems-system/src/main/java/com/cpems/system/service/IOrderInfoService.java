package com.cpems.system.service;

import com.cpems.common.core.domain.PageQuery;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.system.domain.bo.OrderInfoBo;
import com.cpems.system.domain.vo.OrderInfoVo;

import java.util.Collection;
import java.util.List;

/**
 * @Author cpems
 * @Date 2023/10/7 13:53
 **/
public interface IOrderInfoService {

    /**
     * 查询订单信息
     */
    OrderInfoVo queryById(Long id);

    /**
     * 查询订单信息列表
     */
    TableDataInfo<OrderInfoVo> queryPageList(OrderInfoBo bo, PageQuery pageQuery);

    /**
     * 查询订单信息列表
     */
    List<OrderInfoVo> queryList(OrderInfoBo bo);

    /**
     * 新增订单信息
     */
    Boolean insertByBo(OrderInfoBo bo);

    /**
     * 修改订单信息
     */
    Boolean updateByBo(OrderInfoBo bo);

    /**
     * 校验并批量删除订单信息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
