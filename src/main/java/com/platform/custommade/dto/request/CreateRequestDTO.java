package com.platform.custommade.dto.request;

import java.time.LocalDateTime;

public class CreateRequestDTO {

    private String category;
    private String description;
    private String fabricPref;
    private LocalDateTime expectedDeliveryDate;

    // -------- GETTERS & SETTERS --------

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

    public LocalDateTime getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(LocalDateTime expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }
}
