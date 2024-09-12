package com.cpems.web.controller.inspection;

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
import com.cpems.system.domain.vo.RepairOrderVo;
import com.cpems.system.domain.bo.RepairOrderBo;
import com.cpems.system.service.IRepairOrderService;
import com.cpems.common.core.page.TableDataInfo;

/**
 * 维修工单
 *
 * @author cpems
 * @date 2023-04-04
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/order")
public class RepairOrderController extends BaseController {

    private final IRepairOrderService iRepairOrderService;

    /**
     * 查询维修工单列表
     */
    @SaCheckPermission("system:order:list")
    @GetMapping("/list")
    public TableDataInfo<RepairOrderVo> list(RepairOrderBo bo, PageQuery pageQuery) {
        return iRepairOrderService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出维修工单列表
     */
    @SaCheckPermission("system:order:export")
    @Log(title = "维修工单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(RepairOrderBo bo, HttpServletResponse response) {
        List<RepairOrderVo> list = iRepairOrderService.queryList(bo);
        ExcelUtil.exportExcel(list, "维修工单", RepairOrderVo.class, response);
    }

    /**
     * 获取维修工单详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("system:order:query")
    @GetMapping("/{id}")
    public R<RepairOrderVo> getInfo(@NotNull(message = "主键不能为空")
                                    @PathVariable Long id) {
        return R.ok(iRepairOrderService.queryById(id));
    }

    /**
     * 新增维修工单
     */
    @SaCheckPermission("system:order:add")
    @Log(title = "维修工单", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody RepairOrderBo bo) {
        return toAjax(iRepairOrderService.insertByBo(bo));
    }

    /**
     * 修改维修工单
     */
    @SaCheckPermission("system:order:edit")
    @Log(title = "维修工单", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody RepairOrderBo bo) {
        return toAjax(iRepairOrderService.updateByBo(bo));
    }

    /**
     * 删除维修工单
     *
     * @param ids 主键串
     */
    @SaCheckPermission("system:order:remove")
    @Log(title = "维修工单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iRepairOrderService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
