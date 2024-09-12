package com.cpems.system.service;

import cn.hutool.core.lang.tree.Tree;
import com.cpems.system.domain.bo.EnergyBo;
import com.cpems.system.domain.vo.*;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 能源值Service接口
 *
 * @author cpems
 * @date 2023-04-20
 */
public interface IEnergyService {

    /**
     * 根据设备环比分析
     */
    List<ChainDataVo> getChainByDevice(String energyType, String dateType, String date, String areaId);

    /**
     * 同比分析
     */
    List<YearAnalysisVo> yearAnalysis(Long areaId, String year, String energyType);

    /**
     * 损耗分析
     */
    List<Tree<Long>> lossAnalysis(EnergyBo bo);

    /**
     * 用能概况-环比
     *
     * @param areaId
     * @return
     */
    Map<String, Object> getChain(Long areaId, String energyType);

    /**
     * 用能概况-用能趋势-日
     *
     * @param areaId
     * @param nowTime
     * @return
     */
    List<Object> getDayTrend(Long areaId, String nowTime, String energyType);

    /**
     * 用能概况-用你能趋势-月
     *
     * @param areaId
     * @param nowTime
     * @return
     */
    List<Object> getMonthTrend(Long areaId, String nowTime, String energyType);

    /**
     * 用能概况-用能趋势-年
     *
     * @param areaId
     * @param nowTime
     * @return
     */
    List<Object> getYearTrend(Long areaId, String nowTime, String energyType);

    /**
     * 用能概况-日用电功率曲线
     *
     * @param areaId
     * @return
     */
    Map<String, Object> getDailyP(Long areaId, String energyType);

    /**
     * 能耗趋势-日
     *
     * @param areaId
     * @param time
     * @return
     */
    Map<String, Object> getWTrendByDay(Long areaId, String time, String energyType);

    /**
     * 能耗趋势-月
     *
     * @param areaId
     * @param time
     * @return
     */
    Map<String, Object> getWTrendByMonth(Long areaId, String time, String energyType);

    /**
     * 能耗趋势-年
     *
     * @param areaId
     * @param time
     * @return
     */
    Map<String, Object> getWTrendByYear(Long areaId, String time, String energyType);

    /**
     * 碳排分析-本月，本年
     */
    Map<String, Object> getChain();

    /**
     * 碳排分析-按年
     */
    List<Object> getChainByYear(String energyType, String date);

    /**
     * 分析报告-chart
     */
    Map<String, Object> getReportChart(Long areaId, String startTime, String endTime, String energyType);

    /**
     * 分析报告-table
     */
    List<ReportTableVo> getReportTable(Long areaId, String startTime, String endTime);

    /**
     * 统计当前一小时能耗
     *
     * @param energyType
     */
    Boolean statisticsEnergyByHour(String energyType);

    /**
     * 统计当前一小时电功率峰谷值平均值
     *
     * @param energyType
     */
    Boolean statisticsPowerByHour(String energyType);

    /**
     * 统计当前一小时电流峰谷值平均值
     *
     * @param energyType
     */
    Boolean statisticsCurrentByHour(String energyType);


    /**
     * 统计当前一小时电压峰谷值平均值
     *
     * @param energyType
     */
    Boolean statisticsVoltageByHour(String energyType);

    /**
     * 实际累计能耗
     */
    BigDecimal getAccumulate(Long itemId, String quotaType, Date quotaTime);

    /**
     * 电力参数查询
     */
    List<EnergyVo> queryPowerParameter(Long areaId, String startTime, String endTime, String energyType);

    /**
     * 逐日极值查询
     */
    List<PowerStatisticsVo> queryDailyExtremum(Long areaId, String startTime, String endTime, String energyType);

    /**
     * 分析报告-电费chart
     */
    Map<String, Object> getReportChargingChart(Long areaId, String startTime, String endTime, String energyType);

    /**
     * 分析报告-复费率table
     */
    List<ReportTableVo> getRecurringRate(Long areaId, String startTime, String endTime, String energyType);

    /**
     * 费用报表-能耗查询
     */
    List<ExpenseReportVo> getConsumptionExpenseReport(Collection<Long> areaIds, String dateType, String startTime, String endTime, String energyType);

    /**
     * 首页-今日用能统计
     */
    Map<String, Object> getConsumptionStatistics(Long areaId);

    /**
     * 首页-今日能源趋势、碳排放量
     */
    Map<String, Object> getTrendAndCarbon(Long areaId);

}
