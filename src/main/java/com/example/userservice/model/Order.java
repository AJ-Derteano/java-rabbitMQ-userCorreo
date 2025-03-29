package com.example.userservice.model;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PostPersist;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "orders")
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull(message = "User ID is required")
  private Long userId;

  private String orderNumber;

  private LocalDateTime orderDate = LocalDateTime.now();

  @NotBlank(message = "RUC is required")
  private String ruc;

  @NotBlank(message = "Address is required")
  private String address;

  public Order() {
  }

  public Order(Long userId, String ruc, String address) {
    this.userId = userId;
    this.ruc = ruc;
    this.address = address;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getOrderNumber() {
    return orderNumber;
  }

  public void setOrderNumber(String orderNumber) {
    this.orderNumber = orderNumber;
  }

  public LocalDateTime getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(LocalDateTime orderDate) {
    this.orderDate = orderDate;
  }

  public String getRuc() {
    return ruc;
  }

  public void setRuc(String ruc) {
    this.ruc = ruc;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;

    if (o == null || getClass() != o.getClass())
      return false;

    Order order = (Order) o;
    return Objects.equals(id, order.id) &&
        Objects.equals(userId, order.userId);
  }

  @PostPersist
  public void generateOrderNumber() {
    this.orderNumber = String.format("ORD-%06d", this.id);
  }

  @Override
  public String toString() {
    return "Order{" +
        "id=" + id +
        ", userId=" + userId +
        ", orderNumber=" + orderNumber +
        ", orderDate=" + orderDate +
        ", ruc='" + ruc + '\'' +
        ", address='" + address + '\'' +
        '}';
  }

}
