package com.platform.custommade.service;

import com.platform.custommade.exception.ResourceNotFoundException;
import com.platform.custommade.exception.ConflictException;
import com.platform.custommade.model.Order;
import com.platform.custommade.model.OrderStatus;
import com.platform.custommade.model.Review;
import com.platform.custommade.repository.OrderRepository;
import com.platform.custommade.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;

    public ReviewService(ReviewRepository reviewRepository,
                         OrderRepository orderRepository) {
        this.reviewRepository = reviewRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Review createReview(Long orderId, int rating, String comment, String customerEmail) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!order.getCustomer().getEmail().equals(customerEmail)) {
            throw new ConflictException("Not your order");
        }

        if (order.getStatus() != OrderStatus.COMPLETED) {
            throw new ConflictException("Order must be COMPLETED to review");
        }

        if (reviewRepository.existsByOrderId(orderId)) {
            throw new ConflictException("Review already exists");
        }

        Review review = new Review();
        review.setOrder(order);
        review.setCustomer(order.getCustomer());
        review.setTailor(order.getTailor());
        review.setRating(rating);
        review.setComment(comment);
        review.setCreatedAt(LocalDateTime.now());

        return reviewRepository.save(review);
    }

    public double calculateAverageRating(Long tailorId) {
        var reviews = reviewRepository.findByTailorId(tailorId);

        if (reviews.isEmpty()) {
            return 0.0;
        }
        double sum = reviews.stream()
                .mapToInt(Review::getRating)
                .sum();
        return sum / reviews.size();
    }

    public long getReviewCountByTailor(Long tailorId) {
        return reviewRepository.countByTailorId(tailorId);
    }
}

