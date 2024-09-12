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
import com.cpems.system.domain.vo.ProcessInfoVo;
import com.cpems.system.domain.bo.ProcessInfoBo;
import com.cpems.system.service.IProcessInfoService;
import com.cpems.common.core.page.TableDataInfo;

/**
 * 流程管理
 *
 * @author cpems
 * @date 2023-09-11
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/process")
public class ProcessInfoController extends BaseController {

    private final IProcessInfoService iProcessInfoService;

    /**
     * 查询流程管理列表
     */
    @SaCheckPermission("system:process:list")
    @GetMapping("/list")
    public TableDataInfo<ProcessInfoVo> list(ProcessInfoBo bo, PageQuery pageQuery) {
        return iProcessInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出流程管理列表
     */
    @SaCheckPermission("system:process:export")
    @Log(title = "流程管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ProcessInfoBo bo, HttpServletResponse response) {
        List<ProcessInfoVo> list = iProcessInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "流程管理", ProcessInfoVo.class, response);
    }

    /**
     * 获取流程管理详细信息
     *
     * @param processId 主键
     */
    @SaCheckPermission("system:process:query")
    @GetMapping("/{processId}")
    public R<ProcessInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                    @PathVariable Long processId) {
        return R.ok(iProcessInfoService.queryById(processId));
    }

    /**
     * 新增流程管理
     */
    @SaCheckPermission("system:process:add")
    @Log(title = "流程管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ProcessInfoBo bo) {
        return toAjax(iProcessInfoService.insertByBo(bo));
    }

    /**
     * 修改流程管理
     */
    @SaCheckPermission("system:process:edit")
    @Log(title = "流程管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ProcessInfoBo bo) {
        return toAjax(iProcessInfoService.updateByBo(bo));
    }

    /**
     * 删除流程管理
     *
     * @param processIds 主键串
     */
    @SaCheckPermission("system:process:remove")
    @Log(title = "流程管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{processIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] processIds) {
        return toAjax(iProcessInfoService.deleteWithValidByIds(Arrays.asList(processIds), true));
    }
}

