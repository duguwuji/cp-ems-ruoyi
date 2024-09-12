package com.cpems.web.controller.chargingStation;

import java.util.List;
import java.util.Arrays;

import com.cpems.system.domain.bo.OccupancyOrderBo;
import com.cpems.system.domain.vo.OccupancyOrderVo;
import com.cpems.system.service.IOccupancyOrderService;
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
 * 占位订单信息
 *
 * @author cpems
 * @date 2023-10-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/occupancyOrder")
public class OccupancyOrderController extends BaseController {

    private final IOccupancyOrderService iOccupancyOrderService;

    /**
     * 查询占位订单信息列表
     */
    @SaCheckPermission("system:occupancyOrder:list")
    @GetMapping("/list")
    public TableDataInfo<OccupancyOrderVo> list(OccupancyOrderBo bo, PageQuery pageQuery) {
        return iOccupancyOrderService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出占位订单信息列表
     */
    @SaCheckPermission("system:occupancyOrder:export")
    @Log(title = "占位订单信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(OccupancyOrderBo bo, HttpServletResponse response) {
        List<OccupancyOrderVo> list = iOccupancyOrderService.queryList(bo);
        ExcelUtil.exportExcel(list, "占位订单信息", OccupancyOrderVo.class, response);
    }

    /**
     * 获取占位订单信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("system:occupancyOrder:query")
    @GetMapping("/{id}")
    public R<OccupancyOrderVo> getInfo(@NotNull(message = "主键不能为空")
                                       @PathVariable Long id) {
        return R.ok(iOccupancyOrderService.queryById(id));
    }

    /**
     * 新增占位订单信息
     */
    @SaCheckPermission("system:occupancyOrder:add")
    @Log(title = "占位订单信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody OccupancyOrderBo bo) {
        return toAjax(iOccupancyOrderService.insertByBo(bo));
    }

    /**
     * 修改占位订单信息
     */
    @SaCheckPermission("system:occupancyOrder:edit")
    @Log(title = "占位订单信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody OccupancyOrderBo bo) {
        return toAjax(iOccupancyOrderService.updateByBo(bo));
    }

    /**
     * 删除占位订单信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("system:occupancyOrder:remove")
    @Log(title = "占位订单信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iOccupancyOrderService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
