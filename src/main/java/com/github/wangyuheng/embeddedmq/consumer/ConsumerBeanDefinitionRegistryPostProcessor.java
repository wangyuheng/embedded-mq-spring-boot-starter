package com.github.wangyuheng.embeddedmq.consumer;

import com.github.wangyuheng.embeddedmq.store.Store;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * 注册消费者bean
 *
 * @see Consumer
 * @see MessageHandler
 * @see Store
 */
public class ConsumerBeanDefinitionRegistryPostProcessor implements BeanPostProcessor, ApplicationContextAware {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(bean);
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(targetClass);
        for (Method method : methods) {
            if (AnnotatedElementUtils.hasAnnotation(method, Consumer.class)) {
                final String topic = method.getAnnotation(Consumer.class).topic();
                final String id = StringUtils.isEmpty(method.getAnnotation(Consumer.class).id()) ? beanName + method.getName() : method.getAnnotation(Consumer.class).id();
                final BeanFactory beanFactory = applicationContext.getBeanFactory();
                final Store store = beanFactory.getBean(Store.class);
                final MessageHandler messageHandler = message -> {
                    ReflectionUtils.makeAccessible(method);
                    ReflectionUtils.invokeMethod(method, bean, message);
                };

                final BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(ConsumerCluster.class, () -> {
                    ConsumerCluster consumerCluster = new ConsumerCluster();
                    consumerCluster.setId(id);
                    consumerCluster.setTopic(topic);
                    consumerCluster.setMessageHandler(messageHandler);
                    consumerCluster.start(store);
                    return consumerCluster;
                });
                BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
                ((DefaultListableBeanFactory) beanFactory).registerBeanDefinition(beanName + method.getName() + "Listener", beanDefinition);
            }
        }
        return bean;
    }

}
