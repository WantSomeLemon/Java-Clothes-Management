package com.ClothesMangement.service;

import com.ClothesMangement.entity.Bill;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public interface BillService {
    @Transactional
    public Bill createBill(Bill bill);
    
}
