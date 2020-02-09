package com.github.wangyuheng.embeddedmq.consumer;

import com.github.wangyuheng.embeddedmq.Message;

/**
 * 消息处理者
 *
 * @see ConsumerBeanDefinitionRegistryPostProcessor
 */
@FunctionalInterface
public interface MessageHandler {

    /**
     * 消息处理方法
     *
     * @see com.github.wangyuheng.embeddedmq.consumer.ConsumerCluster.ConsumerListener
     */
    void handle(Message message);

}
