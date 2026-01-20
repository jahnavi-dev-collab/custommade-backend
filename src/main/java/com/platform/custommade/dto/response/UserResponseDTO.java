package com.platform.custommade.dto.response;

import com.platform.custommade.model.Role;

public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private Role role;

    private double averageRating;
    private long reviewCount;

    // Constructor
    public UserResponseDTO(Long id, String name, String email, String phone, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }

    public UserResponseDTO(Long id, String name, String email, String phone,
                           Role role, double averageRating, long reviewCount) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.averageRating = averageRating;
        this.reviewCount = reviewCount;
    }

    // Getters only (NO setters)
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Role getRole() {
        return role;
    }

    public double getAverageRating() { return averageRating; }
    public long getReviewCount() { return reviewCount; }
}
