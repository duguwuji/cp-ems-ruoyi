package com.cpems.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cpems.common.exception.user.UserException;
import com.cpems.common.utils.DateUtils;
import com.cpems.common.utils.StringUtils;
import com.cpems.system.domain.EnergyStatistics;
import com.cpems.system.domain.ItemTopology;
import com.cpems.system.domain.bo.ItemTopologyBo;
import com.cpems.system.domain.vo.*;
import com.cpems.system.mapper.EnergyStatisticsMapper;
import com.cpems.system.mapper.ItemTopologyMapper;
import com.cpems.system.service.IEnergyService;
import com.cpems.system.service.IItemTopologyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.cpems.system.domain.bo.QuotaConfigBo;
import com.cpems.system.domain.QuotaConfig;
import com.cpems.system.mapper.QuotaConfigMapper;
import com.cpems.system.service.IQuotaConfigService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 定额配置Service业务层处理
 *
 * @author cpems
 * @date 2023-09-12
 */
@RequiredArgsConstructor
@Service
public class QuotaConfigServiceImpl implements IQuotaConfigService {

    private final QuotaConfigMapper baseMapper;
    private final IItemTopologyService itemTopologyService;
    private final IEnergyService iEnergyService;
    private final ItemTopologyMapper itemTopologyMapper;
    private final EnergyStatisticsMapper energyStatisticsMapper;

    /**
     * 查询定额配置
     */
    @Override
    public QuotaConfigVo queryById(Long quotaId){
        return baseMapper.selectVoById(quotaId);
    }

    /**
     * 查询定额配置列表
     */
    @Override
    public TableDataInfo<QuotaConfigVo> queryPageList(QuotaConfigBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<QuotaConfig> lqw = buildQueryWrapper(bo);
        Page<QuotaConfigVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        //获取括朴结构
        List<ItemTopologyVo> topologyVoList = itemTopologyService.queryList(new ItemTopologyBo());
        for (QuotaConfigVo vo : result.getRecords()) {
            if(ObjectUtil.isEmpty(vo.getItemId())) {
                continue;
            }
            //设定区域名称
            List<ItemTopologyVo> itemTopologyVo = topologyVoList.stream().filter(i -> i.getItemId().toString().equals(vo.getItemId().toString())).collect(Collectors.toList());
            if(ObjectUtil.isNotEmpty(itemTopologyVo)) {
                vo.setItemName(itemTopologyVo.get(0).getItemName());
            }
            //查询实际累计能耗
            BigDecimal total = iEnergyService.getAccumulate(vo.getItemId(), vo.getQuotaType(), vo.getQuotaTime());
            vo.setRealEnergy(total);
            vo.setCriticalVo(vo.getCritical().multiply(BigDecimal.valueOf(100))+"%");
            vo.setOverMedianVo(vo.getOverMedian().multiply(BigDecimal.valueOf(100))+"%");
        }

        return TableDataInfo.build(result);
    }

