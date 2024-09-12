package com.cpems.system.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cpems.common.utils.DateUtils;
import com.cpems.common.utils.StringUtils;
import com.cpems.system.domain.EnergyStatistics;
import com.cpems.system.domain.ItemTopology;
import com.cpems.system.domain.vo.EnergyStatisticsVo;
import com.cpems.system.domain.vo.ItemTopologyVo;
import com.cpems.system.domain.vo.ItemizeVo;
import com.cpems.system.mapper.EnergyStatisticsMapper;
import com.cpems.system.mapper.ItemTopologyMapper;
import com.cpems.system.service.IItemizeAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 分项分析Service业务层处理
 *
 * @Author cpems
 * @Date 2023/9/5 11:32
 */
@RequiredArgsConstructor
@Service
public class ItemizeAnalysisServiceImpl implements IItemizeAnalysisService {

    private final ItemTopologyMapper itemTopologyMapper;
    private final EnergyStatisticsMapper energyStatisticsMapper;

    /**
     * 分项概况
     */
    public List<ItemizeVo> getOverview(String itemId, String dateType, String date) {
        List<ItemizeVo> result = new ArrayList<>();

        //查询设备SN
        List<ItemTopologyVo> topologyVoList = itemTopologyMapper.selectVoList(new LambdaQueryWrapper<ItemTopology>()
            .eq(ItemTopology::getParentId, Long.parseLong(itemId)));
        if (ObjectUtil.isEmpty(topologyVoList)) {
            return result;
        }
        List<String> deviceIds = topologyVoList.stream().map(ItemTopologyVo::getDeviceId).collect(Collectors.toList());
        List<String> deviceList = new ArrayList<>();
        for (String device : deviceIds) {
            deviceList.addAll(Arrays.asList(device.split(",")));
        }
        //去重
        deviceList = deviceList.stream().distinct().collect(Collectors.toList());

        //当日，昨日同期
        Date nowEndTime = DateUtils.parseDate(date);
        Date nowStartTime = DateUtils.dateTime(DateUtils.YYYY_MM_DD, date);
        Date nowDate = DateUtils.parseDate(DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD));
        if (DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, nowStartTime).equals(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, nowDate))) {
            nowEndTime = DateUtils.parseDate(DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS));
        } else {
            nowEndTime = DateUtils.parseDate(DateUtils.parseDateToStr("yyyy-MM-dd 23:59:59", nowEndTime));
        }
        Date lastStartTime = DateUtils.addDays(nowStartTime, -1);
        Date lastEndTime = DateUtils.addDays(nowEndTime, -1);

        //当月，上月同期
        Date nowMonthStart = DateUtils.dateTime(DateUtils.YYYY_MM, date);
        // 当月天数
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowMonthStart);
        int day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        Date monthEndTime = DateUtils.parseDate(DateUtils.parseDateToStr("yyyy-MM-" + day + " 23:59:59", nowEndTime));
        Date lastMonthStart = DateUtils.addMonths(nowMonthStart, -1);
        Date lastMonthEnd = DateUtils.addMonths(nowEndTime, -1);

        //当年，上年同期
        Date nowYearStart = DateUtils.dateTime(DateUtils.YYYY, date);
        Date yearEndTime = DateUtils.parseDate(DateUtils.parseDateToStr("yyyy-12-31 23:59:59", nowEndTime));
        Date lastYearStart = DateUtils.addYears(nowYearStart, -1);
        Date lastYearEnd = DateUtils.addYears(nowEndTime, -1);

        // 当日用能
        String startTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, nowStartTime);
        String endTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, nowEndTime);
        List<EnergyStatisticsVo> nowElectricity = energyStatisticsMapper.selectVoList(buildStatisticQuery(deviceList, startTime, endTime));

        // 昨日同期用能
        startTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, lastStartTime);
        endTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, lastEndTime);
        List<EnergyStatisticsVo> lastElectricity = energyStatisticsMapper.selectVoList(buildStatisticQuery(deviceList, startTime, endTime));

        // 当月用能
        startTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, nowMonthStart);
        endTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, monthEndTime);
        List<EnergyStatisticsVo> nowMonthEle = energyStatisticsMapper.selectVoList(buildStatisticQuery(deviceList, startTime, endTime));

        // 上月同期用能
        startTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, lastMonthStart);
        endTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, lastMonthEnd);
        List<EnergyStatisticsVo> lastMonthEle = energyStatisticsMapper.selectVoList(buildStatisticQuery(deviceList, startTime, endTime));

        // 今年用能
        startTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, nowYearStart);
        endTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, yearEndTime);
        List<EnergyStatisticsVo> nowYearEle = energyStatisticsMapper.selectVoList(buildStatisticQuery(deviceList, startTime, endTime));

        // 去年同期用能
        startTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, lastYearStart);
        endTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, lastYearEnd);
        List<EnergyStatisticsVo> lastYearEle = energyStatisticsMapper.selectVoList(buildStatisticQuery(deviceList, startTime, endTime));

        //计算分项能耗
         for (ItemTopologyVo itemTopologyVo : topologyVoList) {
            ItemizeVo itemizeVo = new ItemizeVo();
            result.add(itemizeVo);
            Map<String, Object> count = new LinkedHashMap<>();
            itemizeVo.setItemizeCount(count);
            itemizeVo.setItemizeId(itemTopologyVo.getItemId().toString());
            itemizeVo.setItemizeName(itemTopologyVo.getItemName());

            //分项查询设备SN
            List<String> deviceId = Arrays.asList(itemTopologyVo.getDeviceId().split(","));
            // 当日能耗
            List<EnergyStatisticsVo> nowData = nowElectricity.stream().filter(t -> deviceId.contains(t.getEquipmentSn())).collect(Collectors.toList());
            BigDecimal now = nowData.stream().map(EnergyStatisticsVo::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
            itemizeVo.setNowEnergy("--");
            if (ObjectUtil.isNotEmpty(nowData)) {
                itemizeVo.setNowEnergy(now);
            }
            // 昨日同期能耗
            List<EnergyStatisticsVo> lastData = lastElectricity.stream().filter(t -> deviceId.contains(t.getEquipmentSn())).collect(Collectors.toList());
            BigDecimal last = lastData.stream().map(EnergyStatisticsVo::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
            itemizeVo.setLastEnergy("--");
            if (ObjectUtil.isNotEmpty(lastData)) {
                itemizeVo.setLastEnergy(last);
            }
            //日趋势
             itemizeVo.setNowTrend("--");
            if (ObjectUtil.isNotEmpty(nowData) && ObjectUtil.isNotEmpty(lastData)) {
                BigDecimal nowTrend = now.subtract(last).setScale(2, RoundingMode.HALF_UP);
                itemizeVo.setNowTrend(nowTrend);
            }
            //日趋势百分比
            if (!last.equals(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP))) {
                itemizeVo.setNowPer(BigDecimal.valueOf(Float.parseFloat(itemizeVo.getNowTrend().toString())).divide(last, 4, RoundingMode.HALF_UP));
            } /*else {
                itemizeVo.setNowPer(BigDecimal.ZERO);
            }*/
            // 当月能耗
            List<EnergyStatisticsVo> nowMonthData = nowMonthEle.stream().filter(t -> deviceId.contains(t.getEquipmentSn())).collect(Collectors.toList());
            BigDecimal nowMonth = nowMonthData.stream().map(EnergyStatisticsVo::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
            itemizeVo.setNowMonthEnergy("--");
            if (ObjectUtil.isNotEmpty(nowMonthData)) {
                itemizeVo.setNowMonthEnergy(nowMonth);
            }
            // 上月同期能耗
            List<EnergyStatisticsVo> lastMonthData = lastMonthEle.stream().filter(t -> deviceId.contains(t.getEquipmentSn())).collect(Collectors.toList());
            BigDecimal lastMonth = lastMonthData.stream().map(EnergyStatisticsVo::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
            itemizeVo.setLastMonthEnergy("--");
            if (ObjectUtil.isNotEmpty(lastMonthData)) {
                itemizeVo.setLastMonthEnergy(lastMonth);
            }
            //月趋势
             itemizeVo.setMonthTrend("--");
            if (ObjectUtil.isNotEmpty(nowMonthData) && ObjectUtil.isNotEmpty(lastMonthData)) {
                BigDecimal monthTrend = nowMonth.subtract(lastMonth).setScale(2, RoundingMode.HALF_UP);
                itemizeVo.setMonthTrend(monthTrend);
            }
            //月趋势百分比
            if (!lastMonth.equals(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)) && !(itemizeVo.getMonthTrend().toString().equals("--"))) {
                itemizeVo.setMonthPer(BigDecimal.valueOf(Float.parseFloat(itemizeVo.getMonthTrend().toString())).divide(lastMonth, 4, RoundingMode.HALF_UP));
            }/* else {
                itemizeVo.setMonthPer(BigDecimal.ZERO);
            }*/
            // 当年能耗
            List<EnergyStatisticsVo> nowYearData = nowYearEle.stream().filter(t -> deviceId.contains(t.getEquipmentSn())).collect(Collectors.toList());
            BigDecimal nowYear = nowYearData.stream().map(EnergyStatisticsVo::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
            itemizeVo.setNowYearEnergy("--");
            if (ObjectUtil.isNotEmpty(nowYearData)) {
                itemizeVo.setNowYearEnergy(nowYear);
            }
            // 上年同期能耗
            List<EnergyStatisticsVo> lastYearData = lastYearEle.stream().filter(t -> deviceId.contains(t.getEquipmentSn())).collect(Collectors.toList());
            BigDecimal lastYear = lastYearData.stream().map(EnergyStatisticsVo::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
            itemizeVo.setLastYearEnergy("--");
            if (ObjectUtil.isNotEmpty(lastYearData)) {
                itemizeVo.setLastYearEnergy(nowYear);
            }
            //年趋势
             itemizeVo.setYearTrend("--");
            if (ObjectUtil.isNotEmpty(nowYearData) && ObjectUtil.isNotEmpty(lastYearData)) {
                BigDecimal yearTrend = nowYear.subtract(lastYear).setScale(2, RoundingMode.HALF_UP);
                itemizeVo.setYearTrend(yearTrend);
            }
            //年趋势百分比
            if (!lastYear.equals(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP))) {
                itemizeVo.setYearPer(BigDecimal.valueOf(Float.parseFloat(itemizeVo.getYearTrend().toString())).divide(lastYear, 4, RoundingMode.HALF_UP));
            } /*else {
                itemizeVo.setYearPer(BigDecimal.ZERO);
            }*/

            if (dateType.equals("date")) {
                Date beginTime = nowStartTime;
                for (int i = 0; i < 24; i++) {
                    if (beginTime.after(nowEndTime)) {
                        break;
                    }

                    Date begin = beginTime;
                    Date finish = DateUtils.addHours(beginTime, 1);
                    List<EnergyStatisticsVo> list = nowData.stream()
                        .filter(e -> e.getUpdateTime().before(finish) && e.getUpdateTime().after(begin)).collect(Collectors.toList());
                    if (ObjectUtil.isNotEmpty(list)) {
                        count.put(String.valueOf(i), list.stream().map(EnergyStatisticsVo::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add)
                            .setScale(2, RoundingMode.HALF_UP));
                    } else {
                        count.put(String.valueOf(i), "--");
                    }
                    beginTime = finish;
                }
            } else if (dateType.equals("month")) {
                Date beginTime = nowMonthStart;

                for (int i = 0; i < day; i++) {
                    if (beginTime.after(DateTime.now())) {
                        break;
                    }

                    Date begin = beginTime;
                    Date finish = DateUtils.addDays(beginTime, 1);
                    List<EnergyStatisticsVo> list = nowMonthData.stream()
                        .filter(e -> e.getUpdateTime().before(finish) && e.getUpdateTime().after(begin)).collect(Collectors.toList());
                    if (ObjectUtil.isNotEmpty(list)) {
                        count.put(String.valueOf(i + 1), list.stream().map(EnergyStatisticsVo::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add)
                            .setScale(2, RoundingMode.HALF_UP));
                    } else {
                        count.put(String.valueOf(i + 1), "--");
                    }
                    beginTime = finish;
                }
            } else if (dateType.equals("year")) {
                Date beginTime = nowYearStart;

                for (int i = 0; i < 12; i++) {
                    if (beginTime.after(DateTime.now())) {
                        break;
                    }

                    Date begin = beginTime;
                    Date finish = DateUtils.addMonths(beginTime, 1);
                    List<EnergyStatisticsVo> list = nowYearData.stream()
                        .filter(e -> e.getUpdateTime().before(finish) && e.getUpdateTime().after(begin)).collect(Collectors.toList());
                    if (ObjectUtil.isNotEmpty(list)) {
                        count.put(String.valueOf(i + 1), list.stream().map(EnergyStatisticsVo::getStatistics).reduce(BigDecimal.ZERO.stripTrailingZeros(), BigDecimal::add)
                            .setScale(2, RoundingMode.HALF_UP));
                    } else {
                        count.put(String.valueOf(i + 1), "--");
                    }
                    beginTime = finish;
                }
            }
            BigDecimal total = BigDecimal.ZERO;
            for (Map.Entry<String, Object> entry : count.entrySet()) {
                if (!entry.getValue().equals("--")) {
                    total = total.add(BigDecimal.valueOf(Float.parseFloat(entry.getValue().toString())));
                }
            }
            itemizeVo.setItemizeTotal(total.setScale(2, RoundingMode.HALF_UP));
        }
        return result;
    }

    private LambdaQueryWrapper<EnergyStatistics> buildStatisticQuery(List<String> deviceId, String startTime, String endTime) {
        return new LambdaQueryWrapper<EnergyStatistics>()
            .in(EnergyStatistics::getEquipmentSn, deviceId)
            .between(StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime), EnergyStatistics::getUpdateTime, startTime, endTime)
            .orderByDesc(EnergyStatistics::getUpdateTime);
    }

    /**
     * 获取指定日期所属周第一天和最后一天
     */
    public static String getFirstAndLastOfDate(String dataStr) throws ParseException {
        Calendar cal = Calendar.getInstance();

        cal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(dataStr));

        int d = 0;
        if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
            d = -6;
        } else {
            d = 2 - cal.get(Calendar.DAY_OF_WEEK);
        }
        cal.add(Calendar.DAY_OF_WEEK, d);
        // 所在周开始日期
        String data1 = new SimpleDateFormat("yyyy/MM/dd").format(cal.getTime());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        // 所在周结束日期
        String data2 = new SimpleDateFormat("yyyy/MM/dd").format(cal.getTime());
        return data1 + "-" + data2;

    }
}
