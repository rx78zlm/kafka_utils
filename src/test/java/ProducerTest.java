import com.kafka.meta.KeyMapMessage;
import com.kafka.producer.MessageProducer;
import com.kafka.utils.ServiceBeanLoader;
import org.junit.Test;

/**
 * @author zhangleimin
 * @package PACKAGE_NAME
 * @date 16-3-29
 */
public class ProducerTest {

    @Test
    /**
     * K/V发送发送到指定的topic上
     */
    public void testKeyMapMessage() {
        ServiceBeanLoader<MessageProducer> loader = ServiceBeanLoader.getServiceBeanLoader(MessageProducer.class);
        MessageProducer<KeyMapMessage> messageProducer = loader.getExtService("keyed");
        messageProducer.init();
        KeyMapMessage message = new KeyMapMessage();
        message.setMessageKey("firstKey");
        message.setMessageValue("firstValue");
        messageProducer.sendMessage("test", message);
    }

    @Test
    /**
     * 简单方式消息发送到指定的topic上
     */
    public void testProducer() {
        ServiceBeanLoader<MessageProducer> loader = ServiceBeanLoader.getServiceBeanLoader(MessageProducer.class);
        MessageProducer<String> messageProducer = loader.getExtService("simple");
        messageProducer.init();
//        System.out.println(messageProducer);
//        String now = DateTime.now().toString(DateTimeFormat.fullDateTime());
//        System.out.println(now);
//        messageProducer.sendMessage("test", now);
        for(int i=0; i<10; i++) {
            messageProducer.sendMessage("test", String.valueOf(i));
        }
    }
}
