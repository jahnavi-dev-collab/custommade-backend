package com.platform.custommade.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link to customer
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    private String garmentType;

    private String gender;

    private String occasion;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private FabricOption fabricOption; // CUSTOMER or TAILOR

    private Double budgetMin;

    private Double budgetMax;

    private LocalDateTime deliveryDeadline;

    @Enumerated(EnumType.STRING)
    private RequestStatus status; // REQUEST_CREATED, CLOSED

    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters and Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getCustomer() { return customer; }
    public void setCustomer(User customer) { this.customer = customer; }

    public String getGarmentType() { return garmentType; }
    public void setGarmentType(String garmentType) { this.garmentType = garmentType; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getOccasion() { return occasion; }
    public void setOccasion(String occasion) { this.occasion = occasion; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public FabricOption getFabricOption() { return fabricOption; }
    public void setFabricOption(FabricOption fabricOption) { this.fabricOption = fabricOption; }

    public Double getBudgetMin() { return budgetMin; }
    public void setBudgetMin(Double budgetMin) { this.budgetMin = budgetMin; }

    public Double getBudgetMax() { return budgetMax; }
    public void setBudgetMax(Double budgetMax) { this.budgetMax = budgetMax; }

    public LocalDateTime getDeliveryDeadline() { return deliveryDeadline; }
    public void setDeliveryDeadline(LocalDateTime deliveryDeadline) { this.deliveryDeadline = deliveryDeadline; }

    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // Enums
    public enum FabricOption {
        CUSTOMER, TAILOR
    }

    public enum RequestStatus {
        REQUEST_CREATED, CLOSED
    }
}
