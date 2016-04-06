package com.kafka.producer.impl;

import com.kafka.common.annotation.Inject;
import com.kafka.common.annotation.SPI;
import com.kafka.init.PropertyBuilder;
import com.kafka.meta.KeyMapMessage;
import com.kafka.producer.MessageProducer;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.List;

/**
 * 将k/v结构的消息发送到指定的topic上
 * 发送端可以根据key来发送消息，消费端可以根据key来获取消息
 * @author zhangleimin
 * @package com.kafka.producer.impl
 * @date 16-3-18
 */
@SPI("keyed")
public class KeyedMessageProducer<T> implements MessageProducer<T> {

    @Inject("producer")
    private PropertyBuilder propertyBuilder;

    @Override
    public void sendMessage(String topicName, T message) {
        if (!KeyMapMessage.class.isInstance(message)) {
            throw new IllegalArgumentException("message type error,must be instance of KeyMapMessage.class");
        }
        KeyMapMessage keyMapMessage = (KeyMapMessage) message;
        Producer producer = new Producer(new ProducerConfig(propertyBuilder.getConfig()));
        producer.send(new KeyedMessage(topicName, keyMapMessage.getMessageKey(), keyMapMessage.getMessageValue()));
    }

    @Override
    public void sendMessages(String topicName, List<T> messages) {
        for(T message : messages) {
            sendMessage(topicName, message);
        }
    }

    @Override
    public boolean init() {
        return propertyBuilder.init();
    }

    @Override
    public boolean update() {
        return propertyBuilder.update();
    }

    @Override
    public boolean update(Object param) {
        return param != null && propertyBuilder.update(param);
    }
}
