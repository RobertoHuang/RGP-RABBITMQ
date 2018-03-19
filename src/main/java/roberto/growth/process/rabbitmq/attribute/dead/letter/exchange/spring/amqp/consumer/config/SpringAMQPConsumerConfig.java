/**
 * Copyright (C), 2015-2018, ND Co., Ltd.
 * FileName: SpringAMQPConsumerConfig
 * Author:   HuangTaiHong
 * Date:     2018-03-19 下午 1:57
 * Description: SpringAMQP消费者配置类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package roberto.growth.process.rabbitmq.attribute.dead.letter.exchange.spring.amqp.consumer.config;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈SpringAMQP消费者配置类〉
 *
 * @author HuangTaiHong
 * @create 2018-03-19
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
    public List<Queue> queueList() {
        // 声明队列并 指定x-dead-letter-exchange
        Map<String, Object> queueProperties = new HashMap<>();
        queueProperties.put("x-dead-letter-exchange", "roberto.order.failure");
        Queue queue = new Queue("roberto.order.add", true, false, false, queueProperties);

        Queue queue2 = new Queue("roberto.order.add.failure", true, false, false, new HashMap<>());
        return Arrays.asList(queue, queue2);
    }

    @Bean
    public List<Exchange> exchangeList() {
        // 声明Dead Letter Exchange
        FanoutExchange fanoutExchange = new FanoutExchange("roberto.order.failure", true, false, new HashMap<>());
        DirectExchange directExchange = new DirectExchange("roberto.order", true, false, new HashMap<>());
        return Arrays.asList(fanoutExchange, directExchange);
    }

    @Bean
    public List<Binding> bindingList() {
        Binding binding = BindingBuilder.bind(new Queue("roberto.order.add")).to(new DirectExchange("roberto.order")).with("add");
        Binding binding2 = BindingBuilder.bind(new Queue("roberto.order.add.failure")).to(new DirectExchange("roberto.order.failure")).with("");
        return Arrays.asList(binding, binding2);
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

        messageListenerContainer.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        messageListenerContainer.setMessageListener(new ChannelAwareMessageListener() {
            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
                System.out.println("----------roberto.order.add----------");
                System.out.println(new String(message.getBody(), "UTF-8"));
                System.out.println("roberto.order.add将消息拒绝");
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            }
        });

        return messageListenerContainer;
    }

    @Bean
    public MessageListenerContainer messageListenerContainer2(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer messageListenerContainer = new SimpleMessageListenerContainer();
        messageListenerContainer.setConnectionFactory(connectionFactory);
        messageListenerContainer.setQueueNames("roberto.order.add.failure");

        messageListenerContainer.setConcurrentConsumers(5);
        messageListenerContainer.setMaxConcurrentConsumers(10);

        Map<String, Object> argumentMap = new HashMap();
        messageListenerContainer.setConsumerArguments(argumentMap);
        messageListenerContainer.setConsumerTagStrategy(new ConsumerTagStrategy() {
            @Override
            public String createConsumerTag(String s) {
                return "RGP订单系统ADD FAILURE处理逻辑消费者";
            }
        });

        messageListenerContainer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    System.out.println("----------roberto.order.add.failure----------");
                    System.out.println(new String(message.getBody(), "UTF-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return messageListenerContainer;
    }
}