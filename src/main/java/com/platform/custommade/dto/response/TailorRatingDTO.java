package com.platform.custommade.dto.response;

public class TailorRatingDTO {

    private Long tailorId;
    private double averageRating;
    private long reviewCount;

    public TailorRatingDTO(Long tailorId, double averageRating, long reviewCount) {
        this.tailorId = tailorId;
        this.averageRating = averageRating;
        this.reviewCount = reviewCount;
    }

    public Long getTailorId() {
        return tailorId;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public long getReviewCount() {
        return reviewCount;
    }
}
