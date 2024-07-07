package com.example.practic2024.Repository;

import com.example.practic2024.Models.ProductEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProductRepo extends CrudRepository<ProductEntity, String> {

    Iterable<ProductEntity> findByOrderId(Long orderId);

    Optional<ProductEntity> findById(Long id);
}
