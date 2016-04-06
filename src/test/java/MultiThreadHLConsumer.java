import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangleimin
 * @package PACKAGE_NAME
 * @date 16-4-1
 */
public class MultiThreadHLConsumer {
    private ExecutorService executor;
    private final ConsumerConnector consumer;
    private final String topic;

    public MultiThreadHLConsumer(String zookeeper, String groupId,
                                 String topic) {
        consumer = Consumer.createJavaConsumerConnector(createConsumerConfig(zookeeper, groupId));
        this.topic = topic;
    }

    private ConsumerConfig createConsumerConfig(String zookeeper, String groupId) {
        Properties properties = new Properties();
        properties.setProperty("zookeeper.connect", zookeeper);
        properties.setProperty("group.id", groupId);
        properties.setProperty("zookeeper.sync.time.ms", "200");
        properties.setProperty("auto.commit.interval.ms", "1000");
        properties.setProperty("auto.offset.reset", "smallest");
        return new ConsumerConfig(properties);
    }

    public void shutdown() {
        if (consumer != null) {
            consumer.shutdown();
        }
        if (executor != null) {
            executor.shutdown();
        }
    }

    public void consume(int threadCount) {
        Map<String, Integer> topicCountMap = new HashMap<>();
        // 为每个主题定义线程数
        topicCountMap.put(topic, threadCount);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);
        executor = Executors.newFixedThreadPool(threadCount);
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
                    }
                }
            });
            startNum++;
        }
//        shutdown();
    }

    public void consume1(int threadCount) {
        Map<String, Integer> topicCountMap = new HashMap<>();
        // 为每个主题定义线程数
        topicCountMap.put(topic, threadCount);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);
        int startNum = 0;
        for(final KafkaStream<byte[], byte[]> stream : streams) {
            System.out.println("stream:" + startNum);
            startNum++;
        }
    }

    public static void main(String[] args) {
        String zk = "127.0.0.1:2181";
        String groupId = "test_topic_10.4.250.129";
        String topic = "test";
        MultiThreadHLConsumer consumer = new MultiThreadHLConsumer(zk, groupId, topic);
        consumer.consume(3);
//        try {
//            TimeUnit.SECONDS.sleep(10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        consumer.shutdown();
    }
}
