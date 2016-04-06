1.简单使用方式：
创建消息生产者：
ServiceBeanLoader<MessageProducer> loader = ServiceBeanLoader.getServiceBeanLoader(MessageProducer.class);
MessageProducer<String> messageProducer = loader.getExtService("simple");
messageProducer.sendMessage("topicName", "message");

创建消息消费者及处理类：
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
messageConsumer.receive("topicName");

2.针对生产端的配置说明如下：
metadata.broker.list 默认值：无，必填
格式为host1:port1,host2:port2，这是一个broker列表，用于获得元数据(topics，partitions和replicas)，建立起来的socket连接用于发送实际数据，这个列表可以是broker的一个子集，或者一个VIP，指向broker的一个子集。

request.required.acks 默认值：0
用来控制一个produce请求怎样才能算完成，准确的说，是有多少broker必须已经提交数据到log文件，并向leader发送ack，可以设置如下的值：
0，意味着producer永远不会等待一个来自broker的ack，这就是0.7版本的行为。这个选项提供了最低的延迟，但是持久化的保证是最弱的，当server挂掉的时候会丢失一些数据。
1，意味着在leader replica已经接收到数据后，producer会得到一个ack。这个选项提供了更好的持久性，因为在server确认请求成功处理后，client才会返回。如果刚写到leader上，还没来得及复制leader就挂了，那么消息才可能会丢失。
-1，意味着在所有的ISR都接收到数据后，producer才得到一个ack。这个选项提供了最好的持久性，只要还有一个replica存活，那么数据就不会丢失。

request.timeout.ms 默认值：10000
请求超时时间。

producer.type 默认值：sync
决定消息是否应在一个后台线程异步发送。合法的值为sync，表示异步发送；sync表示同步发送。设置为async则允许批量发送请求，这回带来更高的吞吐量，但是client的机器挂了的话会丢失还没有发送的数据。

serializer.class 默认值：kafka.serializer.DefaultEncoder
消息的序列化类，默认是的encoder处理一个byte[]，返回一个byte[]。

key.serializer.class 默认值：无
key的序列化类，默认跟消息的序列化类一样。

partitioner.class 默认值：kafka.producer.DefaultPartitioner
用来把消息分到各个partition中，默认行为是对key进行hash。

compression.codec 默认值：none
允许指定压缩codec来对消息进行压缩，合法的值为：none，gzip，snappy。

compressed.topics 默认值：null
允许你指定特定的topic对其进行压缩。如果compression codec设置了除NoCompressionCodec以外的值，那么仅会对本选项指定的topic进行压缩。如果compression codec为NoCompressionCodec，那么压缩就不会启用。

message.send.max.retries 默认值：3
如果producer发送消息失败了会自动重发，本选项指定了重发的次数。注意如果是非0值，那么可能会导致重复发送，就是说的确发送了消息，但是没有收到ack，那么就还会发一次。

retry.backoff.ms 默认值：100
在每次重发之前，producer会刷新相关的topic的元数据，来看看是否选出了一个新leader。由于选举leader会花一些时间，此选项指定了在刷新元数据前等待的时间。

topic.metadata.refresh.interval.ms 默认值：600 * 1000
当出现错误时(缺失partition，leader不可用等)，producer通常会从broker拉取最新的topic的元数据。它也会每隔一段时间轮询(默认是每隔10分钟)。如果设置了一个负数，那么只有当发生错误时才会刷新元数据，当然不推荐这样做。有一个重要的提示：只有在消息被发送后才会刷新，所以如果producer没有发送一个消息的话，则元数据永远不会被刷新。

queue.buffering.max.ms 默认值：5000
当使用异步模式时，缓冲数据的最大时间。例如设为100的话，会每隔100毫秒把所有的消息批量发送。这会提高吞吐量，但是会增加消息的到达延时。

queue.buffering.max.messages 默认值：10000
在异步模式下，producer端允许buffer的最大消息数量，如果producer无法尽快将消息发送给broker，从而导致消息在producer端大量沉积，如果消息的条数达到此配置值，将会导致producer端阻塞或者消息被抛弃。

queue.enqueue.timeout.ms 默认值：-1
当消息在producer端沉积的条数达到 queue.buffering.max.meesages 时， 阻塞一定时间后，队列仍然没有enqueue(producer仍然没有发送出任何消息) 。 此时producer可以继续阻塞或者将消息抛弃，此timeout值用于控制 阻塞 的时间，如果值为-1 则 无阻塞超时限制，消息不会被抛弃；如果值为0 则立即清空队列，消息被抛弃。

