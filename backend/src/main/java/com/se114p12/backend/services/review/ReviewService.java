package com.se114p12.backend.services.review;

import com.se114p12.backend.dtos.review.ReviewRequestDTO;
import com.se114p12.backend.dtos.review.ReviewResponseDTO;
import com.se114p12.backend.vo.PageVO;
import org.springframework.data.domain.Pageable;

public interface ReviewService {
    PageVO<ReviewResponseDTO> getReviews(Pageable pageable);

    ReviewResponseDTO getReviewsByOrder(Long orderId);

    ReviewResponseDTO replyToReview(Long reviewId, String reply);

    ReviewResponseDTO createReview(ReviewRequestDTO request);

    ReviewResponseDTO updateReview(Long id, ReviewRequestDTO request);

    void deleteReview(Long reviewId);
}