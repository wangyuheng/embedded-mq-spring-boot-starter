package com.github.wangyuheng.embeddedmq;

import com.github.wangyuheng.embeddedmq.consumer.ConsumerBeanDefinitionRegistryPostProcessor;
import com.github.wangyuheng.embeddedmq.store.Store;
import com.github.wangyuheng.embeddedmq.store.VmStore;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;

//@SpringBootApplication
//@ComponentScan(basePackages = {"com.github.wangyuheng.embeddedmq"},
//        excludeFilters = {@ComponentScan.Filter(
//                type = FilterType.ASSIGNABLE_TYPE,
//                value = {MqAutoConfiguration.class})
//        })
public class TestNoneConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestNoneConsumerApplication.class, args);
    }


    @Bean
    public ConsumerBeanDefinitionRegistryPostProcessor consumerBeanDefinitionRegistryPostProcessor() {
        return new ConsumerBeanDefinitionRegistryPostProcessor();
    }

    @Bean
    public Store store() {
        return new VmStore();
    }


}
