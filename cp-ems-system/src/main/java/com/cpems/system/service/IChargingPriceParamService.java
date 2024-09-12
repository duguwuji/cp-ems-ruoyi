package com.cpems.system.service;

import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;
import com.cpems.system.domain.ChargingPriceParam;
import com.cpems.system.domain.bo.ChargingPriceParamBo;
import com.cpems.system.domain.vo.ChargingPriceParamVo;

import java.util.Collection;
import java.util.List;

/**
 * 充电策略参数信息Service接口
 *
 * @author cpems
 * @date 2023-10-12
 */
public interface IChargingPriceParamService {

    /**
     * 查询充电策略参数信息
     */
    ChargingPriceParamVo queryById(Long id);

    /**
     * 查询充电策略参数信息列表
     */
    TableDataInfo<ChargingPriceParamVo> queryPageList(ChargingPriceParamBo bo, PageQuery pageQuery);

    /**
     * 查询充电策略参数信息列表
     */
    List<ChargingPriceParamVo> queryList(ChargingPriceParamBo bo);

    /**
     * 新增充电策略参数信息
     */
    Boolean insertByBo(ChargingPriceParamBo bo);

    /**
     * 修改充电策略参数信息
     */
    Boolean updateByBo(ChargingPriceParamBo bo);

    /**
     * 校验并批量删除充电策略参数信息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 更新充电策略的所有价格参数
     * @param paramList
     * @return
     */
    Boolean updateParamList(Collection<ChargingPriceParam> paramList);

}
