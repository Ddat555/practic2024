package com.example.practic2024.Repository;

import com.example.practic2024.Models.CityEntity;
import org.springframework.data.repository.CrudRepository;

public interface CityRepo extends CrudRepository<CityEntity, String> {
}
