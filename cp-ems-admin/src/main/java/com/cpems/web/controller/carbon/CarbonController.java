package com.cpems.web.controller.carbon;

import com.cpems.common.core.controller.BaseController;
import com.cpems.common.core.domain.R;
import com.cpems.system.service.IEnergyService;
import com.cpems.system.service.IElectricityWService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 碳能耗
 *
 * @Author cpems
 * @Date 2023/5/26 10:31
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/data/carbon")
public class CarbonController extends BaseController {

    private final IElectricityWService iElectricityWService;
    private final IEnergyService energyService;

    /**
     * 获取指定日期的碳排放趋势（按小时）
     */
//    @SaCheckPermission("system:carbon:list")
    @GetMapping("/getHourlyData/{date}")
    public R<Map<Integer, BigDecimal>> getHourlyData(@NotNull(message = "日期不能为空") @PathVariable("date") String date) {
        return R.ok(iElectricityWService.getHourlyData(date));
    }

    /**
     * 获取指定日期所属月的碳排放趋势（按日）
     */
//    @SaCheckPermission("system:carbon:list")
    @GetMapping("/getDailyData/{date}")
    public R<Map<Integer,BigDecimal>> getDailyData(@NotNull(message = "日期不能为空") @PathVariable("date") String date) {
        return R.ok(iElectricityWService.getDailyData(date));
    }

    /**
     * 获取指定日期所属年的碳排放趋势（按月）
     */
//    @SaCheckPermission("system:carbon:list")
    @GetMapping("/getMonthlyData/{date}")
    public R<Map<Integer,BigDecimal>> getMonthlyData(@NotNull(message = "日期不能为空") @PathVariable("date") String date) {
        return R.ok(iElectricityWService.getMonthlyData(date));
    }

    /**
     * 获取指定日期所属日、月、年的碳排放数据
     */
//    @SaCheckPermission("system:carbon:list")
    @GetMapping("/getStatistic/{date}")
    public R<Map<String,BigDecimal>> getStatistic(@NotNull(message = "日期不能为空") @PathVariable("date") String date) {
        return R.ok(iElectricityWService.getStatistic(date));
    }

    /**
     * 碳排分析-本月，本年
     */
//    @SaCheckPermission("system:carbon:list")
    @GetMapping("/getChain")
    public R<Map<String,Object>> getChain() {
        return R.ok(energyService.getChain());
    }

    /**
     * 碳排分析-按年
     */
//    @SaCheckPermission("system:carbon:list")
    @GetMapping("/getChainByYear")
    public R<List<Object>> getChainByYear(String energyType, String date) {
        return R.ok(energyService.getChainByYear(energyType,date));
    }
}
