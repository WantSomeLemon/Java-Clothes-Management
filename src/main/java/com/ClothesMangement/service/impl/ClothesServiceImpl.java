package com.ClothesMangement.service.impl;

import com.ClothesMangement.entity.Clothes;
import com.ClothesMangement.repository.ClothesRepository;
import com.ClothesMangement.service.ClothesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClothesServiceImpl implements ClothesService {
    @Autowired
    private ClothesRepository clothesRepository;
    
    @Override
    public List<Clothes> getAllClothes() {
        return clothesRepository.findAll();
    }

    @Override
    public Clothes saveClothes(Clothes clothes) {
        return clothesRepository.save(clothes);
    }

    @Override
    public void deleteClothes(Integer id) {
        clothesRepository.deleteById(id);
    }
}
