package com.ClothesMangement.controller;

import com.ClothesMangement.entity.Clothes;
import com.ClothesMangement.repository.ClothesRepository;
import com.ClothesMangement.service.ClothesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.swing.table.DefaultTableModel;
import java.util.List;

@Controller
public class ClothesController {
    @Autowired
    private ClothesService clothesService;

    // Load dữ liệu lên Table
    public void refreshTable(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        List<Clothes> list = clothesService.getAllClothes();
        for (Clothes c : list) {
            tableModel.addRow(new Object[]{
                    c.getId(),
                    c.getName(),
                    c.getType().getName(),
                    c.getPrice(),
                    c.getQuantity(),
                    c.getColor(),
                    c.getSize()
            });
        }
    }

    public void saveClothes(Clothes clothes) {
        clothesService.saveClothes(clothes);
    }

    public void deleteClothes(Integer id) {
        clothesService.deleteClothes(id);
    }
}
