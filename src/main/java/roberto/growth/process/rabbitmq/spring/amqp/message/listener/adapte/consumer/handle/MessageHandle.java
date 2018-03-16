/**
 * Copyright (C), 2015-2018, ND Co., Ltd.
 * FileName: MessageHandle
 * Author:   HuangTaiHong
 * Date:     2018-03-16 下午 5:17
 * Description: 消息处理器
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package roberto.growth.process.rabbitmq.spring.amqp.message.listener.adapte.consumer.handle;

import java.io.UnsupportedEncodingException;

/**
 * 〈一句话功能简述〉<br> 
 * 〈消息处理器〉
 *
 * @author HuangTaiHong
 * @create 2018-03-16 
 * @since 1.0.0
 */
public class MessageHandle {
    public void add(byte[] message){
        try {
            System.out.println(new String(message,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}