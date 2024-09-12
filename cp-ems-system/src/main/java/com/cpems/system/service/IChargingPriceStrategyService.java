package com.cpems.system.service;

import com.cpems.system.domain.bo.ChargingPriceStrategyBo;
import com.cpems.system.domain.vo.ChargingPriceStrategyVo;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 充电价格策略信息Service接口
 *
 * @author cpems
 * @date 2023-10-12
 */
public interface IChargingPriceStrategyService {

    /**
     * 查询充电价格策略信息
     */
    ChargingPriceStrategyVo queryById(Long id);

    /**
     * 查询充电价格策略信息列表
     */
    TableDataInfo<ChargingPriceStrategyVo> queryPageList(ChargingPriceStrategyBo bo, PageQuery pageQuery);

    /**
     * 查询充电价格策略信息列表
     */
    List<ChargingPriceStrategyVo> queryList(ChargingPriceStrategyBo bo);

    /**
     * 新增充电价格策略信息
     */
    Boolean insertByBo(ChargingPriceStrategyBo bo);

    /**
     * 修改充电价格策略信息
     */
    Boolean updateByBo(ChargingPriceStrategyBo bo);

    /**
     * 校验并批量删除充电价格策略信息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 新增价格策略并返回id
     * @param bo
     * @return
     */
    Long insertPriceStrategy(ChargingPriceStrategyBo bo);
}
