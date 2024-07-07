package com.example.practic2024.Repository;

import com.example.practic2024.Models.ShopEntity;
import org.springframework.data.repository.CrudRepository;

public interface ShopRepo extends CrudRepository<ShopEntity, Long> {
    Iterable<ShopEntity> findByFirmId(Long firmId);
}
