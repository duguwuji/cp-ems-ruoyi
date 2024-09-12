package com.cpems.system.config;

import com.cpems.system.domain.enums.TopicType;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * rabbitmq交换机及各队列配置
 *
 * @Author cpems
 * @Date 2023/4/11 15:28
 */
@Configuration
public class RabbitExChangeConfig {
//
//    //队列 起名：TestDirectQueue
//    @Bean
//    public Queue EquipmentDataQueue() {
//        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
//        // exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
//        // autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
//        //   return new Queue("TestDirectQueue",true,true,false);
//
//        //一般设置一下队列的持久化就好,其余两个就是默认false
//        return new Queue("EquipmentDataQueue",true);
//    }

    //这里给定数据的各个队列
    @Bean
    public Queue UQueue() {
        return new Queue(TopicType.ELECTRIC_U.getInfo());
    }

    @Bean
    public Queue IQueue() {
        return new Queue(TopicType.ELECTRIC_I.getInfo());
    }
    @Bean
    public Queue PQueue() {
        return new Queue(TopicType.ELECTRIC_P.getInfo());
    }
    @Bean
    public Queue WQueue() {
        return new Queue(TopicType.ELECTRIC_W.getInfo());
    }

    @Bean
    public Queue WaterQueue() {
        return new Queue(TopicType.WATER_CONSUMPTION.getInfo());
    }

    //topic交换机配置
    @Bean
    TopicExchange EquipmentDataExchange() {
        return new TopicExchange("EquipmentTopicExchange",true,false);
    }

    //绑定  将队列和交换机绑定, 并设置用于匹配的各个routing为各自的主题
    @Bean
    Binding UExchangeMessage() {
        return BindingBuilder.bind(UQueue()).to(EquipmentDataExchange()).with(TopicType.ELECTRIC_U.getInfo());
    }
    @Bean
    Binding IExchangeMessage() {
        return BindingBuilder.bind(IQueue()).to(EquipmentDataExchange()).with(TopicType.ELECTRIC_I.getInfo());
    }
    @Bean
    Binding PExchangeMessage() {
        return BindingBuilder.bind(WQueue()).to(EquipmentDataExchange()).with(TopicType.ELECTRIC_W.getInfo());
    }
    @Bean
    Binding WExchangeMessage() {
        return BindingBuilder.bind(PQueue()).to(EquipmentDataExchange()).with(TopicType.ELECTRIC_P.getInfo());
    }

    @Bean
    Binding WaterExchangeMessage() {
        return BindingBuilder.bind(WaterQueue()).to(EquipmentDataExchange()).with(TopicType.WATER_CONSUMPTION.getInfo());
    }



/*
    @Bean
    DirectExchange lonelyDirectExchange() {
        return new DirectExchange("lonelyDirectExchange");
    }

*/


}
