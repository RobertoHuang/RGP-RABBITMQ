/**
 * Copyright (C), 2015-2018, ND Co., Ltd.
 * FileName: MessageProducer
 * Author:   HuangTaiHong
 * Date:     2018-03-19 上午 11:55
 * Description: 消息生产者
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package roberto.growth.process.rabbitmq.attribute.length.limit.java.client.producer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import roberto.growth.process.rabbitmq.attribute.length.limit.java.client.utils.ChannelUtils ;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br> 
 * 〈消息生产者〉
 *
 * @author HuangTaiHong
 * @create 2018-03-19 
 * @since 1.0.0
 */
public class MessageProducer {
    public static void main(String[] args) throws IOException {
        Channel channel = ChannelUtils.getChannelInstance("RGP订单系统消息生产者");

        // 设置队列可存放最大消息数量为2条
        Map<String, Object> queueProperties = new HashMap<>();
        queueProperties.put("x-max-length", 2);
        // 设置队列可存放消息内容的最大字节长度为20
        // queueProperties2.put("x-max-length-bytes", 20);
        channel.queueDeclare("roberto.order.add", true, false, false, queueProperties);
        channel.exchangeDeclare("roberto.order", BuiltinExchangeType.DIRECT, true, false, false, new HashMap<>());
        channel.queueBind("roberto.order.add", "roberto.order", "add", new HashMap<>());

        AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder().deliveryMode(2).contentType("UTF-8").build();
        channel.basicPublish("roberto.order", "add", false, basicProperties, "订单信息".getBytes());
        channel.basicPublish("roberto.order", "add", false, basicProperties, "订单信息2".getBytes());
        channel.basicPublish("roberto.order", "add", false, basicProperties, "订单信息3".getBytes());
    }
}