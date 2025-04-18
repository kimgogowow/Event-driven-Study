package com.example.notification_service.listener;

import com.example.notification_service.dto.OrderRequest;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderListener {
    @RabbitListener(queues = "order.queue")
    public void handleOrder(OrderRequest orderRequest){
        System.out.println("📬 Received Order: "+orderRequest);

        //mock failure
        if("FAIL".equalsIgnoreCase(orderRequest.getProduct())){
            System.out.println("❌ 模拟异常，抛出 RuntimeException");
            throw new RuntimeException("模拟消费失败");
        }
        //mock success
        System.out.println("✅ 订单处理成功");
    }
}
