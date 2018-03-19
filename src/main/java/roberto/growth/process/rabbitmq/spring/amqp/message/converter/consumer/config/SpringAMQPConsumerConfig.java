/**
 * Copyright (C), 2015-2018, ND Co., Ltd.
 * FileName: SpringAMQPConsumerConfig
 * Author:   HuangTaiHong
 * Date:     2018-03-16 下午 5:38
 * Description: SpringAMP消费者配置
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package roberto.growth.process.rabbitmq.spring.amqp.message.converter.consumer.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import roberto.growth.process.rabbitmq.spring.amqp.message.converter.consumer.converter.FileMessageConverter;
import roberto.growth.process.rabbitmq.spring.amqp.message.converter.consumer.converter.StringMessageConverter;
import roberto.growth.process.rabbitmq.spring.amqp.message.converter.consumer.entity.Order;
import roberto.growth.process.rabbitmq.spring.amqp.message.converter.consumer.handle.MessageHandle;

import java.util.HashMap;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br> 
 * 〈SpringAMP消费者配置〉
 *
 * @author HuangTaiHong
 * @create 2018-03-16 
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

        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(new MessageHandle());
        messageListenerAdapter.setDefaultListenerMethod("handleMessage");
        Map<String, String> queueOrTagToMethodName = new HashMap<>();
        queueOrTagToMethodName.put("roberto.order.add","add");
        messageListenerAdapter.setQueueOrTagToMethodName(queueOrTagToMethodName);
        messageListenerContainer.setMessageListener(messageListenerAdapter);

        // 设置消息转换器
        ContentTypeDelegatingMessageConverter converter = new ContentTypeDelegatingMessageConverter();
        StringMessageConverter stringMessageConverter = new StringMessageConverter();
        FileMessageConverter fileMessageConverter = new FileMessageConverter();
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("order", Order.class);
        DefaultJackson2JavaTypeMapper javaTypeMapper = new DefaultJackson2JavaTypeMapper();
        javaTypeMapper.setIdClassMapping(idClassMapping);
        jackson2JsonMessageConverter.setJavaTypeMapper(javaTypeMapper);

        // 设置text/html text/plain 使用StringMessageConverter
        converter.addDelegate("text/html", stringMessageConverter);
        converter.addDelegate("text/plain", stringMessageConverter);
        // 设置application/json 使用Jackson2JsonMessageConverter
        converter.addDelegate("application/json", jackson2JsonMessageConverter);
        // 设置image/jpg image/png 使用FileMessageConverter
        converter.addDelegate("image/jpg", fileMessageConverter);
        converter.addDelegate("image/png", fileMessageConverter);
        messageListenerAdapter.setMessageConverter(converter);

        return messageListenerContainer;
    }
}