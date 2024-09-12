package com.cpems.system.config;

/**
 * 直接回调法(无消息确认)
 *
 * @Author cpems
 * @Date 2023/4/11 15:41
 */
/*
@Component
@RabbitListener(queues = "TestDirectQueue")//监听的队列名称 TestDirectQueue
public class DirectReceiver {

    @RabbitHandler
    public void process(Map testMessage) {
        System.out.println("DirectReceiver消费者收到消息  : " + testMessage.toString());
        RedisUtils.setCacheMap("TestDirectQueue",testMessage);
    }

}
*/
