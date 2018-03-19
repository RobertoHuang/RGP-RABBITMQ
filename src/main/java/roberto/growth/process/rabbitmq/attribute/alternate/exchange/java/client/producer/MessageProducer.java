/**
 * Copyright (C), 2015-2018, ND Co., Ltd.
 * FileName: MessageProducer
 * Author:   HuangTaiHong
 * Date:     2018-03-19 上午 10:08
 * Description: 消息生产者
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package roberto.growth.process.rabbitmq.attribute.alternate.exchange.java.client.producer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import roberto.growth.process.rabbitmq.attribute.alternate.exchange.java.client.utils.ChannelUtils;

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

        // 声明AE 类型为Fanout
        channel.exchangeDeclare("roberto.order.failure", BuiltinExchangeType.FANOUT, true, false, false, new HashMap<>());
        // 为roberto.order设置AE
        Map<String, Object> exchangeProperties = new HashMap<>();
        exchangeProperties.put("alternate-exchange", "roberto.order.failure");
        channel.exchangeDeclare("roberto.order", BuiltinExchangeType.DIRECT, true, false, false, exchangeProperties);

        // 发送一条不能正确路由的消息
        AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder().deliveryMode(2).contentType("UTF-8").build();
        channel.basicPublish("roberto.order", "addXXX", false, basicProperties, "订单信息".getBytes());
    }
}