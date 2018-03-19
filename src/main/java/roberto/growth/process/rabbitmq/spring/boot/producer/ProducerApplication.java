/**
 * Copyright (C), 2015-2018, ND Co., Ltd.
 * FileName: ProducerApplication
 * Author:   HuangTaiHong
 * Date:     2018-03-19 下午 8:16
 * Description: 生产者启动类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package roberto.growth.process.rabbitmq.spring.boot.producer;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 〈一句话功能简述〉<br> 
 * 〈生产者启动类〉
 *
 * @author HuangTaiHong
 * @create 2018-03-19 
 * @since 1.0.0
 */
@SpringBootApplication
public class ProducerApplication {
    private static RabbitTemplate rabbitTemplate;

    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        ProducerApplication.rabbitTemplate = rabbitTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(ProducerApplication.class);
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
        messageProperties.setContentType("UTF-8");
        Message message = new Message("订单信息".getBytes(), messageProperties);
        rabbitTemplate.send("roberto.order", "add", message);
    }
}