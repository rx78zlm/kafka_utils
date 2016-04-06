package com.kafka.init;

import com.kafka.common.Updater;

import java.util.Properties;

/**
 * 初始化的配置
 *
 * @author zhangleimin
 * @package com.kafka.init
 * @date 16-1-26
 */
public abstract class AbstractPropertyBuilder implements PropertyBuilder, Updater {

    protected Properties properties;

    /**
     * 获取配置内容
     * @return 配置上下文
     */
    public abstract ConfigContext<String, String> getConfigContext();

    /**
     * 填充配置
     */
    public abstract void fillProperties();

    @Override
    public boolean init() {
        return getConfigContext().init() && getConfig() != null;
    }

    @Override
    public Properties getConfig() {
        if (properties == null || properties.isEmpty()) {
            fillProperties();
        }
        return properties;
    }

    @Override
    public boolean update() {
        getConfigContext().update();
        fillProperties();
        return true;
    }

}
