/**
 * Copyright (C), 2015-2018, ND Co., Ltd.
 * FileName: Order
 * Author:   HuangTaiHong
 * Date:     2018-03-16 下午 5:43
 * Description: 订单实体类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package roberto.growth.process.rabbitmq.spring.amqp.message.converter.consumer.entity;

import java.math.BigDecimal;

/**
 * 〈一句话功能简述〉<br> 
 * 〈订单实体类〉
 *
 * @author HuangTaiHong
 * @create 2018-03-16 
 * @since 1.0.0
 */
public class Order {
    /**
     * 订单编号
     **/
    private String orderId;

    /**
     * 订单金额
     **/
    private BigDecimal orderAmount;

    public Order() {

    }

    public Order(String orderId, BigDecimal orderAmount) {
        this.orderId = orderId;
        this.orderAmount = orderAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }
}