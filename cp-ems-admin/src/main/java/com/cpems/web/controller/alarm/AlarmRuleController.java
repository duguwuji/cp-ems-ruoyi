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
import com.cpems.system.domain.vo.AlarmRuleVo;
import com.cpems.system.domain.bo.AlarmRuleBo;
import com.cpems.system.service.IAlarmRuleService;
import com.cpems.common.core.page.TableDataInfo;

/**
 * 报警规则
 *
 * @author cpems
 * @date 2023-04-06
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/alarmRule")
public class AlarmRuleController extends BaseController {

    private final IAlarmRuleService iAlarmRuleService;

    /**
     * 查询报警规则列表
     */
    @SaCheckPermission("system:alarmRule:list")
    @GetMapping("/list")
    public TableDataInfo<AlarmRuleVo> list(AlarmRuleBo bo, PageQuery pageQuery) {
        return iAlarmRuleService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出报警规则列表
     */
    @SaCheckPermission("system:alarmRule:export")
    @Log(title = "报警规则", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(AlarmRuleBo bo, HttpServletResponse response) {
        List<AlarmRuleVo> list = iAlarmRuleService.queryList(bo);
        ExcelUtil.exportExcel(list, "报警规则", AlarmRuleVo.class, response);
    }

    /**
     * 获取报警规则详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("system:alarmRule:query")
    @GetMapping("/{id}")
    public R<AlarmRuleVo> getInfo(@NotNull(message = "主键不能为空")
                                  @PathVariable Long id) {
        return R.ok(iAlarmRuleService.queryById(id));
    }

    /**
     * 新增报警规则
     */
    @SaCheckPermission("system:alarmRule:add")
    @Log(title = "报警规则", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody AlarmRuleBo bo) {
        return toAjax(iAlarmRuleService.insertByBo(bo));
    }

    /**
     * 修改报警规则
     */
    @SaCheckPermission("system:alarmRule:edit")
    @Log(title = "报警规则", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody AlarmRuleBo bo) {
        return toAjax(iAlarmRuleService.updateByBo(bo));
    }

    /**
     * 删除报警规则
     *
     * @param ids 主键串
     */
    @SaCheckPermission("system:alarmRule:remove")
    @Log(title = "报警规则", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iAlarmRuleService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
