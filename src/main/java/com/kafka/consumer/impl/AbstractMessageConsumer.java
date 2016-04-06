package com.kafka.consumer.impl;

import com.kafka.common.annotation.Inject;
import com.kafka.constants.ConsumerPropDesc;
import com.kafka.consumer.MessageConsumer;
import com.kafka.consumer.MessageHandler;
import com.kafka.init.PropertyBuilder;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 消息消费者基类
 * @author zhangleimin
 * @package com.kafka.consumer.impl
 * @date 16-3-29
 */
@Slf4j
public abstract class AbstractMessageConsumer<V> implements MessageConsumer<V> {

    @Inject("consumer")
    protected PropertyBuilder propertyBuilder;

    // 接收到消息后的处理类
    private MessageHandler<V> messageHandler;

    // 存放不同topic的连接器
    private ConcurrentMap<String, ConsumerConnector> consumerConnectorMap = new ConcurrentHashMap<String, ConsumerConnector>();

    // 每个topic一个线程池
    private ConcurrentMap<String, ExecutorService> executorServiceConcurrentMap = new ConcurrentHashMap<String, ExecutorService>();

    abstract Properties initConnectProperties(String topicName);

    @Override
    public void bindMessageHandler(MessageHandler<V> handler) {
        this.messageHandler = handler;
    }

    @Override
    public void receive(String topicName) {
        ConsumerConnector connector = consumerConnectorMap.get(topicName);
        if (connector != null) {
            receiveAndProcess(connector, topicName);
        }
        receiveAndProcess(putConnector(topicName), topicName);
    }

    private ConsumerConnector putConnector(String topicName) {
        // TODO 增加GC压力，待优化
        ConsumerConnector connector = Consumer.createJavaConsumerConnector(new ConsumerConfig(initConnectProperties(topicName)));
        consumerConnectorMap.putIfAbsent(topicName, connector);
        return consumerConnectorMap.get(topicName);
    }

    /**
     * 接收消息并处理消息
     * @param connector 连接
     * @param topicName 主题
     */
    private void receiveAndProcess(ConsumerConnector connector, final String topicName) {
        Map<String, Integer> topicCountMap = new HashMap<>();
        // 默认处理线程为1，没有获取到配置或失败时按默认
        int threadCount;
        try {
            threadCount = Integer.parseInt(propertyBuilder.getConfig().getProperty(ConsumerPropDesc.THREAD_COUNT.getCfgName()));
        } catch (Exception e) {
            log.warn("get thread count for topic error, use default value 1!");
            threadCount = 1;
        }
        topicCountMap.put(topicName, threadCount);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = connector.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topicName);

        // 创建topic对应的线程池
        ExecutorService executor = executorServiceConcurrentMap.get(topicName);
        if (executor == null) {
            executor = Executors.newFixedThreadPool(threadCount);
            executorServiceConcurrentMap.put(topicName, executor);
        }
        int startNum = 0;
        for(final KafkaStream<byte[], byte[]> stream : streams) {
            final int threadNum = startNum;
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    ConsumerIterator<byte[], byte[]> it = stream.iterator();
                    while (it.hasNext()) {
                        String message = new String(it.next().message());
                        System.out.println(String.format("Message from thread %d, message is %s", threadNum, message));
                        log.info("Message from thread {}, topic is {} message is {}", threadNum, topicName, message);
                        @SuppressWarnings("unchecked")
                        V msg = (V) message;
                        messageHandler.processMessage(msg);
                    }
                }
            });
            startNum++;
        }
    }

    @Override
    public boolean init() {
        return propertyBuilder.init();
    }

    @Override
    public boolean update() {
        return propertyBuilder.update();
    }

    @Override
    public boolean update(Object param) {
        return param != null && propertyBuilder.update(param);
    }

    @Override
    public void close() throws IOException {
        System.out.println("close");
        if (!executorServiceConcurrentMap.isEmpty()) {
            for(ExecutorService executor : executorServiceConcurrentMap.values()) {
                executor.shutdown();
            }
        }
        if (!consumerConnectorMap.isEmpty()) {
            for(ConsumerConnector connector : consumerConnectorMap.values()) {
                connector.shutdown();
            }
        }
    }
}
