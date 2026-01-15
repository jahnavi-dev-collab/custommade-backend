package com.platform.custommade.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CreateRequestDTO {

    @NotBlank
    private String category;   // Shirt, Suit, Dress, etc.

    @NotBlank
    private String description;

    private String fabricPref; // Optional

    @NotNull
    private LocalDateTime expectedDeliveryDate;

    // getters & setters
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
