package com.cpems.web.controller.chargingStation;

import java.util.Collection;
import java.util.List;
import java.util.Arrays;

import com.cpems.system.domain.ChargingPile;
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
import com.cpems.system.domain.vo.ChargingPileVo;
import com.cpems.system.domain.bo.ChargingPileBo;
import com.cpems.system.service.IChargingPileService;
import com.cpems.common.core.page.TableDataInfo;

/**
 * 充电桩信息
 *
 * @author cpems
 * @date 2023-10-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/pile")
public class ChargingPileController extends BaseController {

    private final IChargingPileService iChargingPileService;

    /**
     * 查询充电桩信息列表
     */
    @SaCheckPermission("system:pile:list")
    @GetMapping("/list")
    public TableDataInfo<ChargingPileVo> list(ChargingPileBo bo, PageQuery pageQuery) {
        return iChargingPileService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出充电桩信息列表
     */
    @SaCheckPermission("system:pile:export")
    @Log(title = "充电桩信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ChargingPileBo bo, HttpServletResponse response) {
        List<ChargingPileVo> list = iChargingPileService.queryList(bo);
        ExcelUtil.exportExcel(list, "充电桩信息", ChargingPileVo.class, response);
    }

    /**
     * 获取充电桩信息详细信息
     *
     * @param pileId 主键
     */
    @SaCheckPermission("system:pile:query")
    @GetMapping("/{pileId}")
    public R<ChargingPileVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long pileId) {
        return R.ok(iChargingPileService.queryById(pileId));
    }

    /**
     * 新增充电桩信息
     */
    @SaCheckPermission("system:pile:add")
    @Log(title = "充电桩信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ChargingPileBo bo) {
        return toAjax(iChargingPileService.insertByBo(bo));
    }

    /**
     * 修改充电桩信息
     */
    @SaCheckPermission("system:pile:edit")
    @Log(title = "充电桩信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ChargingPileBo bo) {
        return toAjax(iChargingPileService.updateByBo(bo));
    }

    /**
     * 删除充电桩信息
     *
     * @param pileIds 主键串
     */
    @SaCheckPermission("system:pile:remove")
    @Log(title = "充电桩信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{pileIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] pileIds) {
        return toAjax(iChargingPileService.deleteWithValidByIds(Arrays.asList(pileIds), true));
    }

    /**
     * 批量开启/停用充电桩
     */
    @SaCheckPermission("system:pile:edit")
    @Log(title = "充电桩信息", businessType = BusinessType.UPDATE)
    @PutMapping("/openOrClose")
    public R<Void> openOrClose(@RequestBody Collection<ChargingPile> piles) {
        return toAjax(iChargingPileService.openOrClose(piles));
    }
}
