package com.example.practic2024.Repository;

import com.example.practic2024.Models.ContractEntity;
import com.example.practic2024.Models.DelEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ContractRepo extends CrudRepository<ContractEntity,Long> {

    Iterable<ContractEntity> findByDelId(Long del_id);
    Optional<ContractEntity> findByOrderId(Long order_id);
}
