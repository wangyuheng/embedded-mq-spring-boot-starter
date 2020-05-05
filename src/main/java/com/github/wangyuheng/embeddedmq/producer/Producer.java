package com.github.wangyuheng.embeddedmq.producer;

import com.github.wangyuheng.embeddedmq.Message;

/**
 * 消息发送者
 *
 * @see Message
 */
@FunctionalInterface
public interface Producer<T> {

    void send(Message<T> message);

}
