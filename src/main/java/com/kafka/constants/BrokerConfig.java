package com.kafka.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 针对kafka中broker的配置说明
 * @author zhangleimin
 * @package com.kafka.constants
 * @date 16-1-18
 */
@Getter
@AllArgsConstructor
public enum BrokerConfig {

    ZK_ADDRESS("zookeeper.connect", "zk地址"),
    AUTO_CREATE_TOPIC("auto.create.topics.enable","是否自动创建topic"),
    AUTO_RB_LEADER("auto.leader.rebalance.enable", ""),
    BACKGROUND_THREADS("background.threads", "后台处理线程个数"),
    BROKER_ID("broker.id", "指定broker的id，必须唯一"),
    COMPRESSION("compression.type", "数据压缩方式"),
    DELETE_TOPIC("delete.topic.enable", "是否能够删除topic"),
    HOST_NAME("leader.imbalance.check.interval.seconds", "检查leader不平衡频率"),
    PRODUCER_TYPE("producer.type", "同步发送或异步发送"),
    BROKER_LIST("metadata.broker.list", "broker地址信息，格式：host1:port1,host2:port2"),
    REQ_ACKS("request.required.acks", "请求应答模式"),
    TIMEOUT("request.timeout.ms","超时设置，毫秒")
    ;

    private String cfgName;
    private String description;

}
