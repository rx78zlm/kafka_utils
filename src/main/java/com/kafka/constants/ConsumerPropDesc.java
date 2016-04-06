package com.kafka.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 针对kafka中consumer的配置项说明
 * @author zhangleimin
 * @package com.kafka.constants
 * @date 16-1-26
 */
@Getter
@AllArgsConstructor
public enum ConsumerPropDesc {

    GROUP_ID("group.id", "消费组编号"),
    ZK_ADDRESS("zookeeper.connect", "zk地址"),
    // --------------------以上配置是consumer必须的参数----------------------
    CONSUMER_ID("consumer.id","消费端ID"),
    SOCKET_TIMEOUT("socket.timeout.ms", "网络socket超时设置"),
    SOCKET_RCV_BUFFER("socket.receive.buffer.bytes", "socket接收数据的buffer"),
    MAX_FETCH_SIZE("fetch.message.max.bytes","每次fetch消息的最大字节数"),
    FETCH_THREADS("num.consumer.fetchers","fetch消息线程数"),
    AUTO_COMMIT("auto.commit.enable","consumer消息后将offset自动提交"),
    AUTO_COMMIT_FREQUENCY("auto.commit.interval.ms","自动提交的频率，单位秒"),
    BUFFER_CHUNKS("queued.max.message.chunks","用于缓存消息的最大块数，每块大小由fetch.message.max.bytes决定"),
    MAX_RB_RETRY("rebalance.max.retries","当新consumer加入组时，需要重新为每个consumer分配区，最大重度数次"),
    MIN_FETCH_SIZE("fetch.min.bytes","每次fetch的最小字节数"),
    FETCH_WAIT_TIMES("fetch.wait.max.ms","fetch数据，阻塞等待最大时间"),
    RB_BACKOFF_TIMES("rebalance.backoff.ms","在重试reblance之前backoff时间"),
    REFRESH_LEADER_BACKOFF("refresh.leader.backoff.ms","在试图确定某个partition的leader是否失去他的leader地位之前，需要等待的backoff时间"),
    AUTO_OFFSET_RESET("auto.offset.reset","zk中没有offset时，offset从哪开始"),
    TIMEOUT("consumer.timeout.ms","没有消息可取，等待相应时间还没有消息就超时"),
    EXPLODE_INNER_TOPIC("exclude.internal.topics","是否要暴露内部topic消息给consumer"),
    CLIENT_ID("client.id","可以存放自定义ID，便于追踪"),
    ZK_SESSION_TIMEOUT("zookeeper.session.timeout.ms","consumer与zk的session超时"),
    ZK_TIMEOUT("zookeeper.connection.timeout.ms","与zk建立连接的超时设置"),
    ZK_SYNC_TIME("zookeeper.sync.time.ms","zk集群之间的同步时间"),
    OFFSET_STORAGE("offsets.storage","存放offset的地方"),

    // ------------------------------以下内容是工具框架的附加配置项------------------------------
    THREAD_COUNT("thread.count", "每个topic的线程数")
    ;

    private String cfgName;
    private String description;
}
