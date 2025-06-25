package com.se114p12.backend.services.review;

import com.se114p12.backend.dtos.review.ReviewRequestDTO;
import com.se114p12.backend.dtos.review.ReviewResponseDTO;
import com.se114p12.backend.vo.PageVO;
import org.springframework.data.domain.Pageable;

public interface ReviewService {
    PageVO<ReviewResponseDTO> getReviews(Pageable pageable);

    PageVO<ReviewResponseDTO> getReviewsByOrder(Long orderId, Pageable pageable);

    ReviewResponseDTO replyToReview(Long reviewId, String reply);

    ReviewResponseDTO createReview(ReviewRequestDTO request);

    ReviewResponseDTO updateReview(Long id, ReviewRequestDTO request);

    void deleteReview(Long reviewId);
}