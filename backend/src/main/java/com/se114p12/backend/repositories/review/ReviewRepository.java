package com.se114p12.backend.repositories.review;

import com.se114p12.backend.entities.review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, JpaSpecificationExecutor<Review>,
        PagingAndSortingRepository<Review, Long> {

    // Lấy tất cả review (phân trang)
    Page<Review> findAll(Pageable pageable);

    // Lấy review theo orderId (phân trang)
    Review findByOrderId(Long orderId);
}