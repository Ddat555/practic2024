package com.example.practic2024.Models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ContractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;
    @ManyToOne
    @JoinColumn(name = "del_id", nullable = false)
    private DelEntity del;

    public ContractEntity(){

    }

    public ContractEntity(OrderEntity order, DelEntity del) {
        this.order = order;
        this.del = del;
    }
}
