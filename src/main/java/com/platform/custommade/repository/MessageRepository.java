package com.platform.custommade.repository;

import com.platform.custommade.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    // ✅ THIS METHOD IS REQUIRED
    List<Message> findByOrderId(Long orderId);
    List<Message> findBySenderId(Long senderId);

}
