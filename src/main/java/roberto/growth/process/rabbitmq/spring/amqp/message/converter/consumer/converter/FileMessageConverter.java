/**
 * Copyright (C), 2015-2018, ND Co., Ltd.
 * FileName: FileMessageConverter
 * Author:   HuangTaiHong
 * Date:     2018-03-16 下午 5:47
 * Description: 文件消息转换器
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package roberto.growth.process.rabbitmq.spring.amqp.message.converter.consumer.converter;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 〈一句话功能简述〉<br> 
 * 〈文件消息转换器〉
 *
 * @author HuangTaiHong
 * @create 2018-03-16 
 * @since 1.0.0
 */
public class FileMessageConverter implements MessageConverter {
    @Override
    public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
        return null;
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        String extName = (String) message.getMessageProperties().getHeaders().get("_extName");
        byte[] bytes = message.getBody();
        String fileName = UUID.randomUUID().toString();
        String filePath = System.getProperty("java.io.tmpdir") + fileName + "." + extName;
        File tempFile = new File(filePath);
        try {
            FileCopyUtils.copy(bytes, tempFile);
        } catch (IOException e) {
            throw new MessageConversionException("FileMessageConverter消息转换失败", e);
        }

        return tempFile;
    }
}