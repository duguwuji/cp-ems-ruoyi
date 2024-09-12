package com.cpems.web.controller.system;

import java.util.Collection;
import java.util.List;
import java.util.Arrays;

import com.cpems.system.domain.ChargingStep;
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
import com.cpems.system.domain.vo.ChargingStepVo;
import com.cpems.system.domain.bo.ChargingStepBo;
import com.cpems.system.service.IChargingStepService;
import com.cpems.common.core.page.TableDataInfo;

/**
 * 阶梯计费参数信息
 *
 * @author cpems
 * @date 2023-10-31
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/chargingStep")
public class ChargingStepController extends BaseController {

    private final IChargingStepService iChargingStepService;

    /**
     * 查询阶梯计费参数信息列表
     */
    @SaCheckPermission("system:chargingStep:list")
    @GetMapping("/list")
    public TableDataInfo<ChargingStepVo> list(ChargingStepBo bo, PageQuery pageQuery) {
        return iChargingStepService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出阶梯计费参数信息列表
     */
    @SaCheckPermission("system:chargingStep:export")
    @Log(title = "阶梯计费参数信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ChargingStepBo bo, HttpServletResponse response) {
        List<ChargingStepVo> list = iChargingStepService.queryList(bo);
        ExcelUtil.exportExcel(list, "阶梯计费参数信息", ChargingStepVo.class, response);
    }

    /**
     * 获取阶梯计费参数信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("system:chargingStep:query")
    @GetMapping("/{id}")
    public R<ChargingStepVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iChargingStepService.queryById(id));
    }

    /**
     * 新增阶梯计费参数信息
     */
    @SaCheckPermission("system:chargingStep:add")
    @Log(title = "阶梯计费参数信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ChargingStepBo bo) {
        return toAjax(iChargingStepService.insertByBo(bo));
    }

    /**
     * 修改阶梯计费参数信息
     */
    @SaCheckPermission("system:chargingStep:edit")
    @Log(title = "阶梯计费参数信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ChargingStepBo bo) {
        return toAjax(iChargingStepService.updateByBo(bo));
    }

    /**
     * 删除阶梯计费参数信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("system:chargingStep:remove")
    @Log(title = "阶梯计费参数信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iChargingStepService.deleteWithValidByIds(Arrays.asList(ids), true));
    }


    @SaCheckPermission("system:chargingStep:edit")
    @Log(title = "充电策略参数信息", businessType = BusinessType.UPDATE)
    @PutMapping("update")
    public R<Void> updateChargingStepList(@RequestBody Collection<ChargingStep> paramBos) {
        return toAjax(iChargingStepService.updateChargingStepList(paramBos));
    }
}
