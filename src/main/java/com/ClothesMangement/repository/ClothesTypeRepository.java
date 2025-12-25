package com.ClothesMangement.repository;

import com.ClothesMangement.entity.ClothesType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClothesTypeRepository  extends JpaRepository<ClothesType, Integer> {
}
