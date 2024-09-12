package com.cpems.web.controller.chargingStation;

import java.util.List;
import java.util.Arrays;

import com.cpems.system.domain.bo.OrderInfoBo;
import com.cpems.system.domain.vo.OrderInfoVo;
import com.cpems.system.service.IOrderInfoService;
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
 * 订单信息
 *
 * @author cpems
 * @date 2023-10-07
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/orderInfo")
public class OrderInfoController extends BaseController {

    private final IOrderInfoService iOrderInfoService;

    /**
     * 查询订单信息列表
     */
    @SaCheckPermission("system:orderInfo:list")
    @GetMapping("/list")
    public TableDataInfo<OrderInfoVo> list(OrderInfoBo bo, PageQuery pageQuery) {
        return iOrderInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出订单信息列表
     */
    @SaCheckPermission("system:orderInfo:export")
    @Log(title = "订单信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(OrderInfoBo bo, HttpServletResponse response) {
        List<OrderInfoVo> list = iOrderInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "订单信息", OrderInfoVo.class, response);
    }

    /**
     * 获取订单信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("system:orderInfo:query")
    @GetMapping("/{id}")
    public R<OrderInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                  @PathVariable Long id) {
        return R.ok(iOrderInfoService.queryById(id));
    }

    /**
     * 新增订单信息
     */
    @SaCheckPermission("system:orderInfo:add")
    @Log(title = "订单信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody OrderInfoBo bo) {
        return toAjax(iOrderInfoService.insertByBo(bo));
    }

    /**
     * 修改订单信息
     */
    @SaCheckPermission("system:orderInfo:edit")
    @Log(title = "订单信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody OrderInfoBo bo) {
        return toAjax(iOrderInfoService.updateByBo(bo));
    }

    /**
     * 删除订单信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("system:orderInfo:remove")
    @Log(title = "订单信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iOrderInfoService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
