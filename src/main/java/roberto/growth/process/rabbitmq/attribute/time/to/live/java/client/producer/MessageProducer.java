/**
 * Copyright (C), 2015-2018, ND Co., Ltd.
 * FileName: MessageProducer
 * Author:   HuangTaiHong
 * Date:     2018-03-19 上午 11:28
 * Description: 消息生产者
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package roberto.growth.process.rabbitmq.attribute.time.to.live.java.client.producer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import roberto.growth.process.rabbitmq.attribute.time.to.live.java.client.utils.ChannelUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 〈一句话功能简述〉<br>
 * 〈消息生产者〉
 *
 * @author HuangTaiHong
 * @create 2018-03-19
 * @since 1.0.0
 */
public class MessageProducer {
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = ChannelUtils.getChannelInstance("RGP订单系统消息生产者");

        // 设置队列消息过期时间为10S
        Map<String, Object> queueProperties = new HashMap<>();
        queueProperties.put("x-message-ttl", 10000);
        channel.queueDeclare("roberto.order.add", true, false, false, queueProperties);
        channel.exchangeDeclare("roberto.order", BuiltinExchangeType.DIRECT, true, false, false, new HashMap<>());
        channel.queueBind("roberto.order.add", "roberto.order", "add", new HashMap<>());

        AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder().deliveryMode(2).contentType("UTF-8").build();
        channel.basicPublish("roberto.order", "add", false, basicProperties, "订单信息".getBytes());

        // 设置消息过期时间为3S
        AMQP.BasicProperties basicProperties2 = new AMQP.BasicProperties().builder().deliveryMode(2).contentType("UTF-8").expiration("3000").build();
        channel.basicPublish("roberto.order", "add", false, basicProperties2, "订单信息".getBytes());
    }
}