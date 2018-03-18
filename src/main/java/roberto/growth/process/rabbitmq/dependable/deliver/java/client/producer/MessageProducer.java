/**
 * Copyright (C), 2015-2018, ND Co., Ltd.
 * FileName: MessageProducer
 * Author:   HuangTaiHong
 * Date:     2018/3/18 17:01
 * Description: 消息生产者
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package roberto.growth.process.rabbitmq.dependable.deliver.java.client.producer;

import com.rabbitmq.client.*;
import roberto.growth.process.rabbitmq.dependable.deliver.java.client.utils.ChannelUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

/**
 * 〈一句话功能简述〉<br> 
 * 〈消息生产者〉
 *
 * @author HuangTaiHong
 * @create 2018/3/18
 * @since 1.0.0
 */
public class MessageProducer {
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = ChannelUtils.getChannelInstance("RGP订单系统消息生产者");

        channel.exchangeDeclare("roberto.order", BuiltinExchangeType.DIRECT, true, false, false,new HashMap<>());

        AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder().deliveryMode(2).contentType("UTF-8").build();

        // 当消息没有被正确路由时 回调ReturnListener
        channel.addReturnListener(new ReturnListener() {
            @Override
            public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("replyCode:" + replyCode);
                System.out.println("replyText:" + replyText);
                System.out.println("exchange:" + exchange);
                System.out.println("routingKey:" + routingKey);
                System.out.println("properties:" + properties);
                System.out.println("body:" + new String(body, "UTF-8"));
            }
        });

        // 开启消息确认
        channel.confirmSelect();
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("----------Ack----------");
                System.out.println(deliveryTag);
                System.out.println(multiple);
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("----------Nack----------");
                System.out.println(deliveryTag);
                System.out.println(multiple);
            }
        });

        // 将mandatory属性设置成true
        channel.basicPublish("roberto.order", "add", true, basicProperties, "订单信息".getBytes());
        channel.basicPublish("roberto.order", "addXXX", true, basicProperties, "订单信息".getBytes());
    }
}