package com.cpems.web.controller.alarm;

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
import com.cpems.system.domain.vo.AlarmHistoryVo;
import com.cpems.system.domain.bo.AlarmHistoryBo;
import com.cpems.system.service.IAlarmHistoryService;
import com.cpems.common.core.page.TableDataInfo;

/**
 * 实时报警
 *
 * @author cpems
 * @date 2023-04-04
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/history")
public class AlarmHistoryController extends BaseController {

    private final IAlarmHistoryService iAlarmHistoryService;

    /**
     * 查询实时报警列表
     */
    @SaCheckPermission("system:history:list")
    @GetMapping("/list")
    public TableDataInfo<AlarmHistoryVo> list(AlarmHistoryBo bo, PageQuery pageQuery) {
        return iAlarmHistoryService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出实时报警列表
     */
    @SaCheckPermission("system:history:export")
    @Log(title = "实时报警", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(AlarmHistoryBo bo, HttpServletResponse response) {
        List<AlarmHistoryVo> list = iAlarmHistoryService.queryList(bo);
        ExcelUtil.exportExcel(list, "实时报警", AlarmHistoryVo.class, response);
    }

    /**
     * 获取实时报警详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("system:history:query")
    @GetMapping("/{id}")
    public R<AlarmHistoryVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iAlarmHistoryService.queryById(id));
    }

    /**
     * 新增实时报警
     */
    @SaCheckPermission("system:history:add")
    @Log(title = "实时报警", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody AlarmHistoryBo bo) {
        return toAjax(iAlarmHistoryService.insertByBo(bo));
    }

    /**
     * 修改实时报警
     */
    @SaCheckPermission("system:history:edit")
    @Log(title = "实时报警", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody AlarmHistoryBo bo) {
        return toAjax(iAlarmHistoryService.updateByBo(bo));
    }

    /**
     * 删除实时报警
     *
     * @param ids 主键串
     */
    @SaCheckPermission("system:history:remove")
    @Log(title = "实时报警", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iAlarmHistoryService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
