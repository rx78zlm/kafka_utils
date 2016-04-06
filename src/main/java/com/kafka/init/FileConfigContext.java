package com.kafka.init;

import com.google.common.collect.Lists;
import com.kafka.common.annotation.SPI;
import com.kafka.init.ConfigContext;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 从配置文件获取配置值的实现
 * @author zhangleimin
 * @package com.kafka.init.producer
 * @date 16-1-29
 */
@Slf4j
@NoArgsConstructor
public abstract class FileConfigContext<K, V> implements ConfigContext<K, V> {

    private Properties properties;

//    private String fileName = "producer_config.properties";

    private List<ConfigContext.Node> cfgNodes = Lists.newArrayList();

    private Object[] keys;

    private Object[] values;

    private int size = 0;

    private boolean initFlag = false;

    /**
     * 获取配置文件名
     * @return  配置文件名
     */
    protected abstract String getFileName();

    /**
     * 加载配置文件
     * @param fileName  文件名
     * @return  加载是否成功
     */
    public boolean loadConfigFile(String fileName) {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        try {
            properties = new Properties();
            properties.load(inputStream);
            return true;
        } catch (IOException e) {
            log.error("加载配置文件异常，原因{}", e);
        }
        return false;
    }

    @Override
    public boolean init() {
        // 未初始化过再进行初始化操作
        return initFlag || initDetail();
    }

    @Override
    public boolean update() {
        return initDetail();
    }

    @Override
    public boolean update(Object param) {
        throw new UnsupportedOperationException("only invoke with no parameter");
    }

    /**
     * 初始化过程
     * @return  是否成功
     */
    private boolean initDetail() {
        if (!loadConfigFile(getFileName())) {
            return false;
        }
        keys = new Object[properties.size()];
        values = new Object[properties.size()];
        int index = 0;
        for(Map.Entry entry : properties.entrySet()) {
            // TODO 需要使用SPI实现
            keys[index] = entry.getKey();
            values[index] = entry.getValue();
            cfgNodes.add(new Node<Object, Object>(entry.getKey(), entry.getValue()));
        }
        size = cfgNodes.size();
        initFlag = true;
        return true;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    @SuppressWarnings("unchecked")
    public K[] getConfigKeys() {
        if (size == 0) {
            return null;
        }
        return (K[]) keys;
    }

    @Override
    @SuppressWarnings("unchecked")
    public V[] getConfigValue() {
        if (size == 0) {
            return null;
        }
        return (V[]) values;
    }

    @Override
    @SuppressWarnings("unchecked")
    public V getPropertyValue(K key) {
        if (size == 0) {
            return null;
        }
        //TODO 效率低下，待优化
        for(ConfigContext.Node node : cfgNodes) {
            if (node.getKey().equals(key)) {
                return (V) node.getValue();
            }
        }
        return null;
    }

    @Override
    public boolean containKey(K key) {
        //TODO 效率低下，待优化
        for(ConfigContext.Node node : cfgNodes) {
            if (node.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    class Node<K,V> implements ConfigContext.Node<K,V> {

        final K key;
        V value;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }
    }
}
