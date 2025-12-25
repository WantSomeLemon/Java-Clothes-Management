package com.ClothesMangement.service;

import com.ClothesMangement.entity.Clothes;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClothesService {
    
    public List<Clothes> getAllClothes();

    public Clothes saveClothes(Clothes clothes);

    public void deleteClothes(Integer id);
    
}
