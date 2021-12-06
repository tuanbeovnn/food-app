package com.foodapp.backend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "orders_details")
@Getter
@Setter
public class OrderDetailsEntity extends BaseEntity {

    private double unitPrice;
    private int quantity;
    private double discount;
    private Double total;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity order;



}
