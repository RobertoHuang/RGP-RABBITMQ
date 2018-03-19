/**
 * Copyright (C), 2015-2018, ND Co., Ltd.
 * FileName: SpringAMQPClientConfig
 * Author:   HuangTaiHong
 * Date:     2018-03-19 下午 7:39
 * Description: SpringAMQP客户端配置类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package roberto.growth.process.rabbitmq.rpc.spring.amqp.client.config;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br> 
 * 〈SpringAMQP客户端配置类〉
 *
 * @author HuangTaiHong
 * @create 2018-03-19 
 * @since 1.0.0
 */
@Configuration
public class SpringAMQPClientConfig {
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
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory cachingConnectionFactory) {
        return new RabbitTemplate(cachingConnectionFactory);
    }

    @Bean
    public Exchange exchange() {
        return new DirectExchange("roberto.order", true, false, new HashMap<>());
    }
}