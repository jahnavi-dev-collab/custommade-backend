package com.platform.custommade.dto.response;

import java.time.LocalDateTime;

public class RequestResponseDTO {

    private Long id;
    private String category;
    private String description;
    private String fabricPref;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime expectedDeliveryDate;

    // -------- GETTERS & SETTERS --------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFabricPref() {
        return fabricPref;
    }

    public void setFabricPref(String fabricPref) {
        this.fabricPref = fabricPref;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(LocalDateTime expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

}
