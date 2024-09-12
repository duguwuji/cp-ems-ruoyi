package com.cpems.web.controller.chargingStation;

import java.util.List;
import java.util.Arrays;

import com.cpems.system.domain.bo.BrandBo;
import com.cpems.system.domain.vo.BrandVo;
import com.cpems.system.service.IBrandService;
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
 * 品牌信息
 *
 * @author cpems
 * @date 2023-10-10
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/brand")
public class BrandController extends BaseController {

    private final IBrandService iBrandService;

    /**
     * 查询品牌信息列表
     */
    @SaCheckPermission("system:brand:list")
    @GetMapping("/list")
    public TableDataInfo<BrandVo> list(BrandBo bo, PageQuery pageQuery) {
        return iBrandService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出品牌信息列表
     */
    @SaCheckPermission("system:brand:export")
    @Log(title = "品牌信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(BrandBo bo, HttpServletResponse response) {
        List<BrandVo> list = iBrandService.queryList(bo);
        ExcelUtil.exportExcel(list, "品牌信息", BrandVo.class, response);
    }

    /**
     * 获取品牌信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("system:brand:query")
    @GetMapping("/{id}")
    public R<BrandVo> getInfo(@NotNull(message = "主键不能为空")
                              @PathVariable Long id) {
        return R.ok(iBrandService.queryById(id));
    }

    /**
     * 新增品牌信息
     */
    @SaCheckPermission("system:brand:add")
    @Log(title = "品牌信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody BrandBo bo) {
        return toAjax(iBrandService.insertByBo(bo));
    }

    /**
     * 修改品牌信息
     */
    @SaCheckPermission("system:brand:edit")
    @Log(title = "品牌信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody BrandBo bo) {
        return toAjax(iBrandService.updateByBo(bo));
    }

    /**
     * 删除品牌信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("system:brand:remove")
    @Log(title = "品牌信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iBrandService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
