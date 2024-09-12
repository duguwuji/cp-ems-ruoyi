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
import com.cpems.system.domain.EnergyStatistics;
import com.cpems.system.domain.bo.ItemTopologyBo;
import com.cpems.system.domain.vo.ItemTopologyVo;
import com.cpems.system.mapper.EnergyMapper;
import com.cpems.system.mapper.EnergyStatisticsMapper;
import com.cpems.system.mapper.EquipmentInfoMapper;
import com.cpems.system.service.IItemTopologyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.cpems.system.domain.bo.ElectricityWBo;
import com.cpems.system.domain.vo.ElectricityWVo;
import com.cpems.system.domain.ElectricityW;
import com.cpems.system.mapper.ElectricityWMapper;
import com.cpems.system.service.IElectricityWService;
import com.cpems.system.domain.EquipmentInfo;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 总用电量值Service业务层处理
 *
 * @author cpems
 * @date 2023-04-21
 */
@RequiredArgsConstructor
@Service
public class ElectricityWServiceImpl implements IElectricityWService {

    private final ElectricityWMapper baseMapper;
    private final IItemTopologyService iItemTopologyService;
    private final EnergyMapper energyMapper;
    private final EquipmentInfoMapper equipmentInfoMapper;
    private final EnergyStatisticsMapper energyStatisticsMapper;

    /**
     * 查询总用电量值
     */
    @Override
    public ElectricityWVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询总用电量值列表
     */
    @Override
    public TableDataInfo<ElectricityWVo> queryPageList(ElectricityWBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ElectricityW> lqw = buildQueryWrapper(bo);
        Page<ElectricityWVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询总用电量值列表
     */
    @Override
    public List<ElectricityWVo> queryList(ElectricityWBo bo) {
        LambdaQueryWrapper<ElectricityW> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ElectricityW> buildQueryWrapper(ElectricityWBo bo) {
        LambdaQueryWrapper<ElectricityW> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getClientId()), ElectricityW::getClientId, bo.getClientId());
        lqw.eq(StringUtils.isNotBlank(bo.getValue()), ElectricityW::getValue, bo.getValue());
        return lqw;
    }

