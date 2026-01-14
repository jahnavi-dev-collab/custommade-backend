package com.platform.custommade.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "quotes")
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private Request request;

    @ManyToOne
    @JoinColumn(name = "tailor_id")
    private User tailor;

    private Double price;
    private Double advanceAmount;
    private int deliveryDays;
    private String fabricPlan;

    @Enumerated(EnumType.STRING)
    private QuoteStatus status;

    private LocalDateTime createdAt;

    // Getters & Setters
}
