/**
 * Copyright (C), 2015-2018, ND Co., Ltd.
 * FileName: SpringAMQPMessageHandle
 * Author:   HuangTaiHong
 * Date:     2018-03-16 下午 7:06
 * Description: 消息消费处理器
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package roberto.growth.process.rabbitmq.spring.amqp.annotation.consumer.config;

import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

/**
 * 〈一句话功能简述〉<br>
 * 〈消息消费处理器〉
 *
 * @author HuangTaiHong
 * @create 2018-03-16
 * @since 1.0.0
 */
@Component
@RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "roberto.order.add", durable = "true", autoDelete = "false", exclusive = "false"), exchange = @Exchange(name = "roberto.order"))})
public class SpringAMQPMessageHandle {
    @RabbitHandler
    public void add(byte[] body) {
        System.out.println("----------byte[]方法进行处理----------");
        try {
            System.out.println(new String(body, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}