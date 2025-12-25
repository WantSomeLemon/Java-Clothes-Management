package com.ClothesMangement.service.impl;

import com.ClothesMangement.entity.Bill;
import com.ClothesMangement.entity.BillDetail;
import com.ClothesMangement.entity.Clothes;
import com.ClothesMangement.repository.BillRepository;
import com.ClothesMangement.repository.ClothesRepository;
import com.ClothesMangement.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BillServiceImpl implements BillService {
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private ClothesRepository clothesRepository;
    
    @Transactional // Đảm bảo tính toàn vẹn dữ liệu
    @Override
    public Bill createBill(Bill bill) {
        // 1. Duyệt qua từng chi tiết hóa đơn để kiểm tra kho
        for (BillDetail detail : bill.getDetails()) {
            Clothes item = clothesRepository.findById(detail.getClothes().getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm ID: " + detail.getClothes().getId()));

            // Kiểm tra tồn kho
            if (item.getQuantity() < detail.getQuantity()) {
                throw new RuntimeException("Sản phẩm '" + item.getName() + "' không đủ hàng (Còn: " + item.getQuantity() + ")");
            }

            // 2. Trừ số lượng tồn kho
            item.setQuantity(item.getQuantity() - detail.getQuantity());
            clothesRepository.save(item);

            // Gán ngược bill vào detail để JPA hiểu quan hệ
            detail.setBill(bill);
        }

        // 3. Lưu hóa đơn (Hệ thống tự động lưu BillDetail nhờ CascadeType.ALL)
        return billRepository.save(bill);
    }
    
}
