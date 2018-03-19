/**
 * Copyright (C), 2015-2018, ND Co., Ltd.
 * FileName: RabbitMQConsumerConfig
 * Author:   HuangTaiHong
 * Date:     2018-03-19 下午 8:11
 * Description: RabbitMQ消费者配置类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package roberto.growth.process.rabbitmq.spring.boot.consumer.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br> 
 * 〈RabbitMQ消费者配置类〉
 *
 * @author HuangTaiHong
 * @create 2018-03-19 
 * @since 1.0.0
 */
@Configuration
public class RabbitMQConsumerConfig {
    @Bean
    public Exchange exchange() {
        return new DirectExchange("roberto.order", true, false, new HashMap<>());
    }

    @Bean
    public Queue queue() {
        return new Queue("roberto.order.add", true, false, false, new HashMap<>());
    }

    @Bean
    public Binding binding() {
        return new Binding("roberto.order.add", Binding.DestinationType.QUEUE, "roberto.order", "add", new HashMap<>());
    }

    @Bean
    public MessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer messageListenerContainer = new SimpleMessageListenerContainer();
        messageListenerContainer.setConnectionFactory(connectionFactory);
        messageListenerContainer.setQueueNames("roberto.order.add");

        messageListenerContainer.setConcurrentConsumers(5);
        messageListenerContainer.setMaxConcurrentConsumers(10);

        Map<String, Object> argumentMap = new HashMap();
        messageListenerContainer.setConsumerArguments(argumentMap);
        messageListenerContainer.setConsumerTagStrategy(new ConsumerTagStrategy() {
            @Override
            public String createConsumerTag(String s) {
                return "RGP订单系统ADD处理逻辑消费者";
            }
        });

        messageListenerContainer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    System.out.println(new String(message.getBody(), "UTF-8"));
                    System.out.println(message.getMessageProperties());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return messageListenerContainer;
    }
}