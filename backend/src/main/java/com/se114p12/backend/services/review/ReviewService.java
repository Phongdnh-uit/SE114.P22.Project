//package com.se114p12.backend.service;
//
//import com.se114p12.backend.domains.authentication.User;
//import com.se114p12.backend.domains.order.Order;
//import com.se114p12.backend.domains.review.Review;
//import com.se114p12.backend.dto.request.review.ReviewRequest;
//import com.se114p12.backend.dto.response.review.ReviewResponse;
//import com.se114p12.backend.mapper.ReviewMapper;
//import com.se114p12.backend.repository.review.ReviewRepository;
//import com.se114p12.backend.repository.authentication.UserRepository;
//import com.se114p12.backend.repository.order.OrderRepository;
//import com.se114p12.backend.vo.PageVO;
//import jakarta.persistence.EntityNotFoundException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.*;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@RequiredArgsConstructor
//public class ReviewService {
//
//    private final ReviewRepository reviewRepository;
//    private final UserRepository userRepository;
//    private final OrderRepository orderRepository;
//    private final ReviewMapper reviewMapper;
//
//    @Transactional
//    public ReviewResponse createReview(ReviewRequest request) {
//        User user = userRepository.findById(request.getUserId())
//                .orElseThrow(() -> new EntityNotFoundException("User not found"));
//
//        Order order = orderRepository.findById(request.getProductId())
//                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
//
//        Review review = new Review()
//                .setUser(user)
//                .setOrder(order)
//                .setRate(request.getRate())
//                .setContent(request.getContent())
//                .setReply(request.getReply());
//
//        return reviewMapper.toResponse(reviewRepository.save(review));
//    }
//
//    @Transactional(readOnly = true)
//    public PageVO<ReviewResponse> getReviews(int page, int size) {
//        Page<Review> reviewPage = reviewRepository.findAll(PageRequest.of(page, size, Sort.by("createdAt").descending()));
//        return convertToPageVO(reviewPage);
//    }
//
//    @Transactional(readOnly = true)
//    public PageVO<ReviewResponse> getReviewsByUser(Long userId, int page, int size) {
//        Page<Review> reviewPage = reviewRepository.findByUserId(userId, PageRequest.of(page, size, Sort.by("createdAt").descending()));
//        return convertToPageVO(reviewPage);
//    }
//
//    @Transactional(readOnly = true)
//    public PageVO<ReviewResponse> getReviewsByOrder(Long orderId, int page, int size) {
//        Page<Review> reviewPage = reviewRepository.findByOrderId(orderId, PageRequest.of(page, size, Sort.by("createdAt").descending()));
//        return convertToPageVO(reviewPage);
//    }
//
//    @Transactional
//    public ReviewResponse replyToReview(Long reviewId, String reply) {
//        Review review = reviewRepository.findById(reviewId)
//                .orElseThrow(() -> new EntityNotFoundException("Review not found"));
//        review.setReply(reply);
//        return reviewMapper.toResponse(reviewRepository.save(review));
//    }
//
//    @Transactional
//    public void deleteReview(Long reviewId) {
//        if (!reviewRepository.existsById(reviewId)) {
//            throw new EntityNotFoundException("Review not found");
//        }
//        reviewRepository.deleteById(reviewId);
//    }
//
//    private PageVO<ReviewResponse> convertToPageVO(Page<Review> reviewPage) {
//        return PageVO.<ReviewResponse>builder()
//                .page(reviewPage.getNumber())
//                .size(reviewPage.getSize())
//                .totalElements(reviewPage.getTotalElements())
//                .totalPages(reviewPage.getTotalPages())
//                .numberOfElements(reviewPage.getNumberOfElements())
//                .content(reviewPage.map(reviewMapper::toResponse).getContent())
//                .build();
//    }
//}