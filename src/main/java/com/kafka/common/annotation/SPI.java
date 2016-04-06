package com.kafka.common.annotation;

import java.lang.annotation.*;

/**
 * 扩展点接口标识
 * 配置文件：META-INF/kafka/com.xxx.Factory
 * default=com.kafka.factory.DefaultFactory
 * 使用方式：SPI("default")
 * 在接口上使用表示默认的SPI实现
 * @author zhangleimin
 * @package com.kafka.common
 * @date 16-3-7
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SPI {

    /**
     * 缺少扩展点名
     * @return  扩展点名
     */
    String value() default "";
}
