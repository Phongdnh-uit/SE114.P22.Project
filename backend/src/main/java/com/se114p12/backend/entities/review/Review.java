package com.se114p12.backend.entities.review;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.se114p12.backend.entities.BaseEntity;
import com.se114p12.backend.entities.user.User;
import com.se114p12.backend.entities.order.Order;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "reviews")
public class Review extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference
    private Order order;

    // 5 mức đánh giá: 1, 2, 3, 4, 5
    @Column(name = "rating_score", nullable = false, columnDefinition = "TINYINT")
    private Integer rate;

    // Reference: https://stackoverflow.com/a/31610134
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "reply", columnDefinition = "TEXT")
    private String reply;
}
