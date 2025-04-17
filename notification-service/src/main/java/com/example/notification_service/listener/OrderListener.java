package com.example.notification_service.listener;

import com.example.notification_service.dto.OrderRequest;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderListener {
    @RabbitListener(queues = "order.queue")
    public void handleOrder(OrderRequest orderRequest){
        System.out.println("📬 Received Order: "+orderRequest);
    }
}
