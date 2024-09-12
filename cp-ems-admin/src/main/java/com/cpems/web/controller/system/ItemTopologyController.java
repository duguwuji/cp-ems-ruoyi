package com.cpems.web.controller.system;

import java.util.List;
import java.util.Arrays;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.tree.Tree;
import com.cpems.common.utils.StringUtils;
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
import com.cpems.system.domain.vo.ItemTopologyVo;
import com.cpems.system.domain.bo.ItemTopologyBo;
import com.cpems.system.service.IItemTopologyService;

/**
 * 项目拓扑
 *
 * @author cpems
 * @date 2023-04-06
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/itemTopology")
public class ItemTopologyController extends BaseController {

    private final IItemTopologyService iItemTopologyService;

    /**
     * 查询项目拓扑列表
     */
    @SaCheckPermission("system:itemTopology:list")
    @GetMapping("/list")
    public R<List<ItemTopologyVo>> list(ItemTopologyBo bo, PageQuery pageQuery) {
        List<ItemTopologyVo> itemTopology = iItemTopologyService.queryList(bo);
        return R.ok(itemTopology);
    }

    /**
     * 查询项目列表（排除节点）
     *
     * @param itemId 节点ID
     */
    @SaCheckPermission("system:itemTopology:list")
    @GetMapping("/list/exclude/{itemId}")
    public R<List<ItemTopologyVo>> excludeChild(@PathVariable(value = "itemId", required = false) Long itemId) {
        List<ItemTopologyVo> items = iItemTopologyService.queryList(new ItemTopologyBo());
        items.removeIf(d -> d.getItemId().equals(itemId)
            || StringUtils.splitList(d.getAncestors()).contains(Convert.toStr(itemId)));

        return R.ok(items);
    }

    /**
     * 导出项目拓扑列表
     */
    @SaCheckPermission("system:itemTopology:export")
    @Log(title = "项目拓扑", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ItemTopologyBo bo, HttpServletResponse response) {
        List<ItemTopologyVo> list = iItemTopologyService.queryList(bo);
        ExcelUtil.exportExcel(list, "项目拓扑", ItemTopologyVo.class, response);
    }

    /**
     * 获取项目拓扑详细信息
     *
     * @param itemId 主键
     */
    @SaCheckPermission("system:itemTopology:query")
    @GetMapping("/{itemId}")
    public R<ItemTopologyVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long itemId) {
        return R.ok(iItemTopologyService.queryById(itemId));
    }

    /**
     * 新增项目拓扑
     */
    @SaCheckPermission("system:itemTopology:add")
    @Log(title = "项目拓扑", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ItemTopologyBo bo) {
        return toAjax(iItemTopologyService.insertByBo(bo));
    }

    /**
     * 修改项目拓扑
     */
    @SaCheckPermission("system:itemTopology:edit")
    @Log(title = "项目拓扑", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ItemTopologyBo bo) {
        return toAjax(iItemTopologyService.updateByBo(bo));
    }

    /**
     * 删除项目拓扑
     *
     * @param itemIds 主键串
     */
    @SaCheckPermission("system:itemTopology:remove")
    @Log(title = "项目拓扑", businessType = BusinessType.DELETE)
    @DeleteMapping("/{itemIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] itemIds) {
        return toAjax(iItemTopologyService.deleteWithValidByIds(Arrays.asList(itemIds), true));
    }

    /**
     * 获取拓扑树列表
     */
    @SaCheckPermission("system:itemTopology:list")
    @GetMapping("/topologyTree")
    public R<List<Tree<Long>>> topologyTree(ItemTopologyBo bo) {
        return R.ok(iItemTopologyService.selectTreeList(bo));
    }
}
