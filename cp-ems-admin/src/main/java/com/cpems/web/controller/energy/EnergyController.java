package com.cpems.web.controller.energy;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.lang.tree.Tree;
import com.cpems.common.core.controller.BaseController;
import com.cpems.common.core.domain.R;
import com.cpems.system.domain.bo.EnergyBo;
import com.cpems.system.domain.vo.*;
import com.cpems.system.service.IEnergyService;
import com.cpems.system.service.IElectricityWService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 综合能耗
 *
 * @Author cpems
 * @Date 2023/5/26 10:26
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/data/energy")
public class EnergyController extends BaseController {

    private final IElectricityWService iElectricityWService;
    private final IEnergyService energyService;
/*
    *//**
     * 获取指定日期用能统计
     *//*
//    @SaCheckPermission("system:energy:list")
    @GetMapping("/getByDay/{date}")
    public R<ElectricityWVo> getByDay(@NotNull(message = "日期不能为空") @PathVariable("date") String date) {
        return R.ok(iElectricityWService.getByDay(date));
    }*/

    /**
     * 获取能源流向
     */
//    @SaCheckPermission("system:energy:list")
    @GetMapping("/getFlowData")
    public R<List<ItemTopologyVo>> getFlowData(String startTime, String endTime, String energyType, String status) {
        return R.ok(iElectricityWService.getFlowData(startTime, endTime, energyType,status));
    }

//    /**
//     * 用能概况环比
//     */
////    @SaCheckPermission("system:electricity:list")
//    @GetMapping("/getChain")
//    public R<Map<String, BigDecimal>> getChain() {
//        return R.ok(iElectricityWService.getChain());
//    }

    /**
     * 环比分析
     */
//    @SaCheckPermission("system:electricity:list")
    @GetMapping("/getChainByDevice")
    public R<List<ChainDataVo>> getChainByDevice(String energyType, String dateType, String date,String areaId) {
        return R.ok(energyService.getChainByDevice(energyType,dateType,date,areaId));
    }
/*
    *//**
     * 获取指定日期的用电量趋势（按小时）
     *//*
//    @SaCheckPermission("system:electricity:list")
    @GetMapping("/getHourlyData/{date}")
    public R<Map<Integer, BigDecimal>> getHourlyData(@NotNull(message = "日期不能为空") @PathVariable("date") String date) {
        return R.ok(iElectricityWService.getHourlyData(date));
    }

    *//**
     * 获取指定日期所属月的用电量趋势（按日）
     *//*
//    @SaCheckPermission("system:electricity:list")
    @GetMapping("/getDailyData/{date}")
    public R<Map<Integer,BigDecimal>> getDailyData(@NotNull(message = "日期不能为空") @PathVariable("date") String date) {
        return R.ok(iElectricityWService.getDailyData(date));
    }

    *//**
     * 获取指定日期所属年的用电量趋势（按月）
     *//*
//    @SaCheckPermission("system:electricity:list")
    @GetMapping("/getMonthlyData/{date}")
    public R<Map<Integer,BigDecimal>> getMonthlyData(@NotNull(message = "日期不能为空") @PathVariable("date") String date) {
        return R.ok(iElectricityWService.getMonthlyData(date));
    }*/

    /**
     * 同比分析
     */
//    @SaCheckPermission("system:electricity:list")
    @SaIgnore
    @GetMapping("/getYearAnalysis")
    public R<List<YearAnalysisVo>> getYearAnalysis(Long areaId, String year,String energyType) {
        return R.ok(energyService.yearAnalysis(areaId,year,energyType));
    }

    /**
     * 损耗分析
     */
//    @SaCheckPermission("system:itemTopology:list")
    @SaIgnore
    @PostMapping("/getLossAnalysis")
    public R<List<Tree<Long>>> getLossAnalysis(@RequestBody EnergyBo bo) {
        return R.ok(energyService.lossAnalysis(bo));
    }

    /**
     * 用能概况-环比
     * @return
     */
    @GetMapping("/getChain")
    @SaIgnore
    public R<Map<String, Object>> getChain(@NotNull(message = "地点不能为空")  Long areaId,String energyType) {
        return R.ok(energyService.getChain(areaId,energyType));
    }

    /**
     * 用能概况-能耗趋势-日
     * @return
     */
    @GetMapping("/dayTrend")
    public R<List<Object>> getDayTrend(@NotNull(message = "地点不能为空") Long areaId, String nowTime,String energyType) {
        return R.ok(energyService.getDayTrend(areaId, nowTime,energyType));
    }

    /**
     * 用能概况-能耗趋势-月
     * @return
     */
    @GetMapping("/monthTrend")
    public R<List<Object>> getMonthTrend(@NotNull(message = "地点不能为空") Long areaId, String nowTime,String energyType) {
        return R.ok(energyService.getMonthTrend(areaId, nowTime,energyType));
    }

