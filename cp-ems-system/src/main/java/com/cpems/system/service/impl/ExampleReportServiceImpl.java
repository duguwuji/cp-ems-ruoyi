package com.cpems.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cpems.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.cpems.system.domain.bo.ExampleReportBo;
import com.cpems.system.domain.vo.ExampleReportVo;
import com.cpems.system.domain.ExampleReport;
import com.cpems.system.mapper.ExampleReportMapper;
import com.cpems.system.service.IExampleReportService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 例报管理Service业务层处理
 *
 * @author ruoyi
 * @date 2023-09-11
 */
@RequiredArgsConstructor
@Service
public class ExampleReportServiceImpl implements IExampleReportService {

    private final ExampleReportMapper baseMapper;

    /**
     * 查询例报管理
     */
    @Override
    public ExampleReportVo queryById(Long exampleReportId){
        return baseMapper.selectVoById(exampleReportId);
    }

    /**
     * 查询例报管理列表
     */
    @Override
    public TableDataInfo<ExampleReportVo> queryPageList(ExampleReportBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ExampleReport> lqw = buildQueryWrapper(bo);
        Page<ExampleReportVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询例报管理列表
     */
    @Override
    public List<ExampleReportVo> queryList(ExampleReportBo bo) {
        LambdaQueryWrapper<ExampleReport> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ExampleReport> buildQueryWrapper(ExampleReportBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ExampleReport> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getType()), ExampleReport::getType, bo.getType());
        lqw.eq(StringUtils.isNotBlank(bo.getCycle()), ExampleReport::getCycle, bo.getCycle());
        lqw.eq(StringUtils.isNotBlank(bo.getContent()), ExampleReport::getContent, bo.getContent());
        lqw.eq(StringUtils.isNotBlank(bo.getPushMethod()), ExampleReport::getPushMethod, bo.getPushMethod());
        lqw.eq(StringUtils.isNotBlank(bo.getUserId()), ExampleReport::getUserId, bo.getUserId());
        lqw.eq(StringUtils.isNotBlank(bo.getReceiver()), ExampleReport::getReceiver, bo.getReceiver());
        lqw.eq(bo.getStartDate() != null, ExampleReport::getStartDate, bo.getStartDate());
        lqw.eq(bo.getEndDate() != null, ExampleReport::getEndDate, bo.getEndDate());
        return lqw;
    }

    /**
     * 新增例报管理
     */
    @Override
    public Boolean insertByBo(ExampleReportBo bo) {
        ExampleReport add = BeanUtil.toBean(bo, ExampleReport.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setExampleReportId(add.getExampleReportId());
        }
        return flag;
    }

    /**
     * 修改例报管理
     */
    @Override
    public Boolean updateByBo(ExampleReportBo bo) {
        ExampleReport update = BeanUtil.toBean(bo, ExampleReport.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ExampleReport entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除例报管理
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
