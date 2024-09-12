package com.cpems.system.service;

import com.cpems.system.domain.vo.ScheduleVo;
import com.cpems.system.domain.bo.ScheduleBo;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 调度计划管理Service接口
 *
 * @author ruoyi
 * @date 2023-09-11
 */
public interface IScheduleService {

    /**
     * 查询调度计划管理
     */
    ScheduleVo queryById(Long scheduleId);

    /**
     * 查询调度计划管理列表
     */
    TableDataInfo<ScheduleVo> queryPageList(ScheduleBo bo, PageQuery pageQuery);

    /**
     * 查询调度计划管理列表
     */
    List<ScheduleVo> queryList(ScheduleBo bo);

    /**
     * 新增调度计划管理
     */
    Boolean insertByBo(ScheduleBo bo);

    /**
     * 修改调度计划管理
     */
    Boolean updateByBo(ScheduleBo bo);

    /**
     * 校验并批量删除调度计划管理信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
