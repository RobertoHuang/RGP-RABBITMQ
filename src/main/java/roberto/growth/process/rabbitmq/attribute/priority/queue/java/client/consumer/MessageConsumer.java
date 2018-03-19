/**
 * Copyright (C), 2015-2018, ND Co., Ltd.
 * FileName: MessageConsumer
 * Author:   HuangTaiHong
 * Date:     2018-03-19 下午 2:21
 * Description: 消息消费者
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package roberto.growth.process.rabbitmq.attribute.priority.queue.java.client.consumer;

import com.rabbitmq.client.*;
import roberto.growth.process.rabbitmq.attribute.priority.queue.java.client.utils.ChannelUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 〈一句话功能简述〉<br> 
 * 〈消息消费者〉
 *
 * @author HuangTaiHong
 * @create 2018-03-19 
 * @since 1.0.0
 */
public class MessageConsumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = ChannelUtils.getChannel("RGP订单系统消息消费者");
        channel.exchangeDeclare("roberto.order", BuiltinExchangeType.TOPIC, true, false, false, new HashMap<>());

        // 将roberto.order.add队列绑定到roberto.order交换机上 routing key为add
        Map<String, Object> queueProperties = new HashMap<>();
        queueProperties.put("x-max-priority", 10);
        AMQP.Queue.DeclareOk declareOk = channel.queueDeclare("roberto.order.add", true, false, false, queueProperties);
        channel.queueBind(declareOk.getQueue(), "roberto.order", "add", new HashMap<>());

        // 消费roberto.order.add队列
        channel.basicConsume(declareOk.getQueue(), false, "RGP订单系统ADD处理逻辑消费者", new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    Thread.sleep(3000);
                    System.out.println("----------roberto.order.add----------");
                    System.out.println(new String(body, "UTF-8"));
                    channel.basicAck(envelope.getDeliveryTag(), false);
                } catch (Exception e) {
                    channel.basicNack(envelope.getDeliveryTag(), false, true);
                }
            }
        });
    }
}