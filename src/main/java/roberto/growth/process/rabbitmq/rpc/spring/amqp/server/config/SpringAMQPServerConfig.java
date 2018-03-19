/**
 * Copyright (C), 2015-2018, ND Co., Ltd.
 * FileName: SpringAMQPServerConfig
 * Author:   HuangTaiHong
 * Date:     2018-03-19 下午 7:31
 * Description: SpringAMQP Server端配置
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package roberto.growth.process.rabbitmq.rpc.spring.amqp.server.config;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import roberto.growth.process.rabbitmq.rpc.spring.amqp.server.rpc.RPCMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈SpringAMQP Server端配置〉
 *
 * @author HuangTaiHong
 * @create 2018-03-19
 * @since 1.0.0
 */
@Configuration
public class SpringAMQPServerConfig {
    @Bean
    public CachingConnectionFactory connectionFactory() {
        ConnectionFactory connectionFactory = new ConnectionFactory();

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
    public RabbitAdmin rabbitAdmin(CachingConnectionFactory cachingConnectionFactory) {
        return new RabbitAdmin(cachingConnectionFactory);
    }

    @Bean
    public Exchange exchange() {
        return new DirectExchange("roberto.order", true, false, new HashMap<>());
    }

    @Bean
    public Queue queue() {
        Queue queue = new Queue("roberto.order.add", true, false, false, new HashMap<>());
        return queue;
    }

    @Bean
    public Binding binding() {
        Binding binding = new Binding("roberto.order.add", Binding.DestinationType.QUEUE, "roberto.order", "add", new HashMap<>());
        return binding;
    }

    @Bean
    public MessageListenerContainer messageListenerContainer(CachingConnectionFactory cachingConnectionFactory) {
        SimpleMessageListenerContainer messageListenerContainer = new SimpleMessageListenerContainer();
        messageListenerContainer.setConnectionFactory(cachingConnectionFactory);
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

        RPCMethod rpcMethod = new RPCMethod();
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(rpcMethod);
        messageListenerAdapter.setDefaultListenerMethod("addOrder");

        messageListenerContainer.setMessageListener(messageListenerAdapter);
        return messageListenerContainer;
    }
}