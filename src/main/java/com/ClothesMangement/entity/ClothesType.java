package com.ClothesMangement.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "clothes_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClothesType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false, length = 45)
    private String name;

    @OneToMany(mappedBy = "type", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Clothes> clothes;

    // Thêm Constructor này để dùng trong Seeder
    public ClothesType(String name) {
        this.name = name;
    }

    // Ghi đè toString chỉ trả về Tên để JComboBox hiển thị an toàn
    @Override
    public String toString() {
        return name;
    }
}