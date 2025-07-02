package com.se114p12.backend.services.order;

import com.se114p12.backend.configs.ShopLocationConfig;
import com.se114p12.backend.dtos.delivery.DeliveryResponseDTO;
import com.se114p12.backend.dtos.nofitication.NotificationRequestDTO;
import com.se114p12.backend.dtos.order.OrderRequestDTO;
import com.se114p12.backend.dtos.order.OrderResponseDTO;
import com.se114p12.backend.entities.cart.Cart;
import com.se114p12.backend.entities.cart.CartItem;
import com.se114p12.backend.entities.order.Order;
import com.se114p12.backend.entities.order.OrderDetail;
import com.se114p12.backend.entities.product.Product;
import com.se114p12.backend.entities.promotion.Promotion;
import com.se114p12.backend.entities.shipper.Shipper;
import com.se114p12.backend.entities.variation.VariationOption;
import com.se114p12.backend.enums.NotificationType;
import com.se114p12.backend.enums.OrderStatus;
import com.se114p12.backend.enums.PaymentMethod;
import com.se114p12.backend.enums.PaymentStatus;
import com.se114p12.backend.exceptions.BadRequestException;
import com.se114p12.backend.exceptions.ResourceNotFoundException;
import com.se114p12.backend.mappers.order.OrderMapper;
import com.se114p12.backend.neo4j.entities.BoughtWithRelationship;
import com.se114p12.backend.neo4j.entities.CategoryNode;
import com.se114p12.backend.neo4j.entities.OrderedRelationship;
import com.se114p12.backend.neo4j.entities.ProductNode;
import com.se114p12.backend.neo4j.entities.UserNode;
import com.se114p12.backend.neo4j.repositories.ProductNeo4jRepository;
import com.se114p12.backend.neo4j.repositories.UserNeo4jRepository;
import com.se114p12.backend.repositories.authentication.UserRepository;
import com.se114p12.backend.repositories.cart.CartItemRepository;
import com.se114p12.backend.repositories.cart.CartRepository;
import com.se114p12.backend.repositories.order.OrderDetailRepository;
import com.se114p12.backend.repositories.order.OrderRepository;
import com.se114p12.backend.repositories.promotion.PromotionRepository;
import com.se114p12.backend.repositories.shipper.ShipperRepository;
import com.se114p12.backend.services.delivery.MapService;
import com.se114p12.backend.services.notification.NotificationService;
import com.se114p12.backend.services.promotion.UserPromotionService;
import com.se114p12.backend.util.JwtUtil;
import com.se114p12.backend.util.VnPayUtils;
import com.se114p12.backend.vo.PageVO;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
  private final ShopLocationConfig shopLocationConfig;

  private final OrderRepository orderRepository;
  private final OrderDetailRepository orderDetailRepository;
  private final OrderMapper orderMapper;

  private final UserRepository userRepository;
  private final JwtUtil jwtUtil;

  private final CartRepository cartRepository;
  private final CartItemRepository cartItemRepository;

  private final ShipperRepository shipperRepository;

  private final PromotionRepository promotionRepository;
  private final UserPromotionService userPromotionService;

  private final UserNeo4jRepository userNeo4jRepository;
  private final ProductNeo4jRepository productNeo4jRepository;

  private final MapService mapService;
  private final NotificationService notificationService;

  @Override
  public PageVO<OrderResponseDTO> getAll(Specification<Order> specification, Pageable pageable) {
    Page<Order> orders = orderRepository.findAll(specification, pageable);
    return PageVO.<OrderResponseDTO>builder()
        .content(orders.getContent().stream().map(orderMapper::entityToResponseDTO).toList())
        .totalElements(orders.getTotalElements())
        .totalPages(orders.getTotalPages())
        .numberOfElements(orders.getNumberOfElements())
        .page(orders.getNumber())
        .size(orders.getSize())
        .build();
  }

  @Override
  public OrderResponseDTO getById(Long id) {
    Order order =
        orderRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    return orderMapper.entityToResponseDTO(order);
  }

  @Override
  public PageVO<OrderResponseDTO> getOrdersByUserId(
      Long userId, Specification<Order> specification, Pageable pageable) {
    Specification<Order> userSpec =
        (root, _, criteriaBuilder) -> criteriaBuilder.equal(root.get("user").get("id"), userId);
    specification = specification == null ? userSpec : userSpec.and(specification);
    Page<Order> orders = orderRepository.findAll(specification, pageable);
    return PageVO.<OrderResponseDTO>builder()
        .content(orders.getContent().stream().map(orderMapper::entityToResponseDTO).toList())
        .totalElements(orders.getTotalElements())
        .totalPages(orders.getTotalPages())
        .numberOfElements(orders.getNumberOfElements())
        .page(orders.getNumber())
        .size(orders.getSize())
        .build();
  }

  @Transactional
  @Override
  public OrderResponseDTO create(OrderRequestDTO orderRequestDTO) {
    Long currentUserId = jwtUtil.getCurrentUserId();
    Cart cart =
        cartRepository
            .findByUserId(currentUserId)
            .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

    if (cart.getCartItems().isEmpty()) {
      throw new IllegalArgumentException("Cart is empty");
    }

    Order order = new Order();
    order.setDestinationLatitude(orderRequestDTO.getDestinationLatitude());
    order.setDestinationLongitude(orderRequestDTO.getDestinationLongitude());
    order.setShippingAddress(orderRequestDTO.getShippingAddress());
    order.setNote(orderRequestDTO.getNote());
    order.setUser(
        userRepository
            .findById(currentUserId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found")));

    order.setPaymentMethod(orderRequestDTO.getPaymentMethod());

    // Gán trạng thái cho đơn hàng tùy vào phương thức thanh toán
    OrderStatus initialStatus =  OrderStatus.PENDING;

    order.setOrderStatus(initialStatus);

    order.setPaymentStatus(PaymentStatus.PENDING);

    order.setOrderDetails(new ArrayList<>());
    order.setActualDeliveryTime(null); // sẽ được cập nhật bởi khách

    BigDecimal totalPrice = BigDecimal.ZERO;

    for (CartItem cartItem : cart.getCartItems()) {
      Product product = cartItem.getProduct();
      OrderDetail orderDetail = new OrderDetail();
      orderDetail.setOrder(order);
      orderDetail.setProductId(product.getId());
      orderDetail.setProductName(product.getName());
      orderDetail.setProductImage(product.getImageUrl());
      orderDetail.setQuantity(cartItem.getQuantity());
      orderDetail.setVariationInfo(extractVariationInfo(cartItem));
      orderDetail.setPrice(product.getOriginalPrice());
      orderDetail.setCategoryId(product.getCategory().getId());
      orderDetail.setCategoryName(product.getCategory().getName());
      totalPrice =
          totalPrice.add(
              orderDetail.getPrice().multiply(BigDecimal.valueOf(orderDetail.getQuantity())));
      order.getOrderDetails().add(orderDetail);

      cartItem.getVariationOptions().clear();
      cartItemRepository.delete(cartItem);
    }

    // Trừ giá trị Order theo Promotion
    if (orderRequestDTO.getPromotionId() != null) {
      Promotion appliedPromotion =
          userPromotionService.applyPromotion(
              jwtUtil.getCurrentUserId(), orderRequestDTO.getPromotionId(), totalPrice);

      // Trừ giảm giá nếu có
      BigDecimal discount = appliedPromotion.getDiscountValue();
      totalPrice = totalPrice.subtract(discount);
      userPromotionService.markPromotionAsUsed(
          jwtUtil.getCurrentUserId(), appliedPromotion.getId());
    }

    order.setTotalPrice(
        (totalPrice.compareTo(BigDecimal.ZERO) >= 0) ? totalPrice : BigDecimal.ZERO);

    order.setTxnRef(VnPayUtils.generateTxnRef("ORD"));
    orderRepository.save(order);

    // Gửi thông báo trạng thái
    if (initialStatus != null) {
      sendOrderStatusNotification(order);
    }

    cart.getCartItems().clear();
    cartRepository.deleteById(cart.getId());
    // Gửi thông báo trạng thái
    sendOrderStatusNotification(order);
    updateRecommend(order);
    return orderMapper.entityToResponseDTO(order);
  }

  @Override
  public OrderResponseDTO update(Long id, OrderRequestDTO orderRequestDTO) {
    Order order =
        orderRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

    if (orderRequestDTO.getDestinationLatitude() != null
        && orderRequestDTO.getDestinationLongitude() != null) {
      order.setDestinationLatitude(orderRequestDTO.getDestinationLatitude());
      order.setDestinationLongitude(orderRequestDTO.getDestinationLongitude());
    }

    if (orderRequestDTO.getNote() != null) {
      order.setNote(orderRequestDTO.getNote());
    }

    if (orderRequestDTO.getPaymentMethod() != null) {
      order.setPaymentMethod(orderRequestDTO.getPaymentMethod());
    }

    return orderMapper.entityToResponseDTO(orderRepository.save(order));
  }

  @Override
  public void cancelOrder(Long id) {
    Order order =
        orderRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    if (order.getOrderStatus().getCode() > 1) {
      throw new BadRequestException("Can't cancel order because order is confirm");
    }
    order.setOrderStatus(OrderStatus.CANCELED);
    orderRepository.save(order);
    sendOrderStatusNotification(order);
  }

  @Override
  public void delete(Long id) {
    Order order =
        orderRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    orderRepository.delete(order);
  }

  @Override
  @Transactional
  public void markOrderAsDelivered(Long orderId) {
    Order order =
        orderRepository
            .findById(orderId)
            .orElseThrow(() -> new NoSuchElementException("Order not found"));

    if (!order.getOrderStatus().equals(OrderStatus.SHIPPING)) {
      throw new IllegalStateException("Order must be in SHIPPING status to mark as delivered.");
    }

    order.setOrderStatus(OrderStatus.COMPLETED);
    order.setActualDeliveryTime(Instant.now());

    // Cập nhật shipper: đánh dấu là available
    if (order.getShipper() != null) {
      Shipper shipper = order.getShipper();
      shipper.setIsAvailable(true);
      shipperRepository.save(shipper);
    }

    orderRepository.save(order);

    sendOrderStatusNotification(order);
  }

  @Override
  public void updateStatus(Long id, OrderStatus status) {
    Order order =
        orderRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    int flag = status.getCode() - order.getOrderStatus().getCode();
    if (status != OrderStatus.CANCELED && flag != 1) {
      throw new BadRequestException("Can't update order status to " + status);
    }
    if (status == OrderStatus.SHIPPING) {
      List<Shipper> availableShippers = shipperRepository.findByIsAvailableTrue();
      if (availableShippers.isEmpty()) {
        throw new BadRequestException("No available shippers to assign for this order");
      }

      Shipper selectedShipper =
          availableShippers.get(new Random().nextInt(availableShippers.size()));
      selectedShipper.setIsAvailable(false);
      order.setShipper(selectedShipper);

      // Tọa độ địa chỉ cứng của quán
      double originLat = shopLocationConfig.getLat();
      double originLng = shopLocationConfig.getLng();

      // Tọa độ địa chỉ của khách
      Double destLat = order.getDestinationLatitude();
      Double destLng = order.getDestinationLongitude();

      if (destLat == null || destLng == null) {
        throw new BadRequestException("Destination coordinates are missing.");
      }

      try {
        DeliveryResponseDTO deliveryInfo =
            mapService.calculateExpectedDeliveryTime(originLat, originLng, destLat, destLng);
        Instant now = Instant.now();
        Instant expectedTime =
            now.plusSeconds(deliveryInfo.getExpectedDeliveryTimeInSeconds() + 300);
        order.setExpectedDeliveryTime(expectedTime);
      } catch (Exception e) {
        throw new BadRequestException(
            "Unable to calculate expected delivery time: " + e.getMessage());
      }
    }

    order.setOrderStatus(status);
    orderRepository.save(order);

    sendOrderStatusNotification(order);
  }

  @Override
  @Transactional
  public Long markPaymentCompleted(String txnRef) {
    Order order = orderRepository.findByTxnRef(txnRef)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

    // chỉ ghi nếu chưa ghi
    if (order.getPaymentStatus() != PaymentStatus.COMPLETED) {
      order.setPaymentStatus(PaymentStatus.COMPLETED);
      order.setOrderStatus(OrderStatus.PENDING); // giao hàng chưa bắt đầu
      orderRepository.save(order);
      sendPaymentStatusNotification(order);
      sendOrderStatusNotification(order);
    }

    return order.getId();
  }

  @Override
  @Transactional
  public void markPaymentFailed(String txnRef) {
    Order order =
        orderRepository
            .findByTxnRef(txnRef)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

    order.setPaymentStatus(PaymentStatus.FAILED);
    orderRepository.save(order);

    sendPaymentStatusNotification(order);
  }

  // ============================ SUPPORT METHOD ============================
  private void sendNotificationForOrder(Order order, NotificationType type) {
    if (type == null) return;

    NotificationRequestDTO notification = new NotificationRequestDTO();
    notification.setTitle(type.getTitle());
    notification.setMessage(type.formatMessage(order.getTxnRef()));
    notification.setType(type);
    notification.setUserIds(List.of(order.getUser().getId()));

    notificationService.pushNotification(notification);
  }

  @Async
  private void sendOrderStatusNotification(Order order) {
    NotificationType type =
        switch (order.getOrderStatus()) {
          case PENDING -> NotificationType.ORDER_PLACED;
          case CONFIRMED -> NotificationType.ORDER_RECEIVED;
          case PROCESSING -> NotificationType.ORDER_PREPARING;
          case SHIPPING -> NotificationType.ORDER_DELIVERING;
          case COMPLETED -> NotificationType.ORDER_DELIVERED;
          case CANCELED -> NotificationType.ORDER_CANCELLED;
        };

    sendNotificationForOrder(order, type);
  }

  private void sendPaymentStatusNotification(Order order) {
    NotificationType type =
        switch (order.getPaymentStatus()) {
          case COMPLETED -> NotificationType.ORDER_PAYMENT_SUCCEEDED;
          case FAILED -> NotificationType.ORDER_PAYMENT_FAILED;
          default -> null;
        };

    sendNotificationForOrder(order, type);
  }

  private String extractVariationInfo(CartItem cartItem) {
    if (cartItem.getVariationOptions() == null || cartItem.getVariationOptions().isEmpty()) {
      return null;
    }

    // Đảm bảo thứ tự Variation
    return cartItem.getVariationOptions().stream()
        .filter(vo -> vo.getVariation() != null)
        .collect(Collectors.groupingBy(VariationOption::getVariation))
        .entrySet()
        .stream()
        .sorted(Comparator.comparing(entry -> entry.getKey().getId())) // Đảm bảo thứ tự Variation
        .map(
            entry -> {
              String variationName = entry.getKey().getName();
              String values =
                  entry.getValue().stream()
                      .map(VariationOption::getValue)
                      .collect(Collectors.joining(", "));
              return variationName + ": " + values;
            })
        .collect(Collectors.joining(", "));
  }

  // ============================ NEO4J RECOMMEND SYSTEM ============================
  @Async
  private void updateRecommend(Order order) {
    // Neo4j recommendation systemAdd commentMore actions

    Long currentUserId = jwtUtil.getCurrentUserId();
    // 1. User ordered products
    UserNode userNode =
        userNeo4jRepository
            .findById(currentUserId)
            .orElseGet(
                () -> {
                  UserNode newUser = new UserNode();
                  newUser.setId(currentUserId);
                  newUser.setOrderedProducts(new ArrayList<>());
                  return newUser;
                });

    Map<Long, OrderedRelationship> orderedMap =
        userNode.getOrderedProducts().stream()
            .collect(Collectors.toMap(rel -> rel.getProductNode().getId(), rel -> rel));
    Map<Long, ProductNode> productNodeMap = new HashMap<>();

    for (OrderDetail detail : order.getOrderDetails()) {
      Long productId = detail.getProductId();
      ProductNode productNode =
          productNeo4jRepository
              .findById(productId)
              .orElseGet(
                  () -> {
                    ProductNode newProductNode = new ProductNode();
                    newProductNode.setId(productId);
                    CategoryNode categoryNode = new CategoryNode();
                    categoryNode.setId(detail.getCategoryId());
                    newProductNode.setCategory(categoryNode);
                    return productNeo4jRepository.save(newProductNode);
                  });
      productNodeMap.put(productId, productNode);

      OrderedRelationship rel = orderedMap.get(productId);
      if (rel != null) {
        rel.setCount(rel.getCount() + detail.getQuantity());
      } else {
        OrderedRelationship newRel = new OrderedRelationship();
        newRel.setProductNode(productNode);
        newRel.setCount(detail.getQuantity());
        userNode.getOrderedProducts().add(newRel);
      }
    }
    userNeo4jRepository.save(userNode);

    // 2.bought with relationship
    List<ProductNode> productNodes = new ArrayList<>(productNodeMap.values());
    int n = productNodes.size();
    for (int i = 0; i < n; i++) {
      for (int j = i + 1; j < n; j++) {
        ProductNode p1 = productNodes.get(i);
        ProductNode p2 = productNodes.get(j);

        upsertBoughtWith(p1, p2);
        upsertBoughtWith(p2, p1);
      }
    }
    productNeo4jRepository.saveAll(productNodes);
  }

  private void upsertBoughtWith(ProductNode source, ProductNode target) {
    for (BoughtWithRelationship rel : source.getCoPurchasedProducts()) {
      if (rel.getProduct().getId().equals(target.getId())) {
        rel.setCount(rel.getCount() + 1);
        return;
      }
    }
    BoughtWithRelationship newRel = new BoughtWithRelationship();
    newRel.setProduct(target);
    newRel.setCount(1L);
    source.getCoPurchasedProducts().add(newRel);
  }
}
