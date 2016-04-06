package com.kafka.common.annotation;

import java.lang.annotation.*;

/**
 * 将服务注入时使用
 * @author zhangleimin
 * @package com.kafka.common
 * @date 16-3-15
 * @see SPI#value()
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Inject {

    String value() default "";
}
