package com.example.order_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    //original queue - binding with dlx
    @Bean
    public Queue orderQueue(){
       // return new Queue("order.queue", false);//服务器重启后是否还存在
        return QueueBuilder.durable("order.queue")
                .withArgument("x-dead-letter-exchange","order.dlx")//assign dlx
                .withArgument("x-dead-letter-routing-key","order.dead")//assign routing key
                .build();
    }

    //dlx
    @Bean
    public DirectExchange deadLetterExchange(){
        return new DirectExchange("order.dlx");
    }
    //dlq
    @Bean
    public Queue deadLetterQueue(){//只需要在一边建这个queue就好
        return new Queue("order.dlq",true);
    }

    //bind dlx with dlq
    @Bean
    public Binding deadLetterBinding(){
        return BindingBuilder.bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with("order.dead");
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

}
