package com.kafka.init;

import com.kafka.common.Updater;

/**
 * 用于存放配置信息源
 * @author zhangleimin
 * @package com.kafka.init
 * @date 16-1-29
 */
public interface ConfigContext<K,V> extends BeanInit, Updater {

    /**
     * 返回配置信息的数目
     * @return  数目
     */
    public int getSize();

    /**
     * 获取配置中所有配置项
     * @return 所有配置项
     */
    public K[] getConfigKeys();

    /**
     * 获取配置中所有配置值
     * @return  所有配置值
     */
    public V[] getConfigValue();

    /**
     * 根据key获取参数值
     * @param key key
     * @return  参数值
     */
    public V getPropertyValue(K key);

    /**
     * 检查是否存在Key
     * @param key   key
     * @return  是否存在
     */
    public boolean containKey(K key);

    interface Node<K,V> {
        /**
         * 获取结点的KEY
         * @return 结点KEY
         */
        K getKey();

        /**
         * 返回结点的value
         * @return 结点value
         */
        V getValue();

    }
}
