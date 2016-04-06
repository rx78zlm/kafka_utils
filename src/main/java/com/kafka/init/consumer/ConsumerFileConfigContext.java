package com.kafka.init.consumer;

import com.kafka.common.annotation.SPI;
import com.kafka.init.FileConfigContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangleimin
 * @package com.kafka.init.producer
 * @date 16-3-28
 */
@Slf4j
@SPI("consumer_file")
public class ConsumerFileConfigContext<K, V> extends FileConfigContext<K, V> {

    @Override
    protected String getFileName() {
        return "consumer_config.properties";
    }
}
