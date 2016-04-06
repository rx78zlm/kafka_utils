package com.kafka.consumer.impl;

import com.kafka.common.annotation.SPI;
import com.kafka.constants.ConsumerPropDesc;
import com.kafka.utils.NetUtils;

import java.util.Properties;
import java.util.UUID;

/**
 * 点对点模式
 * @author zhangleimin
 * @package com.kafka.consumer.impl
 * @date 16-3-29
 */
@SPI("queue")
public class QueueMessageConsumer<V> extends AbstractMessageConsumer<V> {

    @Override
    Properties initConnectProperties(String topicName) {
        Properties properties = (Properties) propertyBuilder.getConfig().clone();
        // ip_topicName_uuid
        String id = NetUtils.getLocalAddress().getHostAddress() + "_"+ topicName + "_"+ UUID.randomUUID().toString();
        if (!properties.containsKey(ConsumerPropDesc.GROUP_ID.getCfgName())) {
            // 没有group.id，默认生成一个
            properties.put(ConsumerPropDesc.GROUP_ID.getCfgName(), topicName + "_queue");
        }
        if (properties.containsKey(ConsumerPropDesc.CLIENT_ID.getCfgName())) {
            properties.setProperty(ConsumerPropDesc.CLIENT_ID.getCfgName(), id);
        } else {
            properties.put(ConsumerPropDesc.CLIENT_ID.getCfgName(), id);
        }
        if (properties.containsKey(ConsumerPropDesc.CONSUMER_ID.getCfgName())) {
            properties.setProperty(ConsumerPropDesc.CONSUMER_ID.getCfgName(), id);
        } else {
            properties.put(ConsumerPropDesc.CONSUMER_ID.getCfgName(), id);
        }
        return properties;
    }

}
