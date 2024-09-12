package com.cpems.system.config;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cpems.common.core.domain.entity.SysUser;
import com.cpems.common.utils.DateUtils;
import com.cpems.common.utils.email.MailUtils;
import com.cpems.common.utils.redis.RedisUtils;
import com.cpems.common.utils.spring.SpringUtils;
import com.cpems.system.domain.AlarmRule;
import com.cpems.system.domain.EquipmentInfo;
import com.cpems.system.domain.RealtimeAlarm;
import com.cpems.system.domain.RepairOrder;
import com.cpems.system.domain.enums.AlarmConditionType;
import com.cpems.system.domain.enums.TopicType;
import com.cpems.system.domain.vo.AlarmRuleVo;
import com.cpems.system.domain.vo.EquipmentInfoVo;
import com.cpems.system.domain.vo.RealtimeAlarmVo;
import com.cpems.system.mapper.*;
import com.cpems.system.service.ISysUserService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 创建ACK回调
 *
 * @Author cpems
 * @Date 2023/4/11 17:31
 */
@Component
public class MyAckReceiver implements ChannelAwareMessageListener {
    //TODO:这里后续需要做一个util类来定义Mongo用的方法
    private AlarmRuleMapper alarmRuleMapper = SpringUtils.getBean(AlarmRuleMapper.class);
    private EquipmentInfoMapper equipmentInfoMapper = SpringUtils.getBean(EquipmentInfoMapper.class);
    private RealtimeAlarmMapper realtimeAlarmMapper = SpringUtils.getBean(RealtimeAlarmMapper.class);
    private ISysUserService userService = SpringUtils.getBean(ISysUserService.class);
    private RepairOrderMapper repairOrderMapper = SpringUtils.getBean(RepairOrderMapper.class);

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            byte[] body = message.getBody();
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(body));
            Map<String, Object> msgMap = (Map<String, Object>) ois.readObject();
            BigDecimal value = new BigDecimal(msgMap.get("value").toString());
            String clientId = msgMap.get("clientId").toString();
            //String topic=msgMap.get("topic");
            String createTime = msgMap.get("createTime").toString();
            ois.close();
            /*ElectricityU insert = new ElectricityU();
            insert.setClientId(clientId);
            insert.setCreateTime(DateUtils.parseDate(createTime));*/
            if (message.getMessageProperties().getReceivedRoutingKey().equals(TopicType.ELECTRIC_U.getInfo())) {
                List<AlarmRuleVo> alarmRuleVoList = alarmRuleMapper.selectVoList(new LambdaQueryWrapper<AlarmRule>().eq(AlarmRule::getAlarmSwitch, "0").eq(AlarmRule::getParamName, TopicType.ELECTRIC_U.getCode().toString()));
                for (AlarmRuleVo alarmRuleVo : alarmRuleVoList) {
                    if (alarmRuleVo.getCondition1().equals(AlarmConditionType.G.getInfo())) {
                        if (value.compareTo(alarmRuleVo.getThresholdValue1()) > 0) {
                            BigDecimal last = getLast(clientId,TopicType.ELECTRIC_U.getCode().toString());
                            if (last == null || !(last.compareTo(alarmRuleVo.getThresholdValue1())>0)) {
                                insertAlarm(value, createTime, clientId, alarmRuleVo, TopicType.ELECTRIC_U.getCode().toString());
                            }
                        }
                    }
                    if (alarmRuleVo.getCondition1().equals(AlarmConditionType.L.getInfo())) {
                        if (value.compareTo(alarmRuleVo.getThresholdValue1()) < 0) {
                            BigDecimal last = getLast(clientId,TopicType.ELECTRIC_U.getCode().toString());
                            if (last == null || !(last.compareTo(alarmRuleVo.getThresholdValue1())<0)) {
                                insertAlarm(value, createTime, clientId, alarmRuleVo, TopicType.ELECTRIC_U.getCode().toString());
                            }
                        }
                    }
                    if (alarmRuleVo.getCondition1().equals(AlarmConditionType.E.getInfo())) {
                        if (value.compareTo(alarmRuleVo.getThresholdValue1()) == 0) {
                            BigDecimal last = getLast(clientId,TopicType.ELECTRIC_U.getCode().toString());
                            if (last == null || !(last.compareTo(alarmRuleVo.getThresholdValue1())==0)) {
                                insertAlarm(value, createTime, clientId, alarmRuleVo, TopicType.ELECTRIC_U.getCode().toString());
                            }
                        }
                    }
                    if (alarmRuleVo.getCondition1().equals(AlarmConditionType.GE.getInfo())) {
                        if (value.compareTo(alarmRuleVo.getThresholdValue1()) >= 0) {
                            BigDecimal last = getLast(clientId,TopicType.ELECTRIC_U.getCode().toString());
                            if (last == null || !(last.compareTo(alarmRuleVo.getThresholdValue1())>=0)) {
                                insertAlarm(value, createTime, clientId, alarmRuleVo, TopicType.ELECTRIC_U.getCode().toString());
                            }
                        }
                    }
                    if (alarmRuleVo.getCondition1().equals(AlarmConditionType.LE.getInfo())) {
                        if (value.compareTo(alarmRuleVo.getThresholdValue1()) <= 0) {
                            BigDecimal last = getLast(clientId,TopicType.ELECTRIC_U.getCode().toString());
                            if (last == null || !(last.compareTo(alarmRuleVo.getThresholdValue1())<=0)) {
                                insertAlarm(value, createTime, clientId, alarmRuleVo, TopicType.ELECTRIC_U.getCode().toString());
                            }
                        }
                    }
                    if (alarmRuleVo.getCondition2().equals(AlarmConditionType.G.getInfo())) {
                        if (value.compareTo(alarmRuleVo.getThresholdValue2()) > 0) {
                            BigDecimal last = getLast(clientId,TopicType.ELECTRIC_U.getCode().toString());
                            if (last == null || !(last.compareTo(alarmRuleVo.getThresholdValue2())>0)) {
                                insertAlarm(value, createTime, clientId, alarmRuleVo, TopicType.ELECTRIC_U.getCode().toString());
                            }
                        }
                    }
                    if (alarmRuleVo.getCondition2().equals(AlarmConditionType.L.getInfo())) {
                        if (value.compareTo(alarmRuleVo.getThresholdValue2()) < 0) {
                            BigDecimal last = getLast(clientId,TopicType.ELECTRIC_U.getCode().toString());
                            if (last == null || !(last.compareTo(alarmRuleVo.getThresholdValue2())<0)) {
                                insertAlarm(value, createTime, clientId, alarmRuleVo, TopicType.ELECTRIC_U.getCode().toString());
                            }
                        }
                    }
                    if (alarmRuleVo.getCondition2().equals(AlarmConditionType.E.getInfo())) {
                        if (value.compareTo(alarmRuleVo.getThresholdValue2()) == 0) {
                            BigDecimal last = getLast(clientId,TopicType.ELECTRIC_U.getCode().toString());
                            if (last == null || !(last.compareTo(alarmRuleVo.getThresholdValue2())==0)) {
                                insertAlarm(value, createTime, clientId, alarmRuleVo, TopicType.ELECTRIC_U.getCode().toString());
                            }
                        }
                    }
                    if (alarmRuleVo.getCondition2().equals(AlarmConditionType.GE.getInfo())) {
                        if (value.compareTo(alarmRuleVo.getThresholdValue2()) >= 0) {
                            BigDecimal last = getLast(clientId,TopicType.ELECTRIC_U.getCode().toString());
                            if (last == null || !(last.compareTo(alarmRuleVo.getThresholdValue2())>=0)) {
                                insertAlarm(value, createTime, clientId, alarmRuleVo, TopicType.ELECTRIC_U.getCode().toString());
                            }
                        }
                    }
                    if (alarmRuleVo.getCondition2().equals(AlarmConditionType.LE.getInfo())) {
                        if (value.compareTo(alarmRuleVo.getThresholdValue2()) <= 0) {
                            BigDecimal last = getLast(clientId,TopicType.ELECTRIC_U.getCode().toString());
                            if (last == null || !(last.compareTo(alarmRuleVo.getThresholdValue2())<=0)) {
                                insertAlarm(value, createTime, clientId, alarmRuleVo, TopicType.ELECTRIC_U.getCode().toString());
                            }
                        }
                    }
                }
            }
            if (message.getMessageProperties().getReceivedRoutingKey().equals(TopicType.ELECTRIC_I.getInfo())) {
                List<AlarmRuleVo> alarmRuleVoList = alarmRuleMapper.selectVoList(new LambdaQueryWrapper<AlarmRule>().eq(AlarmRule::getAlarmSwitch, "0").eq(AlarmRule::getParamName, TopicType.ELECTRIC_I.getCode().toString()));
                for (AlarmRuleVo alarmRuleVo : alarmRuleVoList) {
                    if (alarmRuleVo.getCondition1().equals(AlarmConditionType.G.getInfo())) {
                        if (value.compareTo(alarmRuleVo.getThresholdValue1()) > 0) {
                            BigDecimal last = getLast(clientId,TopicType.ELECTRIC_I.getCode().toString());
                            if (last == null || !(last.compareTo(alarmRuleVo.getThresholdValue1())>0)) {
                                insertAlarm(value, createTime, clientId, alarmRuleVo, TopicType.ELECTRIC_I.getCode().toString());
                            }
                        }
                    }
                    if (alarmRuleVo.getCondition1().equals(AlarmConditionType.L.getInfo())) {
                        if (value.compareTo(alarmRuleVo.getThresholdValue1()) < 0) {
                            BigDecimal last = getLast(clientId,TopicType.ELECTRIC_I.getCode().toString());
                            if (last == null || !(last.compareTo(alarmRuleVo.getThresholdValue1())<0)) {
                                insertAlarm(value, createTime, clientId, alarmRuleVo, TopicType.ELECTRIC_I.getCode().toString());
                            }
                        }
                    }
                    if (alarmRuleVo.getCondition1().equals(AlarmConditionType.E.getInfo())) {
                        if (value.compareTo(alarmRuleVo.getThresholdValue1()) == 0) {
                            BigDecimal last = getLast(clientId,TopicType.ELECTRIC_I.getCode().toString());
                            if (last == null || !(last.compareTo(alarmRuleVo.getThresholdValue1())==0)) {
                                insertAlarm(value, createTime, clientId, alarmRuleVo, TopicType.ELECTRIC_I.getCode().toString());
                            }
                        }
                    }
                    if (alarmRuleVo.getCondition1().equals(AlarmConditionType.GE.getInfo())) {
                        if (value.compareTo(alarmRuleVo.getThresholdValue1()) >= 0) {
                            BigDecimal last = getLast(clientId,TopicType.ELECTRIC_I.getCode().toString());
                            if (last == null || !(last.compareTo(alarmRuleVo.getThresholdValue1())>=0)) {
                                insertAlarm(value, createTime, clientId, alarmRuleVo, TopicType.ELECTRIC_I.getCode().toString());
                            }
                        }
                    }
                    if (alarmRuleVo.getCondition1().equals(AlarmConditionType.LE.getInfo())) {
                        if (value.compareTo(alarmRuleVo.getThresholdValue1()) <= 0) {
                            BigDecimal last = getLast(clientId,TopicType.ELECTRIC_I.getCode().toString());
                            if (last == null || !(last.compareTo(alarmRuleVo.getThresholdValue1())<=0)) {
                                insertAlarm(value, createTime, clientId, alarmRuleVo, TopicType.ELECTRIC_I.getCode().toString());
                            }
                        }
                    }
                    if (alarmRuleVo.getCondition2().equals(AlarmConditionType.G.getInfo())) {
                        if (value.compareTo(alarmRuleVo.getThresholdValue2()) > 0) {
                            BigDecimal last = getLast(clientId,TopicType.ELECTRIC_I.getCode().toString());
                            if (last == null || !(last.compareTo(alarmRuleVo.getThresholdValue2())>0)) {
                                insertAlarm(value, createTime, clientId, alarmRuleVo, TopicType.ELECTRIC_I.getCode().toString());
                            }
                        }
                    }
                    if (alarmRuleVo.getCondition2().equals(AlarmConditionType.L.getInfo())) {
                        if (value.compareTo(alarmRuleVo.getThresholdValue2()) < 0) {
                            BigDecimal last = getLast(clientId,TopicType.ELECTRIC_I.getCode().toString());
                            if (last == null || !(last.compareTo(alarmRuleVo.getThresholdValue2())<0)) {
                                insertAlarm(value, createTime, clientId, alarmRuleVo, TopicType.ELECTRIC_I.getCode().toString());
                            }
                        }
                    }
                    if (alarmRuleVo.getCondition2().equals(AlarmConditionType.E.getInfo())) {
                        if (value.compareTo(alarmRuleVo.getThresholdValue2()) == 0) {
                            BigDecimal last = getLast(clientId,TopicType.ELECTRIC_I.getCode().toString());
                            if (last == null || !(last.compareTo(alarmRuleVo.getThresholdValue2())==0)) {
                                insertAlarm(value, createTime, clientId, alarmRuleVo, TopicType.ELECTRIC_I.getCode().toString());
                            }
                        }
                    }
                    if (alarmRuleVo.getCondition2().equals(AlarmConditionType.GE.getInfo())) {
                        if (value.compareTo(alarmRuleVo.getThresholdValue2()) >= 0) {
                            BigDecimal last = getLast(clientId,TopicType.ELECTRIC_I.getCode().toString());
                            if (last == null || !(last.compareTo(alarmRuleVo.getThresholdValue2())>=0)) {
                                insertAlarm(value, createTime, clientId, alarmRuleVo, TopicType.ELECTRIC_I.getCode().toString());
                            }
                        }
                    }
                    if (alarmRuleVo.getCondition2().equals(AlarmConditionType.LE.getInfo())) {
                        if (value.compareTo(alarmRuleVo.getThresholdValue2()) <= 0) {
                            BigDecimal last = getLast(clientId,TopicType.ELECTRIC_I.getCode().toString());
                            if (last == null || !(last.compareTo(alarmRuleVo.getThresholdValue2())<=0)) {
                                insertAlarm(value, createTime, clientId, alarmRuleVo, TopicType.ELECTRIC_I.getCode().toString());
                            }
                        }
                    }
                }
            }
            /*if (message.getMessageProperties().getReceivedRoutingKey().equals(TopicType.ELECTRIC_W.getInfo())) {

            }*/
            if (message.getMessageProperties().getReceivedRoutingKey().equals(TopicType.ELECTRIC_P.getInfo())) {
                List<AlarmRuleVo> alarmRuleVoList = alarmRuleMapper.selectVoList(new LambdaQueryWrapper<AlarmRule>().eq(AlarmRule::getAlarmSwitch, "0").eq(AlarmRule::getParamName, TopicType.ELECTRIC_P.getCode().toString()));
                for (AlarmRuleVo alarmRuleVo : alarmRuleVoList) {
                    if (alarmRuleVo.getCondition1().equals(AlarmConditionType.G.getInfo())) {
                        if (value.compareTo(alarmRuleVo.getThresholdValue1()) > 0) {
                            BigDecimal last = getLast(clientId,TopicType.ELECTRIC_P.getCode().toString());
                            if (last == null || !(last.compareTo(alarmRuleVo.getThresholdValue1())>0)) {
                                insertAlarm(value, createTime, clientId, alarmRuleVo, TopicType.ELECTRIC_P.getCode().toString());
                            }
                        }
                    }
                    if (alarmRuleVo.getCondition1().equals(AlarmConditionType.L.getInfo())) {
                        if (value.compareTo(alarmRuleVo.getThresholdValue1()) < 0) {
                            BigDecimal last = getLast(clientId,TopicType.ELECTRIC_P.getCode().toString());
                            if (last == null || !(last.compareTo(alarmRuleVo.getThresholdValue1())<0)) {
                                insertAlarm(value, createTime, clientId, alarmRuleVo, TopicType.ELECTRIC_P.getCode().toString());
                            }
                        }
                    }
                    if (alarmRuleVo.getCondition1().equals(AlarmConditionType.E.getInfo())) {
                        if (value.compareTo(alarmRuleVo.getThresholdValue1()) == 0) {
                            BigDecimal last = getLast(clientId,TopicType.ELECTRIC_P.getCode().toString());
                            if (last == null || !(last.compareTo(alarmRuleVo.getThresholdValue1())==0)) {
                                insertAlarm(value, createTime, clientId, alarmRuleVo, TopicType.ELECTRIC_P.getCode().toString());
                            }
                        }
                    }
                    if (alarmRuleVo.getCondition1().equals(AlarmConditionType.GE.getInfo())) {
                        if (value.compareTo(alarmRuleVo.getThresholdValue1()) >= 0) {
                            BigDecimal last = getLast(clientId,TopicType.ELECTRIC_P.getCode().toString());
                            if (last == null || !(last.compareTo(alarmRuleVo.getThresholdValue1())>=0)) {
                                insertAlarm(value, createTime, clientId, alarmRuleVo, TopicType.ELECTRIC_P.getCode().toString());
                            }
                        }
                    }
                    if (alarmRuleVo.getCondition1().equals(AlarmConditionType.LE.getInfo())) {
                        if (value.compareTo(alarmRuleVo.getThresholdValue1()) <= 0) {
                            BigDecimal last = getLast(clientId,TopicType.ELECTRIC_P.getCode().toString());
                            if (last == null || !(last.compareTo(alarmRuleVo.getThresholdValue1())<=0)) {
                                insertAlarm(value, createTime, clientId, alarmRuleVo, TopicType.ELECTRIC_P.getCode().toString());
                            }
                        }
                    }
                    if (alarmRuleVo.getCondition2().equals(AlarmConditionType.G.getInfo())) {
                        if (value.compareTo(alarmRuleVo.getThresholdValue2()) > 0) {
                            BigDecimal last = getLast(clientId,TopicType.ELECTRIC_P.getCode().toString());
                            if (last == null || !(last.compareTo(alarmRuleVo.getThresholdValue2())>0)) {
                                insertAlarm(value, createTime, clientId, alarmRuleVo, TopicType.ELECTRIC_P.getCode().toString());
                            }
                        }
                    }
                    if (alarmRuleVo.getCondition2().equals(AlarmConditionType.L.getInfo())) {
                        if (value.compareTo(alarmRuleVo.getThresholdValue2()) < 0) {
                            BigDecimal last = getLast(clientId,TopicType.ELECTRIC_P.getCode().toString());
                            if (last == null || !(last.compareTo(alarmRuleVo.getThresholdValue2())<0)) {
                                insertAlarm(value, createTime, clientId, alarmRuleVo, TopicType.ELECTRIC_P.getCode().toString());
                            }
                        }
                    }
                    if (alarmRuleVo.getCondition2().equals(AlarmConditionType.E.getInfo())) {
                        if (value.compareTo(alarmRuleVo.getThresholdValue2()) == 0) {
                            BigDecimal last = getLast(clientId,TopicType.ELECTRIC_P.getCode().toString());
                            if (last == null || !(last.compareTo(alarmRuleVo.getThresholdValue2())==0)) {
                                insertAlarm(value, createTime, clientId, alarmRuleVo, TopicType.ELECTRIC_P.getCode().toString());
                            }
                        }
                    }
                    if (alarmRuleVo.getCondition2().equals(AlarmConditionType.GE.getInfo())) {
                        if (value.compareTo(alarmRuleVo.getThresholdValue2()) >= 0) {
                            BigDecimal last = getLast(clientId,TopicType.ELECTRIC_P.getCode().toString());
                            if (last == null || !(last.compareTo(alarmRuleVo.getThresholdValue2())>=0)) {
                                insertAlarm(value, createTime, clientId, alarmRuleVo, TopicType.ELECTRIC_P.getCode().toString());
                            }
                        }
                    }
                    if (alarmRuleVo.getCondition2().equals(AlarmConditionType.LE.getInfo())) {
                        if (value.compareTo(alarmRuleVo.getThresholdValue2()) <= 0) {
                            BigDecimal last = getLast(clientId,TopicType.ELECTRIC_P.getCode().toString());
                            if (last == null || !(last.compareTo(alarmRuleVo.getThresholdValue2())<=0)) {
                                insertAlarm(value, createTime, clientId, alarmRuleVo, TopicType.ELECTRIC_P.getCode().toString());
                            }
                        }
                    }
                }

            }
            /*if (message.getMessageProperties().getReceivedRoutingKey().equals(TopicType.ELECTRIC_U.getInfo())) {
                if (ObjectUtil.isNotEmpty(value)) {
                    insert.setValue(value);
                    electricityUMapper.insert(insert);
                }
            }
            if (message.getMessageProperties().getReceivedRoutingKey().equals(TopicType.ELECTRIC_I.getInfo())) {
                if (ObjectUtil.isNotEmpty(value)) {
                    insert.setValue(value);
                    electricityIMapper.insert(BeanUtil.toBean(insert, ElectricityI.class));
                }
            }
            if (message.getMessageProperties().getReceivedRoutingKey().equals(TopicType.ELECTRIC_W.getInfo())) {
                if (ObjectUtil.isNotEmpty(value)) {
                    insert.setValue(value);
                    electricityWMapper.insert(BeanUtil.toBean(insert, ElectricityW.class));
                }
            }
            if (message.getMessageProperties().getReceivedRoutingKey().equals(TopicType.ELECTRIC_P.getInfo())) {
                if (ObjectUtil.isNotEmpty(value)) {
                    insert.setValue(value);
                    electricityPMapper.insert(BeanUtil.toBean(insert, ElectricityP.class));
                }
            }*/

            //System.out.println("  MyAckReceiver  name:"+name+"  content:"+content+"  time:"+time);
            /*String messageId = msgMap.get("messageId");
            String messageData = msgMap.get("messageData");
            String createTime = msgMap.get("createTime");
            ois.close();
            System.out.println("  MyAckReceiver  messageId:"+messageId+"  messageData:"+messageData+"  createTime:"+createTime);*/
            System.out.println("消费的主题消息来自：" + message.getMessageProperties().getConsumerQueue());

            //这里为存储到redids
            RedisUtils.setCacheMap(message.getMessageProperties().getConsumerQueue(), msgMap);
            //这里为存储到mongo
            //mongoTemplate.save(msgMap,"test");


            channel.basicAck(deliveryTag, true); //第二个参数，手动确认可以被批处理，当该参数为 true 时，则可以一次性确认 delivery_tag 小于等于传入值的所有消息
