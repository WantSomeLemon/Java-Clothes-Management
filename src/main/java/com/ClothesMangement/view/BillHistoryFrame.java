package com.ClothesMangement.view;

import com.ClothesMangement.entity.Bill;
import com.ClothesMangement.entity.BillDetail;
import com.ClothesMangement.repository.BillRepository;
import com.formdev.flatlaf.FlatClientProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class BillHistoryFrame extends JPanel {

    @Autowired private BillRepository billRepo;

    private JTable tableBill;
    private JTable tableDetail;
    private DefaultTableModel modelBill;
    private DefaultTableModel modelDetail;

    public BillHistoryFrame() {
        setLayout(new BorderLayout(10, 10));
        initUI();
    }

    private void initUI() {
        // --- BẢNG HÓA ĐƠN (PHÍA TRÊN) ---
        modelBill = new DefaultTableModel(new Object[]{"ID", "Ngày tạo", "Khách hàng", "Người lập"}, 0);
        tableBill = new JTable(modelBill);
        tableBill.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Sự kiện khi click chọn 1 hóa đơn thì hiện chi tiết ở bảng dưới
        tableBill.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadBillDetails();
        });

        // --- BẢNG CHI TIẾT (PHÍA DƯỚI) ---
        modelDetail = new DefaultTableModel(new Object[]{"Sản phẩm", "Số lượng", "Đơn giá", "Thành tiền"}, 0);
        tableDetail = new JTable(modelDetail);

        // Layout chia đôi trên dưới
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(tableBill), new JScrollPane(tableDetail));
        splitPane.setDividerLocation(300);

        add(splitPane, BorderLayout.CENTER);

        // Nút làm mới
        JButton btnRefresh = new JButton("Làm mới danh sách");
        btnRefresh.addActionListener(e -> refreshHistory());
        add(btnRefresh, BorderLayout.NORTH);
    }

    public void refreshHistory() {
        modelBill.setRowCount(0);
        List<Bill> bills = billRepo.findAll();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Bill b : bills) {
            modelBill.addRow(new Object[]{
                    b.getId(),
                    b.getDateSold() != null ? b.getDateSold().format(formatter) : "N/A",
                    b.getCustomerName(),
                    b.getUser() != null ? b.getUser().getUsername() : "Admin"
            });
        }
    }

    private void loadBillDetails() {
        int row = tableBill.getSelectedRow();
        if (row == -1) return;

        int billId = (int) modelBill.getValueAt(row, 0);
        modelDetail.setRowCount(0);

        // Tìm hóa đơn và lấy list details (Nhờ mapping @OneToMany)
        billRepo.findById(billId).ifPresent(bill -> {
            for (BillDetail d : bill.getDetails()) {
                modelDetail.addRow(new Object[]{
                        d.getClothes().getName(),
                        d.getQuantity(),
                        d.getPrice(),
                        d.getQuantity() * d.getPrice()
                });
            }
        });
    }
}