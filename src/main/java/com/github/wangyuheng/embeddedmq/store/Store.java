package com.github.wangyuheng.embeddedmq.store;

import com.github.wangyuheng.embeddedmq.Message;
import com.github.wangyuheng.embeddedmq.consumer.Partition;

import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 存储层
 */
public interface Store {

    void append(Message message, Partition partition);

    LinkedBlockingQueue<Message> findByPartition(Partition partition);

}
