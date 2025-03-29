package com.example.userservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.userservice.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
  Optional<Order> findByOrderNumber(Long orderNumber);

  boolean existsByOrderNumber(Long orderNumber);
}
