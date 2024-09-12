package com.cpems.web.controller.water;

import com.cpems.common.core.controller.BaseController;
import com.cpems.common.core.domain.R;
import com.cpems.system.service.IElectricityWService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 水能耗
 *
 * @Author cpems
 * @Date 2023/5/26 10:28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/data/water")
public class WaterController extends BaseController {

    private final IElectricityWService iElectricityWService;

    /**
     * 获取指定日期的用水量趋势（按小时）
     */
//    @SaCheckPermission("system:water:list")
    @GetMapping("/getHourlyData/{date}")
    public R<Map<Integer, BigDecimal>> getHourlyData(@NotNull(message = "日期不能为空") @PathVariable("date") String date) {
        return R.ok(iElectricityWService.getHourlyData(date));
    }

    /**
     * 获取指定日期所属月的用水量趋势（按日）
     */
//    @SaCheckPermission("system:water:list")
    @GetMapping("/getDailyData/{date}")
    public R<Map<Integer,BigDecimal>> getDailyData(@NotNull(message = "日期不能为空") @PathVariable("date") String date) {
        return R.ok(iElectricityWService.getDailyData(date));
    }

    /**
     * 获取指定日期所属年的用水量趋势（按月）
     */
//    @SaCheckPermission("system:water:list")
    @GetMapping("/getMonthlyData/{date}")
    public R<Map<Integer,BigDecimal>> getMonthlyData(@NotNull(message = "日期不能为空") @PathVariable("date") String date) {
        return R.ok(iElectricityWService.getMonthlyData(date));
    }
}
