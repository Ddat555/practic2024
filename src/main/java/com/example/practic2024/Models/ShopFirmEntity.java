package com.example.practic2024.Models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ShopFirmEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String phone;
    private String email;
    @OneToOne
    @JoinColumn(name = "userID", nullable = false)
    private UserEntity user;

    public ShopFirmEntity(){

    }

    public ShopFirmEntity(String name, String phone, String email, UserEntity user) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.user = user;
    }
}
