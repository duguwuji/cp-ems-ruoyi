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
import com.cpems.system.domain.vo.InspectionRecordVo;
import com.cpems.system.domain.bo.InspectionRecordBo;
import com.cpems.system.service.IInspectionRecordService;
import com.cpems.common.core.page.TableDataInfo;

/**
 * 巡检记录
 *
 * @author cpems
 * @date 2023-04-04
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/record")
public class InspectionRecordController extends BaseController {

    private final IInspectionRecordService iInspectionRecordService;

    /**
     * 查询巡检记录列表
     */
    @SaCheckPermission("system:record:list")
    @GetMapping("/list")
    public TableDataInfo<InspectionRecordVo> list(InspectionRecordBo bo, PageQuery pageQuery) {
        return iInspectionRecordService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出巡检记录列表
     */
    @SaCheckPermission("system:record:export")
    @Log(title = "巡检记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(InspectionRecordBo bo, HttpServletResponse response) {
        List<InspectionRecordVo> list = iInspectionRecordService.queryList(bo);
        ExcelUtil.exportExcel(list, "巡检记录", InspectionRecordVo.class, response);
    }

    /**
     * 获取巡检记录详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("system:record:query")
    @GetMapping("/{id}")
    public R<InspectionRecordVo> getInfo(@NotNull(message = "主键不能为空")
                                         @PathVariable Long id) {
        return R.ok(iInspectionRecordService.queryById(id));
    }

    /**
     * 新增巡检记录
     */
    @SaCheckPermission("system:record:add")
    @Log(title = "巡检记录", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody InspectionRecordBo bo) {
        return toAjax(iInspectionRecordService.insertByBo(bo));
    }

    /**
     * 修改巡检记录
     */
    @SaCheckPermission("system:record:edit")
    @Log(title = "巡检记录", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody InspectionRecordBo bo) {
        return toAjax(iInspectionRecordService.updateByBo(bo));
    }

    /**
     * 删除巡检记录
     *
     * @param ids 主键串
     */
    @SaCheckPermission("system:record:remove")
    @Log(title = "巡检记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iInspectionRecordService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