    /**
     * 查询定额配置列表
     */
    @Override
    public List<QuotaConfigVo> queryList(QuotaConfigBo bo) {
        LambdaQueryWrapper<QuotaConfig> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<QuotaConfig> buildQueryWrapper(QuotaConfigBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<QuotaConfig> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getQuotaType()), QuotaConfig::getQuotaType, bo.getQuotaType());
        lqw.eq(bo.getItemId() != null, QuotaConfig::getItemId, bo.getItemId());
        lqw.eq(bo.getQuotaTime() != null, QuotaConfig::getQuotaTime, bo.getQuotaTime());
        lqw.eq(bo.getQuotaValue() != null, QuotaConfig::getQuotaValue, bo.getQuotaValue());
        lqw.eq(bo.getRealEnergy() != null, QuotaConfig::getRealEnergy, bo.getRealEnergy());
        lqw.eq(bo.getCritical() != null, QuotaConfig::getCritical, bo.getCritical());
        lqw.eq(bo.getOverMedian() != null, QuotaConfig::getOverMedian, bo.getOverMedian());
        lqw.between(params.get("beginTime") != null && params.get("endTime") != null,
            QuotaConfig::getQuotaTime, params.get("beginTime"), params.get("endTime"));
        return lqw;
    }

    /**
     * 新增定额配置
     */
    @Override
    public Boolean insertByBo(QuotaConfigBo bo) {
        QuotaConfig quotaConfig = baseMapper.selectOne(new LambdaQueryWrapper<QuotaConfig>()
            .eq(QuotaConfig::getItemId,bo.getItemId())
            .eq(QuotaConfig::getQuotaType,bo.getQuotaType())
            .eq(QuotaConfig::getQuotaTime,bo.getQuotaTime()));
        if(ObjectUtil.isNotEmpty(quotaConfig)) {
            throw new UserException("quota.config.exist");
        }
        QuotaConfig add = BeanUtil.toBean(bo, QuotaConfig.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setQuotaId(add.getQuotaId());
        }
        return flag;
    }

    /**
     * 修改定额配置
     */
    @Override
    public Boolean updateByBo(QuotaConfigBo bo) {
        QuotaConfig update = BeanUtil.toBean(bo, QuotaConfig.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(QuotaConfig entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除定额配置
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    /**
     * 定额分析
     * @return
     */
    @Override
    public Map<String, Object> analysis(Long itemId, String quotaTime){
        Map<String,Object> result = new HashMap<>();
        List<QuotaAnalysisVo> quotaAnalysisVos = new ArrayList<>();
        result.put("quotaList",quotaAnalysisVos);
        //获取能耗定额
        QuotaConfig monthConfig = baseMapper.selectOne(new LambdaQueryWrapper<QuotaConfig>()
            .eq(QuotaConfig::getItemId,itemId)
            .eq(QuotaConfig::getQuotaType,"0")
            .eq(QuotaConfig::getQuotaTime,quotaTime));
        BigDecimal monthQuota = BigDecimal.ZERO;
        if(ObjectUtil.isNotEmpty(monthConfig)){
            monthQuota = monthConfig.getQuotaValue();
        }
        result.put("monthQuota",monthQuota.setScale(2, RoundingMode.HALF_UP));
        QuotaConfig yearConfig = baseMapper.selectOne(new LambdaQueryWrapper<QuotaConfig>()
            .eq(QuotaConfig::getItemId,itemId)
            .eq(QuotaConfig::getQuotaType,"1")
            .eq(QuotaConfig::getQuotaTime,DateUtils.dateTime(DateUtils.YYYY,quotaTime)));
        BigDecimal yearQuota = BigDecimal.ZERO;
        if(ObjectUtil.isNotEmpty(yearConfig)){
            yearQuota = yearConfig.getQuotaValue();
        }
        result.put("yearQuota",yearQuota.setScale(2, RoundingMode.HALF_UP));

        //获取实际能耗
        ItemTopology itemTopology = itemTopologyMapper.selectById(itemId);
        List<String> devices = Arrays.stream(StringUtils.split(itemTopology.getDeviceId(), ",")).collect(Collectors.toList());
        //找不到对应设备返回
        /*if (ObjectUtil.isEmpty(devices)) {
            return null;
        }*/
        //当月
        Date nowMonthStart = DateUtils.dateTime(DateUtils.YYYY_MM,quotaTime);
        // 当月天数
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowMonthStart);
        int day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.setTime(new Date());
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        if(DateUtils.parseDateToStr(DateUtils.YYYY_MM,nowMonthStart).equals(DateUtils.parseDateToStr(DateUtils.YYYY_MM,new Date()))){
            BigDecimal monthTimePer = BigDecimal.valueOf(date).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(day),2, RoundingMode.HALF_UP);
            result.put("monthTimePer",monthTimePer);
        }else {
            result.put("monthTimePer",100);
        }
        Date nowMonthEnd = DateUtils.parseDate(DateUtils.parseDateToStr("yyyy-MM-" + day + " 23:59:59",nowMonthStart));

        //去年同期(月)
        Date sameMonthStart = DateUtils.addYears(nowMonthStart,-1);
        Date sameMonthEnd = DateUtils.addMonths(sameMonthStart,1);

        //上月环比
        Date lastMonthStart = DateUtils.addMonths(nowMonthStart, -1);
        Date lastMonthEnd = nowMonthStart;

        //当年
        Date nowYearStart = DateUtils.dateTime(DateUtils.YYYY,quotaTime);
        Date nowYearEnd = DateUtils.parseDate(DateUtils.parseDateToStr("yyyy-12-31 23:59:59",nowMonthStart));
        if(DateUtils.parseDateToStr(DateUtils.YYYY,nowMonthStart).equals(DateUtils.parseDateToStr(DateUtils.YYYY,new Date()))){
            int difDay = DateUtils.differentDaysByMillisecond(nowYearStart,new Date());
            BigDecimal yearTimePer = BigDecimal.valueOf(difDay).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(365),2, RoundingMode.HALF_UP);
            result.put("yearTimePer",yearTimePer);
        }else {
            result.put("yearTimePer",100);
        }

        //当月能耗
        List<EnergyStatistics> nowMonthStatistics = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(devices)) {
            nowMonthStatistics = energyStatisticsMapper.selectList(buildQuery(devices,nowMonthStart,nowMonthEnd));
        }