    /**
     * 用能概况-能耗趋势-年
     * @return
     */
    @GetMapping("/yearTrend")
    public R<List<Object>> getYearTrend(@NotNull(message = "地点不能为空") Long areaId, String nowTime,String energyType) {
        return R.ok(energyService.getYearTrend(areaId, nowTime,energyType));
    }

    /**
     * 用能概况-日用电功率曲线
     * @return
     */
    @SaIgnore
    @GetMapping("/getDailyP")
    public R<Map<String, Object>> getDailyP(@NotNull(message = "地点不能为空")  Long areaId,String energyType) {
        return R.ok(energyService.getDailyP(areaId,energyType));
    }

    /**
     * 能耗趋势-日
     * @param areaId
     * @param time
     * @return
     */
    @GetMapping("/getWTrendByDay")
    public R<Map<String, Object>> getWTrendByDay(@NotNull(message = "地点不能为空") Long areaId, @NotNull(message = "时间不能为空") String time,String energyType) {
        return R.ok(energyService.getWTrendByDay(areaId, time,energyType));
    }

    /**
     * 能耗趋势-月
     * @param areaId
     * @param time
     * @return
     */
    @GetMapping("/getWTrendByMonth")
    public R<Map<String, Object>> getWTrendByMonth(@NotNull(message = "地点不能为空") Long areaId, @NotNull(message = "时间不能为空") String time,String energyType) {
        return R.ok(energyService.getWTrendByMonth(areaId, time,energyType));
    }

    /**
     * 能耗趋势-年
     * @param areaId
     * @param time
     * @return
     */
    @GetMapping("/getWTrendByYear")
    public R<Map<String, Object>> getWTrendByYear(@NotNull(message = "地点不能为空") Long areaId, @NotNull(message = "时间不能为空") String time,String energyType) {
        return R.ok(energyService.getWTrendByYear(areaId, time,energyType));
    }

    /**
     * 分析报告-chart
     */
    @GetMapping("/getReportChart")
    @SaIgnore
    public R<Map<String, Object>> getReportChart(@NotNull(message = "地点不能为空") Long areaId, @NotNull(message = "开始时间不能为空") String startTime,@NotNull(message = "结束时间不能为空") String endTime,@NotNull(message = "能源类型不能为空")String energyType) {
        return R.ok(energyService.getReportChart(areaId, startTime,endTime,energyType));
    }

    /**
     * 分析报告-电费chart
     */
    @GetMapping("/getReportChargingChart")
    @SaIgnore
    public R<Map<String, Object>> getReportChargingChart(@NotNull(message = "地点不能为空") Long areaId, @NotNull(message = "开始时间不能为空") String startTime,@NotNull(message = "结束时间不能为空") String endTime,@NotNull(message = "能源类型不能为空")String energyType) {
        return R.ok(energyService.getReportChargingChart(areaId, startTime,endTime,energyType));
    }

    /**
     * 分析报告-table
     */
    @GetMapping("/getReportTable")
    public R<List<ReportTableVo>> getReportTable(@NotNull(message = "地点不能为空") Long areaId, @NotNull(message = "开始时间不能为空") String startTime, @NotNull(message = "结束时间不能为空") String endTime) {
        return R.ok(energyService.getReportTable(areaId, startTime,endTime));
    }

    /**
     * 分析报告-复费率table
     */
    @GetMapping("/getRecurringRate")
    @SaIgnore
    public R<List<ReportTableVo>> getRecurringRate(@NotNull(message = "地点不能为空") Long areaId, @NotNull(message = "开始时间不能为空") String startTime, @NotNull(message = "结束时间不能为空") String endTime,@NotNull(message = "能源类型不能为空")String energyType) {
        return R.ok(energyService.getRecurringRate(areaId, startTime,endTime,energyType));
    }

    /**
     * 费用报表-计算能耗
     */
    @GetMapping("/getConsumptionExpenseReport")
    @SaIgnore
    public R<List<ExpenseReportVo>> getConsumptionExpenseReport(@NotEmpty(message = "地点不能为空") Long[] areaIds, @NotNull(message = "查询时间类型") String dateType, @NotNull(message = "开始时间不能为空") String startTime, @NotNull(message = "结束时间不能为空") String endTime, @NotNull(message = "能源类型不能为空")String energyType) {
        return R.ok(energyService.getConsumptionExpenseReport(Arrays.asList(areaIds), dateType,startTime,endTime,energyType));
    }

    /**
     * 首页-今日用能统计
     * @return
     */
    @GetMapping("/getConsumptionStatistics")
    @SaIgnore
    public R<Map<String, Object>> getConsumptionStatistics(@NotNull(message = "地点不能为空")  Long areaId) {
        return R.ok(energyService.getConsumptionStatistics(areaId));
    }

    /**
     * 首页-今日能源趋势、碳排放量
     * @return
     */
    @GetMapping("/getTrendAndCarbon")
    @SaIgnore
    public R<Map<String, Object>> getTrendAndCarbon(@NotNull(message = "地点不能为空")  Long areaId) {
        return R.ok(energyService.getTrendAndCarbon(areaId));
    }
}
