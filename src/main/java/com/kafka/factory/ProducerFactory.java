package com.kafka.factory;


import com.kafka.common.annotation.SPI;
import kafka.javaapi.producer.Producer;
import kafka.producer.ProducerConfig;

import java.util.Properties;

/**
 * @author zhangleimin
 * @package com.kafka.factory
 * @date 16-1-28
 */
@SPI("producer")
public class ProducerFactory implements Factory {

    @Override
    public <T> T create(Class paraType, Object param) {
        // 查看参数是否为Properties类型，是则直接生成producer
        Producer producer = null;
        if (Properties.class.isInstance(param)) {
            producer = new Producer(new ProducerConfig((Properties)param));
        }
        return (T) producer;
    }
}
