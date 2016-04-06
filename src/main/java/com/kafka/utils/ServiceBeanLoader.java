package com.kafka.utils;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.kafka.common.annotation.Inject;
import com.kafka.common.annotation.SPI;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用扩展点获取
 * 自动根据配置中的实现来实例化Bean
 * @author zhangleimin
 * @package com.kafka.utils
 * @date 16-3-3
 */
@Slf4j
public class ServiceBeanLoader<T> {

    private static final String SERVICES_DIRECTORY = "META-INF/services/";

    // 存放某个类的ServiceBeanLoader
    private static final ConcurrentMap<Class<?>, ServiceBeanLoader<?>> SERVICE_LOADER = new ConcurrentHashMap<Class<?>, ServiceBeanLoader<?>>();

    private static final ConcurrentMap<Class<?>, Object> SERVICE_INSTANCES = new ConcurrentHashMap<Class<?>, Object>();

    private final ConcurrentMap<String, ObjectHolder<Object>> cachedInstances = new ConcurrentHashMap<String, ObjectHolder<Object>>();

    // 包装类
    private Set<Class<?>> cachedWrapperClasses;

    // 用来加载哪个接口
    private final Class<?> type;

    // 加载的类描述，key为类，value为spi值
    private final ConcurrentMap<Class<?>, String> cachedNames = new ConcurrentHashMap<Class<?>, String>();

    // 加载的类描述，key为spi值，value为类
    private final Map<String, Class<?>> cachedClasses = new HashMap<String, Class<?>>();

    // 默认spi的value值
    private String cachedDefaultSPIName;

    private ServiceBeanLoader(Class<?> type) {
        this.type = type;
    }

    private void loadClasses() {
        loadClasses(SERVICES_DIRECTORY);
    }

    /**
     * 加载接口的扩展实现配置
     * @param dir   扩展配置文件所在目录
     */
    private void loadClasses(String dir) {
        String fileName = dir + type.getName();
        ClassLoader classLoader = ServiceBeanLoader.class.getClassLoader();
        try {
            Enumeration<URL> urls;
            if (classLoader != null) {
                urls = classLoader.getResources(fileName);
            } else {
                // 可能会扫到其它jar中的文件
                urls = ClassLoader.getSystemResources(fileName);
            }
            if (urls != null) {
                while (urls.hasMoreElements()) {
                    // 处理配置文件
                    processFile(urls.nextElement());
                }
            }
        } catch (Exception e) {
            log.error("");
        }
    }

    /**
     * 读取文件中每行数据，进行初始化
     * @param url   配置文件路径
     * @throws IOException
     */
    private void processFile(URL url) throws IOException {
        FileProcessor.processFile(url.openStream(), new LineProcessor() {
            @Override
            public boolean process(String line) {
                Splitter.on('=').splitToList(line).get(0);
                String spiName = line.split("=")[0];
                String className = line.split("=")[1];
                if (!Strings.isNullOrEmpty(className)) {
                    try {
                        Class<?> clazz = Class.forName(className, true, ServiceBeanLoader.class.getClassLoader());
                        // 是否是该接口的实现类
                        if (!type.isAssignableFrom(clazz)) {
                            throw new IllegalStateException(String.format("class %s is not subclass of interface %s", clazz.getName(), type));
                        }
                        initClass(clazz, spiName);
                        return true;
                    } catch (Exception e) {
                        throw new IllegalStateException("");
                    }
                }
                return false;
            }
        }, "UTF-8");
    }

    private void initClass(Class<?> clazz, String spiName) throws Exception {
        try {
            // 该类的构造器有入参，说明该类是包装类
            clazz.getConstructor(type);
            if (cachedWrapperClasses == null) {
                cachedWrapperClasses = new ConcurrentSkipListSet<Class<?>>();
            }
            cachedWrapperClasses.add(clazz);
        } catch (NoSuchMethodException e) {
            // 非包装类情况
            clazz.getConstructor();
            if (Strings.isNullOrEmpty(spiName)) {
                spiName = findSPIValue(clazz);
            }
            if (!Strings.isNullOrEmpty(spiName)) {
                if (!cachedNames.containsKey(clazz)) {
                    cachedNames.put(clazz, spiName);
                }
                if (cachedClasses.get(spiName) == null) {
                    cachedClasses.put(spiName, clazz);
                } else {
                    throw new IllegalStateException(String.format("Duplicate %s name %s on %s and %s", type.getName(), spiName, clazz.getName(), clazz.getName()));
                }
            }
        }
    }

