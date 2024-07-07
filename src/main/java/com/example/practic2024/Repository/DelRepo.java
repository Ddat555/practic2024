package com.example.practic2024.Repository;

import com.example.practic2024.Models.DelEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface DelRepo extends CrudRepository<DelEntity, Long> {

    DelEntity findByUserId(Long userid);


}
