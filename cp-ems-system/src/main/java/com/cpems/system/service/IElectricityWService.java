package com.cpems.system.service;

import com.cpems.system.domain.vo.ElectricityWVo;
import com.cpems.system.domain.bo.ElectricityWBo;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;
import com.cpems.system.domain.vo.ItemTopologyVo;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 总用电量值Service接口
 *
 * @author cpems
 * @date 2023-04-21
 */
public interface IElectricityWService {

    /**
     * 查询总用电量值
     */
    ElectricityWVo queryById(Long id);

    /**
     * 查询总用电量值列表
     */
    TableDataInfo<ElectricityWVo> queryPageList(ElectricityWBo bo, PageQuery pageQuery);

    /**
     * 查询总用电量值列表
     */
    List<ElectricityWVo> queryList(ElectricityWBo bo);

    /**
     * 新增总用电量值
     */
    Boolean insertByBo(ElectricityWBo bo);

    /**
     * 修改总用电量值
     */
    Boolean updateByBo(ElectricityWBo bo);

    /**
     * 校验并批量删除总用电量值信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 获取指定日期用能统计
     */
    ElectricityWVo getByDay(String date);

    /**
     * 获取能源流向
     */
    List<ItemTopologyVo> getFlowData(String startTime, String endTime, String energyType,String status);

    /**
     * 获取指定日期的用电量趋势（按小时）
     */
    Map<Integer, BigDecimal> getHourlyData(String date);

    /**
     * 获取指定日期所属月的用电量趋势（按日）
     */
    Map<Integer,BigDecimal> getDailyData(String date);

    /**
     * 获取指定日期所属年的用电量趋势（按月）
     */
    Map<Integer,BigDecimal> getMonthlyData(String date);

    /**
     * 获取指定日期所属日、月、年的碳排放数据
     */
    Map<String,BigDecimal> getStatistic(String date);

    /**
     * 用能概况环比
     */
    Map<String,BigDecimal> getChain();


}
