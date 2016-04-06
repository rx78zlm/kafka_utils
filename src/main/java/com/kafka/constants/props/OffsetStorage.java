package com.kafka.constants.props;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 设置consumer消费消息后offset的位置存放在哪
 * @author zhangleimin
 * @package com.kafka.constants.props
 * @date 16-1-26
 */
@Getter
@AllArgsConstructor
public enum OffsetStorage {

    KAFKA("kafka","存kafka的log中"),
    ZK("zookeeper","存zookeeper中");

    private String storage;
    private String description;
}
