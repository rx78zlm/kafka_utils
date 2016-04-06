package com.kafka.meta;

import lombok.Data;

/**
 * 定义K/V类型消息
 * @author zhangleimin
 * @package com.kafka.meta
 * @date 16-3-22
 */
@Data
public class KeyMapMessage<K, V> {

    private K messageKey;

    private V messageValue;
}
