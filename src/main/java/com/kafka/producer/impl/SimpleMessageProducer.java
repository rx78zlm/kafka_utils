package com.kafka.producer.impl;

import com.kafka.common.annotation.Inject;
import com.kafka.common.annotation.SPI;
import com.kafka.init.PropertyBuilder;
import com.kafka.producer.MessageProducer;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

/**
 * 简单消息发送，将消息发送到指定的topic上
 * @author zhangleimin
 * @package com.kafka.producer.impl
 * @date 16-3-18
 */
@SPI("simple")
public class SimpleMessageProducer<T> implements MessageProducer<T> {

    @Inject("producer")
    private PropertyBuilder propertyBuilder;

    @Override
    public void sendMessage(String topicName, T message) {
        Producer<String, T> producer = new Producer<String, T>(new ProducerConfig(propertyBuilder.getConfig()));
        producer.send(new KeyedMessage<String, T>(topicName, message));
        producer.close();
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
