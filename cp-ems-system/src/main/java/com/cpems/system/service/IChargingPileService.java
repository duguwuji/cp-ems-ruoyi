package com.cpems.system.service;

import com.cpems.system.domain.ChargingPile;
import com.cpems.system.domain.vo.ChargingPileVo;
import com.cpems.system.domain.bo.ChargingPileBo;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 充电桩信息Service接口
 *
 * @author ruoyi
 * @date 2023-10-08
 */
public interface IChargingPileService {

    /**
     * 查询充电桩信息
     */
    ChargingPileVo queryById(Long pileId);

    /**
     * 查询充电桩信息列表
     */
    TableDataInfo<ChargingPileVo> queryPageList(ChargingPileBo bo, PageQuery pageQuery);

    /**
     * 查询充电桩信息列表
     */
    List<ChargingPileVo> queryList(ChargingPileBo bo);

    /**
     * 新增充电桩信息
     */
    Boolean insertByBo(ChargingPileBo bo);

    /**
     * 修改充电桩信息
     */
    Boolean updateByBo(ChargingPileBo bo);

    /**
     * 校验并批量删除充电桩信息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 批量启用/停用充电桩
     */
    Boolean openOrClose(Collection<ChargingPile> piles);
}