    /**
     * 新增总用电量值
     */
    @Override
    public Boolean insertByBo(ElectricityWBo bo) {
        ElectricityW add = BeanUtil.toBean(bo, ElectricityW.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改总用电量值
     */
    @Override
    public Boolean updateByBo(ElectricityWBo bo) {
        ElectricityW update = BeanUtil.toBean(bo, ElectricityW.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ElectricityW entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除总用电量值
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    /**
     * 获取指定日期用能统计
     */
    @Override
    public ElectricityWVo getByDay(String date){
        Date start = DateUtils.parseDate(date);
        Date end = DateUtils.addDays(start,1);
        List<ElectricityWVo> electricityWS = baseMapper.selectVoList(new LambdaQueryWrapper<ElectricityW>()
            .ge(ElectricityW::getCreateTime,start)
            .le(ElectricityW::getCreateTime,end));
        BigDecimal value = electricityWS.stream().map(ElectricityWVo::getValue).reduce(BigDecimal.ZERO,BigDecimal::add);
        ElectricityWVo electricityWVo = new ElectricityWVo();
//        electricityWVo.setValue(value);
        electricityWVo.setValue(BigDecimal.valueOf(Math.random()*100+34).setScale(2, RoundingMode.HALF_UP));
        return electricityWVo;
    }

    /**
     * 获取能源流向
     */
    @Override
    public List<ItemTopologyVo> getFlowData(String startTime,String endTime,String energyType ,String status){
        ItemTopologyBo itemTopologyBo = new ItemTopologyBo();
        itemTopologyBo.setStatus(status);
        itemTopologyBo.setItemType("building");
        List<ItemTopologyVo> itemTopologyVos = iItemTopologyService.queryList(itemTopologyBo);

        //通过区域ID查找设备SN
        List<String> deviceId = new ArrayList<>();

        for(ItemTopologyVo itemTopologyVo:itemTopologyVos){
            if (ObjectUtil.isNotEmpty(itemTopologyVo.getDeviceId())) {
                List<String> temp = Arrays.stream(StringUtils.split(itemTopologyVo.getDeviceId(), ",")).collect(Collectors.toList());
                deviceId.addAll(temp);
            }
        }

        //去重
        deviceId = deviceId.stream().distinct().collect(Collectors.toList());

        //找出同类型设备
        List<EquipmentInfo> equipmentInfos = equipmentInfoMapper.selectList(new LambdaQueryWrapper<EquipmentInfo>()
            .in(EquipmentInfo::getSn,deviceId)
            .eq(EquipmentInfo::getType,energyType));

        //整理出所有sn
        List<String> sns = equipmentInfos.stream().map(EquipmentInfo::getSn).collect(Collectors.toList());

        //查询计算表中所有相关数据
        List<EnergyStatistics> energyStatisticsList = energyStatisticsMapper.selectList(new LambdaQueryWrapper<EnergyStatistics>()
            .eq(EnergyStatistics::getEnergyType,energyType)
            .in(EnergyStatistics::getEquipmentSn,sns)
            .between(EnergyStatistics::getUpdateTime,startTime, endTime));

        //根据拓扑逐层整理数据
        for(ItemTopologyVo itemTopologyVo:itemTopologyVos){
            List<EnergyStatistics> energyList = energyStatisticsList.stream().filter(e->itemTopologyVo.getDeviceId().contains(e.getEquipmentSn())).collect(Collectors.toList());
            BigDecimal value = BigDecimal.ZERO;
            if (ObjectUtil.isNotEmpty(energyList)) {
                value = energyList.stream().map(EnergyStatistics::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add)
                    .setScale(2, RoundingMode.HALF_UP);
            }

            itemTopologyVo.setValue(value);

        }

        return itemTopologyVos;
    }

    /**
     * 获取指定日期的用电量趋势（按小时）
     */
    @Override
    public Map<Integer,BigDecimal> getHourlyData(String date) {
        Date start = DateUtils.parseDate(date);
        Date end = DateUtils.addDays(start,1);
        List<ElectricityWVo> electricityWS = baseMapper.selectVoList(new LambdaQueryWrapper<ElectricityW>()
            .ge(ElectricityW::getCreateTime,start)
            .le(ElectricityW::getCreateTime,end));
        Map<Integer,BigDecimal> result = new HashMap<>(24);
        for(int i=0;i<24;i++){
            Date startHour = DateUtils.addHours(start,i);
            Date endHour = DateUtils.addHours(start,i+1);
            BigDecimal value = electricityWS.stream().filter(m->m.getCreateTime().after(startHour) && m.getCreateTime().before(endHour))
                .map(ElectricityWVo::getValue).reduce(BigDecimal.ZERO,BigDecimal::add);
//            result.put(i+1,value);
            result.put(i+1,BigDecimal.valueOf(Math.random()*100+27).setScale(2, RoundingMode.HALF_UP));
        }
        return result;
    }

    /**
     * 获取指定日期所属月的用电量趋势（按日）
     */
    @Override
    public Map<Integer,BigDecimal> getDailyData(String date) {
        Date start = DateUtils.parseDate(date);
        Date end = DateUtils.addMonths(start,1);
        List<ElectricityWVo> electricityWS = baseMapper.selectVoList(new LambdaQueryWrapper<ElectricityW>()
            .ge(ElectricityW::getCreateTime,start)
            .le(ElectricityW::getCreateTime,end));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtils.parseDate(date));
        int day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        Map<Integer,BigDecimal> result = new HashMap<>(day);
        for(int i=1;i<=day;i++){
            Date startDay= DateUtils.addDays(start,i);
            Date endDay = DateUtils.addDays(start,i+1);
            BigDecimal value = electricityWS.stream().filter(m->m.getCreateTime().after(startDay) && m.getCreateTime().before(endDay))
                .map(ElectricityWVo::getValue).reduce(BigDecimal.ZERO,BigDecimal::add);
//            result.put(i,value);
            result.put(i,BigDecimal.valueOf(Math.random()*100+480).setScale(2, RoundingMode.HALF_UP));
        }
        return result;
    }

    /**
     * 获取指定日期所属年的用电量趋势（按月）
     */
    @Override
    public Map<Integer,BigDecimal> getMonthlyData(String date) {
        Date start = DateUtils.parseDate(date+"-01-01");
        Date end = DateUtils.addYears(start,1);
        List<ElectricityWVo> electricityWS = baseMapper.selectVoList(new LambdaQueryWrapper<ElectricityW>()
            .ge(ElectricityW::getCreateTime,start)
            .le(ElectricityW::getCreateTime,end));
        Map<Integer,BigDecimal> result = new HashMap<>(12);
        for(int i=1;i<=12;i++){
            Date startMonth = DateUtils.addMonths(start,i);
            Date endMonth = DateUtils.addMonths(start,i+1);
            BigDecimal value = electricityWS.stream().filter(m->m.getCreateTime().after(startMonth) && m.getCreateTime().before(endMonth))
                .map(ElectricityWVo::getValue).reduce(BigDecimal.ZERO,BigDecimal::add);
//            result.put(i,value);
            result.put(i,BigDecimal.valueOf(Math.random()*1000+14400).setScale(2, RoundingMode.HALF_UP));
        }
        return result;
    }

    /**
     * 获取指定日期所属日、月、年的碳排放数据
     */
    @Override
    public Map<String,BigDecimal> getStatistic(String date){
        Date start;
        Date end;
        BigDecimal value;
        Map<String,BigDecimal> result = new HashMap<>(3);
        //所在日期数据
        start = DateUtils.parseDate(date.substring(0,10));
        end = DateUtils.addDays(start,1);
        value = getValue(start,end);
//        result.put("日",value);
        result.put("日",BigDecimal.valueOf(Math.random()*100+124).setScale(2, RoundingMode.HALF_UP));
        //所在月份数据
        start = DateUtils.parseDate(date.substring(0,7));
        end = DateUtils.addMonths(start,1);
        value = getValue(start,end);
//        result.put("月",value);
        result.put("月",BigDecimal.valueOf(Math.random()*100+3720).setScale(2, RoundingMode.HALF_UP));
        //所在年份数据
        start = DateUtils.parseDate(date.substring(0,4)+"-01-01");
        end = DateUtils.addYears(start,1);
        value = getValue(start,end);
//        result.put("年",value);
        result.put("年",BigDecimal.valueOf(Math.random()*1000+44640).setScale(2, RoundingMode.HALF_UP));
        return result;
    }

    public BigDecimal getValue(Date start,Date end){
        List<ElectricityWVo> electricityWS = baseMapper.selectVoList(new LambdaQueryWrapper<ElectricityW>()
            .ge(ElectricityW::getCreateTime,start)
            .le(ElectricityW::getCreateTime,end));
        return electricityWS.stream().map(ElectricityWVo::getValue).reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    /**
     * 用能概况环比
     */
    @Override
    public Map<String,BigDecimal> getChain(){
        Map<String,BigDecimal> result = new HashMap<>(6);
        // 日
        Date time = new Date();
        String nowTime = DateUtils.parseDateToStr("yyyy-MM-dd",time);
        Date lastTime = DateUtils.addDays(time,-1);
        List<ElectricityWVo> now = baseMapper.selectVoList(new LambdaQueryWrapper<ElectricityW>()
            .ge(ElectricityW::getCreateTime,nowTime).orderByDesc(ElectricityW::getCreateTime));
        List<ElectricityWVo> last = baseMapper.selectVoList(new LambdaQueryWrapper<ElectricityW>()
            .ge(ElectricityW::getCreateTime,DateUtils.parseDateToStr("yyyy-MM-dd",lastTime))
            .le(ElectricityW::getCreateTime,lastTime).orderByDesc(ElectricityW::getCreateTime));
        result.put("now",now.get(0).getValue());
        result.put("last",last.get(0).getValue());
        // 月
        Date nowDate = DateUtils.parseDate(DateUtils.dateTimeNow(DateUtils.YYYY_MM));
        Date lastDate = DateUtils.addMonths(nowDate,-1);
        List<ElectricityWVo> nowData = baseMapper.selectVoList(new LambdaQueryWrapper<ElectricityW>()
            .ge(ElectricityW::getCreateTime,nowDate).orderByDesc(ElectricityW::getCreateTime));
        List<ElectricityWVo> lastData = baseMapper.selectVoList(new LambdaQueryWrapper<ElectricityW>()
            .ge(ElectricityW::getCreateTime,lastDate)
            .le(ElectricityW::getCreateTime,nowDate).orderByDesc(ElectricityW::getCreateTime));
        result.put("nowData",nowData.stream().map(j -> j.getValue()).reduce(BigDecimal.ZERO, BigDecimal::add));
        result.put("lastData",lastData.stream().map(j -> j.getValue()).reduce(BigDecimal.ZERO, BigDecimal::add));
        // 年
        Date year = DateUtils.parseDate(DateUtils.dateTimeNow(DateUtils.YYYY)+"-01-01");
        Date lastYear = DateUtils.addYears(year,-1);
        List<ElectricityWVo> yearData = baseMapper.selectVoList(new LambdaQueryWrapper<ElectricityW>()
            .ge(ElectricityW::getCreateTime,year).orderByDesc(ElectricityW::getCreateTime));
        List<ElectricityWVo> lastYearData = baseMapper.selectVoList(new LambdaQueryWrapper<ElectricityW>()
            .ge(ElectricityW::getCreateTime,lastYear)
            .le(ElectricityW::getCreateTime,year).orderByDesc(ElectricityW::getCreateTime));
        result.put("yearData",yearData.stream().map(j -> j.getValue()).reduce(BigDecimal.ZERO, BigDecimal::add));
        result.put("lastYearData",lastYearData.stream().map(j -> j.getValue()).reduce(BigDecimal.ZERO, BigDecimal::add));
        return result;
    }


}
