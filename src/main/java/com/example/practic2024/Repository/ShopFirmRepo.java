package com.example.practic2024.Repository;

import com.example.practic2024.Models.DelEntity;
import com.example.practic2024.Models.ShopFirmEntity;
import org.springframework.data.repository.CrudRepository;

public interface ShopFirmRepo extends CrudRepository<ShopFirmEntity, Long> {

    ShopFirmEntity findByUserId(Long userID);
}
