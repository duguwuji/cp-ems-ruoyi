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
import com.cpems.system.domain.vo.RegulationInfoVo;
import com.cpems.system.domain.bo.RegulationInfoBo;
import com.cpems.system.service.IRegulationInfoService;
import com.cpems.common.core.page.TableDataInfo;

/**
 * 制度管理
 *
 * @author cpems
 * @date 2023-09-11
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/regulation")
public class RegulationInfoController extends BaseController {

    private final IRegulationInfoService iRegulationInfoService;

    /**
     * 查询制度管理列表
     */
    @SaCheckPermission("system:regulation:list")
    @GetMapping("/list")
    public TableDataInfo<RegulationInfoVo> list(RegulationInfoBo bo, PageQuery pageQuery) {
        return iRegulationInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出制度管理列表
     */
    @SaCheckPermission("system:regulation:export")
    @Log(title = "制度管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(RegulationInfoBo bo, HttpServletResponse response) {
        List<RegulationInfoVo> list = iRegulationInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "制度管理", RegulationInfoVo.class, response);
    }

    /**
     * 获取制度管理详细信息
     *
     * @param regulationId 主键
     */
    @SaCheckPermission("system:regulation:query")
    @GetMapping("/{regulationId}")
    public R<RegulationInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                       @PathVariable Long regulationId) {
        return R.ok(iRegulationInfoService.queryById(regulationId));
    }

    /**
     * 新增制度管理
     */
    @SaCheckPermission("system:regulation:add")
    @Log(title = "制度管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody RegulationInfoBo bo) {
        return toAjax(iRegulationInfoService.insertByBo(bo));
    }

    /**
     * 修改制度管理
     */
    @SaCheckPermission("system:regulation:edit")
    @Log(title = "制度管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody RegulationInfoBo bo) {
        return toAjax(iRegulationInfoService.updateByBo(bo));
    }

    /**
     * 删除制度管理
     *
     * @param regulationIds 主键串
     */
    @SaCheckPermission("system:regulation:remove")
    @Log(title = "制度管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{regulationIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] regulationIds) {
        return toAjax(iRegulationInfoService.deleteWithValidByIds(Arrays.asList(regulationIds), true));
    }
}

