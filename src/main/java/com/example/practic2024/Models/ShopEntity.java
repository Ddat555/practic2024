package com.example.practic2024.Models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ShopEntity {

    @ManyToOne
    @JoinColumn(name = "firm_id",nullable = false)
    private ShopFirmEntity firm;
    @ManyToOne
    @JoinColumn(name = "city_name", nullable = false)
    private CityEntity city;
    private String address;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public ShopEntity(){

    }

    public ShopEntity(ShopFirmEntity firm, CityEntity city, String address) {
        this.firm = firm;
        this.city = city;
        this.address = address;
    }
}
