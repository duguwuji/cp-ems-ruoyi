package com.cpems.system.service;

import com.cpems.system.domain.vo.RealtimeAlarmVo;
import com.cpems.system.domain.bo.RealtimeAlarmBo;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 实时报警Service接口
 *
 * @author ruoyi
 * @date 2023-04-04
 */
public interface IRealtimeAlarmService {

    /**
     * 查询实时报警
     */
    RealtimeAlarmVo queryById(Long id);

    /**
     * 查询实时报警列表
     */
    TableDataInfo<RealtimeAlarmVo> queryPageList(RealtimeAlarmBo bo, PageQuery pageQuery);

    /**
     * 查询实时报警列表
     */
    List<RealtimeAlarmVo> queryList(RealtimeAlarmBo bo);

    /**
     * 新增实时报警
     */
    Boolean insertByBo(RealtimeAlarmBo bo);

    /**
     * 修改实时报警
     */
    Boolean updateByBo(RealtimeAlarmBo bo);

    /**
     * 校验并批量删除实时报警信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
    /**
     * 获取全部不同报警等级的数量及占比
     */
    List<RealtimeAlarmVo> getCountOfAllStatus();
    /**
     * 获取最新若干条报警数据
     */
    List<RealtimeAlarmVo> getLatestAlarmsByCount(Long count);

    /**
     * 获取报警参数统计
     */
    List<RealtimeAlarmVo> getAlarmParameterStatistics(String startTime,String endTime);

    /**
     * 获取报警等级统计
     */
    List<RealtimeAlarmVo> getAlarmLevelStatistics(String startTime,String endTime);

    /**
     * 获取报警数量统计
     */
    List<RealtimeAlarmVo> getAlarmCountStatistics(String startTime,String endTime);

    /**
     * 获取报警类型统计
     */
    List<RealtimeAlarmVo> getAlarmTypeStatistics(String startTime,String endTime);

    /**
     * 获取报警区域统计
     */
    List<RealtimeAlarmVo> getAlarmAreaStatistics(String startTime,String endTime);

}
