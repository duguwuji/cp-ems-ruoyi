package com.cpems.system.config;




import com.cpems.system.domain.enums.TopicType;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * rabbitmq监听器配置
 *
 * @Author cpems
 * @Date 2023/4/11 17:30
 */
@Configuration
public class MessageListenerConfig {

    @Autowired
    private CachingConnectionFactory connectionFactory;
    @Autowired
    private MyAckReceiver myAckReceiver;//消息接收处理类

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(1);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); // RabbitMQ默认是自动确认，这里改为手动确认消息
        //设置一个队列
        //container.setQueueNames("EquipmentDataQueue");
        //如果同时设置多个如下： 前提是队列都是必须已经创建存在的
        //这里设置设备相关值的队列
          container.setQueueNames(TopicType.ELECTRIC_U.getInfo(),TopicType.ELECTRIC_I.getInfo(),TopicType.ELECTRIC_W.getInfo(),TopicType.ELECTRIC_P.getInfo(),TopicType.WATER_CONSUMPTION.getInfo());


        //另一种设置队列的方法,如果使用这种情况,那么要设置多个,就使用addQueues
        //container.setQueues(new Queue("TestDirectQueue",true));
        //container.addQueues(new Queue("TestDirectQueue2",true));
        //container.addQueues(new Queue("TestDirectQueue3",true));
        container.setMessageListener(myAckReceiver);

        return container;
    }


}
