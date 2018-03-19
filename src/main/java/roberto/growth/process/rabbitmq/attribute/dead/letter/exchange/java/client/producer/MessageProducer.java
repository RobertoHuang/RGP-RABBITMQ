/**
 * Copyright (C), 2015-2018, ND Co., Ltd.
 * FileName: MessageProducer
 * Author:   HuangTaiHong
 * Date:     2018-03-19 下午 1:44
 * Description: 消息生产者
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package roberto.growth.process.rabbitmq.attribute.dead.letter.exchange.java.client.producer;

import com.rabbitmq.client.*;
import roberto.growth.process.rabbitmq.attribute.dead.letter.exchange.java.client.utils.ChannelUtils;

import java.io.IOException;
import java.util.HashMap;
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
        Channel channel = ChannelUtils.getChannel("RGP订单系统消息生产者");
        channel.exchangeDeclare("roberto.order", BuiltinExchangeType.TOPIC, true, false, false, new HashMap<>());
        AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder().deliveryMode(2).contentType("UTF-8").build();
        channel.basicPublish("roberto.order", "add", true, basicProperties, "订单消息信息".getBytes());
    }
}