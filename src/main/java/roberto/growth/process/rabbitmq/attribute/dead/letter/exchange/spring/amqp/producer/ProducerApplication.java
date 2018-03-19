/**
 * Copyright (C), 2015-2018, ND Co., Ltd.
 * FileName: ProducerApplication
 * Author:   HuangTaiHong
 * Date:     2018-03-19 下午 1:57
 * Description: 生产者启动类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package roberto.growth.process.rabbitmq.attribute.dead.letter.exchange.spring.amqp.producer;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.HashMap;

/**
 * 〈一句话功能简述〉<br> 
 * 〈生产者启动类〉
 *
 * @author HuangTaiHong
 * @create 2018-03-19 
 * @since 1.0.0
 */
@ComponentScan(basePackages = "roberto.growth.process.rabbitmq.attribute.dead.letter.exchange.spring.amqp.producer")
public class ProducerApplication {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(roberto.growth.process.rabbitmq.dependable.consumer.spring.amqp.producer.ProducerApplication.class);

        RabbitAdmin rabbitAdmin = context.getBean(RabbitAdmin.class);
        RabbitTemplate rabbitTemplate = context.getBean(RabbitTemplate.class);

        // 声明AE 类型为Fanout
        rabbitAdmin.declareExchange(new FanoutExchange("roberto.order.failure", true, false, new HashMap<>()));

        // 为roberto.order设置AE
        rabbitAdmin.declareExchange(new DirectExchange("roberto.order", true, false, new HashMap<>()));

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
        messageProperties.setContentType("UTF-8");
        Message message = new Message("订单信息".getBytes(), messageProperties);

        rabbitTemplate.send("roberto.order", "add", message, new CorrelationData("201210704116"));
    }
}