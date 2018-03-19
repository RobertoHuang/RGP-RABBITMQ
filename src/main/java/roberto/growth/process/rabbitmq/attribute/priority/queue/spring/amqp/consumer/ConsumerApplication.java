/**
 * Copyright (C), 2015-2018, ND Co., Ltd.
 * FileName: ConsumerApplication
 * Author:   HuangTaiHong
 * Date:     2018-03-19 下午 2:27
 * Description: 消费者启动类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package roberto.growth.process.rabbitmq.attribute.priority.queue.spring.amqp.consumer;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.HashMap;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br> 
 * 〈消费者启动类〉
 *
 * @author HuangTaiHong
 * @create 2018-03-19 
 * @since 1.0.0
 */
@ComponentScan(basePackages = "roberto.growth.process.rabbitmq.attribute.priority.queue.spring.amqp.consumer")
public class ConsumerApplication {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConsumerApplication.class);

        RabbitAdmin rabbitAdmin = context.getBean(RabbitAdmin.class);
        MessageListenerContainer messageListenerContainer = context.getBean("messageListenerContainer", MessageListenerContainer.class);

        // 将roberto.order.add队列绑定到roberto.order交换机上 routing key为add
        Map<String, Object> queueProperties = new HashMap<>();
        queueProperties.put("x-max-priority", 10);
        rabbitAdmin.declareQueue(new Queue("roberto.order.add", true, false, false, queueProperties));
        rabbitAdmin.declareExchange(new DirectExchange("roberto.order", true, false, new HashMap<>()));
        rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue("roberto.order.add")).to(new DirectExchange("roberto.order")).with("add"));

        messageListenerContainer.start();
    }
}