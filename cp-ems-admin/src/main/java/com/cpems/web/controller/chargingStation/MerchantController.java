package com.cpems.web.controller.chargingStation;

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
import com.cpems.system.domain.vo.MerchantVo;
import com.cpems.system.domain.bo.MerchantBo;
import com.cpems.system.service.IMerchantService;
import com.cpems.common.core.page.TableDataInfo;

/**
 * 商户信息
 *
 * @author cpems
 * @date 2023-10-07
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/merchant")
public class MerchantController extends BaseController {

    private final IMerchantService iMerchantService;

    /**
     * 查询商户信息列表
     */
    @SaCheckPermission("system:merchant:list")
    @GetMapping("/list")
    public TableDataInfo<MerchantVo> list(MerchantBo bo, PageQuery pageQuery) {
        return iMerchantService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出商户信息列表
     */
    @SaCheckPermission("system:merchant:export")
    @Log(title = "商户信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MerchantBo bo, HttpServletResponse response) {
        List<MerchantVo> list = iMerchantService.queryList(bo);
        ExcelUtil.exportExcel(list, "商户信息", MerchantVo.class, response);
    }

    /**
     * 获取商户信息详细信息
     *
     * @param merchantId 主键
     */
    @SaCheckPermission("system:merchant:query")
    @GetMapping("/{merchantId}")
    public R<MerchantVo> getInfo(@NotNull(message = "主键不能为空")
                                 @PathVariable Long merchantId) {
        return R.ok(iMerchantService.queryById(merchantId));
    }

    /**
     * 新增商户信息
     */
    @SaCheckPermission("system:merchant:add")
    @Log(title = "商户信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody MerchantBo bo) {
        return toAjax(iMerchantService.insertByBo(bo));
    }

    /**
     * 修改商户信息
     */
    @SaCheckPermission("system:merchant:edit")
    @Log(title = "商户信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody MerchantBo bo) {
        return toAjax(iMerchantService.updateByBo(bo));
    }

    /**
     * 删除商户信息
     *
     * @param merchantIds 主键串
     */
    @SaCheckPermission("system:merchant:remove")
    @Log(title = "商户信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{merchantIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] merchantIds) {
        return toAjax(iMerchantService.deleteWithValidByIds(Arrays.asList(merchantIds), true));
    }
}
