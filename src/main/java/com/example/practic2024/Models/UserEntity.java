package com.example.practic2024.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String password;
    private Date registrationDate;
    private String role;

    public UserEntity(){

    }

    public UserEntity(String login, String password, String role) {
        this.login = login;
        this.password = password;
        registrationDate = new Date();
        this.role = role;
    }
}
