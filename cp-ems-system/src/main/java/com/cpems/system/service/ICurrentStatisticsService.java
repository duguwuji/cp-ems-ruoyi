package com.cpems.system.service;

import com.cpems.system.domain.vo.CurrentStatisticsVo;
import com.cpems.system.domain.bo.CurrentStatisticsBo;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 电流统计Service接口
 *
 * @author ruoyi
 * @date 2023-10-18
 */
public interface ICurrentStatisticsService {

    /**
     * 查询电流统计
     */
    CurrentStatisticsVo queryById(Long id);

    /**
     * 查询电流统计列表
     */
    TableDataInfo<CurrentStatisticsVo> queryPageList(CurrentStatisticsBo bo, PageQuery pageQuery);

    /**
     * 查询电流统计列表
     */
    List<CurrentStatisticsVo> queryList(CurrentStatisticsBo bo);

    /**
     * 新增电流统计
     */
    Boolean insertByBo(CurrentStatisticsBo bo);

    /**
     * 修改电流统计
     */
    Boolean updateByBo(CurrentStatisticsBo bo);

    /**
     * 校验并批量删除电流统计信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