//        List<EnergyStatistics> nowMonthStatistics = energyStatisticsMapper.selectList(buildQuery(devices,nowMonthStart,nowMonthEnd));
        BigDecimal monthTotal = nowMonthStatistics.stream().map(EnergyStatistics::getStatistics).reduce(BigDecimal.ZERO,BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
        result.put("monthTotal",monthTotal);
        //当月余量
        BigDecimal residue = monthQuota.subtract(monthTotal);
        result.put("residue",residue);
        if (!monthQuota.equals(BigDecimal.ZERO)) {
            result.put("residuePer",residue.multiply(BigDecimal.valueOf(100)).divide(monthQuota,2,RoundingMode.HALF_UP));
            result.put("monthPer",monthTotal.multiply(BigDecimal.valueOf(100)).divide(monthQuota,2,RoundingMode.HALF_UP));
        }else {
            result.put("residuePer","0.00");
            result.put("monthPer","0.00");
        }
        //同比(月度)
        List<EnergyStatistics> sameMonthStatistics = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(devices)) {
            sameMonthStatistics = energyStatisticsMapper.selectList(buildQuery(devices,sameMonthStart,sameMonthEnd));
        }
//        List<EnergyStatistics> sameMonthStatistics = energyStatisticsMapper.selectList(buildQuery(devices,sameMonthStart,sameMonthEnd));
        BigDecimal monthYoy = sameMonthStatistics.stream().map(EnergyStatistics::getStatistics).reduce(BigDecimal.ZERO,BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
        result.put("monthYoy",monthYoy);
        if(!monthYoy.equals(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP))){
            result.put("yoyPer",(monthTotal.subtract(monthYoy)).multiply(BigDecimal.valueOf(100)).divide(monthYoy,2,RoundingMode.HALF_UP));
        }else {
            result.put("yoyPer","0.00");
        }
        //环比(月度)
        List<EnergyStatistics> lastMonthStatistics = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(devices)) {
            lastMonthStatistics = energyStatisticsMapper.selectList(buildQuery(devices,lastMonthStart,lastMonthEnd));
        }
//        List<EnergyStatistics> lastMonthStatistics = energyStatisticsMapper.selectList(buildQuery(devices,lastMonthStart,lastMonthEnd));
        BigDecimal monthChain = lastMonthStatistics.stream().map(EnergyStatistics::getStatistics).reduce(BigDecimal.ZERO,BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
        result.put("monthChain",monthChain);
        if(!monthChain.equals(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP))){
            result.put("chainPer",(monthTotal.subtract(monthChain)).multiply(BigDecimal.valueOf(100)).divide(monthChain,2,RoundingMode.HALF_UP));
        }else {
            result.put("chainPer","0.00");
        }
        //当年能耗
        List<EnergyStatistics> nowYearStatistics = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(devices)) {
            nowYearStatistics = energyStatisticsMapper.selectList(buildQuery(devices,nowYearStart,nowYearEnd));
        }
