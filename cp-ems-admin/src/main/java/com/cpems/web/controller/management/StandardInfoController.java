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
import com.cpems.system.domain.vo.StandardInfoVo;
import com.cpems.system.domain.bo.StandardInfoBo;
import com.cpems.system.service.IStandardInfoService;
import com.cpems.common.core.page.TableDataInfo;

/**
 * 作业规范
 *
 * @author cpems
 * @date 2023-09-11
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/standard")
public class StandardInfoController extends BaseController {

    private final IStandardInfoService iStandardInfoService;

    /**
     * 查询作业规范列表
     */
    @SaCheckPermission("system:standard:list")
    @GetMapping("/list")
    public TableDataInfo<StandardInfoVo> list(StandardInfoBo bo, PageQuery pageQuery) {
        return iStandardInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出作业规范列表
     */
    @SaCheckPermission("system:standard:export")
    @Log(title = "作业规范", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(StandardInfoBo bo, HttpServletResponse response) {
        List<StandardInfoVo> list = iStandardInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "作业规范", StandardInfoVo.class, response);
    }

    /**
     * 获取作业规范详细信息
     *
     * @param standardId 主键
     */
    @SaCheckPermission("system:standard:query")
    @GetMapping("/{standardId}")
    public R<StandardInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long standardId) {
        return R.ok(iStandardInfoService.queryById(standardId));
    }

    /**
     * 新增作业规范
     */
    @SaCheckPermission("system:standard:add")
    @Log(title = "作业规范", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody StandardInfoBo bo) {
        return toAjax(iStandardInfoService.insertByBo(bo));
    }

    /**
     * 修改作业规范
     */
    @SaCheckPermission("system:standard:edit")
    @Log(title = "作业规范", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody StandardInfoBo bo) {
        return toAjax(iStandardInfoService.updateByBo(bo));
    }

    /**
     * 删除作业规范
     *
     * @param standardIds 主键串
     */
    @SaCheckPermission("system:standard:remove")
    @Log(title = "作业规范", businessType = BusinessType.DELETE)
    @DeleteMapping("/{standardIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] standardIds) {
        return toAjax(iStandardInfoService.deleteWithValidByIds(Arrays.asList(standardIds), true));
    }
}
