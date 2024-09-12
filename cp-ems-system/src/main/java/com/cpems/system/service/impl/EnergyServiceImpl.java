package com.cpems.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import com.cpems.common.utils.StringUtils;
import com.cpems.system.domain.*;
import com.cpems.system.domain.bo.EnergyStatisticsBo;
import com.cpems.system.domain.bo.ItemTopologyBo;
import com.cpems.system.domain.enums.EnergyType;
import com.cpems.system.domain.vo.*;
import com.cpems.system.mapper.*;
import com.cpems.system.service.IEnergyService;
import com.cpems.system.service.IItemTopologyService;
import com.cpems.system.service.ISysConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.cpems.system.domain.bo.EnergyBo;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cpems.common.utils.DateUtils;
import com.cpems.common.utils.TreeBuildUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 能源值Service业务层处理
 *
 * @author cpems
 * @date 2023-04-21
 */
@RequiredArgsConstructor
@Service
public class EnergyServiceImpl implements IEnergyService {

    private final EnergyMapper baseMapper;
    private final EquipmentInfoMapper equipmentInfoMapper;
    private final ItemTopologyMapper itemTopologyMapper;
    private final IItemTopologyService iItemTopologyService;
    private final EnergyStatisticsMapper energyStatisticsMapper;
    private final PowerStatisticsMapper powerStatisticsMapper;
    private final VoltageStatisticsMapper voltageStatisticsMapper;
    private final CurrentStatisticsMapper currentStatisticsMapper;
    private final ISysConfigService configService;
    private final ChargingMapper chargingMapper;
    private final ChargingStepMapper chargingStepMapper;

