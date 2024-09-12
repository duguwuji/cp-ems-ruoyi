package com.cpems.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cpems.common.utils.DateUtils;
import com.cpems.common.utils.StringUtils;
import com.cpems.system.domain.InspectionRecord;
import com.cpems.system.mapper.InspectionRecordMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.cpems.system.domain.bo.InspectionPlanBo;
import com.cpems.system.domain.vo.InspectionPlanVo;
import com.cpems.system.domain.InspectionPlan;
import com.cpems.system.mapper.InspectionPlanMapper;
import com.cpems.system.service.IInspectionPlanService;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 巡检计划Service业务层处理
 *
 * @author ruoyi
 * @date 2023-04-04
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class InspectionPlanServiceImpl implements IInspectionPlanService {

    private final InspectionPlanMapper baseMapper;
    private final InspectionRecordMapper inspectionRecordMapper;


    /**
     * 查询巡检计划
     */
    @Override
    public InspectionPlanVo queryById(Long id) {
        InspectionPlanVo result = baseMapper.selectVoById(id);
        if (ObjectUtil.isNotNull(result.getUserId())) {
            result.setUserIds(StringUtils.splitList(result.getUserId(), ","));
        }
        if (ObjectUtil.isNotNull(result.getSetDate())) {
            result.setSetDates(StringUtils.splitList(result.getSetDate(), ","));
        }
        return result;
    }

    /**
     * 查询巡检计划列表
     */
    @Override
    public TableDataInfo<InspectionPlanVo> queryPageList(InspectionPlanBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<InspectionPlan> lqw = buildQueryWrapper(bo);
        Page<InspectionPlanVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询巡检计划列表
     */
    @Override
    public List<InspectionPlanVo> queryList(InspectionPlanBo bo) {
        LambdaQueryWrapper<InspectionPlan> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<InspectionPlan> buildQueryWrapper(InspectionPlanBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<InspectionPlan> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getPlanName()), InspectionPlan::getPlanName, bo.getPlanName());
        lqw.eq(StringUtils.isNotBlank(bo.getPlanContent()), InspectionPlan::getPlanContent, bo.getPlanContent());
        lqw.like(StringUtils.isNotBlank(bo.getProjectName()), InspectionPlan::getProjectName, bo.getProjectName());
        lqw.eq(bo.getStartDate() != null, InspectionPlan::getStartDate, bo.getStartDate());
        lqw.eq(bo.getEndDate() != null, InspectionPlan::getEndDate, bo.getEndDate());
        lqw.eq(StringUtils.isNotBlank(bo.getInspectionCycle()), InspectionPlan::getInspectionCycle, bo.getInspectionCycle());
        lqw.eq(StringUtils.isNotBlank(bo.getInspectionPerson()), InspectionPlan::getInspectionPerson, bo.getInspectionPerson());
        lqw.like(ObjectUtil.isNotNull(bo.getUserId()), InspectionPlan::getUserId, bo.getUserId());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), InspectionPlan::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增巡检计划
     */
    @Override
    public Boolean insertByBo(InspectionPlanBo bo) {
        InspectionPlan add = BeanUtil.toBean(bo, InspectionPlan.class);
        if (ObjectUtil.isNotNull(bo.getUserIds())) {
            add.setUserId(StringUtils.join(bo.getUserIds(), ","));
        }
        if (ObjectUtil.isNotNull(bo.getSetDates())) {
            add.setSetDate(StringUtils.join(bo.getSetDates(), ","));
        }
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改巡检计划
     */
    @Override
    public Boolean updateByBo(InspectionPlanBo bo) {
        InspectionPlan update = BeanUtil.toBean(bo, InspectionPlan.class);
        if (ObjectUtil.isNotNull(bo.getUserIds())) {
            update.setUserId(StringUtils.join(bo.getUserIds(), ","));
        }
        if (ObjectUtil.isNotNull(bo.getSetDates())) {
            update.setSetDate(StringUtils.join(bo.getSetDates(), ","));
        }
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(InspectionPlan entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除巡检计划
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    /**
     * 自动巡检产生巡检记录（日）
     */
    @Override
    public Boolean inspectionDayHandler() {
        try {
            inspection("日");
        } catch (Exception e) {
            System.out.println(e);
        }

        return true;
    }

    /**
     * 自动巡检产生巡检记录（周）
     */
    @Override
    public Boolean inspectionWeekHandler() {
        try {
            inspection("周");
        } catch (Exception e) {
            System.out.println(e);
        }

        return true;
    }

    /**
     * 自动巡检产生巡检记录（月）
     */
    @Override
    public Boolean inspectionMonthHandler() {
        try {
            inspection("月");
        } catch (Exception e) {
            System.out.println(e);
        }

        return true;
    }

    /**
     * 自动巡检
     */
    private void inspection(String cycle) {
        Date now = DateUtils.getNowDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        String week = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
        String mouth = DateUtils.parseDateToStr("dd", now);

        List<InspectionPlan> inspectionPlans = baseMapper.selectList(new LambdaQueryWrapper<InspectionPlan>()
                .le(InspectionPlan::getStartDate, now)
                .ge(InspectionPlan::getEndDate, now)
                .eq(InspectionPlan::getStatus, "0"))
            .stream().filter(t -> t.getInspectionCycle().equals(cycle)).collect(Collectors.toList());

        if (cycle.equals("日")) {
            for (InspectionPlan inspectionPlan : inspectionPlans) {
                InspectionRecord record = inspectionRecordMapper.selectOne(new LambdaQueryWrapper<InspectionRecord>()
                    .eq(InspectionRecord::getPlanId, inspectionPlan.getId())
                    .orderByDesc(InspectionRecord::getCreateTime)
                    .last("limit 1"));
                if (ObjectUtil.isNotEmpty(record) && record.getCycle().equals(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now))) {
                    continue;
                } else {
                    InspectionRecord inspectionRecord = getBasicRecord(inspectionPlan, now, cycle);
                    inspectionRecordMapper.insert(inspectionRecord);
                }
            }
        }

        if (cycle.equals("周")) {
            for (InspectionPlan inspectionPlan : inspectionPlans) {
                InspectionRecord record = inspectionRecordMapper.selectOne(new LambdaQueryWrapper<InspectionRecord>()
                    .eq(InspectionRecord::getPlanId, inspectionPlan.getId())
                    .orderByDesc(InspectionRecord::getCreateTime)
                    .last("limit 1"));
                if (ObjectUtil.isNotEmpty(record) && record.getCycle().equals(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now))) {
                    continue;
                } else {
                    if (inspectionPlan.getSetDate().contains(week)) {
                        InspectionRecord inspectionRecord = getBasicRecord(inspectionPlan, now, cycle);
                        inspectionRecordMapper.insert(inspectionRecord);
                    }
                }
            }
        }
        if (cycle.equals("月")) {
            for (InspectionPlan inspectionPlan : inspectionPlans) {
                InspectionRecord record = inspectionRecordMapper.selectOne(new LambdaQueryWrapper<InspectionRecord>()
                    .eq(InspectionRecord::getPlanId, inspectionPlan.getId())
                    .orderByDesc(InspectionRecord::getCreateTime)
                    .last("limit 1"));
                if (ObjectUtil.isNotEmpty(record) && record.getCycle().equals(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now))) {
                    continue;
                } else {
                    if (inspectionPlan.getSetDate().contains(mouth)) {
                        InspectionRecord inspectionRecord = getBasicRecord(inspectionPlan, now, cycle);
                        inspectionRecordMapper.insert(inspectionRecord);
                    }
                }
            }
        }
    }

    /**
     * 自动巡检
     */
    private void inspection1(String cycle) {
        Date now = new Date();
        String month = DateUtils.parseDateToStr(DateUtils.YYYY_MM, now);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String week = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        List<InspectionPlan> inspectionPlans = baseMapper.selectList(new LambdaQueryWrapper<InspectionPlan>()
                .le(InspectionPlan::getStartDate, now)
                .ge(InspectionPlan::getEndDate, now)
                .eq(InspectionPlan::getStatus, "0"))
            .stream().filter(t -> t.getInspectionCycle().equals(cycle)).collect(Collectors.toList());
        for (InspectionPlan inspectionPlan : inspectionPlans) {
            InspectionRecord record = inspectionRecordMapper.selectOne(new LambdaQueryWrapper<InspectionRecord>()
                .eq(InspectionRecord::getPlanId, inspectionPlan.getId())
                .orderByDesc(InspectionRecord::getCreateTime)
                .last("limit 1"));
            if (ObjectUtil.isNotEmpty(record)) {
                if ("日".equals(cycle) && record.getCycle().equals(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now))) {
                    continue;
                } else if ("日".equals(cycle)) {
                    InspectionRecord inspectionRecord = getBasicRecord(inspectionPlan, now, cycle);
                    inspectionRecord.setPlanTime(DateUtils.parseDate(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now) + " " + inspectionPlan.getSetTime()));
                    inspectionRecord.setCycle(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now));
                    inspectionRecordMapper.insert(inspectionRecord);
                }

                if ("周".equals(cycle) && record.getCycle().equals(week)) {
                    continue;
                } else if ("周".equals(cycle)) {
                    InspectionRecord inspectionRecord = getBasicRecord(inspectionPlan, now, cycle);
                    inspectionRecord.setPlanTime(DateUtils.parseDate(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.addWeeks(record.getCreateTime(), 1)) + " " + inspectionPlan.getSetTime()));
                    inspectionRecord.setCycle(week);
                    inspectionRecordMapper.insert(inspectionRecord);
                }

                if ("月".equals(cycle) && record.getCycle().equals(month)) {
                    continue;
                } else if ("月".equals(cycle)) {
                    InspectionRecord inspectionRecord = getBasicRecord(inspectionPlan, now, cycle);
                    inspectionRecord.setPlanTime(DateUtils.parseDate(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.addMonths(record.getCreateTime(), 1)) + " " + inspectionPlan.getSetTime()));
                    inspectionRecord.setCycle(month);
                    inspectionRecordMapper.insert(inspectionRecord);
                }
            } else {
                InspectionRecord inspectionRecord = getBasicRecord(inspectionPlan, now, cycle);
                if ("日".equals(cycle)) {
                    inspectionRecord.setCycle(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now));
                }
                if ("周".equals(cycle)) {
                    inspectionRecord.setCycle(week);
                }
                if ("月".equals(cycle)) {
                    inspectionRecord.setCycle(month);
                }
                inspectionRecordMapper.insert(inspectionRecord);
            }
        }
    }

    private InspectionRecord getBasicRecord(InspectionPlan inspectionPlan, Date now, String cycle) {
        InspectionRecord record = new InspectionRecord();
        record.setPlanName(inspectionPlan.getPlanName());
        record.setPlanContent(inspectionPlan.getPlanContent());
        record.setProjectName(inspectionPlan.getProjectName());
        record.setPlanTime(DateUtils.parseDate(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now) + " " + inspectionPlan.getSetTime()));
        record.setInspectionPerson(inspectionPlan.getInspectionPerson());
        record.setPlanId(inspectionPlan.getId());
        record.setUserId(inspectionPlan.getUserId());
        record.setInspectionCycle(cycle);
        record.setCycle(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now));
        return record;
    }
}
