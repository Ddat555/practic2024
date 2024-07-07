package com.example.practic2024.Models;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer count;
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    public ProductEntity(){

    }

    public ProductEntity(String name, Integer count, OrderEntity order) {
        this.name = name;
        this.count = count;
        this.order = order;
    }
}