    /**
     * 找SPI的值，缺省为类名
     * @param clazz 类描述
     * @return  SPI值
     */
    private String findSPIValue(Class<?> clazz) {
        SPI spi = clazz.getAnnotation(SPI.class);
        // 当spi没有值时，缺省类名为spi值，例如：lruFilter为Filter实现，spi的值为lru
        if (spi == null) {
            String name = clazz.getSimpleName();
            if (name.endsWith(type.getSimpleName())) {
                name = name.substring(0, name.indexOf(type.getSimpleName()));
            }
            return name.toLowerCase();
        }
        return spi.value();
    }

    /**
     * 根据接口类型找到对应的加载器
     * @param type 接口类型
     * @return 加载器
     */
    @SuppressWarnings("unchecked")
    public static <T> ServiceBeanLoader<T> getServiceBeanLoader(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("type is null");
        }
        if (!type.isInterface()) {
            throw new IllegalArgumentException(String.format("type %s is not interface", type));
        }
        ServiceBeanLoader<T> loader = (ServiceBeanLoader<T>) SERVICE_LOADER.get(type);
        if (loader == null) {
            SERVICE_LOADER.putIfAbsent(type, new ServiceBeanLoader<T>(type));
            loader = (ServiceBeanLoader<T>) SERVICE_LOADER.get(type);
        }
        return loader;
    }

    private String getDefaultSPIName() {
        if (!Strings.isNullOrEmpty(cachedDefaultSPIName)) {
            return cachedDefaultSPIName;
        }
        SPI spi = type.getAnnotation(SPI.class);
        if (spi != null) {
            String spiName = spi.value();
            if (!Strings.isNullOrEmpty(spiName)) {
                String names[] = spiName.split(",");
                if (names.length > 1) {
                    throw new IllegalStateException("more than 1 default extension name on extension " + type.getName());
                }
                if (names.length == 1) {
                    cachedDefaultSPIName = names[0];
                }
            }
        }
        return cachedDefaultSPIName;
    }

    @SuppressWarnings("unchecked")
    public T getExtService(String name) {
        if (Strings.isNullOrEmpty(name)) {
            throw new IllegalArgumentException("Extension name == null");
        }
        ObjectHolder<Object> holder = cachedInstances.get(name);
        if (holder == null) {
            // 并发map写操作安全
            cachedInstances.putIfAbsent(name, new ObjectHolder<Object>());
            holder = cachedInstances.get(name);
        }
        Object instance = holder.getValue();
        // double check替代直接使用排它锁，可以提高效率
        if (instance == null) {
            Lock lock = new ReentrantLock();
            lock.lock();
            instance = holder.getValue();
            if (instance == null) {
                instance = createExtService(name);
                holder.setValue(instance);
            }
            lock.unlock();
        }
        return (T) instance;
    }

    @SuppressWarnings("unchecked")
    private T createExtService(String name) {
        Class<?> clazz = getExtClass(name);
        T instance = (T) SERVICE_INSTANCES.get(clazz);
        try {
            if (instance == null) {
                instance = (T) clazz.newInstance();
                SERVICE_INSTANCES.putIfAbsent(clazz, instance);
            }
            return injectInstance(instance);
        } catch (Exception e) {
            throw new IllegalStateException("Extension instance(name: " + name + ", class: " +
                    type + ")  could not be instantiated: " + e.getMessage(), e);
        }
    }

    private Class<?> getExtClass(String name) {
        if (cachedClasses.size() == 0) {
            loadClasses();
        }
        return cachedClasses.get(name);
    }

    /**
     * 如果对象中有需要注入的类，则进行处理
     * @param instance  对象实例
     */
    private T injectInstance(T instance) {
        try {
            if (instance != null) {
                injectClass(instance, instance.getClass());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return instance;
    }

    private void injectClass(T instance, Class clazz) throws IllegalAccessException {
        if (clazz.getSuperclass() != null) {
            injectClass(instance, clazz.getSuperclass());
        }
        for(Field field : clazz.getDeclaredFields()) {
            Inject inject = field.getAnnotation(Inject.class);
            if (inject != null) {
                String value = inject.value();
                field.setAccessible(true);
                field.set(instance, getServiceBeanLoader(field.getType()).getExtService(value));
            }
        }
    }

}
