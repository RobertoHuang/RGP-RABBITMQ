/**
 * Copyright (C), 2015-2018, ND Co., Ltd.
 * FileName: ServerApplication
 * Author:   HuangTaiHong
 * Date:     2018-03-19 下午 7:37
 * Description: Server端启动类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package roberto.growth.process.rabbitmq.rpc.spring.amqp.server;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * 〈一句话功能简述〉<br> 
 * 〈Server端启动类〉
 *
 * @author HuangTaiHong
 * @create 2018-03-19 
 * @since 1.0.0
 */
@ComponentScan("roberto.growth.process.rabbitmq.rpc.spring.amqp.server")
public class ServerApplication {
    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(ServerApplication.class);
    }
}