batch.num.messages 默认值：200
在异步模式下，一个batch发送的消息数量。producer会等待直到要发送的消息数量达到这个值，之后才会发送。但如果消息数量不够，达到queue.buffer.max.ms时也会直接发送。

send.buffer.bytes 默认值：100 * 1024
socket的发送缓存大小。

client.id 默认值：""
这是用户可自定义的client id，附加在每一条消息上来帮助跟踪
#########################################################################################################################################################################

针对消费端的配置说明如下：
group.id 默认值：无

唯一的指明了consumer的group的名字，group名一样的进程属于同一个consumer group。



zookeeper.connect 默认值：无
指定了ZooKeeper的connect string，以hostname:port的形式，hostname和port就是ZooKeeper集群各个节点的hostname和port。 ZooKeeper集群中的某个节点可能会挂掉，所以可以指定多个节点的connect string。
如下所式：
hostname1:port1,hostname2:port2,hostname3:port3.



ZooKeeper也可以允许你指定一个"chroot"的路径，可以让Kafka集群将需要存储在ZooKeeper的数据存储到指定的路径下这可以让多个Kafka集群或其他应用程序公用同一个ZooKeeper集群。
可以使用如下的connect string：
hostname1:port1,hostname2:port2,hostname3:port3/chroot/path


consumer.id 默认值：null
如果没有设置的话则自动生成。


socket.timeout.ms 默认值：30 * 1000
socket请求的超时时间。实际的超时时间为max.fetch.wait + socket.timeout.ms。



socket.receive.buffer.bytes 默认值：64 * 1024
socket的receiver buffer的字节大小。



fetch.message.max.bytes 默认值：1024 * 1024
每一个获取某个topic的某个partition的请求，得到最大的字节数，每一个partition的要被读取的数据会加载入内存，所以这可以帮助控制consumer使用的内存。这个值的设置不能小于在server端设置的最大消息的字节数，否则producer可能会发送大于consumer可以获取的字节数限制的消息。



auto.commit.enable 默认值：true
如果设为true，consumer会定时向ZooKeeper发送已经获取到的消息的offset。当consumer进程挂掉时，已经提交的offset可以继续使用，让新的consumer继续工作。



auto.commit.interval.ms 默认值：60 * 1000
consumer向ZooKeeper发送offset的时间间隔。



queued.max.message.chunks 默认值：10
缓存用来消费的消息的chunk的最大数量，每一个chunk最大可以达到fetch.message.max.bytes。



rebalance.max.retries 默认值：4
当一个新的consumer加入一个consumer group时，会有一个rebalance的操作，导致每一个consumer和partition的关系重新分配。如果这个重分配失败的话，会进行重试，此配置就代表最大的重试次数。



fetch.min.bytes 默认值：1
一个fetch请求最少要返回多少字节的数据，如果数据量比这个配置少，则会等待，知道有足够的数据为止。



fetch.wait.max.ms 默认值：100
在server回应fetch请求前，如果消息不足，就是说小于fetch.min.bytes时，server最多阻塞的时间。如果超时，消息将立即发送给consumer.。



rebalance.backoff.ms 默认值：2000
在rebalance重试时的backoff时间。



refresh.leader.backoff.ms 默认值：200
在consumer发现失去某个partition的leader后，在leader选出来前的等待的backoff时间。



auto.offset.reset 默认值：largest
在Consumer在ZooKeeper中发现没有初始的offset时或者发现offset不在范围呢，该怎么做：
* smallest : 自动把offset设为最小的offset。
* largest : 自动把offset设为最大的offset。
* anything else: 抛出异常。



consumer.timeout.ms 默认值：-1
如果在指定的时间间隔后，没有发现可用的消息可消费，则抛出一个timeout异常。



client.id 默认值： group id value
每一个请求中用户自定义的client id，可帮助追踪调用情况。



zookeeper.session.timeout.ms 默认值：6000
ZooKeeper的session的超时时间，如果在这段时间内没有收到ZK的心跳，则会被认为该Kafka server挂掉了。如果把这个值设置得过低可能被误认为挂掉，如果设置得过高，如果真的挂了，则需要很长时间才能被server得知。



zookeeper.connection.timeout.ms 默认值：6000
client连接到ZK server的超时时间。

zookeeper.sync.time.ms 默认值：2000
一个ZK follower能落后leader多久