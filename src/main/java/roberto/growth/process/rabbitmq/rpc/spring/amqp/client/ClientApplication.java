/**
 * Copyright (C), 2015-2018, ND Co., Ltd.
 * FileName: ClientApplication
 * Author:   HuangTaiHong
 * Date:     2018-03-19 下午 7:40
 * Description: 客户端启动类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package roberto.growth.process.rabbitmq.rpc.spring.amqp.client;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * 〈一句话功能简述〉<br> 
 * 〈客户端启动类〉
 *
 * @author HuangTaiHong
 * @create 2018-03-19 
 * @since 1.0.0
 */
@ComponentScan("roberto.growth.process.rabbitmq.rpc.spring.amqp.client")
public class ClientApplication {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ClientApplication.class);

        RabbitTemplate rabbitTemplate = context.getBean(RabbitTemplate.class);

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
        messageProperties.setContentType("UTF-8");
        Message message = new Message("订单信息".getBytes(), messageProperties);
        // 如果超时未返回 则messageReturn为null 可以通过rabbitTemplate.setReplyTimeout(10000);设置超时时间
        Object messageReturn = rabbitTemplate.sendAndReceive("roberto.order", "add", message, new CorrelationData("201210704116"));
        System.out.println(messageReturn);
    }
}