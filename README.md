# embedded-mq-spring-boot-starter

为Spring-boot提供消息队列能力的starter, 并提供了VM线程的轻量级实现。

## DESIGN

![design diagram](http://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/wangyuheng/embedded-mq-spring-boot-starter/master/.design/design.puml)

## GUIDE

- Consumer

```java
@Consumer(topic = CONSUMER_TOPIC, id = CUSTOM_CONSUMER_ID)
public void consumerMessage(Message message) {
    consumerRecordMap.get(CUSTOM_CONSUMER_ID).add(message);
}
```

- Producer

```java
@Autowired
private DefaultProducer<String> producer;

public void sendMessage(){
    producer.send(new Message<>(CUSTOM_TOPIC, "This is a message!"));
}
```