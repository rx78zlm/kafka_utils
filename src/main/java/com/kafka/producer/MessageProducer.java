package com.kafka.producer;

import com.kafka.common.Updater;
import com.kafka.init.BeanInit;

import java.io.Closeable;
import java.util.List;

/**
 * 消息生产者
 * @author zhangleimin
 * @package com.kafka.producer
 * @date 16-1-26
 */
public interface MessageProducer<T> extends BeanInit, Updater {

    /**
     * 发送消息
     * @param topicName 主题
     * @param message   消息
     */
    public void sendMessage(String topicName, T message);

    /**
     * 批量发送消息
     * @param topicName 主题
     * @param messages  消息集
     */
    public void sendMessages(String topicName, List<T> messages);
}
