package com.ClothesMangement.config;

import com.ClothesMangement.entity.*;
import com.ClothesMangement.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(
            UserRepository userRepo,
            ClothesTypeRepository typeRepo,
            ClothesRepository clothesRepo,
            BillRepository billRepo,
            BillDetailRepository detailRepo) {
        return args -> {
            // 1. SEED USER (Nếu chưa có)
            if (userRepo.count() == 0) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword("admin123");
                admin.setRole(User.Role.admin);
                userRepo.save(admin);

                User staff = new User();
                staff.setUsername("staff");
                staff.setPassword("staff123");
                staff.setRole(User.Role.staff);
                userRepo.save(staff);
            }

            // 2. SEED CLOTHES TYPE (Nếu chưa có)
            if (typeRepo.count() == 0) {
                ClothesType t1 = new ClothesType(); t1.setName("Áo Thun");
                ClothesType t2 = new ClothesType(); t2.setName("Quần Jean");
                ClothesType t3 = new ClothesType(); t3.setName("Áo Khoác");

                typeRepo.save(t1);
                typeRepo.save(t2);
                typeRepo.save(t3);

                // 3. SEED CLOTHES (Dựa trên Type vừa tạo)
                if (clothesRepo.count() == 0) {
                    Clothes c1 = new Clothes();
                    c1.setName("Áo thun Polo Classic");
                    c1.setType(t1);
                    c1.setSize(Clothes.Size.L);
                    c1.setColor("Trắng");
                    c1.setPrice(250000.0);
                    c1.setQuantity(50);

                    Clothes c2 = new Clothes();
                    c2.setName("Quần Jean Slimfit");
                    c2.setType(t2);
                    c2.setSize(Clothes.Size.M);
                    c2.setColor("Xanh đậm");
                    c2.setPrice(450000.0);
                    c2.setQuantity(30);

                    clothesRepo.save(c1);
                    clothesRepo.save(c2);

                    // 4. SEED BILL & BILL DETAIL (Dữ liệu mẫu lịch sử bán hàng)
                    if (billRepo.count() == 0) {
                        User adminUser = userRepo.findByUsername("admin").orElse(null);

                        Bill bill = new Bill();
                        bill.setCustomerName("Nguyễn Văn A");
                        bill.setUser(adminUser);
                        billRepo.save(bill); // Lưu bill trước để có ID

                        BillDetail detail = new BillDetail();
                        detail.setBill(bill);
                        detail.setClothes(c1);
                        detail.setQuantity(2);
                        detail.setPrice(c1.getPrice());
                        // Note: totalPrice được tính tự động ở DB theo columnDefinition của bạn
                        detailRepo.save(detail);
                    }
                }
            }
            System.out.println(">>> SEED DATA COMPLETED SUCCESSFULLY!");
        };
    }
}