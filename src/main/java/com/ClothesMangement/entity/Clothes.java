package com.ClothesMangement.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "clothes")
@Data
public class Clothes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 45)
    private String name;

    @ManyToOne
    @JoinColumn(name = "typeID", nullable = false)
    private ClothesType type;

    @Enumerated(EnumType.STRING)
    private Size size;

    private String color;
    private Double price;
    private Integer quantity;

    public enum Size {
        S, M, L, XL, XXL, OS
    }
}