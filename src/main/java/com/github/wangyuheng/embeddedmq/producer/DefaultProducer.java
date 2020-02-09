package com.github.wangyuheng.embeddedmq.producer;

import com.github.wangyuheng.embeddedmq.Message;
import com.github.wangyuheng.embeddedmq.transport.Transport;
import org.springframework.stereotype.Component;

/**
 * 默认消息发送者
 *
 * @see Transport
 * @see Message
 */
@Component
public class DefaultProducer implements Producer {

    private final Transport transport;

    public DefaultProducer(Transport transport) {
        this.transport = transport;
    }

    /**
     * 发送消息
     */
    @Override
    public void send(Message message) {
        transport.transfer(message);
    }
}
