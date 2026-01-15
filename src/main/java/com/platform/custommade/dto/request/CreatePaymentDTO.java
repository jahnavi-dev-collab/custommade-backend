package com.platform.custommade.dto.request;

public class CreatePaymentDTO {
    private Long orderId;
    private Double amount;
    private String paymentGatewayId;

    // Getters & Setters
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getPaymentGatewayId() { return paymentGatewayId; }
    public void setPaymentGatewayId(String paymentGatewayId) { this.paymentGatewayId = paymentGatewayId; }
}
