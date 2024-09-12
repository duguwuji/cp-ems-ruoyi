package com.cpems.web.controller.inventory;

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
import com.cpems.system.domain.vo.PurveyorInfoVo;
import com.cpems.system.domain.bo.PurveyorInfoBo;
import com.cpems.system.service.IPurveyorInfoService;
import com.cpems.common.core.page.TableDataInfo;

/**
 * 供应商库管理
 *
 * @author cpems
 * @date 2023-09-11
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/purveyor")
public class PurveyorInfoController extends BaseController {

    private final IPurveyorInfoService iPurveyorInfoService;

    /**
     * 查询供应商库管理列表
     */
    @SaCheckPermission("system:purveyor:list")
    @GetMapping("/list")
    public TableDataInfo<PurveyorInfoVo> list(PurveyorInfoBo bo, PageQuery pageQuery) {
        return iPurveyorInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出供应商库管理列表
     */
    @SaCheckPermission("system:purveyor:export")
    @Log(title = "供应商库管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(PurveyorInfoBo bo, HttpServletResponse response) {
        List<PurveyorInfoVo> list = iPurveyorInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "供应商库管理", PurveyorInfoVo.class, response);
    }

    /**
     * 获取供应商库管理详细信息
     *
     * @param purveyorId 主键
     */
    @SaCheckPermission("system:purveyor:query")
    @GetMapping("/{purveyorId}")
    public R<PurveyorInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long purveyorId) {
        return R.ok(iPurveyorInfoService.queryById(purveyorId));
    }

    /**
     * 新增供应商库管理
     */
    @SaCheckPermission("system:purveyor:add")
    @Log(title = "供应商库管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody PurveyorInfoBo bo) {
        return toAjax(iPurveyorInfoService.insertByBo(bo));
    }

    /**
     * 修改供应商库管理
     */
    @SaCheckPermission("system:purveyor:edit")
    @Log(title = "供应商库管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody PurveyorInfoBo bo) {
        return toAjax(iPurveyorInfoService.updateByBo(bo));
    }

    /**
     * 删除供应商库管理
     *
     * @param purveyorIds 主键串
     */
    @SaCheckPermission("system:purveyor:remove")
    @Log(title = "供应商库管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{purveyorIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] purveyorIds) {
        return toAjax(iPurveyorInfoService.deleteWithValidByIds(Arrays.asList(purveyorIds), true));
    }
}
