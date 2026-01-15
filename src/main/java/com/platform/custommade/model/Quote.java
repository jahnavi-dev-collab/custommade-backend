package com.platform.custommade.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "quotes")
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The customer request this quote belongs to
    @ManyToOne
    @JoinColumn(name = "request_id", nullable = false)
    private Request request;

    // The tailor who created this quote
    @ManyToOne
    @JoinColumn(name = "tailor_id", nullable = false)
    private User tailor;

    private Double price;
    private Double advanceAmount;
    private int deliveryDays;
    private String fabricPlan;

    @Enumerated(EnumType.STRING)
    private QuoteStatus status;

    private LocalDateTime createdAt;

    // ---------------- Getters and Setters ----------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public User getTailor() {
        return tailor;
    }

    public void setTailor(User tailor) {
        this.tailor = tailor;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getAdvanceAmount() {
        return advanceAmount;
    }

    public void setAdvanceAmount(Double advanceAmount) {
        this.advanceAmount = advanceAmount;
    }

    public int getDeliveryDays() {
        return deliveryDays;
    }

    public void setDeliveryDays(int deliveryDays) {
        this.deliveryDays = deliveryDays;
    }

    public String getFabricPlan() {
        return fabricPlan;
    }

    public void setFabricPlan(String fabricPlan) {
        this.fabricPlan = fabricPlan;
    }

    public QuoteStatus getStatus() {
        return status;
    }

    public void setStatus(QuoteStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
