package com.github.wangyuheng.embeddedmq.transport;

import com.github.wangyuheng.embeddedmq.Message;
import com.github.wangyuheng.embeddedmq.consumer.ConsumerCluster;
import com.github.wangyuheng.embeddedmq.consumer.Partition;
import com.github.wangyuheng.embeddedmq.store.Store;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 运输层
 *  初始化各分区通道
 *
 * @see Store
 * @see ConsumerCluster
 * @see Partition
 */
public class VmTransport implements Transport {

    private Store store;
    private List<ConsumerCluster> consumerClusterList;

    public VmTransport(Store store, List<ConsumerCluster> consumerClusterList) {
        this.store = store;
        this.consumerClusterList = consumerClusterList;
    }

    private Map<String, Set<String>> topicClientIdMap = new ConcurrentHashMap<>();

    @Override
    public void transfer(Message message) {
        final String topic = message.getTopic();
        topicClientIdMap.get(topic).forEach(clientId -> {
            Partition partition = new Partition(clientId, topic);
            store.append(message, partition);
        });
    }

    @PostConstruct
    public void init() {
        topicClientIdMap.putAll(
                consumerClusterList.stream()
                        .collect(Collectors.groupingBy(ConsumerCluster::getTopic, Collectors.mapping(ConsumerCluster::getId, Collectors.toSet())))
        );
    }

}
