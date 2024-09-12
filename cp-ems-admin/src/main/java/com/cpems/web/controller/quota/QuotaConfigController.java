package com.cpems.web.controller.quota;

import java.util.List;
import java.util.Arrays;
import java.util.Map;

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
import com.cpems.system.domain.vo.QuotaConfigVo;
import com.cpems.system.domain.bo.QuotaConfigBo;
import com.cpems.system.service.IQuotaConfigService;
import com.cpems.common.core.page.TableDataInfo;

/**
 * 定额配置
 *
 * @author cpems
 * @date 2023-09-12
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/quota")
public class QuotaConfigController extends BaseController {

    private final IQuotaConfigService iQuotaConfigService;

    /**
     * 查询定额配置列表
     */
    @SaCheckPermission("system:quota:list")
    @GetMapping("/list")
    public TableDataInfo<QuotaConfigVo> list(QuotaConfigBo bo, PageQuery pageQuery) {
        return iQuotaConfigService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出定额配置列表
     */
    @SaCheckPermission("system:quota:export")
    @Log(title = "定额配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(QuotaConfigBo bo, HttpServletResponse response) {
        List<QuotaConfigVo> list = iQuotaConfigService.queryList(bo);
        ExcelUtil.exportExcel(list, "定额配置", QuotaConfigVo.class, response);
    }

    /**
     * 获取定额配置详细信息
     *
     * @param quotaId 主键
     */
    @SaCheckPermission("system:quota:query")
    @GetMapping("/{quotaId}")
    public R<QuotaConfigVo> getInfo(@NotNull(message = "主键不能为空")
                                    @PathVariable Long quotaId) {
        return R.ok(iQuotaConfigService.queryById(quotaId));
    }

    /**
     * 新增定额配置
     */
    @SaCheckPermission("system:quota:add")
    @Log(title = "定额配置", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody QuotaConfigBo bo) {
        return toAjax(iQuotaConfigService.insertByBo(bo));
    }

    /**
     * 修改定额配置
     */
    @SaCheckPermission("system:quota:edit")
    @Log(title = "定额配置", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody QuotaConfigBo bo) {
        return toAjax(iQuotaConfigService.updateByBo(bo));
    }

    /**
     * 删除定额配置
     *
     * @param quotaIds 主键串
     */
    @SaCheckPermission("system:quota:remove")
    @Log(title = "定额配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{quotaIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] quotaIds) {
        return toAjax(iQuotaConfigService.deleteWithValidByIds(Arrays.asList(quotaIds), true));
    }

    /**
     * 定额分析
     * @return
     */
    @GetMapping("/analysis")
    public R<Map<String, Object>> analysis(Long itemId, String quotaTime) {
        return R.ok(iQuotaConfigService.analysis(itemId,quotaTime));
    }

    /**
     * 用量监测
     * @return
     */
    @GetMapping("/monitor")
    public R<Map<String, Object>> monitor(Long itemId, String quotaTime,String quotaType) {
        return R.ok(iQuotaConfigService.monitor(itemId,quotaTime,quotaType));
    }
}

