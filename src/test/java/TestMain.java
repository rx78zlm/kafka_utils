import com.kafka.consumer.MessageConsumer;
import com.kafka.consumer.MessageHandler;
import com.kafka.utils.ServiceBeanLoader;

/**
 * @author zhangleimin
 * @package com.kafka
 * @date 16-4-5
 */
public class TestMain {

    public static void main(String[] args) {
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
}
