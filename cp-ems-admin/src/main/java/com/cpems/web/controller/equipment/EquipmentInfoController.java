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
import com.cpems.system.domain.vo.EquipmentInfoVo;
import com.cpems.system.domain.bo.EquipmentInfoBo;
import com.cpems.system.service.IEquipmentInfoService;
import com.cpems.common.core.page.TableDataInfo;

/**
 * 设备信息
 *
 * @author cpems
 * @date 2023-04-06
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/equipment")
public class EquipmentInfoController extends BaseController {

    private final IEquipmentInfoService iEquipmentInfoService;

    /**
     * 查询设备信息列表
     */
    @SaCheckPermission("system:equipmentInfo:list")
    @GetMapping("/list")
    public TableDataInfo<EquipmentInfoVo> list(EquipmentInfoBo bo, PageQuery pageQuery) {
        return iEquipmentInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出设备信息列表
     */
    @SaCheckPermission("system:equipmentInfo:export")
    @Log(title = "设备信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(EquipmentInfoBo bo, HttpServletResponse response) {
        List<EquipmentInfoVo> list = iEquipmentInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "设备信息", EquipmentInfoVo.class, response);
    }

    /**
     * 获取设备信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("system:equipmentInfo:query")
    @GetMapping("/info/{id}")
    public R<EquipmentInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                      @PathVariable Long id) {
        return R.ok(iEquipmentInfoService.queryById(id));
    }

    /**
     * 新增设备信息
     */
    @SaCheckPermission("system:equipmentInfo:add")
    @Log(title = "设备信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody EquipmentInfoBo bo) {
        return toAjax(iEquipmentInfoService.insertByBo(bo));
    }

    /**
     * 修改设备信息
     */
    @SaCheckPermission("system:equipmentInfo:edit")
    @Log(title = "设备信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody EquipmentInfoBo bo) {
        return toAjax(iEquipmentInfoService.updateByBo(bo));
    }

    /**
     * 删除设备信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("system:equipmentInfo:remove")
    @Log(title = "设备信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        if (iEquipmentInfoService.hasBindArea(Arrays.asList(ids)))
        {
            return R.warn("该设备已绑定区域，不允许删除");
        }
        return toAjax(iEquipmentInfoService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
    /**
     * 获取全部不同状态的设备数量及占比
     */
//    @SaCheckPermission("system:equipmentInfo:list")
    @GetMapping("/getAllStatus")
    public R<List<EquipmentInfoVo>> getAllStatus() {
        return R.ok(iEquipmentInfoService.getAllStatus());
    }
}
