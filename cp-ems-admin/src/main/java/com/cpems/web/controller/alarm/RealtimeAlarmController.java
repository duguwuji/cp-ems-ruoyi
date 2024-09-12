package com.cpems.web.controller.alarm;

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
import com.cpems.system.domain.vo.RealtimeAlarmVo;
import com.cpems.system.domain.bo.RealtimeAlarmBo;
import com.cpems.system.service.IRealtimeAlarmService;
import com.cpems.common.core.page.TableDataInfo;

/**
 * 实时报警
 *
 * @author cpems
 * @date 2023-04-04
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/alarm")
public class RealtimeAlarmController extends BaseController {

    private final IRealtimeAlarmService iRealtimeAlarmService;

    /**
     * 查询实时报警列表
     */
    @SaCheckPermission("system:alarm:list")
    @GetMapping("/list")
    public TableDataInfo<RealtimeAlarmVo> list(RealtimeAlarmBo bo, PageQuery pageQuery) {
        return iRealtimeAlarmService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出实时报警列表
     */
    @SaCheckPermission("system:alarm:export")
    @Log(title = "实时报警", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(RealtimeAlarmBo bo, HttpServletResponse response) {
        List<RealtimeAlarmVo> list = iRealtimeAlarmService.queryList(bo);
        ExcelUtil.exportExcel(list, "实时报警", RealtimeAlarmVo.class, response);
    }

    /**
     * 获取实时报警详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("system:alarm:query")
    @GetMapping("/{id}")
    public R<RealtimeAlarmVo> getInfo(@NotNull(message = "主键不能为空")
                                      @PathVariable Long id) {
        return R.ok(iRealtimeAlarmService.queryById(id));
    }

    /**
     * 新增实时报警
     */
    @SaCheckPermission("system:alarm:add")
    @Log(title = "实时报警", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody RealtimeAlarmBo bo) {
        return toAjax(iRealtimeAlarmService.insertByBo(bo));
    }

    /**
     * 修改实时报警
     */
    @SaCheckPermission("system:alarm:edit")
    @Log(title = "实时报警", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody RealtimeAlarmBo bo) {
        return toAjax(iRealtimeAlarmService.updateByBo(bo));
    }

    /**
     * 删除实时报警
     *
     * @param ids 主键串
     */
    @SaCheckPermission("system:alarm:remove")
    @Log(title = "实时报警", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iRealtimeAlarmService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
    /**
     * 获取全部不同报警等级的数量及占比
     */
//    @SaCheckPermission("system:alarm:list")
    @SaIgnore
    @GetMapping("/getCountOfAllStatus")
    public R<List<RealtimeAlarmVo>> getCountOfAllStatus() {
        return R.ok(iRealtimeAlarmService.getCountOfAllStatus());
    }
    /**
     * 获取最新若干条报警数据
     */
//    @SaCheckPermission("system:alarm:list")
    @GetMapping("/getLatestAlarmsByCount")
    public R<List<RealtimeAlarmVo>> getLatestAlarmsByCount(Long count) {
        return R.ok(iRealtimeAlarmService.getLatestAlarmsByCount(count));
    }


    /**
     * 获取报警参数统计
     */
    @SaIgnore
    @GetMapping("/getAlarmParameterStatistics")
    public R<List<RealtimeAlarmVo>> getAlarmParameterStatistics(String startTime,String endTime) {
        return R.ok(iRealtimeAlarmService.getAlarmParameterStatistics(startTime,endTime));
    }

    /**
     * 获取报警等级统计
     */
    @SaIgnore
    @GetMapping("/getAlarmLevelStatistics")
    public R<List<RealtimeAlarmVo>> getAlarmLevelStatistics(String startTime,String endTime) {
        return R.ok(iRealtimeAlarmService.getAlarmLevelStatistics(startTime,endTime));
    }

    /**
     * 获取报警数量统计
     */
    @SaIgnore
    @GetMapping("/getAlarmCountStatistics")
    public R<List<RealtimeAlarmVo>> getAlarmCountStatistics(String startTime,String endTime) {
        return R.ok(iRealtimeAlarmService.getAlarmCountStatistics(startTime,endTime));
    }

    /**
     * 获取报警类型统计
     */
    @SaIgnore
    @GetMapping("/getAlarmTypeStatistics")
    public R<List<RealtimeAlarmVo>> getAlarmTypeStatistics(String startTime,String endTime) {
        return R.ok(iRealtimeAlarmService.getAlarmTypeStatistics(startTime,endTime));
    }

    /**
     * 获取报警区域统计
     */
    @SaIgnore
    @GetMapping("/getAlarmAreaStatistics")
    public R<List<RealtimeAlarmVo>> getAlarmAreaStatistics(String startTime,String endTime) {
        return R.ok(iRealtimeAlarmService.getAlarmAreaStatistics(startTime,endTime));
    }
}
