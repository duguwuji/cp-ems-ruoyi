package com.cpems.system.service;
import com.cpems.common.core.domain.PageQuery;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.system.domain.bo.EnergyStatisticsBo;
import com.cpems.system.domain.vo.EnergyStatisticsVo;

import java.util.Collection;
import java.util.List;

/**
 * 能耗统计Service接口
 *
 * @author ruoyi
 * @date 2023-08-24
 */
public interface IEnergyStatisticsService {

    /**
     * 查询能耗统计
     */
    EnergyStatisticsVo queryById(Long id);

    /**
     * 查询能耗统计列表
     */
    TableDataInfo<EnergyStatisticsVo> queryPageList(EnergyStatisticsBo bo, PageQuery pageQuery);

    /**
     * 查询能耗统计列表
     */
    List<EnergyStatisticsVo> queryList(EnergyStatisticsBo bo);

    /**
     * 新增能耗统计
     */
    Boolean insertByBo(EnergyStatisticsBo bo);

    /**
     * 修改能耗统计
     */
    Boolean updateByBo(EnergyStatisticsBo bo);

    /**
     * 校验并批量删除能耗统计信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}

