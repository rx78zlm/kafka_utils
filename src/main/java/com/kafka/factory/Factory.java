package com.kafka.factory;

/**
 * 用于创建对象使用
 * @author zhangleimin
 * @package com.kafka.init
 * @date 16-1-28
 */
public interface Factory {

    /**
     * 创建对象实例
     * @param paraType 参数
     * @param param 参数值
     * @return  目标对象实例
     */
    <T> T create(Class paraType, Object param);
}
