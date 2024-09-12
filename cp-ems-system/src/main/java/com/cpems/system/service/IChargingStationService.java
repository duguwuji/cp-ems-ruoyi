package com.cpems.system.service;

import com.cpems.system.domain.vo.ChargingStationVo;
import com.cpems.system.domain.bo.ChargingStationBo;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 充电站信息Service接口
 *
 * @author ruoyi
 * @date 2023-10-08
 */
public interface IChargingStationService {

    /**
     * 查询充电站信息
     */
    ChargingStationVo queryById(Long stationId);

    /**
     * 查询充电站信息列表
     */
    TableDataInfo<ChargingStationVo> queryPageList(ChargingStationBo bo, PageQuery pageQuery);

    /**
     * 查询充电站信息列表
     */
    List<ChargingStationVo> queryList(ChargingStationBo bo);

    /**
     * 新增充电站信息
     */
    Boolean insertByBo(ChargingStationBo bo);

    /**
     * 修改充电站信息
     */
    Boolean updateByBo(ChargingStationBo bo);

    /**
     * 校验并批量删除充电站信息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
