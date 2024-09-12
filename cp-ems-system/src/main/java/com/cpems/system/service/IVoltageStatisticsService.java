package com.cpems.system.service;

import com.cpems.system.domain.vo.VoltageStatisticsVo;
import com.cpems.system.domain.bo.VoltageStatisticsBo;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 电压统计Service接口
 *
 * @author ruoyi
 * @date 2023-10-18
 */
public interface IVoltageStatisticsService {

    /**
     * 查询电压统计
     */
    VoltageStatisticsVo queryById(Long id);

    /**
     * 查询电压统计列表
     */
    TableDataInfo<VoltageStatisticsVo> queryPageList(VoltageStatisticsBo bo, PageQuery pageQuery);

    /**
     * 查询电压统计列表
     */
    List<VoltageStatisticsVo> queryList(VoltageStatisticsBo bo);

    /**
     * 新增电压统计
     */
    Boolean insertByBo(VoltageStatisticsBo bo);

    /**
     * 修改电压统计
     */
    Boolean updateByBo(VoltageStatisticsBo bo);

    /**
     * 校验并批量删除电压统计信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
