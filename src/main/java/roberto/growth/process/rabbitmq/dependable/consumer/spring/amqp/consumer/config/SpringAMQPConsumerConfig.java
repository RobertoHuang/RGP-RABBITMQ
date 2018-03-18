/**
 * Copyright (C), 2015-2018, ND Co., Ltd.
 * FileName: SpringAMQPConsumerConfig
 * Author:   HuangTaiHong
 * Date:     2018/3/18 21:07
 * Description: SpringAMQP消费者配置类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package roberto.growth.process.rabbitmq.dependable.consumer.spring.amqp.consumer.config;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br> 
 * 〈SpringAMQP消费者配置类〉
 *
 * @author HuangTaiHong
 * @create 2018/3/18
 * @since 1.0.0
 */
@Configuration
public class SpringAMQPConsumerConfig {
    @Bean
    public ConnectionFactory connectionFactory() {
        com.rabbitmq.client.ConnectionFactory connectionFactory = new com.rabbitmq.client.ConnectionFactory();

        connectionFactory.setHost("192.168.56.128");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("roberto");
        connectionFactory.setPassword("roberto");

        connectionFactory.setAutomaticRecoveryEnabled(true);
        connectionFactory.setNetworkRecoveryInterval(10000);

        Map<String, Object> connectionFactoryPropertiesMap = new HashMap();
        connectionFactoryPropertiesMap.put("principal", "RobertoHuang");
        connectionFactoryPropertiesMap.put("description", "RGP订单系统V2.0");
        connectionFactoryPropertiesMap.put("emailAddress", "RobertoHuang@foxmail.com");
        connectionFactory.setClientProperties(connectionFactoryPropertiesMap);

        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(connectionFactory);
        return cachingConnectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
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

        messageListenerContainer.setAutoStartup(false);
        messageListenerContainer.setMessageListener(new ChannelAwareMessageListener() {
            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
                try {
                    System.out.println(new String(message.getBody(), "UTF-8"));
                    System.out.println(message.getMessageProperties());
                    if ("订单信息2".equals(new String(message.getBody(), "UTF-8"))) {
                        throw new RuntimeException();
                    } else {
                        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                    }
                } catch (Exception e) {
                    channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
                }
            }
        });
        // 设置消息确认模式为手动模式
        messageListenerContainer.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return messageListenerContainer;
    }
}