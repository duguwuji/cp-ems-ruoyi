package com.cpems.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.cpems.common.constant.Constants;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cpems.common.utils.DateUtils;
import com.cpems.common.utils.StringUtils;
import com.cpems.system.domain.ItemTopology;
import com.cpems.system.domain.enums.AlarmLevelType;
import com.cpems.system.domain.enums.TopicType;
import com.cpems.system.domain.vo.ItemTopologyVo;
import com.cpems.system.mapper.ItemTopologyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.cpems.system.domain.bo.RealtimeAlarmBo;
import com.cpems.system.domain.vo.RealtimeAlarmVo;
import com.cpems.system.domain.RealtimeAlarm;
import com.cpems.system.mapper.RealtimeAlarmMapper;
import com.cpems.system.service.IRealtimeAlarmService;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 实时报警Service业务层处理
 *
 * @author ruoyi
 * @date 2023-04-04
 */
@RequiredArgsConstructor
@Service
public class RealtimeAlarmServiceImpl implements IRealtimeAlarmService {

    private final RealtimeAlarmMapper baseMapper;
    private final ItemTopologyMapper itemTopologyMapper;

    /**
     * 查询实时报警
     */
    @Override
    public RealtimeAlarmVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询实时报警列表
     */
    @Override
    public TableDataInfo<RealtimeAlarmVo> queryPageList(RealtimeAlarmBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<RealtimeAlarm> lqw = buildQueryWrapper(bo);
        Page<RealtimeAlarmVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询实时报警列表
     */
    @Override
    public List<RealtimeAlarmVo> queryList(RealtimeAlarmBo bo) {
        LambdaQueryWrapper<RealtimeAlarm> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<RealtimeAlarm> buildQueryWrapper(RealtimeAlarmBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<RealtimeAlarm> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getParamName()), RealtimeAlarm::getParamName, bo.getParamName());
        lqw.eq(bo.getAlarmTime() != null, RealtimeAlarm::getAlarmTime, bo.getAlarmTime());
        lqw.between(params.get("beginTime") != null && params.get("endTime") != null,
            RealtimeAlarm::getAlarmTime, params.get("beginTime"), params.get("endTime"));
        lqw.eq(StringUtils.isNotBlank(bo.getAlarmInfo()), RealtimeAlarm::getAlarmInfo, bo.getAlarmInfo());
        lqw.eq(StringUtils.isNotBlank(bo.getAlarmLevel()), RealtimeAlarm::getAlarmLevel, bo.getAlarmLevel());
        lqw.eq(StringUtils.isNotBlank(bo.getArea()), RealtimeAlarm::getArea, bo.getArea());
        lqw.eq(StringUtils.isNotBlank(bo.getEquipment()), RealtimeAlarm::getEquipment, bo.getEquipment());
        lqw.eq(bo.getAlarmVal() != null, RealtimeAlarm::getAlarmVal, bo.getAlarmVal());
        return lqw;
    }

