package com.platform.custommade.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RequestDisputeDTO {

    @NotNull
    private Long orderId;

    @NotNull
    private Long raisedById;

    @NotNull
    @Size(min = 3, max = 500)
    private String reason;

    private String description; // optional

    // Getters & Setters
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Long getRaisedById() { return raisedById; }
    public void setRaisedById(Long raisedById) { this.raisedById = raisedById; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
