# 📘 Day 1 学习笔记：RabbitMQ + Spring Boot 消息系统

日期：2025年4月16日  
目标：掌握 event-driven 架构基本流程，成功打通一个基于 RabbitMQ 的 Spring Boot producer ➜ consumer 消息通道

---

## ✅ 一、学习目标与进展

- ✅ 本地部署 RabbitMQ（M3 芯片支持）
- ✅ 理解 Event-driven 架构
- ✅ 编写 Spring Boot 订单服务（Producer）
- ✅ RabbitTemplate 消息发送机制
- ✅ 配置并发送 Serializable 消息
- ✅ 解决反序列化异常，改为 JSON 格式传输
- ✅ 编写 NotificationService 消费者服务
- ✅ 使用 @RabbitListener 成功接收消息

---

## ✅ 二、知识点回顾

### 1. Event-driven 架构核心：
- 服务通过事件（message）异步通信，解耦 & 可扩展性强

### 2. RabbitMQ 本地部署命令：
```bash
docker run -d --hostname rabbit-local --name rabbitmq \
  -p 5672:5672 -p 15672:15672 \
  -e RABBITMQ_DEFAULT_USER=guest \
  -e RABBITMQ_DEFAULT_PASS=guest \
  rabbitmq:3-management
```

### 3. RabbitTemplate 发送：
```java
rabbitTemplate.convertAndSend("order.queue", orderRequest);
```

### 4. 消息转换异常与解决：
- 错误：`MessageConversionException`
- 解决：使用 `Jackson2JsonMessageConverter`

### 5. JSON 转换配置：
```java
@Bean
public MessageConverter jsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
}
```

---

## ✅ 三、你提出的重要问题与总结

- **Q: RabbitTemplate.convertAndSend 的作用？**  
  A: 对象序列化并发出到队列，可配置 JSON 转换器。
  
- **Q: new Queue("order.queue", false) 的含义？**  
  A: false 表示非持久化，RabbitMQ 重启后队列会丢失。
  
- **Q: 为什么 notification-service 不需要 Controller？**  
  A: 因为它是监听型服务，不对外暴露 API。
  
- **Q: JSON 是如何被自动反序列化的？**  
  A: Spring AMQP 在 `@RabbitListener` 自动使用 MessageConverter。
  
- **Q: 端口冲突如何解决？**  
  A: 通过配置 `server.port=8081` 来避免默认 8080 冲突。

---

## ✅ 四、你完成的实操效果

- ✅ Postman 成功发出订单  
- ✅ RabbitMQ UI 看到消息入队  
- ✅ NotificationService 成功消费消息并打印  
- ✅ 理解 Producer / Consumer 职责划分  
- ✅ 成功处理格式转换和端口冲突

---

## ✅ 五、消费者端配置是否需要声明 Queue？

### ✔️ 是否需要？
不需要，**只要队列由生产者声明并存在于 RabbitMQ 中，消费者服务可以直接监听而无需再声明。**

### 🧠 原因：
- 如果消费者也声明队列，且与生产者属性不同（如 durable 值），可能会导致冲突或抛出异常。
- 最佳实践是：**队列的创建与声明由一个服务负责，其余服务只监听。**

### ✅ 最佳实践总结：

| 内容 | 是否需要在 Consumer 配置 | 说明 |
|------|--------------------------|------|
| `Queue` Bean | ❌ 不需要（如果已由生产者创建） | 避免声明冲突 |
| `Jackson2JsonMessageConverter` | ✅ 必须配置 | 否则不能正确反序列化 JSON |
