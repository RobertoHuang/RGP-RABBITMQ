/**
 * Copyright (C), 2015-2018, ND Co., Ltd.
 * FileName: ProducerApplication
 * Author:   HuangTaiHong
 * Date:     2018-03-16 下午 5:38
 * Description: 生产者启动类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package roberto.growth.process.rabbitmq.spring.amqp.message.converter.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import roberto.growth.process.rabbitmq.spring.amqp.message.converter.consumer.entity.Order;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br> 
 * 〈生产者启动类〉
 *
 * @author HuangTaiHong
 * @create 2018-03-16 
 * @since 1.0.0
 */
@ComponentScan(basePackages = "roberto.growth.process.rabbitmq.spring.amqp.message.converter.producer")
public class ProducerApplication {
    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ProducerApplication.class);

        RabbitAdmin rabbitAdmin = context.getBean(RabbitAdmin.class);
        RabbitTemplate rabbitTemplate = context.getBean(RabbitTemplate.class);

        rabbitAdmin.declareExchange(new DirectExchange("roberto.order", true, false, new HashMap<>()));

        // 发送字符串
        sendString(rabbitTemplate);
        // 发送当个对象JSON
        sendSingle(rabbitTemplate);
        // 发送List集合JSON
        sendList(rabbitTemplate);
        // 发送Map集合JSON
        sendMap(rabbitTemplate);
        // 发送图片
        sendImage(rabbitTemplate);
    }

    public static void sendString(RabbitTemplate rabbitTemplate) {
        // 声明消息 (消息体, 消息属性)
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
        messageProperties.setContentType("text/plain");
        Message message = new Message("订单消息".getBytes(), messageProperties);
        rabbitTemplate.send("roberto.order", "add", message);
    }

    public static void sendSingle(RabbitTemplate rabbitTemplate) throws Exception {
        Order order = new Order("OD0000001", new BigDecimal(888888.888888));
        ObjectMapper objectMapper = new ObjectMapper();

        // 声明消息 (消息体, 消息属性)
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.getHeaders().put("__TypeId__", "order");
        messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
        messageProperties.setContentType("application/json");
        Message message = new Message(objectMapper.writeValueAsString(order).getBytes(), messageProperties);

        rabbitTemplate.send("roberto.order", "add", message);
    }

    public static void sendList(RabbitTemplate rabbitTemplate) throws Exception {
        Order order = new Order("OD0000001", new BigDecimal(888888.888888));
        Order order2 = new Order("OD0000002", new BigDecimal(888888.888888));
        List<Order> orderList = Arrays.asList(order, order2);

        ObjectMapper objectMapper = new ObjectMapper();

        // 声明消息 (消息体, 消息属性)
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.getHeaders().put("__TypeId__", "java.util.List");
        messageProperties.getHeaders().put("__ContentTypeId__", "order");
        messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
        messageProperties.setContentType("application/json");
        Message message = new Message(objectMapper.writeValueAsString(orderList).getBytes(), messageProperties);

        rabbitTemplate.send("roberto.order", "add", message);
    }

    public static void sendMap(RabbitTemplate rabbitTemplate) throws Exception {
        Order order = new Order("OD0000001", new BigDecimal(888888.888888));
        Order order2 = new Order("OD0000002", new BigDecimal(888888.888888));
        Map<String, Order> orderMap = new HashMap<>();
        orderMap.put(order.getOrderId(), order);
        orderMap.put(order2.getOrderId(), order2);

        ObjectMapper objectMapper = new ObjectMapper();
        // 声明消息 (消息体, 消息属性)
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.getHeaders().put("__TypeId__", "java.util.Map");
        messageProperties.getHeaders().put("__KeyTypeId__", "java.lang.String");
        messageProperties.getHeaders().put("__ContentTypeId__", "order");
        messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
        messageProperties.setContentType("application/json");
        Message message = new Message(objectMapper.writeValueAsString(orderMap).getBytes(), messageProperties);

        rabbitTemplate.send("roberto.order", "add", message);
    }

    public static void sendImage(RabbitTemplate rabbitTemplate) throws Exception {
        File file = new File("E:\\a.jpg");
        FileInputStream fileInputStream = new FileInputStream(file);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
        int length;
        byte[] b = new byte[1024];
        while ((length = fileInputStream.read(b)) != -1) {
            byteArrayOutputStream.write(b, 0, length);
        }
        fileInputStream.close();
        byteArrayOutputStream.close();
        byte[] buffer = byteArrayOutputStream.toByteArray();

        // 声明消息 (消息体, 消息属性)
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.getHeaders().put("_extName", "jpg");
        messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
        messageProperties.setContentType("image/jpg");
        Message message = new Message(buffer, messageProperties);

        rabbitTemplate.send("roberto.order", "add", message);
    }
}