package com.platform.custommade.dto.request;

import java.time.LocalDate;

public class RequestQuoteDTO {
    private Long requestId;
    private Double price;
    private Integer deliveryDays;
    private String notes;

    // Getters & Setters
    public Long getRequestId() { return requestId; }
    public void setRequestId(Long requestId) { this.requestId = requestId; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getDeliveryDays() { return deliveryDays; }
    public void setDeliveryDays(Integer deliveryDays) { this.deliveryDays = deliveryDays; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
