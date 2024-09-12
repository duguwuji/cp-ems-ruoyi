package com.cpems.web.controller.chargingStation;

import java.util.List;
import java.util.Arrays;

import com.cpems.system.domain.bo.ModelBo;
import com.cpems.system.domain.vo.ModelVo;
import com.cpems.system.service.IModelService;
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
import com.cpems.common.core.page.TableDataInfo;

/**
 * 型号信息
 *
 * @author cpems
 * @date 2023-10-11
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/model")
public class ModelController extends BaseController {

    private final IModelService iModelService;

    /**
     * 查询型号信息列表
     */
    @SaCheckPermission("system:brand:list")
    @GetMapping("/list")
    public TableDataInfo<ModelVo> list(ModelBo bo, PageQuery pageQuery) {
        return iModelService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出型号信息列表
     */
    @SaCheckPermission("system:brand:export")
    @Log(title = "型号信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ModelBo bo, HttpServletResponse response) {
        List<ModelVo> list = iModelService.queryList(bo);
        ExcelUtil.exportExcel(list, "型号信息", ModelVo.class, response);
    }

    /**
     * 获取型号信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("system:brand:query")
    @GetMapping("/{id}")
    public R<ModelVo> getInfo(@NotNull(message = "主键不能为空")
                              @PathVariable Long id) {
        return R.ok(iModelService.queryById(id));
    }

    /**
     * 新增型号信息
     */
    @SaCheckPermission("system:brand:add")
    @Log(title = "型号信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ModelBo bo) {
        return toAjax(iModelService.insertByBo(bo));
    }

    /**
     * 修改型号信息
     */
    @SaCheckPermission("system:brand:edit")
    @Log(title = "型号信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ModelBo bo) {
        return toAjax(iModelService.updateByBo(bo));
    }

    /**
     * 删除型号信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("system:brand:remove")
    @Log(title = "型号信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iModelService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
