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
import com.cpems.system.domain.vo.ScheduleVo;
import com.cpems.system.domain.bo.ScheduleBo;
import com.cpems.system.service.IScheduleService;
import com.cpems.common.core.page.TableDataInfo;

/**
 * 调度计划管理
 *
 * @author cpems
 * @date 2023-09-11
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/schedule")
public class ScheduleController extends BaseController {

    private final IScheduleService iScheduleService;

    /**
     * 查询调度计划管理列表
     */
    @SaCheckPermission("system:schedule:list")
    @GetMapping("/list")
    public TableDataInfo<ScheduleVo> list(ScheduleBo bo, PageQuery pageQuery) {
        return iScheduleService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出调度计划管理列表
     */
    @SaCheckPermission("system:schedule:export")
    @Log(title = "调度计划管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ScheduleBo bo, HttpServletResponse response) {
        List<ScheduleVo> list = iScheduleService.queryList(bo);
        ExcelUtil.exportExcel(list, "调度计划管理", ScheduleVo.class, response);
    }

    /**
     * 获取调度计划管理详细信息
     *
     * @param scheduleId 主键
     */
    @SaCheckPermission("system:schedule:query")
    @GetMapping("/{scheduleId}")
    public R<ScheduleVo> getInfo(@NotNull(message = "主键不能为空")
                                 @PathVariable Long scheduleId) {
        return R.ok(iScheduleService.queryById(scheduleId));
    }

    /**
     * 新增调度计划管理
     */
    @SaCheckPermission("system:schedule:add")
    @Log(title = "调度计划管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ScheduleBo bo) {
        return toAjax(iScheduleService.insertByBo(bo));
    }

    /**
     * 修改调度计划管理
     */
    @SaCheckPermission("system:schedule:edit")
    @Log(title = "调度计划管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ScheduleBo bo) {
        return toAjax(iScheduleService.updateByBo(bo));
    }

    /**
     * 删除调度计划管理
     *
     * @param scheduleIds 主键串
     */
    @SaCheckPermission("system:schedule:remove")
    @Log(title = "调度计划管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{scheduleIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] scheduleIds) {
        return toAjax(iScheduleService.deleteWithValidByIds(Arrays.asList(scheduleIds), true));
    }
}
