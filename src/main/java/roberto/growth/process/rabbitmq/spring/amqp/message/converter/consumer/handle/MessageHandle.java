/**
 * Copyright (C), 2015-2018, ND Co., Ltd.
 * FileName: MessageHandle
 * Author:   HuangTaiHong
 * Date:     2018-03-16 下午 5:40
 * Description: 消息处理器类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package roberto.growth.process.rabbitmq.spring.amqp.message.converter.consumer.handle;

import roberto.growth.process.rabbitmq.spring.amqp.message.converter.consumer.entity.Order;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br> 
 * 〈消息处理器类〉
 *
 * @author HuangTaiHong
 * @create 2018-03-16 
 * @since 1.0.0
 */
public class MessageHandle {
    public void add(byte[] body) {
        System.out.println("----------byte[]方法进行处理----------");
        System.out.println("body");
    }

    public void add(String message) {
        System.out.println("----------String方法进行处理----------");
        System.out.println(message);
    }

    public void add(File file) {
        System.out.println("----------File方法进行处理----------");
        System.out.println(file.length());
        System.out.println(file.getName());
        System.out.println(file.getAbsolutePath());
    }

    public void add(Order order) {
        System.out.println("----------Order方法进行处理----------");
        System.out.println(order.getOrderId() + "---" + order.getOrderAmount());
    }

    public void add(List<Order> orderList) {
        System.out.println("----------List<Order>方法进行处理----------");
        System.out.println(orderList.size());
        for (Order order : orderList) {
            System.out.println(order.getOrderId() + "---" + order.getOrderAmount());
        }
    }

    public void add(Map<String, Order> orderMap) {
        System.out.println("----------Map<String, Order>方法进行处理----------");
        for (Map.Entry<String, Order> entry : orderMap.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue().getOrderId() + "---" + entry.getValue().getOrderAmount());
        }
    }
}