    /**
     * 新增实时报警
     */
    @Override
    public Boolean insertByBo(RealtimeAlarmBo bo) {
        RealtimeAlarm add = BeanUtil.toBean(bo, RealtimeAlarm.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改实时报警
     */
    @Override
    public Boolean updateByBo(RealtimeAlarmBo bo) {
        RealtimeAlarm update = BeanUtil.toBean(bo, RealtimeAlarm.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(RealtimeAlarm entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除实时报警
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
    /**
     * 获取全部不同报警等级的数量及占比
     */
    @Override
    public List<RealtimeAlarmVo> getCountOfAllStatus(){
        List<RealtimeAlarmVo> list = new ArrayList<>();
        LambdaQueryWrapper<RealtimeAlarm> lqw = new LambdaQueryWrapper<>();
        Long count = baseMapper.selectCount(lqw);
        List<String> levelList = Arrays.asList("0","1","2");
        for(String level:levelList){
            RealtimeAlarmVo realtimeAlarmVo = new RealtimeAlarmVo();
            Long levelCount = baseMapper.selectCount(new LambdaQueryWrapper<RealtimeAlarm>().eq(RealtimeAlarm::getAlarmLevel,level));
            realtimeAlarmVo.setAlarmLevel(level);
            realtimeAlarmVo.setCount(levelCount);
            DecimalFormat df = new DecimalFormat("0.00%");
            String percent = df.format((levelCount / count.doubleValue()));
            realtimeAlarmVo.setProportion(percent);
            list.add(realtimeAlarmVo);
        }
        return list;
    }
    /**
     * 获取最新若干条报警数据
     */
    @Override
    public List<RealtimeAlarmVo> getLatestAlarmsByCount(Long count){
        LambdaQueryWrapper<RealtimeAlarm> lqw = new LambdaQueryWrapper<>();
        List<RealtimeAlarmVo> list = baseMapper.selectVoList(lqw.orderByDesc(RealtimeAlarm::getAlarmTime));
        return list.stream().limit(count).collect(Collectors.toList());
    }

    /**
     * 获取报警参数统计
     */
    @Override
    public List<RealtimeAlarmVo> getAlarmParameterStatistics(String startTime,String endTime){
        List<RealtimeAlarmVo> result = new ArrayList<>();
        Map<String, Long> map;
        //查询所选日期中所有的报警数据
        List<RealtimeAlarmVo> voList = baseMapper.selectVoList(new LambdaQueryWrapper<RealtimeAlarm>()
            .between(StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime),RealtimeAlarm::getAlarmTime,startTime ,endTime));
        //根据参数分类进行报警数量的统计
        map = voList.stream().filter(p -> StringUtils.isNotBlank(p.getParamName())).collect(Collectors.groupingBy(RealtimeAlarmVo::getParamName, Collectors.counting()));

        //遍历所有报警参数存入list
        for (String param : Constants.ALARM_PARAM) {
            RealtimeAlarmVo add = new RealtimeAlarmVo();
            if (map.keySet().contains(param)){
                add.setParamName(param);
                add.setCount(map.get(param));
                result.add(add);
            }else {
                add.setParamName(param);
                add.setCount(0L);
                result.add(add);
            }
        }

        //根据code修改对应等级名称
        for (RealtimeAlarmVo temp: result){
            if (TopicType.ELECTRIC_U.getCode().toString().equals(temp.getParamName())) {
                temp.setParamName(TopicType.ELECTRIC_U.getDesc());
            }
            if (TopicType.ELECTRIC_I.getCode().toString().equals(temp.getParamName())) {
                temp.setParamName(TopicType.ELECTRIC_I.getDesc());
            }
            if (TopicType.ELECTRIC_P.getCode().toString().equals(temp.getParamName())) {
                temp.setParamName(TopicType.ELECTRIC_P.getDesc());
            }
            if (TopicType.ELECTRIC_W.getCode().toString().equals(temp.getParamName())) {
                temp.setParamName(TopicType.ELECTRIC_W.getDesc());
            }
            if (TopicType.WATER_CONSUMPTION.getCode().toString().equals(temp.getParamName())) {
                temp.setParamName(TopicType.WATER_CONSUMPTION.getDesc());
            }
        }
        return result;
    }

    /**
     * 获取报警等级统计
     */
    @Override
    public List<RealtimeAlarmVo> getAlarmLevelStatistics(String startTime,String endTime){
        List<RealtimeAlarmVo> result = new ArrayList<>();
        Map<String, Long> map;
        //查询所选日期中所有的报警数据
        List<RealtimeAlarmVo> voList = baseMapper.selectVoList(new LambdaQueryWrapper<RealtimeAlarm>()
            .between(StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime),RealtimeAlarm::getAlarmTime,startTime,endTime));
        //根据参数分类进行报警数量的统计
        map = voList.stream().filter(p -> StringUtils.isNotBlank(p.getAlarmLevel())).collect(Collectors.groupingBy(RealtimeAlarmVo::getAlarmLevel, Collectors.counting()));

        //遍历所有报警等级存入list
        for (String level : Constants.ALARM_LEVEL) {
            RealtimeAlarmVo add = new RealtimeAlarmVo();
            DecimalFormat df = new DecimalFormat("0.00%");
            if (map.keySet().contains(level)){
                add.setAlarmLevel(level);
                add.setCount(map.get(level));
                add.setProportion(df.format(add.getCount().doubleValue()/voList.size()));
                result.add(add);
            }else {
                add.setAlarmLevel(level);
                add.setCount(0L);
                add.setProportion(df.format(add.getCount().doubleValue()/voList.size()));
                result.add(add);
            }
        }

        //根据code修改对应等级名称
        for (RealtimeAlarmVo temp: result){
            if (AlarmLevelType.KIND.getCode().toString().equals(temp.getAlarmLevel())) {
                temp.setAlarmLevel(AlarmLevelType.KIND.getInfo());
            }
            if (AlarmLevelType.EMERGENT.getCode().toString().equals(temp.getAlarmLevel())) {
                temp.setAlarmLevel(AlarmLevelType.EMERGENT.getInfo());
            }
            if (AlarmLevelType.SERIOUS.getCode().toString().equals(temp.getAlarmLevel())) {
                temp.setAlarmLevel(AlarmLevelType.SERIOUS.getInfo());
            }

        }
        return result;
    }

    /**
     * 获取报警数量统计
     */
    @Override
    public List<RealtimeAlarmVo> getAlarmCountStatistics(String startTime,String endTime){
        List<RealtimeAlarmVo> result = new ArrayList<>();

        Date lastStart=null;
        Date lastEnd=null;
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            lastStart = DateUtils.addYears(DateUtils.parseDate(startTime ), -1);
            lastEnd = DateUtils.addYears(DateUtils.parseDate(endTime ), -1);
        }
        //查询所选本期日期中所有的报警数据
        List<RealtimeAlarmVo> current = baseMapper.selectVoList(new LambdaQueryWrapper<RealtimeAlarm>()
            .between(StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime),RealtimeAlarm::getAlarmTime,startTime ,endTime));

        //查询所选上期日期中所有的报警数据
        List<RealtimeAlarmVo> last = baseMapper.selectVoList(new LambdaQueryWrapper<RealtimeAlarm>()
            .between(ObjectUtil.isNotEmpty(lastStart) && ObjectUtil.isNotEmpty(lastEnd),RealtimeAlarm::getAlarmTime,lastStart,lastEnd));

        Date start = DateUtils.parseDate(startTime);
        Date end = DateUtils.parseDate(endTime );

        if (ObjectUtil.isNotEmpty(start) && ObjectUtil.isNotEmpty(end)) {
            while (start.before(end)) {
                RealtimeAlarmVo vo = new RealtimeAlarmVo();
                Date finalStart = start;
                vo.setCount(current.stream().filter(e -> e.getAlarmTime().after(finalStart) && e.getAlarmTime().before(DateUtils.addDays(finalStart, 1))).count());
                Date finalLastStart = lastStart;
                vo.setLastCount(last.stream().filter(e -> e.getAlarmTime().after(finalLastStart) && e.getAlarmTime().before(DateUtils.addDays(finalLastStart, 1))).count());
                vo.setName(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, start));
                result.add(vo);
                start = DateUtils.addDays(start, 1);
            }
        }
        return result;
    }

