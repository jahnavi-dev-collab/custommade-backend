package com.platform.custommade.controller;

import com.platform.custommade.dto.response.TailorRatingDTO;
import com.platform.custommade.model.Review;
import com.platform.custommade.service.ReviewService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/{orderId}")
    public Review createReview(@PathVariable Long orderId,
                               @RequestParam int rating,
                               @RequestParam String comment,
                               Authentication authentication) {

        String customerEmail = authentication.getName();
        return reviewService.createReview(orderId, rating, comment, customerEmail);
    }

    @GetMapping("/tailor/{tailorId}/rating")
    public TailorRatingDTO getTailorRating(@PathVariable Long tailorId) {
        double averageRating = reviewService.calculateAverageRating(tailorId);
        long reviewCount = reviewService.getReviewCountByTailor(tailorId);

        return new TailorRatingDTO(tailorId, averageRating, reviewCount);
    }


}

