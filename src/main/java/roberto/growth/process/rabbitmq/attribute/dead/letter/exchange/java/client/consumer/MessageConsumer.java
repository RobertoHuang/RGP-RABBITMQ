/**
 * Copyright (C), 2015-2018, ND Co., Ltd.
 * FileName: MessageConsumer
 * Author:   HuangTaiHong
 * Date:     2018-03-19 下午 1:44
 * Description: 消息消费者
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package roberto.growth.process.rabbitmq.attribute.dead.letter.exchange.java.client.consumer;

import com.rabbitmq.client.*;
import roberto.growth.process.rabbitmq.attribute.dead.letter.exchange.java.client.utils.ChannelUtils;

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

        // 声明Dead Letter Exchange
        channel.exchangeDeclare("roberto.order.failure", BuiltinExchangeType.FANOUT, true, false, false, new HashMap<>());
        channel.exchangeDeclare("roberto.order", BuiltinExchangeType.DIRECT, true, false, false, new HashMap<>());

        // 声明队列并 指定x-dead-letter-exchange
        Map<String, Object> queueProperties = new HashMap<>();
        queueProperties.put("x-dead-letter-exchange","roberto.order.failure");
        AMQP.Queue.DeclareOk declareOk = channel.queueDeclare("roberto.order.add", true, false, false, queueProperties);

        channel.queueBind(declareOk.getQueue(), "roberto.order", "add", new HashMap<>());

        // 将roberto.order.add.failure队列绑定到roberto.order.failure交换机上 无需指定routing key
        AMQP.Queue.DeclareOk declareOk2 = channel.queueDeclare("roberto.order.add.failure", true, false, false, new HashMap<>());
        channel.queueBind(declareOk2.getQueue(), "roberto.order.failure", "", new HashMap<>());

        // 消费roberto.order.add队列
        channel.basicConsume(declareOk.getQueue(), false, "RGP订单系统ADD处理逻辑消费者", new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    System.out.println("----------roberto.order.add----------");
                    System.out.println(new String(body, "UTF-8"));
                    System.out.println("roberto.order.add将消息拒绝");
                    channel.basicNack(envelope.getDeliveryTag(), false,false);
                } catch (Exception e) {
                    channel.basicNack(envelope.getDeliveryTag(), false, true);
                }
            }
        });

        // 消费roberto.order.add.failure队列
        channel.basicConsume(declareOk2.getQueue(), false, "RGP订单系统ADD FAILURE处理逻辑消费者", new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    System.out.println("----------roberto.order.add.failure----------");
                    System.out.println(new String(body, "UTF-8"));
                    channel.basicAck(envelope.getDeliveryTag(), false);
                } catch (Exception e) {
                    channel.basicNack(envelope.getDeliveryTag(), false, true);
                }
            }
        });
    }
}