package com.cpems.web.controller.inspection;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.cpems.common.annotation.Log;
import com.cpems.common.annotation.RepeatSubmit;
import com.cpems.common.core.controller.BaseController;
import com.cpems.common.core.domain.PageQuery;
import com.cpems.common.core.domain.R;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.validate.AddGroup;
import com.cpems.common.core.validate.EditGroup;
import com.cpems.common.enums.BusinessType;
import com.cpems.common.utils.poi.ExcelUtil;
import com.cpems.system.domain.bo.DutyBo;
import com.cpems.system.domain.vo.DutyVo;
import com.cpems.system.service.IDutyService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 值班管理
 *
 * @author cpems
 * @date 2023-09-11
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/duty")
public class DutyController extends BaseController {

    private final IDutyService iDutyService;

    /**
     * 查询值班管理列表
     */
    @SaCheckPermission("system:duty:list")
    @GetMapping("/list")
    public TableDataInfo<DutyVo> list(DutyBo bo, PageQuery pageQuery) {
        return iDutyService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出值班管理列表
     */
    @SaCheckPermission("system:duty:export")
    @Log(title = "值班管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DutyBo bo, HttpServletResponse response) {
        List<DutyVo> list = iDutyService.queryList(bo);
        ExcelUtil.exportExcel(list, "值班管理", DutyVo.class, response);
    }

    /**
     * 获取值班管理详细信息
     *
     * @param dutyId 主键
     */
    @SaCheckPermission("system:duty:query")
    @GetMapping("/{dutyId}")
    public R<DutyVo> getInfo(@NotNull(message = "主键不能为空")
                             @PathVariable Long dutyId) {
        return R.ok(iDutyService.queryById(dutyId));
    }

    /**
     * 新增值班管理
     */
    @SaCheckPermission("system:duty:add")
    @Log(title = "值班管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DutyBo bo) {
        return toAjax(iDutyService.insertByBo(bo));
    }

    /**
     * 修改值班管理
     */
    @SaCheckPermission("system:duty:edit")
    @Log(title = "值班管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DutyBo bo) {
        return toAjax(iDutyService.updateByBo(bo));
    }

    /**
     * 删除值班管理
     *
     * @param dutyIds 主键串
     */
    @SaCheckPermission("system:duty:remove")
    @Log(title = "值班管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dutyIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] dutyIds) {
        return toAjax(iDutyService.deleteWithValidByIds(Arrays.asList(dutyIds), true));
    }
}
