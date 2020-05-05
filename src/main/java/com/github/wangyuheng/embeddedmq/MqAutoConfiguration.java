package com.github.wangyuheng.embeddedmq;

import com.github.wangyuheng.embeddedmq.consumer.ConsumerBeanDefinitionRegistryPostProcessor;
import com.github.wangyuheng.embeddedmq.consumer.ConsumerCluster;
import com.github.wangyuheng.embeddedmq.producer.DefaultProducer;
import com.github.wangyuheng.embeddedmq.producer.Producer;
import com.github.wangyuheng.embeddedmq.store.Store;
import com.github.wangyuheng.embeddedmq.store.VmStore;
import com.github.wangyuheng.embeddedmq.transport.Transport;
import com.github.wangyuheng.embeddedmq.transport.VmTransport;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 消息队列自动配置
 *  扫描类及初始化
 *
 * @see ConsumerBeanDefinitionRegistryPostProcessor
 * @see Store
 * @see Transport
 */
@Configuration
@ComponentScan("com.github.wangyuheng.embeddedmq")
public class MqAutoConfiguration {

    @Bean
    public ConsumerBeanDefinitionRegistryPostProcessor consumerBeanDefinitionRegistryPostProcessor() {
        return new ConsumerBeanDefinitionRegistryPostProcessor();
    }

    @Bean
    @ConditionalOnMissingBean(Store.class)
    public Store store() {
        return new VmStore();
    }

    @Bean
    @ConditionalOnMissingBean(Transport.class)
    public Transport transport(Store store, List<ConsumerCluster> consumerClusterList) {
        return new VmTransport(store, consumerClusterList);
    }

    @Bean
    @ConditionalOnMissingBean(Producer.class)
    public <T> Producer<T> producer(Transport transport) {
        return new DefaultProducer<>(transport);
    }


}
