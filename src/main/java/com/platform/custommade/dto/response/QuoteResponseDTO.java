// QuoteResponseDTO.java — for Customer viewing quotes
package com.platform.custommade.dto.response;

public class QuoteResponseDTO {
    private Long id;
    private Long requestId;
    private Long tailorId;
    private Double price;
    private Integer deliveryDays;
    private String notes;
    private String status;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRequestId() { return requestId; }
    public void setRequestId(Long requestId) { this.requestId = requestId; }

    public Long getTailorId() { return tailorId; }
    public void setTailorId(Long tailorId) { this.tailorId = tailorId; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getDeliveryDays() { return deliveryDays; }
    public void setDeliveryDays(Integer deliveryDays) { this.deliveryDays = deliveryDays; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
