package com.cpems.web.controller.equipment;

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
import com.cpems.system.domain.vo.GatewayVo;
import com.cpems.system.domain.bo.GatewayBo;
import com.cpems.system.service.IGatewayService;
import com.cpems.common.core.page.TableDataInfo;

/**
 * 网关信息
 *
 * @author cpems
 * @date 2023-04-10
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/gateway")
public class GatewayController extends BaseController {

    private final IGatewayService iGatewayService;

    /**
     * 查询网关信息列表
     */
    @SaCheckPermission("system:gateway:list")
    @GetMapping("/list")
    public TableDataInfo<GatewayVo> list(GatewayBo bo, PageQuery pageQuery) {
        return iGatewayService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出网关信息列表
     */
    @SaCheckPermission("system:gateway:export")
    @Log(title = "网关信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(GatewayBo bo, HttpServletResponse response) {
        List<GatewayVo> list = iGatewayService.queryList(bo);
        ExcelUtil.exportExcel(list, "网关信息", GatewayVo.class, response);
    }

    /**
     * 获取网关信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("system:gateway:query")
    @GetMapping("/{id}")
    public R<GatewayVo> getInfo(@NotNull(message = "主键不能为空")
                                @PathVariable Long id) {
        return R.ok(iGatewayService.queryById(id));
    }

    /**
     * 新增网关信息
     */
    @SaCheckPermission("system:gateway:add")
    @Log(title = "网关信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody GatewayBo bo) {
        return toAjax(iGatewayService.insertByBo(bo));
    }

    /**
     * 修改网关信息
     */
    @SaCheckPermission("system:gateway:edit")
    @Log(title = "网关信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody GatewayBo bo) {
        return toAjax(iGatewayService.updateByBo(bo));
    }

    /**
     * 删除网关信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("system:gateway:remove")
    @Log(title = "网关信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iGatewayService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
