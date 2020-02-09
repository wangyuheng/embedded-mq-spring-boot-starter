package com.github.wangyuheng.embeddedmq.transport;

import com.github.wangyuheng.embeddedmq.Message;

/**
 * 运输层
 */
public interface Transport {

    void transfer(Message message);

}
