package com.kafka.consumer;

/**
 * 用于接收到消息后的处理方式
 * @author zhangleimin
 * @package com.kafka.consumer
 * @date 16-3-29
 */
public interface MessageHandler<V> {

    void processMessage(V message);
}
