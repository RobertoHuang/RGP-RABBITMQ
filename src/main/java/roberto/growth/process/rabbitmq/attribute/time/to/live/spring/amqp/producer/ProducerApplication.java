/**
 * Copyright (C), 2015-2018, ND Co., Ltd.
 * FileName: ProducerApplication
 * Author:   HuangTaiHong
 * Date:     2018-03-19 上午 11:44
 * Description: 生产者启动类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package roberto.growth.process.rabbitmq.attribute.time.to.live.spring.amqp.producer;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.HashMap;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br> 
 * 〈生产者启动类〉
 *
 * @author HuangTaiHong
 * @create 2018-03-19 
 * @since 1.0.0
 */
@ComponentScan(basePackages = "roberto.growth.process.rabbitmq.attribute.time.to.live.spring.amqp.producer")
public class ProducerApplication {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ProducerApplication.class);

        RabbitAdmin rabbitAdmin = context.getBean(RabbitAdmin.class);
        RabbitTemplate rabbitTemplate = context.getBean(RabbitTemplate.class);

        // 设置队列消息过期时间为10S
        Map<String, Object> queueProperties = new HashMap<>();
        queueProperties.put("x-message-ttl", 10000);
        rabbitAdmin.declareQueue(new Queue("roberto.order.add", true, false, false, queueProperties));
        rabbitAdmin.declareExchange(new DirectExchange("roberto.order", true, false, new HashMap<>()));
        rabbitAdmin.declareBinding(new Binding("roberto.order.add", Binding.DestinationType.QUEUE, "roberto.order", "add", new HashMap<>()));

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
        messageProperties.setContentType("UTF-8");
        Message message = new Message("订单信息".getBytes(), messageProperties);
        rabbitTemplate.send("roberto.order", "add", message, new CorrelationData("201210704116"));

        MessageProperties messageProperties2 = new MessageProperties();
        messageProperties2.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
        messageProperties2.setContentType("UTF-8");
        messageProperties2.setExpiration("3000");
        Message message2 = new Message("订单信息".getBytes(), messageProperties2);
        rabbitTemplate.send("roberto.order", "add", message2, new CorrelationData("201210704116"));
    }
}