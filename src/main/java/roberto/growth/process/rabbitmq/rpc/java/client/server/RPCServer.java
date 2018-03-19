/**
 * Copyright (C), 2015-2018, ND Co., Ltd.
 * FileName: RPCServer
 * Author:   HuangTaiHong
 * Date:     2018-03-19 下午 7:08
 * Description: RPC服务提供
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package roberto.growth.process.rabbitmq.rpc.java.client.server;

import com.rabbitmq.client.*;
import roberto.growth.process.rabbitmq.rpc.java.client.utils.ChannelUtils;

import java.io.IOException;
import java.util.HashMap;

/**
 * 〈一句话功能简述〉<br>
 * 〈RPC服务提供〉
 *
 * @author HuangTaiHong
 * @create 2018-03-19
 * @since 1.0.0
 */
public class RPCServer {
    public static void main(String[] args) throws IOException {
        Channel channel = ChannelUtils.getChannel("RGP订单系统Server端");

        channel.queueDeclare("roberto.order.add", true, false, false, new HashMap<>());
        channel.exchangeDeclare("roberto.order", BuiltinExchangeType.DIRECT, true, false, false, new HashMap<>());

        channel.basicConsume("roberto.order.add", true, "RGP订单系统Server端", new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String replyTo = properties.getReplyTo();
                String correlationId = properties.getCorrelationId();

                System.out.println("----------收到RPC调用请求消息----------");
                System.out.println(consumerTag);
                System.out.println("消息属性为:" + properties);
                System.out.println("消息内容为" + new String(body));
                try {
                    String orderId = RPCMethod.addOrder(new String(body));
                    AMQP.BasicProperties replyProperties = new AMQP.BasicProperties().builder().deliveryMode(2).contentType("UTF-8").correlationId(correlationId).build();
                    channel.basicPublish("", replyTo, replyProperties, orderId.getBytes());
                    System.out.println("----------RPC调用成功 结果已返回----------");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}