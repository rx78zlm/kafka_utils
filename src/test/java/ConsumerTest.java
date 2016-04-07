import com.kafka.consumer.MessageConsumer;
import com.kafka.consumer.MessageHandler;
import com.kafka.utils.ServiceBeanLoader;
import kafka.consumer.*;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zhangleimin
 * @package PACKAGE_NAME
 * @date 16-3-29
 */
public class ConsumerTest {

    @Test
    public void testTopicConsumer() {
        ServiceBeanLoader<MessageConsumer> loader = ServiceBeanLoader.getServiceBeanLoader(MessageConsumer.class);
        MessageConsumer messageConsumer = loader.getExtService("topic");
        messageConsumer.init();
        MessageHandler<String> handler = new MessageHandler<String>() {
            @Override
            public void processMessage(String message) {
                System.out.println("do biz:" + message);
            }
        };
        messageConsumer.bindMessageHandler(handler);
        messageConsumer.receive("test");
    }

    @Test
    public void testKV() {
        Properties properties = new Properties();
        properties.setProperty("zookeeper.connect", "127.0.0.1:2181");
        properties.setProperty("group.id", "test");
        properties.setProperty("zookeeper.sync.time.ms", "200");
        properties.setProperty("auto.commit.interval.ms", "1000");
        properties.setProperty("auto.offset.reset", "smallest");
        ConsumerConnector connector = Consumer.createJavaConsumerConnector(new ConsumerConfig(properties));
        Map<String, Integer> topicCountMap = new HashMap<>();
        // 1即单线程
        topicCountMap.put("test", 1);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = connector.createMessageStreams(topicCountMap);
        KafkaStream<byte[], byte[]> stream = consumerMap.get("test").get(0);
        ConsumerIterator<byte[], byte[]> it = stream.iterator();
        // 阻塞
        while (it.hasNext()) {
            MessageAndMetadata<byte[], byte[]> metadata = it.next();
            System.out.println(String.format("message is %s", new String(metadata.message())));
            System.out.println(String.format("key is %s", new String(metadata.key())));
        }
    }

    @Test
    public void testFilterConsumer() {
        Properties properties = new Properties();
        properties.setProperty("zookeeper.connect", "127.0.0.1:2181");
        properties.setProperty("group.id", "test");
        properties.setProperty("zookeeper.sync.time.ms", "200");
        properties.setProperty("auto.commit.interval.ms", "1000");
        properties.setProperty("auto.offset.reset", "smallest");
        ConsumerConnector connector = Consumer.createJavaConsumerConnector(new ConsumerConfig(properties));
        List<KafkaStream<byte[], byte[]>> streams = connector.createMessageStreamsByFilter(new Whitelist(".*"), 2);
        int startNum = 0;
        ExecutorService executor = Executors.newFixedThreadPool(2);
        for(final KafkaStream<byte[], byte[]> stream : streams) {
            final int threadNum = startNum;
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    ConsumerIterator<byte[], byte[]> it = stream.iterator();
                    System.out.println("begin add " + threadNum);
                    while (it.hasNext()) {
                        MessageAndMetadata<byte[], byte[]> metadata = it.next();
                        String message = new String(metadata.message());
                        String topic = metadata.topic();
                        System.out.println(String.format("Message from thread %d, topic is %s, message is %s, partitions is %d", threadNum, topic, message, metadata.partition()));
                    }
                }
            });
            startNum++;
            System.out.println("add finish " + startNum);
        }
        try {
            System.out.println(System.in.read());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
