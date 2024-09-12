package com.cpems.system.service;

import com.cpems.system.domain.vo.ChargingVo;
import com.cpems.system.domain.bo.ChargingBo;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 计费方案信息Service接口
 *
 * @author ruoyi
 * @date 2023-10-30
 */
public interface IChargingService {

    /**
     * 查询计费方案信息
     */
    ChargingVo queryById(Long chargingId);

    /**
     * 查询计费方案信息列表
     */
    TableDataInfo<ChargingVo> queryPageList(ChargingBo bo, PageQuery pageQuery);

    /**
     * 查询计费方案信息列表
     */
    List<ChargingVo> queryList(ChargingBo bo);

    /**
     * 新增计费方案信息
     */
    Boolean insertByBo(ChargingBo bo);

    /**
     * 修改计费方案信息
     */
    Boolean updateByBo(ChargingBo bo);

    /**
     * 校验并批量删除计费方案信息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * @param bo
     * @return
     */
    Long insertCharging(ChargingBo bo);
}
