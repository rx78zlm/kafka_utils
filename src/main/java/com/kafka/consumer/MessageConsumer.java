package com.kafka.consumer;

import com.kafka.common.Updater;
import com.kafka.init.BeanInit;

import java.io.Closeable;

/**
 * 接收消息
 * @author zhangleimin
 * @package com.kafka.consumer
 * @date 16-3-29
 */
public interface MessageConsumer<V> extends BeanInit, Updater, Closeable {

    /**
     * 绑定接收消息后的处理类
     * @param handler   消息处理类
     */
    public void bindMessageHandler(MessageHandler<V> handler);

    /**
     * 接收消息
     * @param topicName 主题
     */
    public void receive(String topicName);

}