//        List<EnergyStatistics> nowYearStatistics = energyStatisticsMapper.selectList(buildQuery(devices,nowYearStart,nowYearEnd));
        BigDecimal yearTotal = nowYearStatistics.stream().map(EnergyStatistics::getStatistics).reduce(BigDecimal.ZERO,BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
        result.put("yearTotal",yearTotal);
        if (!yearQuota.equals(BigDecimal.ZERO)) {
            result.put("yearPer",yearTotal.multiply(BigDecimal.valueOf(100)).divide(yearQuota,2,RoundingMode.HALF_UP));
        }else {
            result.put("yearPer","0.00");
        }
        //上月、当月能耗
        List<EnergyStatistics> dailyStatistics = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(devices)) {
            dailyStatistics = energyStatisticsMapper.selectList(buildQuery(devices,lastMonthStart,nowMonthEnd));
        }
//        List<EnergyStatistics> dailyStatistics = energyStatisticsMapper.selectList(buildQuery(devices,lastMonthStart,nowMonthEnd));
        for (int i = 0; i < day; i++) {
            QuotaAnalysisVo quotaAnalysisVo = new QuotaAnalysisVo();
            quotaAnalysisVo.setUnit("kW.h");

            Date begin = DateUtils.addDays(nowMonthStart, i);
            Date finish = DateUtils.addDays(nowMonthStart,i+1);
            //实际能耗
            List<EnergyStatistics> voList = dailyStatistics.stream()
                .filter(e -> e.getUpdateTime().before(finish) && e.getUpdateTime().after(begin)).collect(Collectors.toList());

            String key = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,begin).substring(0,10);
            quotaAnalysisVo.setDate(key);
            //日均定额
            BigDecimal average = monthQuota.divide(BigDecimal.valueOf(day),2,RoundingMode.HALF_UP);
            quotaAnalysisVo.setQuotaAverage(average);
            BigDecimal real = voList.stream().map(EnergyStatistics::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
            quotaAnalysisVo.setRealEnergy(real);
            if(!average.equals(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP))){
                quotaAnalysisVo.setQuotaPercent(real.multiply(BigDecimal.valueOf(100)).divide(average,2,RoundingMode.HALF_UP)+"%");
            }else {
                quotaAnalysisVo.setQuotaPercent("0.00%");
            }
            //能耗环比
            Date lastBegin = DateUtils.addDays(begin, -1);
            Date lastFinish = DateUtils.addDays(finish,-1);
            List<EnergyStatistics> lastList = dailyStatistics.stream()
                .filter(e -> e.getUpdateTime().before(lastFinish) && e.getUpdateTime().after(lastBegin)).collect(Collectors.toList());
            quotaAnalysisVo.setChainEnergy(lastList.stream().map(EnergyStatistics::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
            //能耗同比
            Date sameBegin = DateUtils.addMonths(begin, -1);
            Date sameFinish = DateUtils.addMonths(finish,-1);
            List<EnergyStatistics> sameList = dailyStatistics.stream()
                .filter(e -> e.getUpdateTime().before(sameFinish) && e.getUpdateTime().after(sameBegin)).collect(Collectors.toList());
            quotaAnalysisVo.setYoyEnergy(sameList.stream().map(EnergyStatistics::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
            quotaAnalysisVos.add(quotaAnalysisVo);
        }
        return result;
    }

    private LambdaQueryWrapper<EnergyStatistics> buildQuery(List<String> devices,Date startTime,Date endTime) {
        return new LambdaQueryWrapper<EnergyStatistics>()
            .in(EnergyStatistics::getEquipmentSn, devices)
            .between(ObjectUtil.isNotEmpty(startTime) && ObjectUtil.isNotEmpty(endTime),EnergyStatistics::getTime,startTime, endTime)
            .orderByDesc(EnergyStatistics::getUpdateTime);
    }

    /**
     * 用量监测
     * @return
     */
    @Override
    public Map<String, Object> monitor(Long itemId, String quotaTime,String quotaType){
        Map<String,Object> result = new HashMap<>();
        List<DosageVo> dosageVoList = new ArrayList<>();
        result.put("dosageVoList",dosageVoList);
        //获取设备列表
        ItemTopology itemTopology = itemTopologyMapper.selectById(itemId);
        List<String> devices = Arrays.stream(StringUtils.split(itemTopology.getDeviceId(), ",")).collect(Collectors.toList());
        //找不到对应设备返回
        /*if (ObjectUtil.isEmpty(devices)) {
            return result;
        }*/
        //当月
        Date nowMonthStart = DateUtils.dateTime(DateUtils.YYYY_MM,quotaTime);
        if(quotaType.equals("0")){
            //获取能耗定额
            QuotaConfig monthConfig = baseMapper.selectOne(new LambdaQueryWrapper<QuotaConfig>()
                .eq(QuotaConfig::getItemId,itemId)
                .eq(QuotaConfig::getQuotaType,"0")
                .eq(QuotaConfig::getQuotaTime,quotaTime));
            BigDecimal monthQuota = BigDecimal.ZERO;
            if(ObjectUtil.isNotEmpty(monthConfig)){
                monthQuota = monthConfig.getQuotaValue();
            }
            result.put("monthQuota",monthQuota.setScale(2, RoundingMode.HALF_UP));

            // 当月天数
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(nowMonthStart);
            int day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            calendar.setTime(new Date());
            int date = calendar.get(Calendar.DAY_OF_MONTH);
            result.put("totalDay",day);
            if(DateUtils.parseDateToStr(DateUtils.YYYY_MM,nowMonthStart).equals(DateUtils.parseDateToStr(DateUtils.YYYY_MM,new Date()))){
                BigDecimal monthTimePer = BigDecimal.valueOf(date).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(day),2, RoundingMode.HALF_UP);
                result.put("monthTimePer",monthTimePer);
                result.put("date",date);
            }else {
                result.put("monthTimePer",100);
                result.put("date",day);
            }
            Date nowMonthEnd = DateUtils.parseDate(DateUtils.parseDateToStr("yyyy-MM-" + day + " 23:59:59",nowMonthStart));

            //当月能耗
            List<EnergyStatistics> nowMonthStatistics = new ArrayList<>();
            if (ObjectUtil.isNotEmpty(devices)) {
                 nowMonthStatistics = energyStatisticsMapper.selectList(buildQuery(devices,nowMonthStart,nowMonthEnd));

            }
            BigDecimal monthTotal = nowMonthStatistics.stream().map(EnergyStatistics::getStatistics).reduce(BigDecimal.ZERO,BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
            result.put("monthTotal",monthTotal);
            if (!monthQuota.equals(BigDecimal.ZERO)) {
                result.put("monthPer",monthTotal.multiply(BigDecimal.valueOf(100)).divide(monthQuota,2,RoundingMode.HALF_UP));
            }else {
                result.put("monthPer","0.00");
            }

            //月度用量
            for (int i = 0; i < day; i++) {
                DosageVo dosageVo = new DosageVo();

                Date begin = DateUtils.addDays(nowMonthStart, i);
                Date finish = DateUtils.addDays(nowMonthStart,i+1);
                if (begin.after(DateTime.now())) {
                    break;
                }
                //实际能耗
                List<EnergyStatistics> voList = nowMonthStatistics.stream()
                    .filter(e -> e.getUpdateTime().before(finish) && e.getUpdateTime().after(begin)).collect(Collectors.toList());

                String key = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,begin).substring(0,10);
                dosageVo.setDate(key);
                //能耗数据
                BigDecimal real = voList.stream().map(EnergyStatistics::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
                dosageVo.setEnergyData(real);
                if(real.stripTrailingZeros().equals(BigDecimal.ZERO)){
                    dosageVo.setEnergyData("--");
                }
                dosageVoList.add(dosageVo);
            }
        }else if(quotaType.equals("1")){
            //获取能耗定额
            QuotaConfig yearConfig = baseMapper.selectOne(new LambdaQueryWrapper<QuotaConfig>()
                .eq(QuotaConfig::getItemId,itemId)
                .eq(QuotaConfig::getQuotaType,"1")
                .eq(QuotaConfig::getQuotaTime,DateUtils.dateTime(DateUtils.YYYY,quotaTime)));
            BigDecimal yearQuota = BigDecimal.ZERO;
            if(ObjectUtil.isNotEmpty(yearConfig)){
                yearQuota = yearConfig.getQuotaValue();
            }
            result.put("yearQuota",yearQuota.setScale(2, RoundingMode.HALF_UP));

            //当年
            Date nowYearStart = DateUtils.dateTime(DateUtils.YYYY,quotaTime);
            Date nowYearEnd = DateUtils.parseDate(DateUtils.parseDateToStr("yyyy-12-31 23:59:59",nowMonthStart));
            result.put("totalDay",365);
            int date = DateUtils.differentDaysByMillisecond(nowYearStart,new Date());
            if(DateUtils.parseDateToStr(DateUtils.YYYY,nowMonthStart).equals(DateUtils.parseDateToStr(DateUtils.YYYY,new Date()))){
                int difDay = DateUtils.differentDaysByMillisecond(nowYearStart,new Date());
                BigDecimal yearTimePer = BigDecimal.valueOf(difDay).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(365),2, RoundingMode.HALF_UP);
                result.put("yearTimePer",yearTimePer);
                result.put("date",date);
            }else {
                result.put("yearTimePer",100);
                result.put("date",365);
            }


            //当年能耗
            List<EnergyStatistics> nowYearStatistics = new ArrayList<>();
            if (ObjectUtil.isNotEmpty(devices)) {
                 nowYearStatistics = energyStatisticsMapper.selectList(buildQuery(devices,nowYearStart,nowYearEnd));
            }
            BigDecimal yearTotal = nowYearStatistics.stream().map(EnergyStatistics::getStatistics).reduce(BigDecimal.ZERO,BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
            result.put("yearTotal",yearTotal);
            if (!yearQuota.stripTrailingZeros().equals(BigDecimal.ZERO)) {
                result.put("yearPer",yearTotal.multiply(BigDecimal.valueOf(100)).divide(yearQuota,2,RoundingMode.HALF_UP));
            }else {
                result.put("yearPer","0.00");
            }

            //年度用量
            for (int i = 0; i < 12; i++) {
                DosageVo dosageVo = new DosageVo();

                Date begin = DateUtils.addMonths(nowYearStart, i);
                Date finish = DateUtils.addMonths(nowYearStart,i+1);
                //实际能耗
                List<EnergyStatistics> voList = nowYearStatistics.stream()
                    .filter(e -> e.getUpdateTime().before(finish) && e.getUpdateTime().after(begin)).collect(Collectors.toList());

                String key = (i+1)+"月";
                dosageVo.setDate(key);
                //定额数据
                QuotaConfig monthConfig = baseMapper.selectOne(new LambdaQueryWrapper<QuotaConfig>()
                    .eq(QuotaConfig::getItemId,itemId)
                    .eq(QuotaConfig::getQuotaType,"0")
                    .eq(QuotaConfig::getQuotaTime,begin));
                BigDecimal monthQuota = BigDecimal.ZERO;
                if(ObjectUtil.isNotEmpty(monthConfig)){
                    monthQuota = monthConfig.getQuotaValue();
                }
                dosageVo.setQuotaData(monthQuota);
                if(monthQuota.stripTrailingZeros().equals(BigDecimal.ZERO)){
                    dosageVo.setQuotaData("--");
                }
                //能耗数据
                BigDecimal real = voList.stream().map(EnergyStatistics::getStatistics).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
                dosageVo.setEnergyData(real);
                if(real.stripTrailingZeros().equals(BigDecimal.ZERO)){
                    dosageVo.setEnergyData("--");
                }
                dosageVoList.add(dosageVo);
            }
        }
        return result;
    }
}
