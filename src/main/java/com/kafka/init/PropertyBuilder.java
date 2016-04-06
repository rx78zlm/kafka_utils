package com.kafka.init;

import com.kafka.common.Updater;

import java.util.Properties;

/**
 * 用于初始化producer、consumer的配置使用
 * @author zhangleimin
 * @package com.kafka.init
 * @date 16-1-26
 */
public interface PropertyBuilder extends BeanInit, Updater {

    /**
     * 获取配置
     * @return  properties配置
     */
    public Properties getConfig();

}
