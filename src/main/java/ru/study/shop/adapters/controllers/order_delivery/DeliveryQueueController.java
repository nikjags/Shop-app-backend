package ru.study.shop.adapters.controllers.order_delivery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;
import ru.study.shop.entities.Order;

@Controller
public class DeliveryQueueController {
    @Autowired
    private final JmsTemplate jmsTemplate;

    @Value("${spring.activemq.queues.delivery-request-queue-name}")
    private String destinationQueue;

    public DeliveryQueueController(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void setToDelivery(Order order, boolean isNew) {
        jmsTemplate.convertAndSend(destinationQueue, order, message -> {
            message.setBooleanProperty("isNew", isNew);
            return message;
        });
    }
}
