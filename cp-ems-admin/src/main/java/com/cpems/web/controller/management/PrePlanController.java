package com.cpems.web.controller.management;

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
import com.cpems.system.domain.vo.PrePlanVo;
import com.cpems.system.domain.bo.PrePlanBo;
import com.cpems.system.service.IPrePlanService;
import com.cpems.common.core.page.TableDataInfo;

/**
 * 预案管理
 *
 * @author cpems
 * @date 2023-09-11
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/prePlan")
public class PrePlanController extends BaseController {

    private final IPrePlanService iPrePlanService;

    /**
     * 查询预案管理列表
     */
    @SaCheckPermission("system:prePlan:list")
    @GetMapping("/list")
    public TableDataInfo<PrePlanVo> list(PrePlanBo bo, PageQuery pageQuery) {
        return iPrePlanService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出预案管理列表
     */
    @SaCheckPermission("system:prePlan:export")
    @Log(title = "预案管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(PrePlanBo bo, HttpServletResponse response) {
        List<PrePlanVo> list = iPrePlanService.queryList(bo);
        ExcelUtil.exportExcel(list, "预案管理", PrePlanVo.class, response);
    }

    /**
     * 获取预案管理详细信息
     *
     * @param prePlanId 主键
     */
    @SaCheckPermission("system:prePlan:query")
    @GetMapping("/{prePlanId}")
    public R<PrePlanVo> getInfo(@NotNull(message = "主键不能为空")
                                @PathVariable Long prePlanId) {
        return R.ok(iPrePlanService.queryById(prePlanId));
    }

    /**
     * 新增预案管理
     */
    @SaCheckPermission("system:prePlan:add")
    @Log(title = "预案管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody PrePlanBo bo) {
        return toAjax(iPrePlanService.insertByBo(bo));
    }

    /**
     * 修改预案管理
     */
    @SaCheckPermission("system:prePlan:edit")
    @Log(title = "预案管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody PrePlanBo bo) {
        return toAjax(iPrePlanService.updateByBo(bo));
    }

    /**
     * 删除预案管理
     *
     * @param prePlanIds 主键串
     */
    @SaCheckPermission("system:prePlan:remove")
    @Log(title = "预案管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{prePlanIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] prePlanIds) {
        return toAjax(iPrePlanService.deleteWithValidByIds(Arrays.asList(prePlanIds), true));
    }
}