    /**
     * 根据设备环比分析
     */
    @Override
    public List<ChainDataVo> getChainByDevice(String energyType, String dateType, String date, String areaIds) {
        List<String> deviceId = new ArrayList<>();
        List<ChainDataVo> chainDataVos = new ArrayList<>();
        if (StringUtils.isEmpty(areaIds)) {
            return chainDataVos;
        }
        //整理出所有区域
        List<String> areaId = Arrays.asList(areaIds.split(","));
        List<ItemTopology> itemTopologies = itemTopologyMapper.selectBatchIds(areaId);

        //整理出所有相关设备
        for (ItemTopology itemTopology : itemTopologies) {
            if (ObjectUtil.isNotEmpty(itemTopology.getDeviceId())) {
                List<String> temp = Arrays.stream(StringUtils.split(itemTopology.getDeviceId(), ",")).collect(Collectors.toList());
                deviceId.addAll(temp);
            }
        }

        //去重
        deviceId = deviceId.stream().distinct().collect(Collectors.toList());

        //整理出同类型设备
        List<EquipmentInfo> equipmentInfos = equipmentInfoMapper.selectList(new LambdaQueryWrapper<EquipmentInfo>()
            .in(EquipmentInfo::getSn, deviceId)
            .eq(EquipmentInfo::getType, energyType));

        //整理出所有sn
        List<String> sns = equipmentInfos.stream().map(EquipmentInfo::getSn).collect(Collectors.toList());

        //根据时间类型筛选数据
        if (dateType.equals("date")) {
            Date now = DateUtils.parseDate(date);
            Date last = DateUtils.addDays(now, -1);
            List<EnergyStatistics> nowData = energyStatisticsMapper.selectList(buildStatisticQuery(sns, energyType)
                .between(EnergyStatistics::getTime, date + " 00:00:00", date + " 23:59:59"));
            List<EnergyStatistics> lastData = energyStatisticsMapper.selectList(buildStatisticQuery(sns, energyType)
                .between(EnergyStatistics::getTime, DateUtils.dateTime(last) + " 00:00:00", DateUtils.dateTime(last) + " 23:59:59"));
            chainDataVos = getStatisticData(nowData, lastData, itemTopologies);
        } else if (dateType.equals("week")) {
            int year = Integer.parseInt(date.substring(0, 4));
            int week = Integer.parseInt(date.substring(5, 7)) + 1;
            List<EnergyStatistics> nowData = energyStatisticsMapper.selectList(buildStatisticQuery(sns, energyType)
                .between(EnergyStatistics::getTime, getFirstDayOfWeek(year, week) + " 00:00:00", getLastDayOfWeek(year, week) + " 23:59:59"));
            List<EnergyStatistics> lastData = energyStatisticsMapper.selectList(buildStatisticQuery(sns, energyType)
                .between(EnergyStatistics::getTime, getFirstDayOfWeek(year, week - 1) + " 00:00:00", getLastDayOfWeek(year, week - 1) + " 23:59:59"));
            chainDataVos = getStatisticData(nowData, lastData, itemTopologies);
        } else if (dateType.equals("month")) {
            //计算指定月份天数
            Date now = DateUtils.parseDate(date.substring(0, 7));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now);
            int day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            Date last = DateUtils.addMonths(now, -1);
            calendar.setTime(last);
            int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            List<EnergyStatistics> nowData = energyStatisticsMapper.selectList(buildStatisticQuery(sns, energyType)
                .between(EnergyStatistics::getTime, date.substring(0, 7) + "-01 00:00:00", date.substring(0, 7) + "-" + day + " 23:59:59"));
            List<EnergyStatistics> lastData = energyStatisticsMapper.selectList(buildStatisticQuery(sns, energyType)
                .between(EnergyStatistics::getTime, DateUtils.parseDateToStr("yyyy-MM", last) + "-01 00:00:00", DateUtils.parseDateToStr("yyyy-MM", last) + "-" + lastDay + " 23:59:59"));
            chainDataVos = getStatisticData(nowData, lastData, itemTopologies);
        } else if (dateType.equals("year")) {
            Date now = DateUtils.parseDate(date.substring(0, 4) + "-01-01");
            Date last = DateUtils.addYears(now, -1);
            List<EnergyStatistics> nowData = energyStatisticsMapper.selectList(buildStatisticQuery(sns, energyType)
                .between(EnergyStatistics::getTime, now, DateUtils.parseDate(date.substring(0, 4) + "-12-31 23:59:59")));
            List<EnergyStatistics> lastData = energyStatisticsMapper.selectList(buildStatisticQuery(sns, energyType)
                .between(EnergyStatistics::getTime, last, DateUtils.parseDate(new SimpleDateFormat("yyyy").format(last) + "-12-31 23:59:59")));
            chainDataVos = getStatisticData(nowData, lastData, itemTopologies);
        }
        return chainDataVos;
    }

    private List<ChainDataVo> getData(List<Energy> nowData, List<Energy> lastData, List<String> deviceId, List<String> areaId) {
        List<ChainDataVo> chainDataVos = new ArrayList<>();
        for (int t = 0; t < deviceId.size(); t++) {
            int index = t;
            List<Energy> nowList = nowData.stream().filter(i -> i.getClientId().equals(deviceId.get(index))).collect(Collectors.toList());
            List<Energy> lastList = lastData.stream().filter(i -> i.getClientId().equals(deviceId.get(index))).collect(Collectors.toList());
            float nowVal = 0;
            float lastVal = 0;
            BigDecimal chainVal;
            if (nowList.size() >= 2) {
                nowVal = nowList.get(0).getVal() - nowList.get(nowList.size() - 1).getVal();
            }
            if (lastList.size() >= 2) {
                lastVal = lastList.get(0).getVal() - lastList.get(lastList.size() - 1).getVal();
            }
            float addVal = nowVal - lastVal;
            if (lastVal == 0) {
                chainVal = BigDecimal.ZERO;
            } else {
                chainVal = BigDecimal.valueOf(addVal / lastVal).multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
            }
            ChainDataVo chainDataVo = new ChainDataVo();
            chainDataVo.setDeviceId(deviceId.get(index));
            chainDataVo.setAreaId(areaId.get(index));
            chainDataVo.setCurrentEnergy(BigDecimal.valueOf(nowVal).setScale(2, RoundingMode.HALF_UP));
            chainDataVo.setSameEnergy(BigDecimal.valueOf(lastVal).setScale(2, RoundingMode.HALF_UP));
            chainDataVo.setAddEnergy(BigDecimal.valueOf(addVal).setScale(2, RoundingMode.HALF_UP));
            chainDataVo.setChain(chainVal);
            chainDataVos.add(chainDataVo);
        }
        return chainDataVos;
    }

    private List<ChainDataVo> getStatisticData(List<EnergyStatistics> nowData, List<EnergyStatistics> lastData, List<ItemTopology> itemTopologies) {
        List<ChainDataVo> chainDataVos = new ArrayList<>();

        //根据拓扑逐层整理数据
        for (ItemTopology itemTopology : itemTopologies) {
            List<EnergyStatistics> nowList = nowData.stream().filter(e -> itemTopology.getDeviceId().contains(e.getEquipmentSn())).collect(Collectors.toList());
            List<EnergyStatistics> lastList = lastData.stream().filter(e -> itemTopology.getDeviceId().contains(e.getEquipmentSn())).collect(Collectors.toList());

            BigDecimal chainVal;

            BigDecimal nowVal = nowList.stream().map(EnergyStatistics::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal lastVal = lastList.stream().map(EnergyStatistics::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal addVal = nowVal.subtract(lastVal);
            if (lastVal.stripTrailingZeros().equals(BigDecimal.ZERO)) {
                chainVal = BigDecimal.ZERO;
            } else {
                chainVal = addVal.divide(lastVal, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
            }
            ChainDataVo chainDataVo = new ChainDataVo();
            chainDataVo.setDeviceId(itemTopology.getDeviceId());
            chainDataVo.setAreaId(itemTopology.getItemId().toString());
            chainDataVo.setCurrentEnergy(nowVal.setScale(2, RoundingMode.HALF_UP));
            chainDataVo.setSameEnergy(lastVal.setScale(2, RoundingMode.HALF_UP));
            chainDataVo.setAddEnergy(addVal.setScale(2, RoundingMode.HALF_UP));
            chainDataVo.setChain(chainVal);
            chainDataVos.add(chainDataVo);
        }
        return chainDataVos;
    }

    /**
     * 获取指定周的第一天
     *
     * @param year
     * @param week
     * @return
     */
    public static String getFirstDayOfWeek(int year, int week) {
        if (week > 1) {
            Calendar cal = Calendar.getInstance();
            // 设置年份
            cal.set(Calendar.YEAR, year);
            // 设置周
            cal.set(Calendar.WEEK_OF_YEAR, week);
            // 设置该周第一天为星期一
            cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            cal.setMinimalDaysInFirstWeek(0);

            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return simpleDateFormat.format(cal.getTime());
        } else {
            Calendar cal = Calendar.getInstance();
            cal.set(year, 0, 1);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return simpleDateFormat.format(cal.getTime());
        }
    }

    /**
     * 获取指定周的最后一天
     *
     * @param year
     * @param week
     * @return
     */
    public static String getLastDayOfWeek(int year, int week) {
        if (week * 7 >= 365) {  // 说明是年的最后一天
            Calendar cal = Calendar.getInstance();
            cal.set(year, 11, 31);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return simpleDateFormat.format(cal.getTime());
        } else {
            Calendar cal = Calendar.getInstance();
            // 设置年份
            cal.set(Calendar.YEAR, year);
            // 设置周
            cal.set(Calendar.WEEK_OF_YEAR, week);
            // 设置该周第一天为星期一
            cal.setFirstDayOfWeek(Calendar.MONDAY);
            // 设置最后一天是星期日
            cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek() + 5); // Sunday
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return simpleDateFormat.format(cal.getTime());
        }
    }

    private LambdaQueryWrapper<Energy> buildQuery(List<String> deviceId, String tableName) {
        return new LambdaQueryWrapper<Energy>()
            .in(Energy::getClientId, deviceId)
            .eq(Energy::getType, tableName)
            .orderByDesc(Energy::getTs);
    }

    private LambdaQueryWrapper<EnergyStatistics> buildStatisticQuery(List<String> deviceId, String type) {
        return new LambdaQueryWrapper<EnergyStatistics>()
            .in(EnergyStatistics::getEquipmentSn, deviceId)
            .eq(EnergyStatistics::getEnergyType, type)
            .orderByDesc(EnergyStatistics::getTime);
    }

    /**
     * 同比分析
     */
    @Override
    public List<YearAnalysisVo> yearAnalysis(Long areaId, String year, String energyType) {
        List<YearAnalysisVo> result = new ArrayList<>();

        //找出当前楼层房间绑定的设备
        ItemTopology itemTopology = itemTopologyMapper.selectById(areaId);
        List<String> deviceId = Arrays.stream(StringUtils.split(itemTopology.getDeviceId(), ",")).collect(Collectors.toList());
        //找不到对应设备返回
        if (ObjectUtil.isEmpty(deviceId)) {
            return null;
        }
        //获取选定时间年
        Date startCurrent = DateUtils.parseDate(DateUtils.parseDateToStr(year + "-01", DateUtils.getNowDate()));
        Date endCurrent = DateUtils.addMonths(startCurrent, 1);

        //12个月循环
        for (int i = 1; i < 13; i++) {
            YearAnalysisVo yearAnalysisVo = new YearAnalysisVo();
            //设定月份
            yearAnalysisVo.setMonth(i);

            List<EnergyStatistics> current = energyStatisticsMapper.selectList(new LambdaQueryWrapper<EnergyStatistics>()
                .between(EnergyStatistics::getTime, startCurrent, endCurrent)
                .in(EnergyStatistics::getEquipmentSn, deviceId)
                .eq(EnergyStatistics::getEnergyType, energyType));

            yearAnalysisVo.setCurrentPeriod("--");
            //计算当年该月份用电量
            if (ObjectUtil.isNotEmpty(current)) {
                yearAnalysisVo.setCurrentPeriod(current.stream().map(EnergyStatistics::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).toString());
            }

            //获取选定时间年的前一年
            Date startLast = DateUtils.addYears(startCurrent, -1);
            Date endLast = DateUtils.addMonths(startLast, 1);

            List<EnergyStatistics> last = energyStatisticsMapper.selectList(new LambdaQueryWrapper<EnergyStatistics>()
                .between(EnergyStatistics::getTime, startLast, endLast)
                .in(EnergyStatistics::getEquipmentSn, deviceId)
                .eq(EnergyStatistics::getEnergyType, energyType));

            yearAnalysisVo.setCorrespondingPeriod("--");
            //计算去年该月份用电量
            if (ObjectUtil.isNotEmpty(last)) {
                yearAnalysisVo.setCorrespondingPeriod(last.stream().map(EnergyStatistics::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP).toString());
            }
            //去年指定月份用电量不为空时计算增幅
            if (!(yearAnalysisVo.getCorrespondingPeriod().equals("--")) && !(yearAnalysisVo.getCurrentPeriod().equals("--"))) {
                yearAnalysisVo.setYoy((BigDecimal.valueOf(Float.parseFloat(yearAnalysisVo.getCurrentPeriod())).subtract(BigDecimal.valueOf(Float.parseFloat(yearAnalysisVo.getCorrespondingPeriod())))).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(Float.parseFloat(yearAnalysisVo.getCorrespondingPeriod())), 2, RoundingMode.HALF_UP));
            }
            result.add(yearAnalysisVo);

            startCurrent = endCurrent;
            endCurrent = DateUtils.addMonths(startCurrent, 1);
        }
        return result;
    }

    /**
     * 损耗分析
     */
    @Override
    public List<Tree<Long>> lossAnalysis(EnergyBo bo) {

        List<ItemTopologyVo> topologyVoList = itemTopologyMapper.selectVoList(new LambdaQueryWrapper<ItemTopology>()
            .eq(ItemTopology::getItemType, "building"));

        return buildTreeSelect(topologyVoList, bo);
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param voList 拓扑列表
     * @return 下拉树结构列表
     */
    public List<Tree<Long>> buildTreeSelect(List<ItemTopologyVo> voList, EnergyBo bo) {
        if (CollUtil.isEmpty(voList)) {
            return CollUtil.newArrayList();
        }
        //通过区域ID查找设备SN
        List<String> deviceId = new ArrayList<>();

        for (ItemTopologyVo itemTopologyVo : voList) {
            if (ObjectUtil.isNotEmpty(itemTopologyVo.getDeviceId())) {
                List<String> temp = Arrays.stream(StringUtils.split(itemTopologyVo.getDeviceId(), ",")).collect(Collectors.toList());
                deviceId.addAll(temp);
            }
        }

        //去重
        deviceId = deviceId.stream().distinct().collect(Collectors.toList());

        //筛选同类型设备
        List<EquipmentInfo> equipmentInfos = equipmentInfoMapper.selectList(new LambdaQueryWrapper<EquipmentInfo>()
            .in(EquipmentInfo::getSn, deviceId)
            .eq(EquipmentInfo::getType, bo.getEnergyType()));

        //整理出所有的sn
        List<String> sns = equipmentInfos.stream().map(EquipmentInfo::getSn).collect(Collectors.toList());

        //筛选计算表中的所有相关数据
        List<EnergyStatistics> energyStatisticsList = energyStatisticsMapper.selectList(new LambdaQueryWrapper<EnergyStatistics>()
            .in(EnergyStatistics::getEquipmentSn, sns)
            .eq(StringUtils.isNotBlank(bo.getEnergyType()), EnergyStatistics::getEnergyType, bo.getEnergyType())
            .between(StringUtils.isNotBlank(bo.getStartTime()) && StringUtils.isNotBlank(bo.getEndTime()), EnergyStatistics::getTime, bo.getStartTime(), bo.getEndTime()));

        //根据拓扑逐层计算能耗
        for (ItemTopologyVo vo : voList) {
            List<EnergyStatistics> energyList = energyStatisticsList.stream().filter(e -> vo.getDeviceId().contains(e.getEquipmentSn())).collect(Collectors.toList());
            vo.setCurrentConsumption(null);
            //计算当年该月份用电量
            if (ObjectUtil.isNotEmpty(energyList)) {
                vo.setCurrentConsumption(energyList.stream().map(EnergyStatistics::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add));
            }

        }

        //根据拓扑逐层计差值与损耗
        for (ItemTopologyVo vo : voList) {
            double lowerConsumption = 0;
            List<ItemTopologyVo> children = voList.stream().filter(m -> m.getParentId().equals(vo.getItemId())).collect(Collectors.toList());
            for (ItemTopologyVo child : children) {
                if (ObjectUtil.isNotNull(child.getCurrentConsumption())) {
                    lowerConsumption += Double.parseDouble(String.valueOf(child.getCurrentConsumption()));
                }
            }
            if (lowerConsumption != 0) {
                vo.setLowerConsumption(BigDecimal.valueOf(lowerConsumption).setScale(2, RoundingMode.HALF_UP));
            }
            if (ObjectUtil.isNotNull(vo.getLowerConsumption()) && ObjectUtil.isNotNull(vo.getCurrentConsumption())) {
                vo.setDifference(vo.getLowerConsumption().subtract(vo.getCurrentConsumption()).setScale(2, RoundingMode.HALF_UP));
                vo.setPercentage(vo.getDifference().multiply(BigDecimal.valueOf(100)).divide(vo.getCurrentConsumption(), 2, RoundingMode.HALF_UP));
            }
        }

        return TreeBuildUtils.build(voList, (topologyVo, tree) -> {
            tree.setId(topologyVo.getItemId());
            tree.setParentId(topologyVo.getParentId());
            tree.setName(topologyVo.getItemName());
            tree.setWeight(topologyVo.getOrderNum());
            tree.putExtra("currentConsumption", topologyVo.getCurrentConsumption());
            tree.putExtra("lowerConsumption", topologyVo.getLowerConsumption());
            tree.putExtra("difference", topologyVo.getDifference());
            tree.putExtra("percentage", topologyVo.getPercentage());
        });
    }

    private LambdaQueryWrapper<EnergyStatistics> queryWrapper(EnergyStatisticsBo bo) {
        LambdaQueryWrapper<EnergyStatistics> lqw = new LambdaQueryWrapper<>();
        lqw.eq(StringUtils.isNotBlank(bo.getEnergyType()), EnergyStatistics::getEnergyType, bo.getEnergyType())
            .in(StringUtils.isNotBlank(bo.getEquipmentSn()), EnergyStatistics::getEquipmentSn, Arrays.stream(StringUtils.split(bo.getEquipmentSn(), ",")).collect(Collectors.toList()))
            .between(StringUtils.isNotBlank(bo.getStartTime()) && StringUtils.isNotBlank(bo.getEndTime()), EnergyStatistics::getUpdateTime, bo.getStartTime(), bo.getEndTime());
        return lqw;
    }

    /**
     * 能耗概况-环比
     *
     * @return
     */
    @Override
    public Map<String, Object> getChain(Long areaId, String energyType) {
        Map<String, Object> result = new HashMap<>(12);

        //今日，昨日同期
        Date nowEndTime = DateUtils.parseDate(DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS));
        Date nowStartTime = DateUtils.parseDate(DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD));
        Date lastStartTime = DateUtils.addDays(nowStartTime, -1);
        Date lastEndTime = DateUtils.addDays(nowEndTime, -1);

        //当月，上月同期
        Date nowMonthStart = DateUtils.parseDate(DateUtils.dateTimeNow(DateUtils.YYYY_MM));
        Date lastMonthStart = DateUtils.addMonths(nowMonthStart, -1);
        Date lastMonthEnd = DateUtils.addMonths(nowEndTime, -1);

        //今年，去年同期
        Date nowYearStart = DateUtils.parseDate(DateUtils.dateTimeNow(DateUtils.YYYY) + "-01-01");
        Date lastYearStart = DateUtils.addYears(nowYearStart, -1);
        Date lastYearEnd = DateUtils.addYears(nowEndTime, -1);

        // 通过区域id查询设备编号
        //找出当前楼层房间绑定的设备
        ItemTopology itemTopology = itemTopologyMapper.selectById(areaId);

        if (ObjectUtil.isEmpty(itemTopology.getDeviceId())) {
            return result;
        }

        EnergyStatisticsBo bo = new EnergyStatisticsBo();
        bo.setEnergyType(energyType);
        bo.setEquipmentSn(itemTopology.getDeviceId());
        bo.setStartTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, nowStartTime));
        bo.setEndTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, nowEndTime));

        // 今日用能
        List<EnergyStatisticsVo> nowElectricity = energyStatisticsMapper.selectVoList(queryWrapper(bo));
        if (ObjectUtil.isNotEmpty(nowElectricity)) {
            result.put("now", nowElectricity.stream().map(EnergyStatisticsVo::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
            result.put("nowTrend", BigDecimal.valueOf(Float.parseFloat(result.get("now").toString())).setScale(2, RoundingMode.HALF_UP));
        } else {
            result.put("now", "--");
            result.put("nowTrend","--");
        }
        // 昨日同期用能
        bo.setStartTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, lastStartTime));
        bo.setEndTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, lastEndTime));
        List<EnergyStatisticsVo> lastElectricity = energyStatisticsMapper.selectVoList(queryWrapper(bo));
        if (ObjectUtil.isNotEmpty(lastElectricity)) {
            result.put("last", lastElectricity.stream().map(EnergyStatisticsVo::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        } else {
            result.put("last", "--");
        }

        // 当月用能
        bo.setStartTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, nowMonthStart));
        bo.setEndTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, nowEndTime));

        List<EnergyStatisticsVo> nowMonthEle = energyStatisticsMapper.selectVoList(queryWrapper(bo));
        if (ObjectUtil.isNotNull(nowMonthEle)) {
            result.put("nowMonth", nowMonthEle.stream().map(EnergyStatisticsVo::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
            result.put("monthTrend", BigDecimal.valueOf(Float.parseFloat(result.get("nowMonth").toString())).setScale(2, RoundingMode.HALF_UP));

        } else {
            result.put("nowMonth", "--");
            result.put("monthTrend","--");
        }
        // 上月同期用能
        bo.setStartTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, lastMonthStart));
        bo.setEndTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, lastMonthEnd));

        List<EnergyStatisticsVo> lastMonthEle = energyStatisticsMapper.selectVoList(queryWrapper(bo));
        if (ObjectUtil.isNotEmpty(lastMonthEle)) {
            result.put("lastMonth", lastMonthEle.stream().map(EnergyStatisticsVo::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        } else {
            result.put("lastMonth", "--");
        }

        // 今年用能
        bo.setStartTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, nowYearStart));
        bo.setEndTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, nowEndTime));

        List<EnergyStatisticsVo> nowYearEle = energyStatisticsMapper.selectVoList(queryWrapper(bo));
        if (ObjectUtil.isNotEmpty(nowYearEle)) {
            result.put("nowYear", nowYearEle.stream().map(EnergyStatisticsVo::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
            result.put("yearTrend", BigDecimal.valueOf(Float.parseFloat(result.get("nowYear").toString())).setScale(2, RoundingMode.HALF_UP));
        } else {
            result.put("nowYear", "--");
            result.put("yearTrend","--");
        }
        // 去年同期用能
        bo.setStartTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, lastYearStart));
        bo.setEndTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, lastYearEnd));

        List<EnergyStatisticsVo> lastYearEle = energyStatisticsMapper.selectVoList(queryWrapper(bo));
        if (ObjectUtil.isNotEmpty(lastYearEle)) {
            result.put("lastYear", lastYearEle.stream().map(EnergyStatisticsVo::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
        } else {
            result.put("lastYear", "--");
        }
        // 趋势



//        if (!(BigDecimal.valueOf(Float.parseFloat(result.get("last").toString())).stripTrailingZeros()).equals(BigDecimal.ZERO)) {
        if (!(result.get("last").toString().equals("--")) && !(result.get("last").toString().equals("0.00"))) {
            result.put("nowTrend", BigDecimal.valueOf(Float.parseFloat(result.get("now").toString())).subtract(BigDecimal.valueOf(Float.parseFloat(result.get("last").toString()))).setScale(2, RoundingMode.HALF_UP));
            result.put("nowPer", BigDecimal.valueOf(Float.parseFloat(result.get("nowTrend").toString())).divide(BigDecimal.valueOf(Float.parseFloat(result.get("last").toString())), 4, RoundingMode.HALF_UP));
        }
//        if (!(BigDecimal.valueOf(Float.parseFloat(result.get("lastMonth").toString())).stripTrailingZeros()).equals(BigDecimal.ZERO)) {
        if (!(result.get("lastMonth").toString().equals("--"))  && !(result.get("lastMonth").toString().equals("0.00"))) {
            result.put("monthTrend", BigDecimal.valueOf(Float.parseFloat(result.get("nowMonth").toString())).subtract(BigDecimal.valueOf(Float.parseFloat(result.get("lastMonth").toString()))).setScale(2, RoundingMode.HALF_UP));
            result.put("monthPer", BigDecimal.valueOf(Float.parseFloat(result.get("monthTrend").toString())).divide(BigDecimal.valueOf(Float.parseFloat(result.get("lastMonth").toString())), 4, RoundingMode.HALF_UP));
        }
//        if (!(BigDecimal.valueOf(Float.parseFloat(result.get("lastYear").toString())).stripTrailingZeros()).equals(BigDecimal.ZERO)) {
        if (!(result.get("lastYear").toString().equals("--")) && !(result.get("lastYear").toString().equals("0.00"))) {
            result.put("yearTrend", BigDecimal.valueOf(Float.parseFloat(result.get("nowYear").toString())).subtract(BigDecimal.valueOf(Float.parseFloat(result.get("lastYear").toString()))).setScale(2, RoundingMode.HALF_UP));
            result.put("yearPer", BigDecimal.valueOf(Float.parseFloat(result.get("yearTrend").toString())).divide(BigDecimal.valueOf(Float.parseFloat(result.get("lastYear").toString())), 4, RoundingMode.HALF_UP));
        }

        for (String key :result.keySet()){
            if (result.get(key).toString().equals("0.00")){
                result.put(key,"--");
            }
        }
        return result;
    }

    /**
     * 能耗概况-能耗趋势-日
     *
     * @return
     */
    @Override
    public List<Object> getDayTrend(Long areaId, String nowTime, String energyType) {
        List<Object> result = new ArrayList<>();

        Date nowDate = DateUtils.parseDate(nowTime);
        Date startTime = DateUtils.dateTime(DateUtils.YYYY_MM_DD, nowTime);

        result = getWDayByJob(startTime, nowDate, areaId, energyType);

        return result;
    }

    /**
     * 能耗概况-能耗趋势-月
     *
     * @return
     */
    @Override
    public List<Object> getMonthTrend(Long areaId, String nowTime, String energyType) {
        List<Object> result = new ArrayList<>();

        Date nowDate = DateUtils.parseDate(nowTime);
        Date startDate = DateUtils.dateTime(DateUtils.YYYY_MM, nowTime);

        result = getWMonthByJob(startDate, nowDate, areaId, energyType);
        return result;
    }

    /**
     * 用能概况-用能趋势-年
     *
     * @return
     */
    @Override
    public List<Object> getYearTrend(Long areaId, String nowTime, String energyType) {
        List<Object> result = new ArrayList<>();

        Date nowDate = DateUtils.parseDate(nowTime);
        Date startDate = DateUtils.dateTime(DateUtils.YYYY, nowTime);

        result = getWYearByJob(startDate, nowDate, areaId, energyType);
        return result;
    }

    /**
     * 用能概况-日用电功率曲线
     *
     * @return
     */
    @Override
    public Map<String, Object> getDailyP(Long areaId, String energyType) {
        Map<String, Object> result = new HashMap<>(4);
        List<Object> todayData = new ArrayList<>();
        List<Object> yesterdayData = new ArrayList<>();
        result.put("todayData", todayData);
        result.put("yesterdayData", yesterdayData);
        result.put("todayMax", "--");
        result.put("yesterdayMax", "--");

        Date nowTime = DateUtils.getNowDate();
        Date todayStart = DateUtils.parseDate(DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD));
        Date yesterday = DateUtils.addDays(todayStart, -1);

        // 通过区域id查询设备编号
        ItemTopology itemTopology = itemTopologyMapper.selectById(areaId);
        if (ObjectUtil.isEmpty(itemTopology.getDeviceId())) {
            return result;
        }
        List<String> deviceId = Arrays.stream(StringUtils.split(itemTopology.getDeviceId(), ",")).collect(Collectors.toList());
        if (ObjectUtil.isEmpty(deviceId)) {
            return result;
        }
        String tableName = "electricityp";
//        if(energyType.equals(EnergyType.ELECTRICITY.getCode())){
//            tableName = "electricityp";
//        }
        EnergyBo energyBo = new EnergyBo();
        energyBo.setType(tableName);
//        energyBo.setClientId(equipmentInfoVos.get(0).getSn());
        energyBo.setStartTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, yesterday));
        energyBo.setEndTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, nowTime));
