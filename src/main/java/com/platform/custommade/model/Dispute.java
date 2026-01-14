package com.platform.custommade.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "disputes")
public class Dispute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "raised_by")
    private User raisedBy; // CUSTOMER

    private String reason;
    private String description;

    @Enumerated(EnumType.STRING)
    private DisputeStatus status;

    private LocalDateTime createdAt;

    // Getters & Setters
}
