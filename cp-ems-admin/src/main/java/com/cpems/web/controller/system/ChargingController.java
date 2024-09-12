package com.cpems.web.controller.system;

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
import com.cpems.system.domain.vo.ChargingVo;
import com.cpems.system.domain.bo.ChargingBo;
import com.cpems.system.service.IChargingService;
import com.cpems.common.core.page.TableDataInfo;

/**
 * 计费方案信息
 *
 * @author cpems
 * @date 2023-10-30
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/charging")
public class ChargingController extends BaseController {

    private final IChargingService iChargingService;

    /**
     * 查询计费方案信息列表
     */
    @SaCheckPermission("system:charging:list")
    @GetMapping("/list")
    public TableDataInfo<ChargingVo> list(ChargingBo bo, PageQuery pageQuery) {
        return iChargingService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出计费方案信息列表
     */
    @SaCheckPermission("system:charging:export")
    @Log(title = "计费方案信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ChargingBo bo, HttpServletResponse response) {
        List<ChargingVo> list = iChargingService.queryList(bo);
        ExcelUtil.exportExcel(list, "计费方案信息", ChargingVo.class, response);
    }

    /**
     * 获取计费方案信息详细信息
     *
     * @param chargingId 主键
     */
    @SaCheckPermission("system:charging:query")
    @GetMapping("/{chargingId}")
    public R<ChargingVo> getInfo(@NotNull(message = "主键不能为空")
                                 @PathVariable Long chargingId) {
        return R.ok(iChargingService.queryById(chargingId));
    }

    /**
     * 新增计费方案信息
     */
    @SaCheckPermission("system:charging:add")
    @Log(title = "计费方案信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ChargingBo bo) {
        return toAjax(iChargingService.insertByBo(bo));
    }

    /**
     * 修改计费方案信息
     */
    @SaCheckPermission("system:charging:edit")
    @Log(title = "计费方案信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ChargingBo bo) {
        return toAjax(iChargingService.updateByBo(bo));
    }

    /**
     * 删除计费方案信息
     *
     * @param chargingIds 主键串
     */
    @SaCheckPermission("system:charging:remove")
    @Log(title = "计费方案信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{chargingIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] chargingIds) {
        return toAjax(iChargingService.deleteWithValidByIds(Arrays.asList(chargingIds), true));
    }

    /**
     * 新增充电价格策略信息
     */
    @SaCheckPermission("system:charging:add")
    @Log(title = "充电价格策略信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/insert")
    public R<Long> insert(@Validated(AddGroup.class) @RequestBody ChargingBo bo) {
        return R.ok(iChargingService.insertCharging(bo));
    }
}
