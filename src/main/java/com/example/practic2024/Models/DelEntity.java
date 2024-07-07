package com.example.practic2024.Models;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class DelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String address;
    @OneToOne
    @JoinColumn(name = "userID", nullable = false)
    private UserEntity user;

    public DelEntity(){

    }

    public DelEntity(String name, String phone, String email, String address, UserEntity user) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.user = user;
    }
}
