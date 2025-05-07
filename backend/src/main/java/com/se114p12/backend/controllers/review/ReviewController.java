package com.se114p12.backend.controller;

import com.se114p12.backend.dto.request.review.ReviewRequest;
import com.se114p12.backend.dto.response.review.ReviewResponse;
import com.se114p12.backend.service.ReviewService;
import com.se114p12.backend.vo.PageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // Tạo đánh giá mới
    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(@RequestBody ReviewRequest request) {
        return ResponseEntity.ok(reviewService.createReview(request));
    }

    // Lấy tất cả đánh giá
    @GetMapping
    public ResponseEntity<PageVO<ReviewResponse>> getAllReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(reviewService.getReviews(page, size));
    }

    // Lấy đánh giá theo người dùng
    @GetMapping("/user/{userId}")
    public ResponseEntity<PageVO<ReviewResponse>> getReviewsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(reviewService.getReviewsByUser(userId, page, size));
    }

    // Lấy đánh giá theo đơn hàng
    @GetMapping("/order/{orderId}")
    public ResponseEntity<PageVO<ReviewResponse>> getReviewsByOrder(
            @PathVariable Long orderId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(reviewService.getReviewsByOrder(orderId, page, size));
    }

    // Phản hồi một đánh giá
    @PatchMapping("/{reviewId}/reply")
    public ResponseEntity<ReviewResponse> replyToReview(
            @PathVariable Long reviewId,
            @RequestParam String reply
    ) {
        return ResponseEntity.ok(reviewService.replyToReview(reviewId, reply));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}