//        List<EnergyVo> energyVos = baseMapper.selectVoList(buildQueryWrapper(energyBo).in(Energy::getClientId,deviceId).orderByDesc(Energy::getTs));

        List<PowerStatistics> powerStatistics = powerStatisticsMapper.selectList(new LambdaQueryWrapper<PowerStatistics>()
            .in(PowerStatistics::getEquipmentSn, deviceId)
            .between(PowerStatistics::getTime, energyBo.getStartTime(), energyBo.getEndTime()));

//        今日电功率峰值
        Date finalTodayStart1 = todayStart;
        List<PowerStatistics> todayVos = powerStatistics.stream()
            .filter(e -> e.getCreateTime().after(new Timestamp(finalTodayStart1.getTime())) && e.getCreateTime().before(nowTime))
            .sorted(Comparator.comparing(PowerStatistics::getMax).reversed()).collect(Collectors.toList());
        if (ObjectUtil.isNotEmpty(todayVos)) {
            result.put("todayMax", todayVos.get(0));
        }

//        昨日电功率峰值
        Date finalYesterday1 = yesterday;
        List<PowerStatistics> yesterdayVos = powerStatistics.stream()
            .filter(e -> e.getCreateTime().after(new Timestamp(finalYesterday1.getTime())) && e.getCreateTime().before(finalTodayStart1))
            .sorted(Comparator.comparing(PowerStatistics::getMax).reversed()).collect(Collectors.toList());
        if (ObjectUtil.isNotEmpty(yesterdayVos)) {
            result.put("yesterdayMax", yesterdayVos.get(0));
        }

        for (int i = 0; i < 24; i++) {
//        昨日电功率
            Date yesterdatEnd = DateUtils.addHours(yesterday, 1);
            Date finalYesterday = yesterday;
            List<PowerStatistics> voList = powerStatistics.stream().
                filter(e -> e.getCreateTime().after(new Timestamp(finalYesterday.getTime())) && e.getCreateTime().before(yesterdatEnd))
                .sorted(Comparator.comparing(PowerStatistics::getMax).reversed()).collect(Collectors.toList());
            if (ObjectUtil.isNotEmpty(voList)) {
                yesterdayData.add(voList.get(0).getMax().setScale(2, RoundingMode.HALF_UP));
            } else {
                yesterdayData.add("--");
            }

            yesterday = yesterdatEnd;

//        今日电功率
            if (todayStart.after(nowTime)) {
                continue;
            }

            Date todayEnd = DateUtils.addHours(todayStart, 1);
            Date finalTodayStart = todayStart;
            List<PowerStatistics> todayList = powerStatistics.stream()
                .filter(e -> e.getCreateTime().after(new Timestamp(finalTodayStart.getTime())) && e.getCreateTime().before(todayEnd))
                .sorted(Comparator.comparing(PowerStatistics::getMax).reversed()).collect(Collectors.toList());

            if (ObjectUtil.isNotEmpty(todayList)) {
                todayData.add(todayList.get(0).getMax().setScale(2, RoundingMode.HALF_UP));
            } else {
                todayData.add("--");
            }

            todayStart = todayEnd;
        }

        return result;
    }

    /**
     * 能耗趋势-日
     *
     * @param areaId
     * @param time
     * @return
     */
    @Override
    public Map<String, Object>   getWTrendByDay(Long areaId, String time, String energyType) {
        Map<String, Object> result = new HashMap<>(3);

        Date nowDate = DateUtils.parseDate(DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD));
        Date startTime = DateUtils.parseDate(time);
        Date endTime = DateUtils.addDays(startTime, 1);
        if (startTime.equals(nowDate)) {
            endTime = DateUtils.parseDate(DateUtils.getTime());
        }
        Date yesterdayStart = DateUtils.addDays(startTime, -1);

        // 当日能耗
        List<Object> currentList = getWDayByJob(startTime, endTime, areaId, energyType);
        // 昨日能耗
        List<Object> beforeList = getWDayByJob(yesterdayStart, startTime, areaId, energyType);

        BigDecimal currentTotal = BigDecimal.ZERO;

        for (Object item : currentList) {
            if (!(item.toString().equals("--"))) {
                currentTotal = currentTotal.add(BigDecimal.valueOf(Float.parseFloat(item.toString())));
            }
        }

        result.put("currentList", currentList);
        result.put("beforeList", beforeList);
        result.put("currentTotal", currentTotal.setScale(2,RoundingMode.HALF_UP));
        return result;
    }

    /**
     * 能耗趋势-月
     *
     * @param areaId
     * @param time
     * @return
     */
    @Override
    public Map<String, Object> getWTrendByMonth(Long areaId, String time, String energyType) {
        Map<String, Object> result = new HashMap<>(3);

        Date nowDate = DateUtils.parseDate(DateUtils.dateTimeNow(DateUtils.YYYY_MM));
        Date startTime = DateUtils.parseDate(time);
        Date endTime = DateUtils.addMonths(startTime, 1);
        if (startTime.equals(nowDate)) {
            endTime = DateUtils.parseDate(DateUtils.getTime());
        }
        Date lastMonth = DateUtils.addMonths(startTime, -1);

        // 当月能耗
        List<Object> currentList = getWMonthByJob(startTime, endTime, areaId, energyType);
        // 上月能耗
        List<Object> beforeList = getWMonthByJob(lastMonth, startTime, areaId, energyType);

        BigDecimal currentTotal = BigDecimal.ZERO;

        for (Object item : currentList) {
            if (!(item.toString().equals("--"))) {
                currentTotal = currentTotal.add(BigDecimal.valueOf(Float.parseFloat(item.toString())));
            }
        }

        result.put("currentList", currentList);
        result.put("beforeList", beforeList);
        result.put("currentTotal", currentTotal.setScale(2,RoundingMode.HALF_UP));
        return result;
    }

    /**
     * 能耗趋势-年
     *
     * @param areaId
     * @param time
     * @return
     */
    @Override
    public Map<String, Object> getWTrendByYear(Long areaId, String time, String energyType) {
        Map<String, Object> result = new HashMap<>(3);

        Date nowDate = DateUtils.parseDate(DateUtils.dateTimeNow(DateUtils.YYYY) + "-01");
        Date startTime = DateUtils.parseDate(time + "-01");
        Date endTime = DateUtils.addYears(startTime, 1);
        if (startTime.equals(nowDate)) {
            endTime = DateUtils.parseDate(DateUtils.getTime());
        }
        Date lastMonth = DateUtils.addYears(startTime, -1);

        // 当年能耗
        List<Object> currentList = getWYearByJob(startTime, endTime, areaId, energyType);
        // 去年能耗
        List<Object> beforeList = getWYearByJob(lastMonth, startTime, areaId, energyType);

        BigDecimal currentTotal = BigDecimal.ZERO;

        for (Object item : currentList) {
            if (!(item.toString().equals("--"))) {
                currentTotal = currentTotal.add(BigDecimal.valueOf(Float.parseFloat(item.toString())));
            }
        }

        result.put("currentList", currentList);
        result.put("beforeList", beforeList);
        result.put("currentTotal", currentTotal.setScale(2,RoundingMode.HALF_UP));
        return result;
    }

    private LambdaQueryWrapper<Energy> buildQueryWrapper(EnergyBo bo) {
        LambdaQueryWrapper<Energy> lqw = new LambdaQueryWrapper<>();
        lqw.eq(StringUtils.isNotBlank(bo.getClientId()), Energy::getClientId, bo.getClientId());
        lqw.eq(StringUtils.isNotBlank(bo.getType()), Energy::getType, bo.getType());
        lqw.between(StringUtils.isNotBlank(bo.getStartTime()) && StringUtils.isNotBlank(bo.getEndTime()), Energy::getTs, bo.getStartTime(), bo.getEndTime());
        return lqw;
    }

    /**
     * 当日按小时统计电能——查询Job
     *
     * @param startTime
     * @param endTime
     * @param areaId
     * @return
     */
    private List<Object> getWDayByJob(Date startTime, Date endTime, Long areaId, String energyType) {
        List<Object> result = new ArrayList<>();
        // 通过区域id查询设备编号
        ItemTopology itemTopology = itemTopologyMapper.selectById(areaId);
        if (ObjectUtil.isEmpty(itemTopology.getDeviceId())) {
            return result;
        }
        List<String> deviceId = Arrays.stream(StringUtils.split(itemTopology.getDeviceId(), ",")).collect(Collectors.toList());

        List<EnergyStatistics> energyStatisticsList = energyStatisticsMapper.selectList(new LambdaQueryWrapper<EnergyStatistics>()
            .eq(EnergyStatistics::getEnergyType, energyType)
            .in(EnergyStatistics::getEquipmentSn, deviceId)
            .ge(EnergyStatistics::getUpdateTime, startTime));

        for (int i = 0; i < 24; i++) {
            if (startTime.after(endTime)) {
                break;
            }

            Date endHour = DateUtils.addHours(startTime, 1);
            Date finalStartTime = startTime;
            List<EnergyStatistics> list = energyStatisticsList.stream()
                .filter(e -> e.getUpdateTime().before(endHour) && e.getUpdateTime().after(finalStartTime)).collect(Collectors.toList());

            if (ObjectUtil.isNotEmpty(list) && !list.stream().map(EnergyStatistics::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add).stripTrailingZeros().equals(BigDecimal.ZERO)) {
                result.add(list.stream().map(EnergyStatistics::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add)
                    .setScale(2, RoundingMode.HALF_UP));
            } else {
                result.add("--");
            }

            startTime = endHour;
        }
        return result;
    }

    /**
     * 当月按天统计能耗——查询Job
     *
     * @param startTime
     * @param endTime
     * @param areaId
     * @return
     */
    private List<Object> getWMonthByJob(Date startTime, Date endTime, Long areaId, String energyType) {
        List<Object> result = new ArrayList<>();

        // 通过区域id查询设备编号
        ItemTopology itemTopology = itemTopologyMapper.selectById(areaId);
        if (ObjectUtil.isEmpty(itemTopology.getDeviceId())) {
            return result;
        }
        List<String> deviceId = Arrays.stream(StringUtils.split(itemTopology.getDeviceId(), ",")).collect(Collectors.toList());

        List<EnergyStatistics> energyStatisticsList = energyStatisticsMapper.selectList(new LambdaQueryWrapper<EnergyStatistics>()
            .eq(EnergyStatistics::getEnergyType, energyType)
            .in(EnergyStatistics::getEquipmentSn, deviceId)
            .ge(EnergyStatistics::getUpdateTime, startTime));

        // 当月天数
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        int day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < day; i++) {
            if (startTime.after(endTime)) {
                break;
            }

            Date endDate = DateUtils.addDays(startTime, 1);
            Date finalStartDate = startTime;
            List<EnergyStatistics> list = energyStatisticsList.stream()
                .filter(e -> e.getUpdateTime().before(endDate) && e.getUpdateTime().after(finalStartDate)).collect(Collectors.toList());

            if (ObjectUtil.isNotEmpty(list)) {
                result.add(list.stream().map(EnergyStatistics::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add)
                    .setScale(2, RoundingMode.HALF_UP));
            } else {
                result.add("--");
            }

            startTime = endDate;
        }

        return result;
    }

    /**
     * 能耗趋势-年——查询Job
     *
     * @param startTime
     * @param endTime
     * @param areaId
     * @return
     */
    private List<Object> getWYearByJob(Date startTime, Date endTime, Long areaId, String energyType) {
        List<Object> result = new ArrayList<>();

        // 通过区域id查询设备编号
        ItemTopology itemTopology = itemTopologyMapper.selectById(areaId);
        if (ObjectUtil.isEmpty(itemTopology.getDeviceId())) {
            return result;
        }
        List<String> deviceId = Arrays.stream(StringUtils.split(itemTopology.getDeviceId(), ",")).collect(Collectors.toList());

        List<EnergyStatistics> energyStatisticsList = energyStatisticsMapper.selectList(new LambdaQueryWrapper<EnergyStatistics>()
            .eq(EnergyStatistics::getEnergyType, energyType)
            .in(EnergyStatistics::getEquipmentSn, deviceId)
            .ge(EnergyStatistics::getUpdateTime, startTime));

        for (int i = 0; i < 12; i++) {
            if (startTime.after(endTime)) {
                break;
            }

            Date endDate = DateUtils.addMonths(startTime, 1);
            Date finalStartTime = startTime;
            List<EnergyStatistics> list = energyStatisticsList.stream()
                .filter(e -> e.getUpdateTime().before(endDate) && e.getUpdateTime().after(finalStartTime)).collect(Collectors.toList());

            if (ObjectUtil.isNotEmpty(list)) {
                result.add(list.stream().map(EnergyStatistics::getStatistics).reduce(BigDecimal.ZERO.stripTrailingZeros(), BigDecimal::add)
                    .setScale(2, RoundingMode.HALF_UP));
            } else {
                result.add("--");
            }

            startTime = endDate;
        }

        return result;
    }

    /**
     * 碳排分析-本月，本年
     */
    @Override
    public Map<String, Object> getChain() {
        Map<String, Object> result = new HashMap<>(6);

        //用水量碳排放
        Map<String, BigDecimal> waterResult = getTotalCarbon(EnergyType.WATER.getCode(), 0.91);
        //用电量碳排放
        Map<String, BigDecimal> electricityResult = getTotalCarbon(EnergyType.ELECTRICITY.getCode(), 0.785);

        //总碳排放
        result.put("nowMonth", waterResult.get("nowMonth").add(electricityResult.get("nowMonth")));
        result.put("lastMonth", waterResult.get("lastMonth").add(electricityResult.get("lastMonth")));
        result.put("nowYear", waterResult.get("nowYear").add(electricityResult.get("nowYear")));
        result.put("lastYear", waterResult.get("lastYear").add(electricityResult.get("lastYear")));

        // 趋势
        result.put("yearTrend", BigDecimal.valueOf(Float.parseFloat(result.get("nowYear").toString())).subtract(BigDecimal.valueOf(Float.parseFloat(result.get("lastYear").toString()))).setScale(3, RoundingMode.HALF_UP));
        result.put("monthTrend",  BigDecimal.valueOf(Float.parseFloat(result.get("nowMonth").toString())).subtract(BigDecimal.valueOf(Float.parseFloat(result.get("lastMonth").toString()))).setScale(3, RoundingMode.HALF_UP));

        for (String key :result.keySet()){
            if (result.get(key).toString().equals("0.000")){
                result.put(key,"--");
            }
        }
        return result;
    }

    /**
     * 获取分类碳排放
     */
    public Map<String, BigDecimal> getTotalCarbon(String energyType, double ratio) {
        Map<String, BigDecimal> result = new HashMap<>(4);
        //碳排放系数
        BigDecimal factor = BigDecimal.valueOf(ratio);

        Date nowEndTime = DateUtils.parseDate(DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS));

        Date nowMonthStart = DateUtils.parseDate(DateUtils.dateTimeNow(DateUtils.YYYY_MM));
        Date lastMonthStart = DateUtils.addMonths(nowMonthStart, -1);
        Date lastMonthEnd = DateUtils.addMonths(nowEndTime, -1);

        Date nowYearStart = DateUtils.parseDate(DateUtils.dateTimeNow(DateUtils.YYYY) + "-01-01");
        Date lastYearStart = DateUtils.addYears(nowYearStart, -1);
        Date lastYearEnd = DateUtils.addYears(nowEndTime, -1);

        //查找总节点
        List<Tree<Long>> topologyVoList = iItemTopologyService.selectTreeList(new ItemTopologyBo());
        Long areaId = topologyVoList.get(0).getId();

