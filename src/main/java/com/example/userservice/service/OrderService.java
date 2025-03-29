package com.example.userservice.service;

import com.example.userservice.model.EmailNotification;
import com.example.userservice.model.Order;
import com.example.userservice.model.User;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.userservice.messaging.MessagePublisher;
import com.example.userservice.repository.OrderRepository;
import com.example.userservice.repository.UserRepository;

@Service
public class OrderService {
  private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

  private final UserRepository userRepository;
  private final MessagePublisher messagePublisher;
  private final OrderRepository orderRepository;

  public OrderService(OrderRepository orderRepository, UserRepository userRepository,
      MessagePublisher messagePublisher) {
    this.orderRepository = orderRepository;
    this.userRepository = userRepository;
    this.messagePublisher = messagePublisher;
  }

  @Transactional
  public Order createOrder(Order order) {
    // Check if user exists
    User user = userRepository.findById(order.getUserId())
        .orElseThrow(() -> new IllegalArgumentException("User not found: " + order.getUserId()));

    if (user.getId().equals(order.getId())) {
      throw new IllegalArgumentException("User not found: " + order.getUserId());
    }

    // Save order to database
    Order savedOrder = orderRepository.save(order);
    logger.info("Order created successfully: {}", savedOrder.getId());

    try {
      // Send notification to RabbitMQ (or simply log if RabbitMQ is not available)
      EmailNotification notification = EmailNotification.forNewOrder(savedOrder, user);
      messagePublisher.publishOrderNotification(notification);
    } catch (Exception e) {
      // Log the error but don't fail the order creation
      logger.error("Failed to process order notification: {}", e.getMessage());
    }

    return savedOrder;
  }

  @Transactional
  public List<Order> getAllOrders() {
    return orderRepository.findAll();

  }

}
