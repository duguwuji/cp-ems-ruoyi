package com.cpems.system.service;

import com.cpems.system.domain.vo.PowerStatisticsVo;
import com.cpems.system.domain.bo.PowerStatisticsBo;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 功率统计Service接口
 *
 * @author ruoyi
 * @date 2023-09-08
 */
public interface IPowerStatisticsService {

    /**
     * 查询功率统计
     */
    PowerStatisticsVo queryById(Long id);

    /**
     * 查询功率统计列表
     */
    TableDataInfo<PowerStatisticsVo> queryPageList(PowerStatisticsBo bo, PageQuery pageQuery);

    /**
     * 查询功率统计列表
     */
    List<PowerStatisticsVo> queryList(PowerStatisticsBo bo);

    /**
     * 新增功率统计
     */
    Boolean insertByBo(PowerStatisticsBo bo);

    /**
     * 修改功率统计
     */
    Boolean updateByBo(PowerStatisticsBo bo);

    /**
     * 校验并批量删除功率统计信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
