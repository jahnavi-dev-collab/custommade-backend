package com.platform.custommade.service;

import com.platform.custommade.model.Message;
import com.platform.custommade.model.Order;
import com.platform.custommade.model.User;
import com.platform.custommade.repository.MessageRepository;
import com.platform.custommade.repository.OrderRepository;
import com.platform.custommade.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public MessageService(
            MessageRepository messageRepository,
            OrderRepository orderRepository,
            UserRepository userRepository
    ) {
        this.messageRepository = messageRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    // ✅ Send a message
    public Message sendMessage(Long orderId, Long senderId, String text) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Message message = new Message();
        message.setOrder(order);
        message.setSender(sender);
        message.setMessage(text);
        message.setCreatedAt(LocalDateTime.now());

        return messageRepository.save(message);
    }

    // ✅ Get chat messages for an order
    public List<Message> getMessagesByOrder(Long orderId) {
        return messageRepository.findByOrderId(orderId);
    }

    // ✅ Get all messages sent by a specific user
    public List<Message> getMessagesByUser(Long userId) {
        return messageRepository.findBySenderId(userId);
    }
}