//        List<EquipmentInfoVo> equipmentInfoVos = equipmentInfoMapper.selectVoList(new LambdaQueryWrapper<EquipmentInfo>().eq(EquipmentInfo::getArea, areaId.toString()));
        // 通过区域id查询设备编号
        //找出当前楼层房间绑定的设备
        ItemTopology itemTopology = itemTopologyMapper.selectById(areaId);

        if (ObjectUtil.isEmpty(itemTopology.getDeviceId())) {
            return result;
        }

        EnergyStatisticsBo bo = new EnergyStatisticsBo();
        bo.setEnergyType(energyType);
        bo.setEquipmentSn(itemTopology.getDeviceId());

        // 当月用能
        bo.setStartTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, nowMonthStart));
        bo.setEndTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, nowEndTime));

        List<EnergyStatisticsVo> nowMonthEle = energyStatisticsMapper.selectVoList(queryWrapper(bo));
        if (ObjectUtil.isNotNull(nowMonthEle)) {
            BigDecimal nowMonth = nowMonthEle.stream().map(EnergyStatisticsVo::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
            result.put("nowMonth", nowMonth.multiply(factor).divide(BigDecimal.valueOf(1000)).setScale(3, RoundingMode.HALF_UP));
        } else {
            result.put("nowMonth", BigDecimal.ZERO);
        }
        // 上月同期用能
        bo.setStartTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, lastMonthStart));
        bo.setEndTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, lastMonthEnd));

        List<EnergyStatisticsVo> lastMonthEle = energyStatisticsMapper.selectVoList(queryWrapper(bo));
        if (ObjectUtil.isNotNull(lastMonthEle)) {
            BigDecimal lastMonth = lastMonthEle.stream().map(EnergyStatisticsVo::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
            result.put("lastMonth", lastMonth.multiply(factor).divide(BigDecimal.valueOf(1000)).setScale(3, RoundingMode.HALF_UP));
        } else {
            result.put("lastMonth", BigDecimal.ZERO);
        }

        // 今年用能
        bo.setStartTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, nowYearStart));
        bo.setEndTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, nowEndTime));

        List<EnergyStatisticsVo> nowYearEle = energyStatisticsMapper.selectVoList(queryWrapper(bo));
        if (ObjectUtil.isNotNull(nowYearEle)) {
            BigDecimal nowYear = nowYearEle.stream().map(EnergyStatisticsVo::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
            result.put("nowYear", nowYear.multiply(factor).divide(BigDecimal.valueOf(1000)).setScale(3, RoundingMode.HALF_UP));
        } else {
            result.put("nowYear", BigDecimal.ZERO);
        }
        // 去年同期用能
        bo.setStartTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, lastYearStart));
        bo.setEndTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, lastYearEnd));

        List<EnergyStatisticsVo> lastYearEle = energyStatisticsMapper.selectVoList(queryWrapper(bo));
        if (ObjectUtil.isNotNull(lastYearEle)) {
            BigDecimal lastYear = lastYearEle.stream().map(EnergyStatisticsVo::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
            result.put("lastYear", lastYear.multiply(factor).divide(BigDecimal.valueOf(1000)).setScale(3, RoundingMode.HALF_UP));
        } else {
            result.put("lastYear", BigDecimal.ZERO);
        }
        return result;
    }

    /**
     * 碳排分析-按年
     */
    @Override
    public List<Object> getChainByYear(String energyType, String date) {
        List<Object> result = new ArrayList<>();
        //碳排放系数
        BigDecimal factor = BigDecimal.valueOf(0.785);
        if (energyType.equals(EnergyType.ELECTRICITY.getCode())) {
            factor = BigDecimal.valueOf(0.785);
        } else if (energyType.equals(EnergyType.WATER.getCode())) {
            factor = BigDecimal.valueOf(0.91);
        }
        //查找总节点
        List<Tree<Long>> topologyVoList = iItemTopologyService.selectTreeList(new ItemTopologyBo());
        Long areaId = topologyVoList.get(0).getId();

        Date nowDate = DateUtils.parseDate(DateUtils.dateTimeNow(DateUtils.YYYY) + "-01");
        Date startTime = DateUtils.parseDate(date + "-01");
        Date endTime = DateUtils.addYears(startTime, 1);
        if (startTime.equals(nowDate)) {
            endTime = DateUtils.parseDate(DateUtils.getTime());
        }

        List<Object> list = getWYearByJob(startTime, endTime, areaId, energyType);
        for (Object big : list) {
            if (!big.toString().equals("--")) {
                BigDecimal res = BigDecimal.valueOf(Float.parseFloat(big.toString())).multiply(factor).divide(BigDecimal.valueOf(1000)).setScale(3, RoundingMode.HALF_UP);
                result.add(res);
            }else {
                result.add("--");
            }

        }

        return result;
    }

    /**
     * 分析报告-chart
     */
    @Override
    public Map<String, Object> getReportChart(Long areaId, String startTime, String endTime, String energyType) {
        Map<String, Object> result = new HashMap<>(4);

        //相差天数
        int day = DateUtils.differentDaysByMillisecond(DateUtils.parseDate(startTime), DateUtils.parseDate(endTime));
        Map<String, BigDecimal> data = new LinkedHashMap<>(day + 1);
        result.put("chartData", data);
        // 通过区域id查询设备编号
        ItemTopology itemTopology = itemTopologyMapper.selectById(areaId);
        /*if () {
            return result;
        }*/

        EnergyStatisticsBo bo = new EnergyStatisticsBo();
        bo.setEnergyType(energyType);
        bo.setEquipmentSn(itemTopology.getDeviceId());
        bo.setStartTime(startTime);
        bo.setEndTime(endTime);

        List<EnergyStatisticsVo> energyVos = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(itemTopology.getDeviceId())) {
             energyVos = energyStatisticsMapper.selectVoList(queryWrapper(bo));
        }

        Date start = DateUtils.parseDate(startTime);
        Date end = DateUtils.parseDate(endTime);
        for (int i = 0; i < day + 1; i++) {
//            if (start.after(end)) {
//                break;
//            }

            Date begin = DateUtils.addDays(start, i);
            Date finish = DateUtils.addDays(start, i + 1);
            List<EnergyStatisticsVo> voList = energyVos.stream()
                .filter(e -> e.getUpdateTime().before(finish) && e.getUpdateTime().after(begin)).collect(Collectors.toList());

            String key = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, begin).substring(5, 10);
            if (ObjectUtil.isNotEmpty(voList)) {
                data.put(key, voList.stream().map(EnergyStatisticsVo::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
            } else {
                data.put(key, BigDecimal.ZERO);
            }
        }

        //总用量
        BigDecimal total = BigDecimal.ZERO;

        for (BigDecimal item : data.values()) {
            total = total.add(item);
        }
        result.put("total", total);

        //最大值
        Collection<BigDecimal> collection = data.values();
        Object[] obj = collection.toArray();
        Arrays.sort(obj);
        BigDecimal max = (BigDecimal) obj[data.size() - 1];
        result.put("max", max);

        //最大值日期
        result.put("maxDate", minOrMaxValueKeyBigDecimal(data, "max"));

        return result;
    }

    /**
     * 获取map集合最大或最小值对应的key
     * 该方法仅用来判断value为BigDecimal类型的Map集合
     *
     * @param map    集合
     * @param choose min最小值 max最大值
     * @return 最大或最小值的key
     */
    public static String minOrMaxValueKeyBigDecimal(Map<?, BigDecimal> map, String choose) {
        List<Map.Entry<?, BigDecimal>> list = new ArrayList<>(map.entrySet());
        list.sort(Comparator.comparingDouble(o -> o.getValue().doubleValue()));
        if (choose.equals("min")) {
            return String.valueOf(list.get(0).getKey());
        }
        return String.valueOf(list.get(list.size() - 1).getKey());
    }

    /**
     * 分析报告-table
     */
    @Override
    public List<ReportTableVo> getReportTable(Long areaId, String startTime, String endTime) {
        List<ReportTableVo> result = new ArrayList<>();

        //相差天数
        int day = DateUtils.differentDaysByMillisecond(DateUtils.parseDate(startTime), DateUtils.parseDate(endTime));
        //上期
        Date lastTime = DateUtils.addDays(DateUtils.parseDate(startTime), -day);
        //同期
        Date sameStartTime = DateUtils.addYears(DateUtils.parseDate(startTime), -1);
        Date sameEndTime = DateUtils.addYears(DateUtils.parseDate(endTime), -1);

        //本期用电量
        Map<String, Object> nowElectricity = getReportChart(areaId, startTime, endTime, "0");
        //上期用电量
        Map<String, Object> lastElectricity = getReportChart(areaId, DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, lastTime), startTime, "0");
        //环比
        BigDecimal chainElectricity = BigDecimal.ZERO;
        if (!lastElectricity.get("total").equals(BigDecimal.ZERO)) {
            chainElectricity = (((BigDecimal) nowElectricity.get("total")).subtract((BigDecimal) lastElectricity.get("total")))
                .multiply(BigDecimal.valueOf(100)).divide((BigDecimal) lastElectricity.get("total"), 2, RoundingMode.HALF_UP);
        }
        //同期用电量
        Map<String, Object> sameElectricity = getReportChart(areaId, DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, sameStartTime), DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, sameEndTime), "0");
        //同比
        BigDecimal yoyElectricity = BigDecimal.ZERO;
        if (!sameElectricity.get("total").equals(BigDecimal.ZERO)) {
            yoyElectricity = (((BigDecimal) nowElectricity.get("total")).subtract((BigDecimal) sameElectricity.get("total")))
                .multiply(BigDecimal.valueOf(100)).divide((BigDecimal) sameElectricity.get("total"), 2, RoundingMode.HALF_UP);
        }

        //本期用水量
        Map<String, Object> nowWater = getReportChart(areaId, startTime, endTime, "1");
        //上期用水量
        Map<String, Object> lastWater = getReportChart(areaId, DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, lastTime), startTime, "1");
        //环比
        BigDecimal chainWater = BigDecimal.ZERO;
        if (!lastWater.get("total").equals(BigDecimal.ZERO)) {
            chainWater = (((BigDecimal) nowWater.get("total")).subtract((BigDecimal) lastWater.get("total")))
                .multiply(BigDecimal.valueOf(100)).divide((BigDecimal) lastWater.get("total"), 2, RoundingMode.HALF_UP);
        }
        //同期用水量
        Map<String, Object> sameWater = getReportChart(areaId, DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, sameStartTime), DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, sameEndTime), "1");
        //同比
        BigDecimal yoyWater = BigDecimal.ZERO;
        if (!sameWater.get("total").equals(BigDecimal.ZERO)) {
            yoyWater = (((BigDecimal) nowWater.get("total")).subtract((BigDecimal) sameWater.get("total")))
                .multiply(BigDecimal.valueOf(100)).divide((BigDecimal) sameWater.get("total"), 2, RoundingMode.HALF_UP);
        }

        //本期综合能耗
        BigDecimal nowEnergy = ((BigDecimal) nowElectricity.get("total")).divide(BigDecimal.valueOf(8.167), 2, RoundingMode.HALF_UP)
            .add(((BigDecimal) nowWater.get("total")).multiply(BigDecimal.valueOf(0.4857)).setScale(2, RoundingMode.HALF_UP));
        //上期综合能耗
        BigDecimal lastEnergy = ((BigDecimal) lastElectricity.get("total")).divide(BigDecimal.valueOf(8.167), 2, RoundingMode.HALF_UP)
            .add(((BigDecimal) lastWater.get("total")).multiply(BigDecimal.valueOf(0.4857)).setScale(2, RoundingMode.HALF_UP));
        //环比
        BigDecimal chainEnergy = BigDecimal.ZERO;
        if (lastEnergy.compareTo(BigDecimal.ZERO) != 0) {
            chainEnergy = (nowEnergy.subtract(lastEnergy)).multiply(BigDecimal.valueOf(100))
                .divide(lastEnergy, 2, RoundingMode.HALF_UP);
        }
        //同期综合能耗
        BigDecimal sameEnergy = ((BigDecimal) sameElectricity.get("total")).divide(BigDecimal.valueOf(8.167), 2, RoundingMode.HALF_UP)
            .add(((BigDecimal) sameWater.get("total")).multiply(BigDecimal.valueOf(0.4857)).setScale(2, RoundingMode.HALF_UP));
        //同比
        BigDecimal yoyEnergy = BigDecimal.ZERO;
        if (sameEnergy.compareTo(BigDecimal.ZERO) != 0) {
            yoyEnergy = (nowEnergy.subtract(sameEnergy)).multiply(BigDecimal.valueOf(100))
                .divide(sameEnergy, 2, RoundingMode.HALF_UP);
        }

        //本期碳排放量
        BigDecimal nowCarbon = ((BigDecimal) nowElectricity.get("total")).multiply(BigDecimal.valueOf(0.785)).setScale(2, RoundingMode.HALF_UP)
            .add(((BigDecimal) nowWater.get("total")).multiply(BigDecimal.valueOf(0.91)).setScale(2, RoundingMode.HALF_UP));
        //上期碳排放量
        BigDecimal lastCarbon = ((BigDecimal) lastElectricity.get("total")).multiply(BigDecimal.valueOf(0.785)).setScale(2, RoundingMode.HALF_UP)
            .add(((BigDecimal) lastWater.get("total")).multiply(BigDecimal.valueOf(0.91)).setScale(2, RoundingMode.HALF_UP));
        //环比
        BigDecimal chainCarbon = BigDecimal.ZERO;
        if (lastCarbon.compareTo(BigDecimal.ZERO) != 0) {
            chainCarbon = (nowCarbon.subtract(lastCarbon)).multiply(BigDecimal.valueOf(100))
                .divide(lastCarbon, 2, RoundingMode.HALF_UP);
        }
        //同期碳排放量
        BigDecimal sameCarbon = ((BigDecimal) sameElectricity.get("total")).multiply(BigDecimal.valueOf(0.785)).setScale(2, RoundingMode.HALF_UP)
            .add(((BigDecimal) sameWater.get("total")).multiply(BigDecimal.valueOf(0.91)).setScale(2, RoundingMode.HALF_UP));
        //环比
        BigDecimal yoyCarbon = BigDecimal.ZERO;
        if (sameCarbon.compareTo(BigDecimal.ZERO) != 0) {
            yoyCarbon = (nowCarbon.subtract(sameCarbon)).multiply(BigDecimal.valueOf(100))
                .divide(sameCarbon, 2, RoundingMode.HALF_UP);
        }

        ReportTableVo electricity = new ReportTableVo();
        ReportTableVo water = new ReportTableVo();
        ReportTableVo energy = new ReportTableVo();
        ReportTableVo carbon = new ReportTableVo();

        electricity.setType("用电量");
        electricity.setData((BigDecimal) nowElectricity.get("total"));
        electricity.setChain(chainElectricity);
        electricity.setSame(yoyElectricity);

        water.setType("用水量");
        water.setData((BigDecimal) nowWater.get("total"));
        water.setChain(chainWater);
        water.setSame(yoyWater);

        energy.setType("综合能耗");
        energy.setData(nowEnergy);
        energy.setChain(chainEnergy);
        energy.setSame(yoyEnergy);

        carbon.setType("碳排放量");
        carbon.setData(nowCarbon);
        carbon.setChain(chainCarbon);
        carbon.setSame(yoyCarbon);

        result.add(electricity);
        result.add(water);
        result.add(energy);
        result.add(carbon);

        return result;
    }

    /**
     * 统计当前一小时能耗
     *
     * @param energyType
     */
    @Override
    public Boolean statisticsEnergyByHour(String energyType) {
        // 获取当前小时时间范围
        String startTime = DateUtils.dateTimeNow("yyyy-MM-dd HH:00:00");

        // 查询所有设备
        List<EquipmentInfoVo> equipmentInfoVos = equipmentInfoMapper.selectVoList(new LambdaQueryWrapper<EquipmentInfo>()
            .eq(StringUtils.isNotBlank(energyType), EquipmentInfo::getType, energyType));
        if (ObjectUtil.isEmpty(equipmentInfoVos)) {
            return Boolean.TRUE;
        }

        // 能源类型
        String tableName = EnergyType.ELECTRICITY.getInfo();
        if (energyType.equals(EnergyType.ELECTRICITY.getCode())) {
            tableName = EnergyType.ELECTRICITY.getInfo();
        } else if (energyType.equals(EnergyType.WATER.getCode())) {
            tableName = EnergyType.WATER.getInfo();
        }
        // 查询td表中当前一小时数据
        List<EnergyVo> nowElectricity = baseMapper.selectVoList(new LambdaQueryWrapper<Energy>()
            .eq(Energy::getType, tableName)
            .ge(Energy::getTs, startTime).orderByDesc(Energy::getTs));

        // 查询统计表中当前一小时的数据
        List<EnergyStatistics> energyStatisticsList = energyStatisticsMapper.selectList(new LambdaQueryWrapper<EnergyStatistics>()
            .eq(EnergyStatistics::getEnergyType, energyType)
            .ge(EnergyStatistics::getTime, startTime));

        // 新增、修改列表
        List<EnergyStatistics> updateList = new ArrayList<>();

        // 统计每台设备当前一小时能耗
        for (EquipmentInfoVo equipmentInfoVo : equipmentInfoVos) {
            List<EnergyVo> energyVos = nowElectricity.stream().filter(e -> e.getClientId().equals(equipmentInfoVo.getSn())).sorted(Comparator.comparing(EnergyVo::getTs)).collect(Collectors.toList());
            BigDecimal statistics = BigDecimal.ZERO;
            if (ObjectUtil.isNotEmpty(energyVos)) {
                statistics = BigDecimal.valueOf(energyVos.get(energyVos.size() - 1).getVal() - energyVos.get(0).getVal());
            }

            List<EnergyStatistics> statisticsList = energyStatisticsList.stream().filter(e -> e.getEquipmentSn().equals(equipmentInfoVo.getSn())).collect(Collectors.toList());
            // 如果有修改，没有新增
            EnergyStatistics updateItem = new EnergyStatistics();
            if (ObjectUtil.isEmpty(statisticsList)) {
                updateItem.setStatistics(statistics);
                updateItem.setEnergyType(energyType);
                updateItem.setTime(DateUtils.parseDate(startTime));
                updateItem.setEquipmentSn(equipmentInfoVo.getSn());
                updateList.add(updateItem);
            } else {
                updateItem = statisticsList.get(0);
                updateItem.setStatistics(statistics);
                updateList.add(updateItem);
            }
        }
        return energyStatisticsMapper.insertOrUpdateBatch(updateList);
    }

    /**
     * 统计当前一小时电功率峰谷值平均值
     *
     * @param energyType
     */
    @Override
    public Boolean statisticsPowerByHour(String energyType) {
        // 获取当前小时时间范围
        String startTime = DateUtils.dateTimeNow("yyyy-MM-dd HH:00:00");

        // 查询所有设备
        List<EquipmentInfoVo> equipmentInfoVos = equipmentInfoMapper.selectVoList(new LambdaQueryWrapper<EquipmentInfo>()
            .eq(StringUtils.isNotBlank(energyType), EquipmentInfo::getType, energyType));
        if (ObjectUtil.isEmpty(equipmentInfoVos)) {
            return Boolean.TRUE;
        }

        // 能源类型
        String tableName = EnergyType.ELECTRICITYP.getInfo();
        // 查询td表中当前一小时数据
        List<EnergyVo> nowElectricity = baseMapper.selectVoList(new LambdaQueryWrapper<Energy>()
            .eq(Energy::getType, tableName)
            .ge(Energy::getTs, startTime).orderByDesc(Energy::getTs));

        // 查询统计表中当前一小时的数据
        List<PowerStatistics> powerStatisticsList = powerStatisticsMapper.selectList(new LambdaQueryWrapper<PowerStatistics>()
            .eq(PowerStatistics::getEnergyType, energyType)
            .ge(PowerStatistics::getTime, startTime));

        // 新增、修改列表
        List<PowerStatistics> updateList = new ArrayList<>();

        // 统计每台设备当前一小时能耗
        for (EquipmentInfoVo equipmentInfoVo : equipmentInfoVos) {
            List<EnergyVo> energyVos = nowElectricity.stream().filter(e -> e.getClientId().equals(equipmentInfoVo.getSn())).sorted(Comparator.comparing(EnergyVo::getTs)).collect(Collectors.toList());
            BigDecimal min = BigDecimal.ZERO;
            BigDecimal max = BigDecimal.ZERO;
            BigDecimal ave = BigDecimal.ZERO;
            if (ObjectUtil.isNotEmpty(energyVos)) {
                min = BigDecimal.valueOf(energyVos.stream().min(Comparator.comparing(EnergyVo::getVal)).get().getVal()).setScale(2, RoundingMode.HALF_UP);
                max = BigDecimal.valueOf(energyVos.stream().max(Comparator.comparing(EnergyVo::getVal)).get().getVal()).setScale(2, RoundingMode.HALF_UP);
                ave = BigDecimal.valueOf(energyVos.stream().mapToDouble(EnergyVo::getVal).average().getAsDouble());
            }

            List<PowerStatistics> statisticsList = powerStatisticsList.stream().filter(e -> e.getEquipmentSn().equals(equipmentInfoVo.getSn())).collect(Collectors.toList());
            // 如果有修改，没有新增
            PowerStatistics updateItem = new PowerStatistics();
            if (ObjectUtil.isEmpty(statisticsList)) {
                updateItem.setMin(min);
                updateItem.setMax(max);
                updateItem.setAve(ave);
                updateItem.setEnergyType(energyType);
                updateItem.setTime(DateUtils.parseDate(startTime));
                updateItem.setEquipmentSn(equipmentInfoVo.getSn());
                updateList.add(updateItem);
            } else {
                updateItem = statisticsList.get(0);
                updateItem.setMax(max);
                updateItem.setMin(min);
                updateItem.setAve(ave);
                updateList.add(updateItem);
            }
        }
        return powerStatisticsMapper.insertOrUpdateBatch(updateList);
    }

    /**
     * 统计当前一小时电流峰谷值平均值
     *
     * @param energyType
     */
    @Override
    public Boolean statisticsCurrentByHour(String energyType) {
        // 获取当前小时时间范围
        String startTime = DateUtils.dateTimeNow("yyyy-MM-dd HH:00:00");

        // 查询所有设备
        List<EquipmentInfoVo> equipmentInfoVos = equipmentInfoMapper.selectVoList(new LambdaQueryWrapper<EquipmentInfo>()
            .eq(StringUtils.isNotBlank(energyType), EquipmentInfo::getType, energyType));
        if (ObjectUtil.isEmpty(equipmentInfoVos)) {
            return Boolean.TRUE;
        }

        // 能源类型
        String tableName = EnergyType.ELECTRICITYI.getInfo();
        // 查询td表中当前一小时数据
        List<EnergyVo> nowElectricity = baseMapper.selectVoList(new LambdaQueryWrapper<Energy>()
            .eq(Energy::getType, tableName)
            .ge(Energy::getTs, startTime).orderByDesc(Energy::getTs));

        // 查询统计表中当前一小时的数据
        List<CurrentStatistics> currentStatisticsList = currentStatisticsMapper.selectList(new LambdaQueryWrapper<CurrentStatistics>()
            .eq(CurrentStatistics::getEnergyType, energyType)
            .ge(CurrentStatistics::getTime, startTime));

        // 新增、修改列表
        List<CurrentStatistics> updateList = new ArrayList<>();

        // 统计每台设备当前一小时能耗
        for (EquipmentInfoVo equipmentInfoVo : equipmentInfoVos) {
            List<EnergyVo> energyVos = nowElectricity.stream().filter(e -> e.getClientId().equals(equipmentInfoVo.getSn())).sorted(Comparator.comparing(EnergyVo::getTs)).collect(Collectors.toList());
            BigDecimal min = BigDecimal.ZERO;
            BigDecimal max = BigDecimal.ZERO;
            BigDecimal ave = BigDecimal.ZERO;
            if (ObjectUtil.isNotEmpty(energyVos)) {
                min = BigDecimal.valueOf(energyVos.stream().min(Comparator.comparing(EnergyVo::getVal)).get().getVal()).setScale(2, RoundingMode.HALF_UP);
                max = BigDecimal.valueOf(energyVos.stream().max(Comparator.comparing(EnergyVo::getVal)).get().getVal()).setScale(2, RoundingMode.HALF_UP);
                ave = BigDecimal.valueOf(energyVos.stream().mapToDouble(EnergyVo::getVal).average().getAsDouble());
            }

            List<CurrentStatistics> statisticsList = currentStatisticsList.stream().filter(e -> e.getEquipmentSn().equals(equipmentInfoVo.getSn())).collect(Collectors.toList());
            // 如果有修改，没有新增
            CurrentStatistics updateItem = new CurrentStatistics();
            if (ObjectUtil.isEmpty(statisticsList)) {
                updateItem.setMin(min);
                updateItem.setMax(max);
                updateItem.setAve(ave);
                updateItem.setEnergyType(energyType);
                updateItem.setTime(DateUtils.parseDate(startTime));
                updateItem.setEquipmentSn(equipmentInfoVo.getSn());
                updateList.add(updateItem);
            } else {
                updateItem = statisticsList.get(0);
                updateItem.setMax(max);
                updateItem.setMin(min);
                updateItem.setAve(ave);
                updateList.add(updateItem);
            }
        }
        return currentStatisticsMapper.insertOrUpdateBatch(updateList);
    }

    /**
     * 统计当前一小时电压峰谷值平均值
     *
     * @param energyType
     */
    @Override
    public Boolean statisticsVoltageByHour(String energyType) {
        // 获取当前小时时间范围
        String startTime = DateUtils.dateTimeNow("yyyy-MM-dd HH:00:00");

        // 查询所有设备
        List<EquipmentInfoVo> equipmentInfoVos = equipmentInfoMapper.selectVoList(new LambdaQueryWrapper<EquipmentInfo>()
            .eq(StringUtils.isNotBlank(energyType), EquipmentInfo::getType, energyType));
        if (ObjectUtil.isEmpty(equipmentInfoVos)) {
            return Boolean.TRUE;
        }

        // 能源类型
        String tableName = EnergyType.ELECTRICITYU.getInfo();
        // 查询td表中当前一小时数据
        List<EnergyVo> nowElectricity = baseMapper.selectVoList(new LambdaQueryWrapper<Energy>()
            .eq(Energy::getType, tableName)
            .ge(Energy::getTs, startTime).orderByDesc(Energy::getTs));

        // 查询统计表中当前一小时的数据
        List<VoltageStatistics> voltageStatisticsList = voltageStatisticsMapper.selectList(new LambdaQueryWrapper<VoltageStatistics>()
            .eq(VoltageStatistics::getEnergyType, energyType)
            .ge(VoltageStatistics::getTime, startTime));

        // 新增、修改列表
        List<VoltageStatistics> updateList = new ArrayList<>();

        // 统计每台设备当前一小时能耗
        for (EquipmentInfoVo equipmentInfoVo : equipmentInfoVos) {
            List<EnergyVo> energyVos = nowElectricity.stream().filter(e -> e.getClientId().equals(equipmentInfoVo.getSn())).sorted(Comparator.comparing(EnergyVo::getTs)).collect(Collectors.toList());
            BigDecimal min = BigDecimal.ZERO;
            BigDecimal max = BigDecimal.ZERO;
            BigDecimal ave = BigDecimal.ZERO;
            if (ObjectUtil.isNotEmpty(energyVos)) {
                min = BigDecimal.valueOf(energyVos.stream().min(Comparator.comparing(EnergyVo::getVal)).get().getVal()).setScale(2, RoundingMode.HALF_UP);
                max = BigDecimal.valueOf(energyVos.stream().max(Comparator.comparing(EnergyVo::getVal)).get().getVal()).setScale(2, RoundingMode.HALF_UP);
                ave = BigDecimal.valueOf(energyVos.stream().mapToDouble(EnergyVo::getVal).average().getAsDouble());
            }

            List<VoltageStatistics> statisticsList = voltageStatisticsList.stream().filter(e -> e.getEquipmentSn().equals(equipmentInfoVo.getSn())).collect(Collectors.toList());
            // 如果有修改，没有新增
            VoltageStatistics updateItem = new VoltageStatistics();
            if (ObjectUtil.isEmpty(statisticsList)) {
                updateItem.setMin(min);
                updateItem.setMax(max);
                updateItem.setAve(ave);
                updateItem.setEnergyType(energyType);
                updateItem.setTime(DateUtils.parseDate(startTime));
                updateItem.setEquipmentSn(equipmentInfoVo.getSn());
                updateList.add(updateItem);
            } else {
                updateItem = statisticsList.get(0);
                updateItem.setMax(max);
                updateItem.setMin(min);
                updateItem.setAve(ave);
                updateList.add(updateItem);
            }
        }
        return voltageStatisticsMapper.insertOrUpdateBatch(updateList);
    }

    /**
     * 实际累计能耗
     */
    @Override
    public BigDecimal getAccumulate(Long itemId, String quotaType, Date quotaTime) {
        BigDecimal result = BigDecimal.ZERO;
        ItemTopology itemTopology = itemTopologyMapper.selectById(itemId);
        List<String> devices = Arrays.stream(StringUtils.split(itemTopology.getDeviceId(), ",")).collect(Collectors.toList());
        Date endTime = new Date();
        if (quotaType.equals("0")) {
            // 当月天数
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(quotaTime);
            int day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            endTime = DateUtils.parseDate(DateUtils.parseDateToStr("yyyy-MM-" + day + " 23:59:59", quotaTime));
        } else if (quotaType.equals("1")) {
            endTime = DateUtils.parseDate(DateUtils.parseDateToStr("yyyy-12-31 23:59:59", quotaTime));
        }
        List<EnergyStatistics> energyStatistics = energyStatisticsMapper.selectList(new LambdaQueryWrapper<EnergyStatistics>()
            .between(EnergyStatistics::getTime, quotaTime, endTime)
            .in(EnergyStatistics::getEquipmentSn, devices));
        if (ObjectUtil.isNotEmpty(energyStatistics)) {
            result = energyStatistics.stream().map(EnergyStatistics::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
        }
        return result;
    }

    /**
     * 电力参数查询
     */
    @Override
    public List<EnergyVo> queryPowerParameter(Long areaId, String startTime, String endTime, String energyType) {

        //找出当前所选楼层
        ItemTopology itemTopology = itemTopologyMapper.selectById(areaId);
        List<String> equip = StringUtils.splitList(itemTopology.getDeviceId(), ",");

        //整理出同类型设备
        EquipmentInfo equipmentInfo = equipmentInfoMapper.selectOne(new LambdaQueryWrapper<EquipmentInfo>()
            .in(EquipmentInfo::getSn, equip)
            .eq(EquipmentInfo::getType, EnergyType.ELECTRICITY.getCode())
            .last("limit 1"));
        if (ObjectUtil.isEmpty(equipmentInfo)) {
            return new ArrayList<>();
        }

        // 查询td表中相关设备数据
        return baseMapper.selectVoList(new LambdaQueryWrapper<Energy>()
            .eq(Energy::getClientId, equipmentInfo.getSn())
            .eq(Energy::getType, energyType)
            .between(Energy::getTs, startTime, endTime));
    }

    /**
     * 逐日极值查询
     */
    @Override
    public List<PowerStatisticsVo> queryDailyExtremum(Long areaId, String startTime, String endTime, String energyType) {
        Date start = DateUtils.dateTime(DateUtils.YYYY_MM_DD, startTime);
        Date end = DateUtils.dateTime(DateUtils.YYYY_MM_DD, endTime);
        List<PowerStatisticsVo> result = new ArrayList<>();
        //找出当前所选楼层
        ItemTopology itemTopology = itemTopologyMapper.selectById(areaId);
        List<String> equip = StringUtils.splitList(itemTopology.getDeviceId(), ",");

        //整理出同类型设备
        EquipmentInfo equipmentInfo = equipmentInfoMapper.selectOne(new LambdaQueryWrapper<EquipmentInfo>()
            .in(EquipmentInfo::getSn, equip)
            .eq(EquipmentInfo::getType, EnergyType.ELECTRICITY.getCode())
            .last("limit 1"));
        if (ObjectUtil.isEmpty(equipmentInfo)) {
            return new ArrayList<>();
        }

        if (energyType.equals(EnergyType.ELECTRICITYP.getInfo())) {
            // 筛选所选时间范围内该设备的中间计算结果
            List<PowerStatistics> all = powerStatisticsMapper.selectList(new LambdaQueryWrapper<PowerStatistics>()
                .eq(PowerStatistics::getEquipmentSn, equipmentInfo.getSn())
                .between(PowerStatistics::getCreateTime, start, DateUtils.addDays(end, 1)));
            while (start.before(end) || start.equals(end)) {
                Date tempEnd = DateUtils.addDays(start, 1);
                PowerStatisticsVo ps = new PowerStatisticsVo();
                Date finalStart = start;
                List<PowerStatistics> temp = all.stream().filter(e -> e.getCreateTime().after(finalStart) && e.getCreateTime().before(tempEnd)).collect(Collectors.toList());
                BigDecimal max = BigDecimal.ZERO;
                BigDecimal min = BigDecimal.ZERO;
                BigDecimal average = BigDecimal.ZERO;
                //求最大值
                if (ObjectUtil.isNotEmpty(temp)) {
                    max = temp.stream().map(PowerStatistics::getMax).max(BigDecimal::compareTo).get();
                    //求最小值
                    min = temp.stream().map(PowerStatistics::getMin).min(Comparator.naturalOrder()).get();
                    //求平均值
                    average = temp.stream().map(vo -> ObjectUtil.isEmpty(vo.getAve()) ? new BigDecimal(0) : vo.getAve()).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(temp.size()), 2, BigDecimal.ROUND_HALF_UP);
                }

                ps.setMin(min);
                ps.setMax(max);
                ps.setAve(average);
                ps.setDate(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, start));
                result.add(ps);
                start = tempEnd;
            }
        }
        if (energyType.equals(EnergyType.ELECTRICITYI.getInfo())) {
            // 筛选所选时间范围内该设备的中间计算结果
            List<CurrentStatistics> all = currentStatisticsMapper.selectList(new LambdaQueryWrapper<CurrentStatistics>()
                .eq(CurrentStatistics::getEquipmentSn, equipmentInfo.getSn())
                .between(CurrentStatistics::getCreateTime, start, DateUtils.addDays(end, 1)));
            while (start.before(end) || start.equals(end)) {
                Date tempEnd = DateUtils.addDays(start, 1);
                PowerStatisticsVo ps = new PowerStatisticsVo();
                Date finalStart = start;
                List<CurrentStatistics> temp = all.stream().filter(e -> e.getCreateTime().after(finalStart) && e.getCreateTime().before(tempEnd)).collect(Collectors.toList());
                BigDecimal max = BigDecimal.ZERO;
                BigDecimal min = BigDecimal.ZERO;
                BigDecimal average = BigDecimal.ZERO;
                //求最大值
                if (ObjectUtil.isNotEmpty(temp)) {
                    max = temp.stream().map(CurrentStatistics::getMax).max(BigDecimal::compareTo).get();
                    //求最小值
                    min = temp.stream().map(CurrentStatistics::getMin).min(Comparator.naturalOrder()).get();
                    //求平均值
                    average = temp.stream().map(vo -> ObjectUtil.isEmpty(vo.getAve()) ? new BigDecimal(0) : vo.getAve()).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(temp.size()), 2, BigDecimal.ROUND_HALF_UP);
                }

                ps.setMin(min);
                ps.setMax(max);
                ps.setAve(average);
                ps.setDate(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, start));
                result.add(ps);
                start = tempEnd;
            }
        }
        if (energyType.equals(EnergyType.ELECTRICITYU.getInfo())) {
            // 筛选所选时间范围内该设备的中间计算结果
            List<VoltageStatistics> all = voltageStatisticsMapper.selectList(new LambdaQueryWrapper<VoltageStatistics>()
                .eq(VoltageStatistics::getEquipmentSn, equipmentInfo.getSn())
                .between(VoltageStatistics::getCreateTime, start, DateUtils.addDays(end, 1)));
            while (start.before(end) || start.equals(end)) {
                Date tempEnd = DateUtils.addDays(start, 1);
                PowerStatisticsVo ps = new PowerStatisticsVo();
                Date finalStart = start;
                List<VoltageStatistics> temp = all.stream().filter(e -> e.getCreateTime().after(finalStart) && e.getCreateTime().before(tempEnd)).collect(Collectors.toList());
                BigDecimal max = BigDecimal.ZERO;
                BigDecimal min = BigDecimal.ZERO;
                BigDecimal average = BigDecimal.ZERO;
                //求最大值
                if (ObjectUtil.isNotEmpty(temp)) {
                    max = temp.stream().map(VoltageStatistics::getMax).max(BigDecimal::compareTo).get();
                    //求最小值
                    min = temp.stream().map(VoltageStatistics::getMin).min(Comparator.naturalOrder()).get();
                    //求平均值
                    average = temp.stream().map(vo -> ObjectUtil.isEmpty(vo.getAve()) ? new BigDecimal(0) : vo.getAve()).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(temp.size()), 2, BigDecimal.ROUND_HALF_UP);
                }

                ps.setMin(min);
                ps.setMax(max);
                ps.setAve(average);
                ps.setDate(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, start));
                result.add(ps);
                start = tempEnd;
            }
        }
        return result;
    }

    /**
     * 分析报告-电费chart
     */
    @Override
    public Map<String, Object> getReportChargingChart(Long areaId, String startTime, String endTime, String energyType) {
        Map<String, Object> result = new HashMap<>(4);

        //相差天数
        int day = DateUtils.differentDaysByMillisecond(DateUtils.parseDate(startTime), DateUtils.parseDate(endTime));
        Map<String, BigDecimal> data = new LinkedHashMap<>(day + 1);
        result.put("chartData", data);
        // 通过区域id查询设备编号
        ItemTopology itemTopology = itemTopologyMapper.selectById(areaId);
        if (ObjectUtil.isEmpty(itemTopology.getDeviceId())) {
            return result;
        }

        //获取最新的计费规则
        ChargingVo chargingVo = chargingMapper.selectVoOne(new LambdaQueryWrapper<Charging>()
            .eq(Charging::getStatus, "0")
            .eq(Charging::getType, energyType)
            .le(Charging::getStartDate, DateUtils.getNowDate())
            .ge(Charging::getEndDate, DateUtils.getNowDate())
            .last("limit 1")
        );

        if (ObjectUtil.isEmpty(chargingVo)) {
            return result;
        }

        //获取尖峰平谷对应的所有时间段
        String sharp = configService.selectConfigByKey("sys.sharp");
        String peek = configService.selectConfigByKey("sys.peek");
        String ordinary = configService.selectConfigByKey("sys.ordinary");
        String valley = configService.selectConfigByKey("sys.valley");

        //获取阶梯计费开启时所有的阶梯计费规则
        List<ChargingStep> chargingSteps = chargingStepMapper.selectList(new LambdaQueryWrapper<ChargingStep>()
            .eq(ChargingStep::getChargingId, chargingVo.getChargingId())
            .orderByAsc(ChargingStep::getStartStep));


        EnergyStatisticsBo bo = new EnergyStatisticsBo();
        bo.setEnergyType(energyType);
        bo.setEquipmentSn(itemTopology.getDeviceId());
        bo.setStartTime(startTime);
        bo.setEndTime(endTime);

        //获取所选时间段内所有的中间计算结果
        List<EnergyStatisticsVo> energyVos = energyStatisticsMapper.selectVoList(queryWrapper(bo));


        Date start = DateUtils.parseDate(startTime);
        Date end = DateUtils.parseDate(endTime);
        for (int i = 0; i < day + 1; i++) {
//            if (start.after(end)) {
//                break;
//            }

            Date begin = DateUtils.addDays(start, i);
            Date finish = DateUtils.addDays(start, i + 1);

            //获取当前时段内所有的中间计算结果
            List<EnergyStatisticsVo> voList = energyVos.stream()
                .filter(e -> e.getUpdateTime().before(finish) && e.getUpdateTime().after(begin)).collect(Collectors.toList());

            //获取当前循环日期
            String key = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, begin).substring(5, 10);
            data.put(key, getFinalCharging(chargingVo, chargingSteps, voList, sharp, peek, ordinary, valley));
        }


        //总用量
        BigDecimal total = BigDecimal.ZERO;

        for (BigDecimal item : data.values()) {
            total = total.add(item);
        }
        result.put("total", total);

