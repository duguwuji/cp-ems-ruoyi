package com.cpems.web.controller.chargingStation;

import java.util.List;
import java.util.Arrays;

import com.cpems.system.domain.bo.ChargingPriceStrategyBo;
import com.cpems.system.domain.vo.ChargingPriceStrategyVo;
import com.cpems.system.service.IChargingPriceStrategyService;
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
 * 充电价格策略信息
 *
 * @author cpems
 * @date 2023-10-12
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/priceStrategy")
public class ChargingPriceStrategyController extends BaseController {

    private final IChargingPriceStrategyService iChargingPriceStrategyService;

    /**
     * 查询充电价格策略信息列表
     */
    @SaCheckPermission("system:priceStrategy:list")
    @GetMapping("/list")
    public TableDataInfo<ChargingPriceStrategyVo> list(ChargingPriceStrategyBo bo, PageQuery pageQuery) {
        return iChargingPriceStrategyService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出充电价格策略信息列表
     */
    @SaCheckPermission("system:priceStrategy:export")
    @Log(title = "充电价格策略信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ChargingPriceStrategyBo bo, HttpServletResponse response) {
        List<ChargingPriceStrategyVo> list = iChargingPriceStrategyService.queryList(bo);
        ExcelUtil.exportExcel(list, "充电价格策略信息", ChargingPriceStrategyVo.class, response);
    }

    /**
     * 获取充电价格策略信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("system:priceStrategy:query")
    @GetMapping("/{id}")
    public R<ChargingPriceStrategyVo> getInfo(@NotNull(message = "主键不能为空")
                                              @PathVariable Long id) {
        return R.ok(iChargingPriceStrategyService.queryById(id));
    }

    /**
     * 新增充电价格策略信息
     */
    @SaCheckPermission("system:priceStrategy:add")
    @Log(title = "充电价格策略信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ChargingPriceStrategyBo bo) {
        return toAjax(iChargingPriceStrategyService.insertByBo(bo));
    }

    /**
     * 修改充电价格策略信息
     */
    @SaCheckPermission("system:priceStrategy:edit")
    @Log(title = "充电价格策略信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ChargingPriceStrategyBo bo) {
        return toAjax(iChargingPriceStrategyService.updateByBo(bo));
    }

    /**
     * 删除充电价格策略信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("system:priceStrategy:remove")
    @Log(title = "充电价格策略信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iChargingPriceStrategyService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 新增充电价格策略信息
     */
    @SaCheckPermission("system:priceStrategy:add")
    @Log(title = "充电价格策略信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/insert")
    public R<Long> insert(@Validated(AddGroup.class) @RequestBody ChargingPriceStrategyBo bo) {
        return R.ok(iChargingPriceStrategyService.insertPriceStrategy(bo));
    }
}
