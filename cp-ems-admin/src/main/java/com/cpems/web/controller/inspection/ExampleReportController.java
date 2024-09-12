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
import com.cpems.system.domain.vo.ExampleReportVo;
import com.cpems.system.domain.bo.ExampleReportBo;
import com.cpems.system.service.IExampleReportService;
import com.cpems.common.core.page.TableDataInfo;

/**
 * 例报管理
 *
 * @author cpems
 * @date 2023-09-11
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/exampleReport")
public class ExampleReportController extends BaseController {

    private final IExampleReportService iExampleReportService;

    /**
     * 查询例报管理列表
     */
    @SaCheckPermission("system:exampleReport:list")
    @GetMapping("/list")
    public TableDataInfo<ExampleReportVo> list(ExampleReportBo bo, PageQuery pageQuery) {
        return iExampleReportService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出例报管理列表
     */
    @SaCheckPermission("system:exampleReport:export")
    @Log(title = "例报管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ExampleReportBo bo, HttpServletResponse response) {
        List<ExampleReportVo> list = iExampleReportService.queryList(bo);
        ExcelUtil.exportExcel(list, "例报管理", ExampleReportVo.class, response);
    }

    /**
     * 获取例报管理详细信息
     *
     * @param exampleReportId 主键
     */
    @SaCheckPermission("system:exampleReport:query")
    @GetMapping("/{exampleReportId}")
    public R<ExampleReportVo> getInfo(@NotNull(message = "主键不能为空")
                                      @PathVariable Long exampleReportId) {
        return R.ok(iExampleReportService.queryById(exampleReportId));
    }

    /**
     * 新增例报管理
     */
    @SaCheckPermission("system:exampleReport:add")
    @Log(title = "例报管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ExampleReportBo bo) {
        return toAjax(iExampleReportService.insertByBo(bo));
    }

    /**
     * 修改例报管理
     */
    @SaCheckPermission("system:exampleReport:edit")
    @Log(title = "例报管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ExampleReportBo bo) {
        return toAjax(iExampleReportService.updateByBo(bo));
    }

    /**
     * 删除例报管理
     *
     * @param exampleReportIds 主键串
     */
    @SaCheckPermission("system:exampleReport:remove")
    @Log(title = "例报管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{exampleReportIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] exampleReportIds) {
        return toAjax(iExampleReportService.deleteWithValidByIds(Arrays.asList(exampleReportIds), true));
    }
}
