package com.kafka.init.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 当producerType为async模式时具有的一些配置信息
 * @author zhangleimin
 * @package com.kafka.init.config
 * @date 16-1-29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsyncConfig {

    private String maxBufferTime;   // 用户缓存数据的最大时间间隔
    private String maxBufferCount;  // 缓存到队列中的未发送的最大消息条数
    private String maxBufferMsg;    // 可以批量处理消息的最大条数
}
