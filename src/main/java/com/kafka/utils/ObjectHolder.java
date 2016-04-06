package com.kafka.utils;

import lombok.Getter;
import lombok.Setter;

/**
 * 持有某个对象实例
 * 一般用于将holder存于map中，直接根据key找到相应类的holder后进行更新操作
 * @author zhangleimin
 * @package com.kafka.utils
 * @date 16-3-22
 */
public class ObjectHolder<T> {

    @Getter
    @Setter
    private volatile T value;
}
