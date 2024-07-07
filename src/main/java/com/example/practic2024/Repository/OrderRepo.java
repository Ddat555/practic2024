package com.example.practic2024.Repository;

import com.example.practic2024.Models.OrderEntity;
import com.example.practic2024.Models.ShopEntity;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepo extends CrudRepository<OrderEntity, Long> {
    Iterable<OrderEntity> findByFirm_Id(Long firmId);

    Iterable<OrderEntity> findByShopId(Long shopId);

}
