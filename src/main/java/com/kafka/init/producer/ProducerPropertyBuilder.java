package com.kafka.init.producer;

import com.google.common.base.Strings;
import com.kafka.common.annotation.Inject;
import com.kafka.common.annotation.SPI;
import com.kafka.constants.ProducerPropDesc;
import com.kafka.init.AbstractPropertyBuilder;
import com.kafka.init.ConfigContext;

import java.util.Map;
import java.util.Properties;

/**
 * 初始化消息生产者的配置
 *
 * @author zhangleimin
 * @package com.kafka.init
 * @date 16-1-26
 */
@SPI("producer")
public class ProducerPropertyBuilder extends AbstractPropertyBuilder {

    @Inject("producer_file")
    private ConfigContext<String, String> configContext;

    @Override
    public ConfigContext<String, String> getConfigContext() {
        return configContext;
    }

    @Override
    public void fillProperties() {
        properties = new Properties();
        for (ProducerPropDesc desc : ProducerPropDesc.class.getEnumConstants()) {
            String value = configContext.getPropertyValue(desc.getCfgName());
            if (!Strings.isNullOrEmpty(value)) {
                properties.setProperty(desc.getCfgName(), value);
            }
        }
    }

    @Override
    public boolean update(Object param) {
        // 更新参数，目前仅支持键值传递
        if (Map.class.isAssignableFrom(param.getClass())) {
            Map<String, String> map = (Map<String, String>) param;
            for (String key : map.keySet()) {
                ProducerPropDesc desc = ProducerPropDesc.valueOf(key);
                String value = configContext.getPropertyValue(desc.getCfgName());
                if (!Strings.isNullOrEmpty(value)) {
                    properties.setProperty(desc.getCfgName(), value);
                }
            }
        } else {
            throw new IllegalArgumentException("parameter must be implements Map");
        }
        return true;
//        throw new UnsupportedOperationException("only invoke with no parameter");
    }

}
