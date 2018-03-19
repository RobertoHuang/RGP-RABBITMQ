/**
 * Copyright (C), 2015-2018, ND Co., Ltd.
 * FileName: RabbitMQProducerConfig
 * Author:   HuangTaiHong
 * Date:     2018-03-19 下午 8:16
 * Description: RabbitMQ生产者配置类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package roberto.growth.process.rabbitmq.spring.boot.producer.config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * 〈一句话功能简述〉<br> 
 * 〈RabbitMQ生产者配置类〉
 *
 * @author HuangTaiHong
 * @create 2018-03-19 
 * @since 1.0.0
 */
@Configuration
public class RabbitMQProducerConfig {
    @Bean
    public Exchange exchange() {
        return new DirectExchange("roberto.order", true, false, new HashMap<>());
    }
}