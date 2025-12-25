package com.ClothesMangement.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;


@Entity
@Table(name = "bill_detail")
@Data
public class BillDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "billID", nullable = false)
    private Bill bill;

    @ManyToOne
    @JoinColumn(name = "clothesID", nullable = false)
    private Clothes clothes;

    private Integer quantity;
    private Double price;

    // totalPrice sẽ được tính toán tự động trong Java hoặc DB
    @Column(insertable = false, updatable = false)
    private Double totalPrice;
}
