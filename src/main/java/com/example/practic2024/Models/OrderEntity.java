package com.example.practic2024.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date date_end;
    private String status;
    @ManyToOne
    @JoinColumn(name = "firm_id",nullable = false)
    private ShopFirmEntity firm;
    @ManyToOne
    @JoinColumn(name = "shop_id", nullable = false)
    private ShopEntity shop;

    public OrderEntity(){

    }

    public OrderEntity(Date date_end, String status, ShopFirmEntity firm, ShopEntity shop) {
        this.date_end = date_end;
        this.status = status;
        this.firm = firm;
        this.shop = shop;
    }
}
