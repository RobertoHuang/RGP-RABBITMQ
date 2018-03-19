/**
 * Copyright (C), 2015-2018, ND Co., Ltd.
 * FileName: RPCMethod
 * Author:   HuangTaiHong
 * Date:     2018-03-19 下午 7:09
 * Description: RPC调用方法
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package roberto.growth.process.rabbitmq.rpc.java.client.server;

/**
 * 〈一句话功能简述〉<br>
 * 〈RPC调用方法〉
 *
 * @author HuangTaiHong
 * @create 2018-03-19
 * @since 1.0.0
 */
public class RPCMethod {
    public static String addOrder(String orderInfo) {
        try {
            System.out.println("orderInfo已添加到数据库");
            return "订单ID";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}