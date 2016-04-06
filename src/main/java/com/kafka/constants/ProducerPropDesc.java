package com.kafka.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 针对kafka中Producer的配置项说明
 * @author zhangleimin
 * @package com.kafka.constants
 * @date 16-1-18
 */
@Getter
@AllArgsConstructor
public enum ProducerPropDesc {

    // 0.9版本为bootstrap.servers
    BROKER_LIST("metadata.broker.list", "broker地址信息，格式：host1:port1,host2:port2"),
    KEY_SERIALIZER("key.serializer.class", "消息key的序列化处理类"),
    VALUE_SERIALIZER("value.serializer.class", "消息value的序列化处理类"),
    SERIALIZER("serializer.class", "消息的序列化处理类"),
    REQ_ACKS("request.required.acks", "请求应答模式"),
    PRODUCER_TYPE("producer.type", "同步发送或异步发送"),
    // --------------------以上配置是producer必须的参数----------------------

    TIMEOUT("request.timeout.ms","超时设置，毫秒"),
    COMPRESSION("compression.codec", "数据压缩方式"),
    PARTITIONER("partitioner.class", "分区处理类"),
    RETRIES("message.send.max.retries", "重试次数，没有接收到acks会重发"),
    RETRIES_BACKOFF("retry.backoff.ms", "在每次重试之前等待更新topic的metadata时间"),
    REFRESH_FREQUENCY("topic.metadata.refresh.interval.ms", "失败情况下更新topic的metadata频率"),
    MAX_BUFFER_TIME("queue.buffering.max.ms", "当应用async模式时，用户缓存数据的最大时间间隔"),
    MAX_BUFFER_COUNT("queue.buffering.max.messages", "当应用async模式时，缓存到队列中的未发送的最大消息条数"),
    MAX_BUFFER_MSG("batch.num.messages","使用async模式时，可以批量处理消息的最大条数"),
    BUFFER_SIZE("send.buffer.bytes", "发送时buffer大小"),
    CLIENT_ID("client.id", "可以存放自定义ID，便于追踪")
    ;

    private String cfgName;
    private String description;

}
