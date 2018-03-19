/**
 * Copyright (C), 2015-2018, ND Co., Ltd.
 * FileName: ConsumerApplication
 * Author:   HuangTaiHong
 * Date:     2018-03-19 下午 8:09
 * Description: 消费者启动类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package roberto.growth.process.rabbitmq.spring.boot.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 〈一句话功能简述〉<br>
 * 〈消费者启动类〉
 *
 * @author HuangTaiHong
 * @create 2018-03-19
 * @since 1.0.0
 */
@SpringBootApplication
public class ConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class);
    }
}