    /**
     * 获取报警分类统计
     */
    @Override
    public List<RealtimeAlarmVo> getAlarmTypeStatistics(String startTime,String endTime){
        List<RealtimeAlarmVo> result = new ArrayList<>();
        Map<String, Long> currentMap;
        Map<String, Long> lastMap;

        Date lastStart=null;
        Date lastEnd=null;
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
             lastStart = DateUtils.addYears(DateUtils.parseDate(startTime ), -1);
             lastEnd = DateUtils.addYears(DateUtils.parseDate(endTime ), -1);
        }

        //查询所选本期日期中所有的报警数据
        List<RealtimeAlarmVo> current = baseMapper.selectVoList(new LambdaQueryWrapper<RealtimeAlarm>()
            .between(StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime),RealtimeAlarm::getAlarmTime,startTime ,endTime));

        currentMap = current.stream().filter(p -> StringUtils.isNotBlank(p.getAlarmInfo())).collect(Collectors.groupingBy(RealtimeAlarmVo::getAlarmInfo, Collectors.counting()));

        //查询所选上期日期中所有的报警数据
        List<RealtimeAlarmVo> last = baseMapper.selectVoList(new LambdaQueryWrapper<RealtimeAlarm>()
            .between(ObjectUtil.isNotEmpty(lastStart) && ObjectUtil.isNotEmpty(lastEnd),RealtimeAlarm::getAlarmTime,lastStart,lastEnd));

        lastMap = last.stream().filter(p -> StringUtils.isNotBlank(p.getAlarmInfo())).collect(Collectors.groupingBy(RealtimeAlarmVo::getAlarmInfo, Collectors.counting()));

        //遍历所有报警信息存入list
        for (String info : currentMap.keySet()) {
            RealtimeAlarmVo vo = new RealtimeAlarmVo();
            vo.setAlarmInfo(info);
            vo.setCount(currentMap.get(info));
            vo.setLastCount(lastMap.get(info));
            result.add(vo);
        }
        return result;
    }

    /**
     * 获取报警区域统计
     */
    @Override
    public List<RealtimeAlarmVo> getAlarmAreaStatistics(String startTime,String endTime){
        List<RealtimeAlarmVo> result = new ArrayList<>();


        List<ItemTopologyVo> topologyVoList = itemTopologyMapper.selectVoList(new LambdaQueryWrapper<ItemTopology>()
            .eq(ItemTopology::getItemType,"building"));

        Date lastStart=null;
        Date lastEnd=null;
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            lastStart = DateUtils.addYears(DateUtils.parseDate(startTime ), -1);
            lastEnd = DateUtils.addYears(DateUtils.parseDate(endTime), -1);
        }
        //查询所选本期日期中所有的报警数据
        List<RealtimeAlarmVo> current = baseMapper.selectVoList(new LambdaQueryWrapper<RealtimeAlarm>()
            .between(StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime),RealtimeAlarm::getAlarmTime,startTime ,endTime));

        //查询所选上期日期中所有的报警数据
        List<RealtimeAlarmVo> last = baseMapper.selectVoList(new LambdaQueryWrapper<RealtimeAlarm>()
            .between(ObjectUtil.isNotEmpty(lastStart) && ObjectUtil.isNotEmpty(lastEnd),RealtimeAlarm::getAlarmTime,lastStart,lastEnd));

        for (ItemTopologyVo itemTopologyVo:topologyVoList){
            RealtimeAlarmVo vo = new RealtimeAlarmVo();
            vo.setArea(itemTopologyVo.getItemName());
            vo.setCount(current.stream().filter(e->itemTopologyVo.getDeviceId().contains(e.getEquipment())).count());
            vo.setLastCount(last.stream().filter(e->itemTopologyVo.getDeviceId().contains(e.getEquipment())).count());
            result.add(vo);
        }
        return result;
    }
}
