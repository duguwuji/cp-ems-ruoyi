package com.cpems.web.controller.chargingStation;

import java.util.Collection;
import java.util.List;
import java.util.Arrays;

import com.cpems.system.domain.ChargingPriceParam;
import com.cpems.system.domain.bo.ChargingPriceParamBo;
import com.cpems.system.domain.vo.ChargingPriceParamVo;
import com.cpems.system.service.IChargingPriceParamService;
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
import com.cpems.common.core.page.TableDataInfo;

/**
 * 充电策略参数信息
 *
 * @author cpems
 * @date 2023-10-12
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/priceParam")
public class ChargingPriceParamController extends BaseController {

    private final IChargingPriceParamService iChargingPriceParamService;

    /**
     * 查询充电策略参数信息列表
     */
    @SaCheckPermission("system:priceStrategy:list")
    @GetMapping("/list")
    public TableDataInfo<ChargingPriceParamVo> list(ChargingPriceParamBo bo, PageQuery pageQuery) {
        return iChargingPriceParamService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出充电策略参数信息列表
     */
    @SaCheckPermission("system:priceStrategy:export")
    @Log(title = "充电策略参数信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ChargingPriceParamBo bo, HttpServletResponse response) {
        List<ChargingPriceParamVo> list = iChargingPriceParamService.queryList(bo);
        ExcelUtil.exportExcel(list, "充电策略参数信息", ChargingPriceParamVo.class, response);
    }

    /**
     * 获取充电策略参数信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("system:priceStrategy:query")
    @GetMapping("/{id}")
    public R<ChargingPriceParamVo> getInfo(@NotNull(message = "主键不能为空")
                                           @PathVariable Long id) {
        return R.ok(iChargingPriceParamService.queryById(id));
    }

    /**
     * 新增充电策略参数信息
     */
    @SaCheckPermission("system:priceStrategy:add")
    @Log(title = "充电策略参数信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ChargingPriceParamBo bo) {
        return toAjax(iChargingPriceParamService.insertByBo(bo));
    }

    /**
     * 修改充电策略参数信息
     */
    @SaCheckPermission("system:priceStrategy:edit")
    @Log(title = "充电策略参数信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ChargingPriceParamBo bo) {
        return toAjax(iChargingPriceParamService.updateByBo(bo));
    }

    /**
     * 删除充电策略参数信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("system:priceStrategy:remove")
    @Log(title = "充电策略参数信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iChargingPriceParamService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    @SaCheckPermission("system:priceStrategy:edit")
    @Log(title = "充电策略参数信息", businessType = BusinessType.UPDATE)
    @PutMapping("update")
    public R<Void> updateParamList(@RequestBody Collection<ChargingPriceParam> paramBos) {
        return toAjax(iChargingPriceParamService.updateParamList(paramBos));
    }
}
