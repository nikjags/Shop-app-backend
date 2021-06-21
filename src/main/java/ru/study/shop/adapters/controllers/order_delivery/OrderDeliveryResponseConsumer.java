package ru.study.shop.adapters.controllers.order_delivery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import ru.study.shop.entities.Order;
import ru.study.shop.services.interfaces.OrderService;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.Optional;

import static java.lang.Long.parseLong;

@Component
public class OrderDeliveryResponseConsumer {
    private static final String ORDER_ID_PROPERTY_NAME = "orderId";
    private static final String IS_DELIVERED_PROPERTY_NAME = "isDelivered";

    @Autowired
    OrderService orderService;

    @JmsListener(destination = "${spring.activemq.queues.delivery-response-queue-name}")
    public void receiveMessage(Message message) {

        try {
            Long orderId = parseLong(message.getStringProperty(ORDER_ID_PROPERTY_NAME));
            boolean isDelivered = message.getBooleanProperty(IS_DELIVERED_PROPERTY_NAME);

            Optional<Order> optionalOrder = orderService.findById(orderId);
            if (optionalOrder.isPresent()) {
                Order order = optionalOrder.get();
                order.setDelivered(isDelivered);

                orderService.saveOrder(order);
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
