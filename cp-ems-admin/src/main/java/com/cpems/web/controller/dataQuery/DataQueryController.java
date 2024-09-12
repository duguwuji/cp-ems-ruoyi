package com.cpems.web.controller.dataQuery;

import cn.dev33.satoken.annotation.SaIgnore;
import com.cpems.common.core.controller.BaseController;
import com.cpems.common.core.domain.R;
import com.cpems.system.domain.vo.EnergyVo;
import com.cpems.system.domain.vo.PowerStatisticsVo;
import com.cpems.system.service.IEnergyService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 数据查询
 *
 * @Author cpems
 * @Date 2023/10/17 13:53
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/dataQuery")
public class DataQueryController extends BaseController {
    private final IEnergyService energyService;

    /**
     * 电力参数查询
     */
    @SaIgnore
    @GetMapping("/powerParameter")
    public R<List<EnergyVo>> queryPowerParameter(Long areaId, String startTime, String endTime,String energyType) {
        return R.ok(energyService.queryPowerParameter( areaId, startTime, endTime,energyType));
    }

    /**
     * 逐日极值查询
     */
    @SaIgnore
    @GetMapping("/dailyExtremum")
    public R<List<PowerStatisticsVo>> queryDailyExtremum(Long areaId, String startTime, String endTime, String energyType) {
        return R.ok(energyService.queryDailyExtremum(areaId, startTime, endTime,energyType));
    }

}
