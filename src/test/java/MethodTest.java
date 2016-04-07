import com.kafka.constants.ProducerPropDesc;
import com.kafka.consumer.impl.TopicMessageConsumer;
import com.kafka.factory.Factory;
import com.kafka.init.PropertyBuilder;
import com.kafka.init.producer.ProducerPropertyBuilder;
import com.kafka.utils.NetUtils;
import com.kafka.utils.ServiceBeanLoader;
import kafka.admin.TopicCommand;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * @author zhangleimin
 * @package PACKAGE_NAME
 * @date 16-1-29
 */
public class MethodTest {

    @Test
    public void testType() {
        Properties properties = new Properties();
        Object o = new Object();
        System.out.println(Properties.class.isInstance(properties));
        System.out.println(Properties.class.isInstance(o));
        System.out.println(Properties.class.getName());
    }

    @Test
    public void testEnum() {
        for(ProducerPropDesc producerPropDesc : ProducerPropDesc.class.getEnumConstants()) {
            System.out.println(String.format("key is %s, value is %s", producerPropDesc.getCfgName(), producerPropDesc.getDescription()));
        }
    }

    @Test
    public void testClassLoader() {
        try {
            String fileName = "config.properties";
            Enumeration<URL> urls = ClassLoader.getSystemResources(fileName);
            Enumeration<URL> urlEnumeration = MethodTest.class.getClassLoader().getResources(fileName);
            showUrls(urls);
            showUrls(urlEnumeration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showUrls(Enumeration<URL> urls) {
        while (urls.hasMoreElements()) {
            System.out.println(urls.nextElement().toString());
        }
    }

    @Test
    public void testSplit() {
        String name = "fixed,cache";
        Pattern pattern = Pattern.compile("\\s*[,]+\\s*");
        String[] names = pattern.split(name);
        for(String s : names) {
            System.out.println(s);
        }
    }

    @Test
    public void testIndex() {
        String interfaceName = PropertyBuilder.class.getSimpleName();
        String subClass = ProducerPropertyBuilder.class.getSimpleName();
        System.out.println(subClass.substring(0, subClass.indexOf(interfaceName)));
    }

    @Test
    public void testServiceLoader() {
        Factory factory = ServiceBeanLoader.getServiceBeanLoader(Factory.class).getExtService("producer");
        System.out.println(factory.toString());
    }

    @Test
    public void testMap() {
        Properties properties = new Properties();
        properties.setProperty("1", "one");
        Map<String, String> map = new HashMap<>();
        map.put("2", "one");
        showMap(properties);
        showMap(map);
    }

    public void showMap(Object param) {
        if (Map.class.isAssignableFrom(param.getClass())) {
            Map map = (Map) param;
            for(Object key : map.keySet()) {
                System.out.println(key);
            }
        }
    }

    @Test
    public void testMapEnum() {
        Map map = new HashMap();
        map.put(ProducerPropDesc.RETRIES.name(), "20");
        for(Object key : map.keySet()) {
            String s = (String) key;
            System.out.println(ProducerPropDesc.valueOf(s).getDescription());
        }
    }

    @Test
    public void getLocalAddress() {
        InetAddress address = NetUtils.getLocalAddress();
        System.out.println(address);
        System.out.println(String.format("IP is %s", address.getHostAddress()));
        System.out.println(String.format("host name is %s", address.getHostName()));
    }

    @Test
    public void testFields() {
        System.out.println(TopicMessageConsumer.class.getFields().length);
        System.out.println(TopicMessageConsumer.class.getDeclaredFields().length);
        System.out.println(TopicMessageConsumer.class.getSuperclass().getDeclaredFields().length);
    }

    @Test
    public void testSuperClass() {
        hasSuperClass(TopicMessageConsumer.class);
    }

    private void hasSuperClass(Class clazz) {
        if (clazz.getSuperclass() != null) {
            hasSuperClass(clazz.getSuperclass());
        }
        System.out.println("current is " + clazz.getName());
    }

    @Test
    public void testProperties() {
        Properties properties = new Properties();
        properties.put("a", "111");
        Properties properties1 = (Properties) properties.clone();
        properties1.setProperty("a", "222");
        System.out.println("properties:" + properties.get("a"));
        System.out.println("properties1:" + properties1.get("a"));
    }

    @Test
    public void testTopicCommand() {
        String cmd[] = {"--create", "--zookeeper", "localhost:2181", "--replication-factor", "2", "--partitions", "4", "--topic", "demo"};
        TopicCommand.main(cmd);
    }
}
