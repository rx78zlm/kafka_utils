package com.kafka.init.config;

import com.kafka.constants.props.ProducerType;
import com.kafka.constants.props.RequiredAck;
import lombok.Data;

/**
 * producer的配置信息
 * @author zhangleimin
 * @package com.kafka.init.config
 * @date 16-1-28
 */
@Data
public class BaseProducerConfig {

    private String brokers;
    private String keySerializer;
    private String valueSerializer;
    private RequiredAck requiredAck;    // 消息发送后的应答方式
    private ProducerType producerType;
    private String topic;
//    ---------------------以上内容是基本配置---------------------------------
    private boolean retry;      // 发送失败是否重试
    private String retryTimes;     // 重试次数，当retry为false时无效果
    private String timeout;       // 超时时间
    private String partitioner; // 分区策略处理类
    private String retryBackoff;  // 在每次重试之前等待更新topic的metadata时间
    private String refreshFrequency;  // 失败情况下更新topic的metadata频率
    private AsyncConfig asyncConfig;    // 当producerType为async模式时具有的一些配置信息
    private String bufferSize;      // 发送时buffer大小
    private String clientId;        // 生产者的id
}
