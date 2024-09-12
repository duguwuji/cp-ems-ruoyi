package com.cpems.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.cpems.common.core.page.TableDataInfo;
import com.cpems.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cpems.common.utils.StringUtils;
import com.cpems.system.domain.bo.ItemTopologyBo;
import com.cpems.system.domain.vo.ItemTopologyVo;
import com.cpems.system.service.IItemTopologyService;
import com.cpems.system.service.ISysOssService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.cpems.system.domain.bo.EquipmentInfoBo;
import com.cpems.system.domain.vo.EquipmentInfoVo;
import com.cpems.system.domain.EquipmentInfo;
import com.cpems.system.mapper.EquipmentInfoMapper;
import com.cpems.system.service.IEquipmentInfoService;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 设备信息Service业务层处理
 *
 * @author ruoyi
 * @date 2023-04-06
 */
@RequiredArgsConstructor
@Service
public class EquipmentInfoServiceImpl implements IEquipmentInfoService {

    private final EquipmentInfoMapper baseMapper;
    private final ISysOssService sysOssService;
    private final IItemTopologyService itemTopologyService;

    /**
     * 查询设备信息
     */
    @Override
    public EquipmentInfoVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询设备信息列表
     */
    @Override
    public TableDataInfo<EquipmentInfoVo> queryPageList(EquipmentInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<EquipmentInfo> lqw = buildQueryWrapper(bo);
        Page<EquipmentInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);

//        获取所有区域
        List<ItemTopologyVo> topologyVoList = itemTopologyService.queryList(new ItemTopologyBo());

        for (EquipmentInfoVo vo : result.getRecords()) {
//            图片
            if(StringUtils.isNotBlank(vo.getImg())) {
                List<Long> collect = Arrays.stream(StringUtils.split(vo.getImg(), ",")).map(e -> Convert.toLong(e)).collect(Collectors.toList());
                vo.setImgOss(sysOssService.listByIds(collect));
            }
        }

        return TableDataInfo.build(result);
    }

    /**
     * 查询设备信息列表
     */
    @Override
    public List<EquipmentInfoVo> queryList(EquipmentInfoBo bo) {
        LambdaQueryWrapper<EquipmentInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<EquipmentInfo> buildQueryWrapper(EquipmentInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<EquipmentInfo> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), EquipmentInfo::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getSn()), EquipmentInfo::getSn, bo.getSn());
        lqw.eq(StringUtils.isNotBlank(bo.getModel()), EquipmentInfo::getModel, bo.getModel());
        lqw.eq(StringUtils.isNotBlank(bo.getDescription()), EquipmentInfo::getDescription, bo.getDescription());
        lqw.eq(StringUtils.isNotBlank(bo.getImg()), EquipmentInfo::getImg, bo.getImg());
        lqw.eq(StringUtils.isNotBlank(bo.getQrCode()), EquipmentInfo::getQrCode, bo.getQrCode());
        lqw.eq(StringUtils.isNotBlank(bo.getFactory()), EquipmentInfo::getFactory, bo.getFactory());
        lqw.eq(StringUtils.isNotBlank(bo.getType()), EquipmentInfo::getType, bo.getType());
        return lqw;
    }

    /**
     * 新增设备信息
     */
    @Override
    public Boolean insertByBo(EquipmentInfoBo bo) {
        EquipmentInfo add = BeanUtil.toBean(bo, EquipmentInfo.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改设备信息
     */
    @Override
    public Boolean updateByBo(EquipmentInfoBo bo) {
        EquipmentInfo update = BeanUtil.toBean(bo, EquipmentInfo.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(EquipmentInfo entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除设备信息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
    /**
     * 获取全部不同状态的设备数量及占比
     */
    @Override
    public List<EquipmentInfoVo> getAllStatus(){
        List<EquipmentInfoVo> list = new ArrayList<>();
        LambdaQueryWrapper<EquipmentInfo> lqw = new LambdaQueryWrapper<>();
        Long count = baseMapper.selectCount(lqw);
        List<String> statusList = Arrays.asList("0","1","2");
        for(String status:statusList){
            EquipmentInfoVo equipmentInfoVo = new EquipmentInfoVo();
            Long statusCount = baseMapper.selectCount(new LambdaQueryWrapper<EquipmentInfo>().eq(EquipmentInfo::getStatus,status));
            equipmentInfoVo.setStatus(status);
            equipmentInfoVo.setCount(statusCount);
            DecimalFormat df = new DecimalFormat("0.00%");
            String percent = df.format((statusCount / count.doubleValue()));
            equipmentInfoVo.setProportion(percent);
            list.add(equipmentInfoVo);
        }
        return list;
    }

    /**
     * 判断设备是否绑定区域
     */
    @Override
    public Boolean hasBindArea(Collection<Long> ids){
        List<String> deviceId = new ArrayList<>();

        List<EquipmentInfo> equipmentInfos = baseMapper.selectBatchIds(ids);
        List<String> sns = equipmentInfos.stream().map(EquipmentInfo::getSn).collect(Collectors.toList());

        List<ItemTopologyVo> itemTopologyVos =itemTopologyService.queryList(new ItemTopologyBo());

        for(ItemTopologyVo itemTopologyVo:itemTopologyVos){
            if (ObjectUtil.isNotEmpty(itemTopologyVo.getDeviceId())) {
                List<String> temp = Arrays.stream(StringUtils.split(itemTopologyVo.getDeviceId(), ",")).collect(Collectors.toList());
                deviceId.addAll(temp);
            }
        }

        //去重
        deviceId = deviceId.stream().distinct().collect(Collectors.toList());

        for (String sn:sns){
            if (deviceId.contains(sn)){
                return  true;
            }
        }
        return false;
    }
}
