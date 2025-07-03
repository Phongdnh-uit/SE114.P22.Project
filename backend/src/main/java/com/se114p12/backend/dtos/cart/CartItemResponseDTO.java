package com.se114p12.backend.dtos.cart;

import com.se114p12.backend.dtos.BaseResponseDTO;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;

@Data
public class CartItemResponseDTO extends BaseResponseDTO {
  private Long cartId;
  private Long quantity;
  private BigDecimal price;

  private Long productId;
  private String productName;

  private Set<Long> variationOptionIds;
  private String VariationOptionInfo;

  private String imageUrl;
  private Boolean available;
}
