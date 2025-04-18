package com.example.notification_service.listener;

import com.example.notification_service.dto.OrderRequest;
import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
public class DeadLetterListener {

    @RabbitListener(queues = "order.dlq")
    public void handleDeadLetter(OrderRequest orderRequest) {
        System.out.println("☠️ 收到死信消息（DLQ）:");
        System.out.println("Body: " + orderRequest);
    }

    @PostConstruct
    public void init() {
        System.out.println("🚀 DeadLetterListener 被加载了");
    }


}
