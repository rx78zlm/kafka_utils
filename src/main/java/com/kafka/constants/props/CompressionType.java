package com.kafka.constants.props;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息压缩方式
 * @author zhangleimin
 * @package com.kafka.constants.props
 * @date 16-1-29
 */
@Getter
@AllArgsConstructor
public enum CompressionType {

    NONE("none", "不压缩"),
    GZIP("gzip", "采用gzip方式压缩"),
    SNAPPY("snappy", "采用snappy方式压缩");

    private String type;
    private String description;
}
