package com.platform.custommade.controller;

import com.platform.custommade.model.Message;
import com.platform.custommade.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    // -------------------------------
    // Send a message (for an order)
    // -------------------------------
    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(
            @RequestParam Long orderId,
            @RequestParam Long senderId,
            @RequestParam String messageText) {

        Message message = messageService.sendMessage(orderId, senderId, messageText);
        return ResponseEntity.ok(message);
    }

    // -------------------------------
    // Get all messages for an order
    // -------------------------------
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<Message>> getMessagesByOrder(@PathVariable Long orderId) {
        List<Message> messages = messageService.getMessagesByOrder(orderId);
        return ResponseEntity.ok(messages);
    }

    // -------------------------------
    // Get all messages sent by a user (optional)
    // -------------------------------
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Message>> getMessagesByUser(@PathVariable Long userId) {
        List<Message> messages = messageService.getMessagesByUser(userId);
        return ResponseEntity.ok(messages);
    }
}
