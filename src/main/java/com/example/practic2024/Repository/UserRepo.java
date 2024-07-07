package com.example.practic2024.Repository;

import com.example.practic2024.Models.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepo extends CrudRepository<UserEntity, Long> {

    UserEntity findByLogin(String login);
}