/*        //最大值
        Collection<BigDecimal> collection = data.values();
        Object[] obj = collection.toArray();
        Arrays.sort(obj);
        BigDecimal max = (BigDecimal) obj[data.size() - 1];
        result.put("max", max);

        //最大值日期
        result.put("maxDate", minOrMaxValueKeyBigDecimal(data, "max"));*/

        return result;
    }

    /**
     * 阶梯计费下计算电费
     *
     * @param total         总用电量
     * @param chargingSteps 阶梯
     * @param unitPrice     原始价格
     * @return
     */
    private BigDecimal getStepCharging(BigDecimal total, List<ChargingStep> chargingSteps, BigDecimal unitPrice) {
        BigDecimal price = BigDecimal.ZERO;
        for (ChargingStep step : chargingSteps) {
            if (total.intValue() < step.getStartStep()) {
                price = price.add(total.multiply(unitPrice)
                    .setScale(2, RoundingMode.HALF_UP));
                break;
            }
            if (total.intValue() > step.getStartStep() && total.intValue() < step.getEndStep()) {
                price = price.add(total.subtract(BigDecimal.valueOf(step.getStartStep()))
                    .multiply(unitPrice.add(step.getPriceDifference()))
                    .setScale(2, RoundingMode.HALF_UP));
                break;
            }
            if (total.intValue() > step.getEndStep()) {
                price = price.add(BigDecimal.valueOf(step.getEndStep() - step.getStartStep()).multiply(unitPrice.add(step.getPriceDifference())).setScale(2, RoundingMode.HALF_UP));
            }
        }
        return price;
    }

    /**
     * 分析报告-复费率table
     */
    @Override
    public List<ReportTableVo> getRecurringRate(Long areaId, String startTime, String endTime, String energyType) {
        List<ReportTableVo> result = new ArrayList<>();

        // 通过区域id查询设备编号
        ItemTopology itemTopology = itemTopologyMapper.selectById(areaId);
        if (ObjectUtil.isEmpty(itemTopology.getDeviceId())) {
            return result;
        }

        EnergyStatisticsBo bo = new EnergyStatisticsBo();
        bo.setEnergyType(energyType);
        bo.setEquipmentSn(itemTopology.getDeviceId());
        bo.setStartTime(startTime);
        bo.setEndTime(endTime);

        //获取所选时间段内所有的中间计算结果
        List<EnergyStatisticsVo> energyVos = energyStatisticsMapper.selectVoList(queryWrapper(bo));

        bo.setStartTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, DateUtils.addMonths(DateUtils.parseDate(startTime), -1)));
        bo.setEndTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, DateUtils.addMonths(DateUtils.parseDate(endTime), -1)));

        //获取所选时间段内上一期所有的中间计算结果
        List<EnergyStatisticsVo> lastEnergyVos = energyStatisticsMapper.selectVoList(queryWrapper(bo));

        //获取尖峰平谷对应的所有时间段
        String sharp = configService.selectConfigByKey("sys.sharp");
        BigDecimal currentSharp = getPeriodStatisticAll(sharp, energyVos);
        BigDecimal lastSharp = getPeriodStatisticAll(sharp, lastEnergyVos);
        ReportTableVo sharpReportTable = new ReportTableVo();
        sharpReportTable.setType("尖");
        sharpReportTable.setData(currentSharp);
        sharpReportTable.setChain(BigDecimal.ZERO);
        if (!(lastSharp.equals(BigDecimal.ZERO))) {
            sharpReportTable.setChain(currentSharp.subtract(lastSharp)
                .multiply(BigDecimal.valueOf(100)).divide(lastSharp, 2, RoundingMode.HALF_UP));
        }
        result.add(sharpReportTable);

        String peek = configService.selectConfigByKey("sys.peek");
        BigDecimal currentPeek = getPeriodStatisticAll(peek, energyVos);
        BigDecimal lastPeek = getPeriodStatisticAll(peek, lastEnergyVos);
        ReportTableVo peekReportTable = new ReportTableVo();
        peekReportTable.setType("峰");
        peekReportTable.setData(currentPeek);
        peekReportTable.setChain(BigDecimal.ZERO);
        if (!(lastPeek.equals(BigDecimal.ZERO))) {
            peekReportTable.setChain(currentPeek.subtract(lastPeek)
                .multiply(BigDecimal.valueOf(100)).divide(lastPeek, 2, RoundingMode.HALF_UP));
        }
        result.add(peekReportTable);

        String ordinary = configService.selectConfigByKey("sys.ordinary");
        BigDecimal currentOrdinary = getPeriodStatisticAll(ordinary, energyVos);
        BigDecimal lastOrdinary = getPeriodStatisticAll(ordinary, lastEnergyVos);
        ReportTableVo ordinaryReportTable = new ReportTableVo();
        ordinaryReportTable.setType("平");
        ordinaryReportTable.setData(currentOrdinary);
        ordinaryReportTable.setChain(BigDecimal.ZERO);
        if (!(lastOrdinary.equals(BigDecimal.ZERO))) {
            ordinaryReportTable.setChain(currentOrdinary.subtract(lastOrdinary)
                .multiply(BigDecimal.valueOf(100)).divide(lastOrdinary, 2, RoundingMode.HALF_UP));
        }
        result.add(ordinaryReportTable);

        String valley = configService.selectConfigByKey("sys.valley");
        BigDecimal currentValley = getPeriodStatisticAll(valley, energyVos);
        BigDecimal lastValley = getPeriodStatisticAll(valley, lastEnergyVos);
        ReportTableVo valleyReportTable = new ReportTableVo();
        valleyReportTable.setType("谷");
        valleyReportTable.setData(currentValley);
        valleyReportTable.setChain(BigDecimal.ZERO);
        if (!(lastValley.equals(BigDecimal.ZERO))) {
            valleyReportTable.setChain(currentValley.subtract(lastValley)
                .multiply(BigDecimal.valueOf(100)).divide(lastValley, 2, RoundingMode.HALF_UP));
        }
        result.add(valleyReportTable);

        return result;
    }

    /**
     * 计算在开启尖峰平谷开关下各个时段的电能消耗量
     *
     * @param value     时间段
     * @param energyVos 需要筛选的片段
     * @return
     */
    private BigDecimal getPeriodStatisticAll(String value, List<EnergyStatisticsVo> energyVos) {
        List<EnergyStatisticsVo> result = new ArrayList<>();
        List<String> values = StringUtils.splitList(value, ",");
        for (String temp : values) {
            List<String> dates = StringUtils.splitList(temp, "~");
            List<EnergyStatisticsVo> voList = energyVos.stream().filter(e -> DateUtils.dateTime(DateUtils.HH_MM_SS, DateUtils.parseDateToStr(DateUtils.HH_MM_SS, e.getCreateTime()))
                .after(DateUtils.dateTime(DateUtils.HH_MM_SS, dates.get(0))) && DateUtils.dateTime(DateUtils.HH_MM_SS, DateUtils.parseDateToStr(DateUtils.HH_MM_SS, e.getCreateTime()))
                .before(DateUtils.dateTime(DateUtils.HH_MM_SS, dates.get(1)))).collect(Collectors.toList());
            result.addAll(voList);
        }
        if (ObjectUtil.isNotEmpty(result)) {
            return result.stream().map(EnergyStatisticsVo::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    /**
     * 费用报表-能耗查询
     */
    @Override
    public List<ExpenseReportVo> getConsumptionExpenseReport(Collection<Long> areaIds, String dateType, String startTime, String endTime, String energyType) {
        List<ExpenseReportVo> result = new ArrayList<>();
        List<String> deviceId = new ArrayList<>();

        // 通过区域id查询设备编号
        List<ItemTopology> itemTopologys = itemTopologyMapper.selectBatchIds(areaIds);
        if (ObjectUtil.isEmpty(itemTopologys)) {
            return result;
        }

        //整理出所有相关设备
        for (ItemTopology itemTopology : itemTopologys) {
            if (ObjectUtil.isNotEmpty(itemTopology.getDeviceId())) {
                List<String> temp = Arrays.stream(StringUtils.split(itemTopology.getDeviceId(), ",")).collect(Collectors.toList());
                deviceId.addAll(temp);
            }
        }

        //去重
        deviceId = deviceId.stream().distinct().collect(Collectors.toList());
        EnergyStatisticsBo bo = new EnergyStatisticsBo();
        bo.setEnergyType(energyType);
        bo.setStartTime(startTime);
        bo.setEndTime(endTime);
        bo.setEquipmentSn(StringUtils.join(deviceId, ","));

        //获取所选时间段内所有的中间计算结果
        List<EnergyStatisticsVo> energyVos = energyStatisticsMapper.selectVoList(queryWrapper(bo));

        //获取最新的计费规则
        ChargingVo chargingVo = chargingMapper.selectVoOne(new LambdaQueryWrapper<Charging>()
            .eq(Charging::getStatus, "0")
            .eq(Charging::getType, energyType)
            .le(Charging::getStartDate, DateUtils.getNowDate())
            .ge(Charging::getEndDate, DateUtils.getNowDate())
            .last("limit 1")
        );

        //获取尖峰平谷对应的所有时间段
        String sharp = configService.selectConfigByKey("sys.sharp");
        String peek = configService.selectConfigByKey("sys.peek");
        String ordinary = configService.selectConfigByKey("sys.ordinary");
        String valley = configService.selectConfigByKey("sys.valley");

        //获取阶梯计费开启时所有的阶梯计费规则
        List<ChargingStep> chargingSteps = chargingStepMapper.selectList(new LambdaQueryWrapper<ChargingStep>()
            .eq(ChargingStep::getChargingId, chargingVo.getChargingId())
            .orderByAsc(ChargingStep::getStartStep));

        if (dateType.equals("date")) {
            for (ItemTopology itemTopology : itemTopologys) {
                ExpenseReportVo reportVo = new ExpenseReportVo();
                List<ExpenseReportVo> perList = new ArrayList<>();
                Date start = DateUtils.dateTime(DateUtils.HH_MM_SS, "00:00:00");
                for (int i = 0; i < 24; i++) {
                    Date startTemp = DateUtils.addHours(start, i);
                    Date endTemp = DateUtils.addHours(start, i + 1);
                    ExpenseReportVo reportTemp = new ExpenseReportVo();
                    List<EnergyStatisticsVo> voList = energyVos.stream().filter(e -> itemTopology.getDeviceId().contains(e.getEquipmentSn())
                        && DateUtils.dateTime(DateUtils.HH_MM_SS, DateUtils.parseDateToStr(DateUtils.HH_MM_SS, e.getCreateTime()))
                        .after(startTemp) && DateUtils.dateTime(DateUtils.HH_MM_SS, DateUtils.parseDateToStr(DateUtils.HH_MM_SS, e.getCreateTime()))
                        .before(endTemp)).collect(Collectors.toList());
                    reportTemp.setDate(String.valueOf(i));
                    if (i < 10) {
                        reportTemp.setDate("0" + i);
                    }
                    reportTemp.setEnergy(voList.stream().map(EnergyStatisticsVo::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
                    reportTemp.setCost(getFinalCharging(chargingVo, chargingSteps, voList, sharp, peek, ordinary, valley));
                    perList.add(reportTemp);
                }
                reportVo.setNode(itemTopology.getItemName());
                reportVo.setList(perList);
                reportVo.setTotalCost(perList.stream().map(ExpenseReportVo::getCost).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
                reportVo.setTotalEnergy(perList.stream().map(ExpenseReportVo::getEnergy).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
                result.add(reportVo);
            }
        }
        if (dateType.equals("week")) {
            for (ItemTopology itemTopology : itemTopologys) {
                ExpenseReportVo reportVo = new ExpenseReportVo();
                List<ExpenseReportVo> perList = new ArrayList<>();
                Date start = DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS, startTime);
                for (int i = 0; i < 7; i++) {
                    Date startTemp = DateUtils.addDays(start, i);
                    Date endTemp = DateUtils.addDays(start, i + 1);
                    ExpenseReportVo reportTemp = new ExpenseReportVo();
                    List<EnergyStatisticsVo> voList = energyVos.stream().filter(e -> itemTopology.getDeviceId().contains(e.getEquipmentSn())
                        &&  e.getCreateTime().after(startTemp) &&e.getCreateTime().before(endTemp)).collect(Collectors.toList());

                    reportTemp.setDate(DateUtils.parseDateToStr("MM-dd",startTemp));
                    reportTemp.setEnergy(voList.stream().map(EnergyStatisticsVo::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
                    reportTemp.setCost(getFinalCharging(chargingVo, chargingSteps, voList, sharp, peek, ordinary, valley));
                    perList.add(reportTemp);
                }
                reportVo.setNode(itemTopology.getItemName());
                reportVo.setList(perList);
                reportVo.setTotalCost(perList.stream().map(ExpenseReportVo::getCost).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
                reportVo.setTotalEnergy(perList.stream().map(ExpenseReportVo::getEnergy).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
                result.add(reportVo);
            }
        }

        if (dateType.equals("month") ||dateType.equals("daterange")) {
            int day = DateUtils.differentDaysByMillisecond(DateUtils.parseDate(startTime), DateUtils.parseDate(endTime));
            for (ItemTopology itemTopology : itemTopologys) {
                List<ExpenseReportVo> perList = new ArrayList<>();
                ExpenseReportVo reportVo = new ExpenseReportVo();
                Date start = DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS, startTime);
                for (int i = 0; i < day+1; i++) {
                    Date startTemp = DateUtils.addDays(start, i);
                    Date endTemp = DateUtils.addDays(start, i + 1);
                    ExpenseReportVo reportTemp = new ExpenseReportVo();
                    List<EnergyStatisticsVo> voList = energyVos.stream().filter(e -> itemTopology.getDeviceId().contains(e.getEquipmentSn())
                        &&  e.getCreateTime().after(startTemp) &&e.getCreateTime().before(endTemp)).collect(Collectors.toList());
                    reportTemp.setDate(DateUtils.parseDateToStr("MM-dd",startTemp));
                    reportTemp.setEnergy(voList.stream().map(EnergyStatisticsVo::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
                    reportTemp.setCost(getFinalCharging(chargingVo, chargingSteps, voList, sharp, peek, ordinary, valley));
                    perList.add(reportTemp);
                }
                reportVo.setNode(itemTopology.getItemName());
                reportVo.setList(perList);
                reportVo.setTotalCost(perList.stream().map(ExpenseReportVo::getCost).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
                reportVo.setTotalEnergy(perList.stream().map(ExpenseReportVo::getEnergy).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
                result.add(reportVo);
            }
        }

        if (dateType.equals("year")) {
            for (ItemTopology itemTopology : itemTopologys) {
                List<ExpenseReportVo> perList = new ArrayList<>();
                ExpenseReportVo reportVo = new ExpenseReportVo();
                Date start = DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS, startTime);
                for (int i = 0; i < 12; i++) {
                    Date startTemp = DateUtils.addMonths(start, i);
                    Date endTemp = DateUtils.addMonths(start, i + 1);
                    ExpenseReportVo reportTemp = new ExpenseReportVo();
                    List<EnergyStatisticsVo> voList = energyVos.stream().filter(e -> itemTopology.getDeviceId().contains(e.getEquipmentSn())
                        &&  e.getCreateTime().after(startTemp) &&e.getCreateTime().before(endTemp)).collect(Collectors.toList());
                    reportTemp.setDate(DateUtils.parseDateToStr("MM",startTemp));
                    reportTemp.setEnergy(voList.stream().map(EnergyStatisticsVo::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
                    reportTemp.setCost(getFinalCharging(chargingVo, chargingSteps, voList, sharp, peek, ordinary, valley));
                    perList.add(reportTemp);
                }
                reportVo.setNode(itemTopology.getItemName());
                reportVo.setList(perList);
                reportVo.setTotalCost(perList.stream().map(ExpenseReportVo::getCost).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
                reportVo.setTotalEnergy(perList.stream().map(ExpenseReportVo::getEnergy).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
                result.add(reportVo);
            }
        }
        return result;
    }

    private BigDecimal getFinalCharging(ChargingVo chargingVo, List<ChargingStep> chargingSteps, List<EnergyStatisticsVo> voList, String sharp, String peek, String ordinary, String valley) {
        //尖峰平谷关闭、阶梯计费关闭时
        if (chargingVo.getPeriodSwitch().equals("1") && chargingVo.getStepSwitch().equals("1")) {
            if (ObjectUtil.isNotEmpty(voList)) {
                return voList.stream().map(EnergyStatisticsVo::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add).multiply(chargingVo.getUnitPrice()).setScale(2, RoundingMode.HALF_UP);
            } else {
                return BigDecimal.ZERO;
            }
        }

        //尖峰平谷开启、阶梯计费关闭时
        if (chargingVo.getPeriodSwitch().equals("0") && chargingVo.getStepSwitch().equals("1")) {
            if (ObjectUtil.isNotEmpty(voList)) {
                BigDecimal sharpValue = getPeriodStatisticAll(sharp, voList);
                BigDecimal peekValue = getPeriodStatisticAll(peek, voList);
                BigDecimal ordinaryValue = getPeriodStatisticAll(ordinary, voList);
                BigDecimal valleyValue = getPeriodStatisticAll(valley, voList);
                return (sharpValue.multiply(chargingVo.getSharpPrice()))
                    .add(peekValue.multiply(chargingVo.getPeekPrice()))
                    .add(ordinaryValue.multiply(chargingVo.getOrdinaryPrice()))
                    .add(valleyValue.multiply(chargingVo.getValleyPrice())).setScale(2, RoundingMode.HALF_UP);

            } else {
                return BigDecimal.ZERO;
            }
        }

        //尖峰平谷关闭、阶梯计费开启时
        if (chargingVo.getPeriodSwitch().equals("1") && chargingVo.getStepSwitch().equals("0")) {
            if (ObjectUtil.isNotEmpty(voList)) {
                BigDecimal total = voList.stream().map(EnergyStatisticsVo::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add).multiply(chargingVo.getUnitPrice()).setScale(2, RoundingMode.HALF_UP);
                return getStepCharging(total, chargingSteps, chargingVo.getUnitPrice());
            } else {
                return BigDecimal.ZERO;
            }
        }

        //尖峰平谷开启、阶梯计费开启时
        if (chargingVo.getPeriodSwitch().equals("0") && chargingVo.getStepSwitch().equals("0")) {
            if (ObjectUtil.isNotEmpty(voList)) {
                BigDecimal sharpValue = getPeriodStatisticAll(sharp, voList);
                BigDecimal peekValue = getPeriodStatisticAll(peek, voList);
                BigDecimal ordinaryValue = getPeriodStatisticAll(ordinary, voList);
                BigDecimal valleyValue = getPeriodStatisticAll(valley, voList);
                return getStepCharging(sharpValue, chargingSteps, chargingVo.getSharpPrice())
                    .add(getStepCharging(peekValue, chargingSteps, chargingVo.getPeekPrice()))
                    .add(getStepCharging(ordinaryValue, chargingSteps, chargingVo.getOrdinaryPrice()))
                    .add(getStepCharging(valleyValue, chargingSteps, chargingVo.getValleyPrice())).setScale(2, RoundingMode.HALF_UP);

            } else {
                return BigDecimal.ZERO;
            }
        }
        return BigDecimal.ZERO;
    }

    /**
     * 首页-用能统计
     */
    @Override
    public Map<String, Object> getConsumptionStatistics(Long areaId){
        Map<String, Object> result = new HashMap<>(12);

        //今日
        Date nowEndTime = DateUtils.parseDate(DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS));
        Date nowStartTime = DateUtils.parseDate(DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD));

        // 通过区域id查询设备编号
        //找出当前楼层房间绑定的设备
        ItemTopology itemTopology = itemTopologyMapper.selectById(areaId);

        if (ObjectUtil.isEmpty(itemTopology.getDeviceId())) {
            return result;
        }

        EnergyStatisticsBo bo = new EnergyStatisticsBo();
        bo.setEquipmentSn(itemTopology.getDeviceId());
        bo.setStartTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, nowStartTime));
        bo.setEndTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, nowEndTime));

        // 今日水电用能
        List<EnergyStatisticsVo> nowElectricity = energyStatisticsMapper.selectVoList(queryWrapper(bo));
        List<EnergyStatisticsVo> electricity = nowElectricity.stream().filter(e->e.getEnergyType().equals("0")).collect(Collectors.toList());
        List<EnergyStatisticsVo> water = nowElectricity.stream().filter(e->e.getEnergyType().equals("1")).collect(Collectors.toList());
        if (ObjectUtil.isNotEmpty(nowElectricity)) {
            BigDecimal electricityTotal = electricity.stream().map(EnergyStatisticsVo::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
            BigDecimal waterTotal = water.stream().map(EnergyStatisticsVo::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
            result.put("electricity",electricityTotal);
            result.put("kgce",(electricityTotal.divide(BigDecimal.valueOf(8.167),4,RoundingMode.HALF_UP).add(waterTotal.multiply(BigDecimal.valueOf(0.4857)))).setScale(2,RoundingMode.HALF_UP));
            result.put("kg",((electricityTotal.multiply(BigDecimal.valueOf(0.785))).add(waterTotal.multiply(BigDecimal.valueOf(0.91)))).setScale(2,RoundingMode.HALF_UP));

        } else {
            result.put("kgce","--");
            result.put("kg", "--");
            result.put("electricity","--");
        }

        for (String key :result.keySet()){
            if (result.get(key).toString().equals("0.00")){
                result.put(key,"--");
            }
        }
        return result;
    }

    /**
     * 首页-今日能源趋势、碳排放量
     */
    @Override
    public Map<String, Object> getTrendAndCarbon(Long areaId){
        Map<String,Object> result = new HashMap<>();
        Date nowDate = DateUtils.getNowDate();
        Date startTime = DateUtils.dateTime(DateUtils.YYYY_MM_DD, DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,nowDate));
        List<Object> trendResult = new ArrayList<>();
        List<Object> carbonResult = new ArrayList<>();
        // 通过区域id查询设备编号
        ItemTopology itemTopology = itemTopologyMapper.selectById(areaId);
        if (ObjectUtil.isEmpty(itemTopology.getDeviceId())) {
            return result;
        }
        List<String> deviceId = Arrays.stream(StringUtils.split(itemTopology.getDeviceId(), ",")).collect(Collectors.toList());

        List<EnergyStatistics> energyStatisticsList = energyStatisticsMapper.selectList(new LambdaQueryWrapper<EnergyStatistics>()
            .in(EnergyStatistics::getEquipmentSn, deviceId)
            .ge(EnergyStatistics::getUpdateTime, startTime));

        for (int i = 0; i < 24; i++) {
            if (startTime.after(nowDate)) {
                break;
            }

            Date endHour = DateUtils.addHours(startTime, 1);
            Date finalStartTime = startTime;
            List<EnergyStatistics> list = energyStatisticsList.stream()
                .filter(e -> e.getUpdateTime().before(endHour) && e.getUpdateTime().after(finalStartTime)).collect(Collectors.toList());

            if (ObjectUtil.isNotEmpty(list)) {
                BigDecimal electricityTotal = list.stream().filter(e->e.getEnergyType().equals("0")).map(EnergyStatistics::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
                BigDecimal waterTotal = list.stream().filter(e->e.getEnergyType().equals("1")).map(EnergyStatistics::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
                trendResult.add((electricityTotal.divide(BigDecimal.valueOf(8.167),4,RoundingMode.HALF_UP).add(waterTotal.multiply(BigDecimal.valueOf(0.4857)))).setScale(2,RoundingMode.HALF_UP));
                carbonResult.add(((electricityTotal.multiply(BigDecimal.valueOf(0.785))).add(waterTotal.multiply(BigDecimal.valueOf(0.91)))).setScale(2,RoundingMode.HALF_UP));
            } else {
                trendResult.add("--");
                carbonResult.add("--");
            }

            startTime = endHour;
        }
        for (int i=0;i<trendResult.size();i++){
            if (trendResult.get(i).toString().equals("0.00")){
                trendResult.set(i,"--");
            }
            if (carbonResult.get(i).toString().equals("0.00")){
                carbonResult.set(i,"--");
            }
        }
        result.put("trendResult",trendResult);
        result.put("carbonResult",carbonResult);
        return result;
    }
}
