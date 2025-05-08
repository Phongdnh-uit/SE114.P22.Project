//package com.se114p12.backend.domains.review;
//
//import com.se114p12.backend.domains.BaseEntity;
//import com.se114p12.backend.domains.authentication.User;
//import com.se114p12.backend.domains.order.Order;
//import jakarta.persistence.*;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.Accessors;
//
//@Data
//@Entity
//@Accessors(chain = true)
//@Table(name = "reviews")
//public class Review extends BaseEntity {
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "order_id", nullable = false)
//    private Order order;
//
//    // 5 mức đánh giá: 1, 2, 3, 4, 5
//    @Column(name = "rating_score", nullable = false, columnDefinition = "TINYINT")
//    private Integer rate;
//
//    // Reference: https://stackoverflow.com/a/31610134
//    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
//    private String content;
//
//    @Column(name = "reply", columnDefinition = "TEXT")
//    private String reply;
//}
