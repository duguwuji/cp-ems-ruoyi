package com.cpems.web.controller.camera;

import java.util.List;
import java.util.Arrays;

import cn.dev33.satoken.annotation.SaIgnore;
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
import com.cpems.system.domain.vo.CameraConfigVo;
import com.cpems.system.domain.bo.CameraConfigBo;
import com.cpems.system.service.ICameraConfigService;
import com.cpems.common.core.page.TableDataInfo;

/**
 * 视频配置
 *
 * @author cpems
 * @date 2023-04-04
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/cameraConfig")
public class CameraConfigController extends BaseController {

    private final ICameraConfigService iCameraConfigService;

    /**
     * 查询视频配置列表
     */
    @SaCheckPermission("system:cameraConfig:list")
    @GetMapping("/list")
    public TableDataInfo<CameraConfigVo> list(CameraConfigBo bo, PageQuery pageQuery) {
        return iCameraConfigService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出视频配置列表
     */
    @SaCheckPermission("system:cameraConfig:export")
    @Log(title = "视频配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(CameraConfigBo bo, HttpServletResponse response) {
        List<CameraConfigVo> list = iCameraConfigService.queryList(bo);
        ExcelUtil.exportExcel(list, "视频配置", CameraConfigVo.class, response);
    }

    /**
     * 获取视频配置详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("system:cameraConfig:query")
    @GetMapping("/{id}")
    public R<CameraConfigVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iCameraConfigService.queryById(id));
    }

    /**
     * 新增视频配置
     */
    @SaCheckPermission("system:cameraConfig:add")
    @Log(title = "视频配置", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody CameraConfigBo bo) {
        return toAjax(iCameraConfigService.insertByBo(bo));
    }

    /**
     * 修改视频配置
     */
    @SaCheckPermission("system:cameraConfig:edit")
    @Log(title = "视频配置", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody CameraConfigBo bo) {
        return toAjax(iCameraConfigService.updateByBo(bo));
    }

    /**
     * 删除视频配置
     *
     * @param ids 主键串
     */
    @SaCheckPermission("system:cameraConfig:remove")
    @Log(title = "视频配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iCameraConfigService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 根据序列号返回视频地址
     *
     */
    @SaIgnore
    @GetMapping("/getUrlBySerialNumber")
    public R<CameraConfigVo> getUrlBySerialNumber(String serialNumber) {
        return R.ok(iCameraConfigService.getUrlBySerialNumber(serialNumber));
    }
}
