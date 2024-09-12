package com.cpems.web.controller.chargingStation;

import java.util.List;
import java.util.Arrays;

import lombok.RequiredArgsConstructor;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import com.cpems.common.annotation.RepeatSubmit;
import com.cpems.common.annotation.Log;
import com.cpems.common.core.controller.BaseController;
import com.cpems.common.core.domain.PageQuery;
import com.cpems.common.core.domain.R;
import com.cpems.common.core.validate.AddGroup;
import com.cpems.common.core.validate.EditGroup;
import com.cpems.common.enums.BusinessType;
import com.cpems.common.utils.poi.ExcelUtil;
import com.cpems.system.domain.vo.ChargingStationVo;
import com.cpems.system.domain.bo.ChargingStationBo;
import com.cpems.system.service.IChargingStationService;
import com.cpems.common.core.page.TableDataInfo;

/**
 * 充电站信息
 *
 * @author cpems
 * @date 2023-10-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/station")
public class ChargingStationController extends BaseController {

    private final IChargingStationService iChargingStationService;

    /**
     * 查询充电站信息列表
     */
    @SaCheckPermission("system:station:list")
    @GetMapping("/list")
    public TableDataInfo<ChargingStationVo> list(ChargingStationBo bo, PageQuery pageQuery) {
        return iChargingStationService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出充电站信息列表
     */
    @SaCheckPermission("system:station:export")
    @Log(title = "充电站信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ChargingStationBo bo, HttpServletResponse response) {
        List<ChargingStationVo> list = iChargingStationService.queryList(bo);
        ExcelUtil.exportExcel(list, "充电站信息", ChargingStationVo.class, response);
    }

    /**
     * 获取充电站信息详细信息
     *
     * @param stationId 主键
     */
    @SaCheckPermission("system:station:query")
    @GetMapping("/{stationId}")
    public R<ChargingStationVo> getInfo(@NotNull(message = "主键不能为空")
                                        @PathVariable Long stationId) {
        return R.ok(iChargingStationService.queryById(stationId));
    }

    /**
     * 新增充电站信息
     */
    @SaCheckPermission("system:station:add")
    @Log(title = "充电站信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ChargingStationBo bo) {
        return toAjax(iChargingStationService.insertByBo(bo));
    }

    /**
     * 修改充电站信息
     */
    @SaCheckPermission("system:station:edit")
    @Log(title = "充电站信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ChargingStationBo bo) {
        return toAjax(iChargingStationService.updateByBo(bo));
    }

    /**
     * 删除充电站信息
     *
     * @param stationIds 主键串
     */
    @SaCheckPermission("system:station:remove")
    @Log(title = "充电站信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{stationIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] stationIds) {
        return toAjax(iChargingStationService.deleteWithValidByIds(Arrays.asList(stationIds), true));
    }
}
