/**
 * Copyright (C), 2015-2018, ND Co., Ltd.
 * FileName: ConsumerApplication
 * Author:   HuangTaiHong
 * Date:     2018-03-19 上午 10:35
 * Description: 消费者启动类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package roberto.growth.process.rabbitmq.attribute.alternate.exchange.spring.amqp.consumer;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
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
@ComponentScan(basePackages = "roberto.growth.process.rabbitmq.attribute.alternate.exchange.spring.amqp.consumer")
public class ConsumerApplication {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConsumerApplication.class);

        RabbitAdmin rabbitAdmin = context.getBean(RabbitAdmin.class);
        MessageListenerContainer messageListenerContainer = context.getBean("messageListenerContainer", MessageListenerContainer.class);
        MessageListenerContainer messageListenerContainer2 = context.getBean("messageListenerContainer2", MessageListenerContainer.class);

        rabbitAdmin.declareQueue(new Queue("roberto.order.add", true, false, false, new HashMap<>()));
        // 声明AE 类型为Fanout
        rabbitAdmin.declareExchange(new FanoutExchange("roberto.order.failure", true, false, new HashMap<>()));
        // 为roberto.order设置AE
        Map<String, Object> exchangeProperties = new HashMap<>();
        exchangeProperties.put("alternate-exchange", "roberto.order.failure");
        rabbitAdmin.declareExchange(new DirectExchange("roberto.order", true, false, exchangeProperties));
        rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue("roberto.order.add")).to(new DirectExchange("roberto.order")).with("add"));

        // 将roberto.order.add.failure队列绑定到roberto.order.failure交换机上 无需指定routing key
        rabbitAdmin.declareQueue(new Queue("roberto.order.add.failure", true, false, false, new HashMap<>()));
        rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue("roberto.order.add.failure")).to(new DirectExchange("roberto.order.failure")).with(""));

        messageListenerContainer.start();
        messageListenerContainer2.start();
    }
}