package com.ClothesMangement.controller;

import com.ClothesMangement.entity.Bill;
import com.ClothesMangement.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.swing.*;

@Controller
public class BillController {

    @Autowired
    private BillService billService;

    public void checkout(Bill bill, JFrame currentFrame) {
        if (bill.getDetails() == null || bill.getDetails().isEmpty()) {
            JOptionPane.showMessageDialog(currentFrame, "Giỏ hàng trống!");
            return;
        }

        try {
            billService.createBill(bill);
            JOptionPane.showMessageDialog(currentFrame, "Thanh toán thành công! Kho đã được cập nhật.");
        } catch (Exception e) {
            // Hiển thị thông báo lỗi từ Service (ví dụ: Hết hàng)
            JOptionPane.showMessageDialog(currentFrame, "Lỗi: " + e.getMessage(), "Lỗi thanh toán", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}