//			channel.basicReject(deliveryTag, true);//第二个参数，true会重新放回队列，所以需要自己根据业务逻辑判断什么时候使用拒绝
        } catch (Exception e) {
            channel.basicReject(deliveryTag, false);
            e.printStackTrace();
        }
    }

    private void insertAlarm(BigDecimal value, String createTime, String clientId, AlarmRuleVo vo, String electricType) {
        RealtimeAlarm insert = new RealtimeAlarm();
        insert.setParamName(electricType);
        insert.setAlarmTime(DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS, createTime));
        insert.setAlarmVal(value);
        insert.setAlarmInfo(vo.getAlarmInfo());
        insert.setEquipment(clientId);
        EquipmentInfoVo equipmentInfoVo = equipmentInfoMapper.selectVoOne(new LambdaQueryWrapper<EquipmentInfo>().eq(EquipmentInfo::getSn, clientId));
        insert.setArea(equipmentInfoVo.getName());
        insert.setAlarmLevel(vo.getAlarmLevel());
        realtimeAlarmMapper.insert(insert);
        SysUser user = userService.selectUserById(Long.valueOf(vo.getUserId()));
//        realtimeAlarmService.insertByBo(insert);
        if (ObjectUtil.isNotEmpty(user.getEmail())) {
            MailUtils.sendText(user.getEmail(), "报警提醒", "区域:" + insert.getArea() + "设备:" + insert.getEquipment() + vo.getAlarmInfo() + insert.getAlarmVal().toString());
        }
        if ("0".equals(vo.getCreateOrderSwitch())){
            RepairOrder repairOrder = new RepairOrder();
            repairOrder.setOrderNo("WX"+DateUtils.dateTimeNow("yyyyMMddHHmmssSSS"));
            repairOrder.setOrderContent(vo.getAlarmInfo());
            repairOrder.setProjectName(equipmentInfoVo.getName());
            repairOrderMapper.insert(repairOrder);
        }
    }

    private BigDecimal getLast(String clientId, String electricType) {
        RealtimeAlarmVo realtimeAlarmVo = realtimeAlarmMapper.selectVoOne(new LambdaQueryWrapper<RealtimeAlarm>().eq(RealtimeAlarm::getParamName, electricType).eq(RealtimeAlarm::getEquipment, clientId)
            .orderByDesc(RealtimeAlarm::getAlarmTime).last("limit 1"));
        if (ObjectUtil.isNotEmpty(realtimeAlarmVo)) {
            return realtimeAlarmVo.getAlarmVal();
        }
        return null;
    }

}
