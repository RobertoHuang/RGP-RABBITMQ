/**
 * Copyright (C), 2015-2018, ND Co., Ltd.
 * FileName: Application2
 * Author:   HuangTaiHong
 * Date:     2018-03-16 下午 3:33
 * Description: 消费者启动类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package roberto.growth.process.rabbitmq.spring.amqp.manual.declare.consumer;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.HashMap;

/**
 * 〈一句话功能简述〉<br> 
 * 〈消费者启动类〉
 *
 * @author HuangTaiHong
 * @create 2018-03-16 
 * @since 1.0.0
 */
@ComponentScan(basePackages = "roberto.growth.process.rabbitmq.spring.amqp.manual.declare.consumer")
public class ConsumerApplication {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConsumerApplication.class);

        RabbitAdmin rabbitAdmin = context.getBean(RabbitAdmin.class);
        MessageListenerContainer messageListenerContainer = context.getBean(MessageListenerContainer.class);

        // 声明队列 (队列名", 是否持久化, 是否排他, 是否自动删除, 队列属性);
        rabbitAdmin.declareQueue(new Queue("roberto.order.add", true, false, false, new HashMap<>()));

        // 声明Direct Exchange (交换机名, 是否持久化, 是否自动删除, 交换机属性);
        rabbitAdmin.declareExchange(new DirectExchange("roberto.order", true, false, new HashMap<>()));

        // 将队列Binding到交换机上 Routing key为add
        rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue("roberto.order.add")).to(new DirectExchange("roberto.order")).with("add"));

        // 开始监听队列
        messageListenerContainer.start();
    }
}