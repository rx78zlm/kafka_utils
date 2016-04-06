package com.kafka.constants.props;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 针对kafka中生产端发送消息模式的配置
 * @author zhangleimin
 * @package com.kafka.constants
 * @date 16-1-18
 */
@Getter
@AllArgsConstructor
public enum ProducerType {
    SYNC("sync","同步消息，即时发送"),
    ASYNC("async","异步消息，待缓冲达到指定大小再发送");

    private String syncFlag;
    private String description;
}
