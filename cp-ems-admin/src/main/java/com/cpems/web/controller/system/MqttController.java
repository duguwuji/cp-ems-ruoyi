package com.cpems.web.controller.system;

import cn.dev33.satoken.annotation.SaIgnore;
import com.cpems.system.config.MqttMsg;
import com.cpems.system.config.MyMQTTClient;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * MQTT测试控制器
 *
 * @Author cpems
 * @Date 2023/4/7 14:06
 */
@RestController
@RequestMapping("/system/mqtt")
public class MqttController {

    @Autowired
    private MyMQTTClient myMQTTClient;

//    Queue<String> msgQueue = new LinkedList<>();
//    int count = 1;

    /**
     * mqtt测试发送消息到rabbitmq消费到mysql、redis、mongodb
     * @param mqttMsg
     */
    @PostMapping("/sendEquipmentData")
    @ResponseBody
    @SaIgnore
    public synchronized void mqttMsg(MqttMsg mqttMsg) {
//        System.out.println("队列元素数量：" + msgQueue.size());
       // System.out.println("***************" + mqttMsg.getParamName() + ":" + mqttMsg.getParamValue() + "****************");

        //时间格式化
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String time = df.format(new Date());
        mqttMsg.setCreateTime(time);
//        count++;

        //map转json
        JSONObject json = JSONObject.fromObject(mqttMsg);
        String sendMsg = json.toString();
        System.out.println(sendMsg);

        //队列添加元素
//        boolean flag = msgQueue.offer(sendMsg);
//        if (flag) {
            //发布消息  自定义发送消息到对应的主题
            /*myMQTTClient.publish(msgQueue.poll(), TopicType.ELECTRIC_U.getInfo());
            myMQTTClient.publish(msgQueue.poll(), TopicType.ELECTRIC_I.getInfo());
            myMQTTClient.publish(msgQueue.poll(), TopicType.ELECTRIC_W.getInfo());
            myMQTTClient.publish(msgQueue.poll(), TopicType.ELECTRIC_P.getInfo());*/
//          myMQTTClient.publish(sendMsg, TopicType.ELECTRIC_U.getInfo());
//        myMQTTClient.publish(sendMsg, TopicType.ELECTRIC_I.getInfo());
//        myMQTTClient.publish(sendMsg, TopicType.WATER_CONSUMPTION.getInfo());
//        myMQTTClient.publish(sendMsg, TopicType.ELECTRIC_P.getInfo());


//            System.out.println("时间戳" + System.currentTimeMillis());
//        }
//        System.out.println("队列元素数量：" + msgQueue.size());
    }
}

