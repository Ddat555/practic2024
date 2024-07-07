package com.example.practic2024.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class CityEntity {

    @Id
    private String name;
    private String region;
    private String timezone;

    public CityEntity(){

    }

    public CityEntity(String name, String region, String timezone) {
        this.name = name;
        this.region = region;
        this.timezone = timezone;
    }
}
