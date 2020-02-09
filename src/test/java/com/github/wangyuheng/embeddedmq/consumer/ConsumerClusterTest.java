package com.github.wangyuheng.embeddedmq.consumer;

import com.github.wangyuheng.embeddedmq.Message;
import com.github.wangyuheng.embeddedmq.producer.Producer;
import com.github.wangyuheng.embeddedmq.store.Store;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConsumerClusterTest {

    private static final String topic = UUID.randomUUID().toString();
    private static final String clientId = UUID.randomUUID().toString();
    private static List<Message> consumedMessages = new ArrayList<>();
    @Autowired
    private Producer producer;
    @Autowired
    @Qualifier(value = "testConsumerCluster1")
    private ConsumerCluster cluster;

    @Before
    public void setUp() {
        consumedMessages.clear();
    }

    @Test
    public void should_toggle_consume_when_restart_or_pause() throws InterruptedException {
        producer.send(new Message<>(topic, "This is a message 1!"));
        Thread.sleep(200);
        Assert.assertEquals(1, consumedMessages.size());
        cluster.pause();
        producer.send(new Message<>(topic, "This is a message 2!"));
        Thread.sleep(200);
        Assert.assertEquals(1, consumedMessages.size());
        producer.send(new Message<>(topic, "This is a message 3!"));
        cluster.restart();
        Thread.sleep(200);
        Assert.assertEquals(3, consumedMessages.size());
    }

    @Test
    public void should_shutdown_consume_thread_and_cannot_restart() throws InterruptedException {
        producer.send(new Message<>(topic, "This is a message 1!"));
        Thread.sleep(200);
        Assert.assertEquals(1, consumedMessages.size());
        cluster.shutdown();
        cluster.restart();
        producer.send(new Message<>(topic, "This is a message 2!"));
        Thread.sleep(200);
        Assert.assertEquals(1, consumedMessages.size());
    }

    @TestConfiguration
    static class ConfigConsumerClusterTest {

        @Bean
        public ConsumerCluster testConsumerCluster1(Store store) {
            ConsumerCluster cluster = new ConsumerCluster();
            cluster.setId(clientId);
            cluster.setTopic(topic);
            cluster.setMessageHandler(message -> consumedMessages.add(message));
            cluster.start(store);
            return cluster;
        }

    }
}