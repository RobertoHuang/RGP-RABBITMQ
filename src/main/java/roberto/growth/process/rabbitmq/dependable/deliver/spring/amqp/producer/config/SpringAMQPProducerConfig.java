/**
 * Copyright (C), 2015-2018, ND Co., Ltd.
 * FileName: SpringAMQPProducerConfig
 * Author:   HuangTaiHong
 * Date:     2018/3/18 18:52
 * Description: SpringAQMP生产者配置类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package roberto.growth.process.rabbitmq.dependable.deliver.spring.amqp.producer.config;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br> 
 * 〈SpringAQMP生产者配置类〉
 *
 * @author HuangTaiHong
 * @create 2018/3/18
 * @since 1.0.0
 */
@Configuration
public class SpringAMQPProducerConfig {
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
        // 将CachingConnectionFactory的PublisherConfirms设置为true
        cachingConnectionFactory.setPublisherConfirms(true);
        // 将CachingConnectionFactory的PublisherReturns设置为true
        cachingConnectionFactory.setPublisherReturns(true);

        return cachingConnectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        // 设置RabbitTemplate的Mandatory属性为true
        rabbitTemplate.setMandatory(true);
        // 为RabbitTemplate设置ReturnCallback
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                try {
                    System.out.println("replyCode:" + replyCode);
                    System.out.println("replyText:" + replyText);
                    System.out.println("exchange:" + exchange);
                    System.out.println("routingKey:" + routingKey);
                    System.out.println("properties:" + message.getMessageProperties());
                    System.out.println("body:" + new String(message.getBody(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        // 为RabbitTemplate设置ConfirmCallback
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                System.out.println(ack);
                System.out.println(cause);
                System.out.println(correlationData.getId());
            }
        });

        return rabbitTemplate;
    }
}