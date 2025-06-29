package com.se114p12.backend.services.review;

import com.se114p12.backend.entities.order.Order;
import com.se114p12.backend.entities.review.Review;
import com.se114p12.backend.dtos.review.ReviewRequestDTO;
import com.se114p12.backend.dtos.review.ReviewResponseDTO;
import com.se114p12.backend.mappers.review.ReviewMapper;
import com.se114p12.backend.repositories.review.ReviewRepository;
import com.se114p12.backend.repositories.order.OrderRepository;
import com.se114p12.backend.vo.PageVO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final ReviewMapper reviewMapper;

    @Override
    @Transactional(readOnly = true)
    public PageVO<ReviewResponseDTO> getReviews(Pageable pageable) {
        Page<Review> reviewPage = reviewRepository.findAll(pageable);
        return convertToPageVO(reviewPage);
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewResponseDTO getReviewsByOrder(Long orderId) {
        Review reviewPage = reviewRepository.findByOrderId(orderId);
        return reviewMapper.toResponse(reviewPage);
    }

    @Override
    @Transactional
    public ReviewResponseDTO replyToReview(Long reviewId, String reply) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));
        review.setReply(reply);
        return reviewMapper.toResponse(reviewRepository.save(review));
    }

    @Override
    @Transactional
    public ReviewResponseDTO createReview(ReviewRequestDTO request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        Review review = new Review()
                .setOrder(order)
                .setRate(request.getRate())
                .setContent((request.getContent() == null
                        || request.getContent().trim().isEmpty())
                        ? null : request.getContent().trim())
                .setReply(null);

        return reviewMapper.toResponse(reviewRepository.save(review));
    }

    @Override
    @Transactional
    public ReviewResponseDTO updateReview(Long id, ReviewRequestDTO request) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));

        if (review.getReply() != null && !review.getReply().isEmpty()) {
            throw new IllegalStateException("This order was already replied to");
        }

        review.setRate(request.getRate());
        review.setContent(request.getContent());

        Review updatedReview = reviewRepository.save(review);
        return reviewMapper.toResponse(updatedReview);
    }

    @Override
    @Transactional
    public void deleteReview(Long reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new EntityNotFoundException("Review not found");
        }
        reviewRepository.deleteById(reviewId);
    }

    private PageVO<ReviewResponseDTO> convertToPageVO(Page<Review> reviewPage) {
        return PageVO.<ReviewResponseDTO>builder()
                .page(reviewPage.getNumber())
                .size(reviewPage.getSize())
                .totalElements(reviewPage.getTotalElements())
                .totalPages(reviewPage.getTotalPages())
                .numberOfElements(reviewPage.getNumberOfElements())
                .content(reviewPage.map(reviewMapper::toResponse).getContent())
                .build();
    }
}