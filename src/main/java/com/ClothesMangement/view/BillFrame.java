package com.ClothesMangement.view;

import com.ClothesMangement.controller.BillController;
import com.ClothesMangement.controller.ClothesController;
import com.ClothesMangement.entity.Bill;
import com.ClothesMangement.entity.BillDetail;
import com.ClothesMangement.entity.Clothes;
import com.formdev.flatlaf.FlatClientProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class BillFrame extends JPanel {

    @Autowired private BillController billController;
    @Autowired private ClothesController clothesController;

    private JTable tableStock;
    private JTable tableCart;
    private DefaultTableModel modelStock;
    private DefaultTableModel modelCart;

    private JTextField txtCustomer = new JTextField(20);
    private JLabel lblTotalMoney = new JLabel("0 VNĐ");
    private List<BillDetail> currentCart = new ArrayList<>();
    private double totalAmount = 0;

    public BillFrame() {
        setLayout(new BorderLayout(15, 15));
        initUI();
    }

    private void initUI() {
        // --- 1. LEFT: STOCK TABLE ---
        JPanel pnlLeft = new JPanel(new BorderLayout(5, 5));
        pnlLeft.setBorder(BorderFactory.createTitledBorder("Danh sách sản phẩm kho"));

        modelStock = new DefaultTableModel(new Object[]{"ID", "Tên SP", "Loại", "Giá", "Tồn", "Màu", "Size"}, 0);
        tableStock = new JTable(modelStock);

        tableStock.removeColumn(tableStock.getColumnModel().getColumn(6));
        tableStock.removeColumn(tableStock.getColumnModel().getColumn(5));
        tableStock.removeColumn(tableStock.getColumnModel().getColumn(2));
        pnlLeft.add(new JScrollPane(tableStock), BorderLayout.CENTER);

        JButton btnAddCart = new JButton("Thêm vào giỏ >>");
        btnAddCart.putClientProperty(FlatClientProperties.STYLE, "background: #3498db; foreground: #ffffff; arc: 10");
        pnlLeft.add(btnAddCart, BorderLayout.SOUTH);

        // --- 2. RIGHT: CART TABLE ---
        JPanel pnlRight = new JPanel(new BorderLayout(5, 5));
        pnlRight.setBorder(BorderFactory.createTitledBorder("Chi tiết hóa đơn"));

        modelCart = new DefaultTableModel(new Object[]{"Tên SP", "SL", "Đơn giá", "Thành tiền"}, 0);
        tableCart = new JTable(modelCart);
        pnlRight.add(new JScrollPane(tableCart), BorderLayout.CENTER);

        JButton btnRemove = new JButton("Xóa khỏi giỏ");
        btnRemove.putClientProperty(FlatClientProperties.STYLE, "background: #e67e22; foreground: #ffffff; arc: 10");
        pnlRight.add(btnRemove, BorderLayout.SOUTH);

        // --- 3. BOTTOM: CHECKOUT PANEL ---
        JPanel pnlBottom = new JPanel(new BorderLayout());
        pnlBottom.putClientProperty(FlatClientProperties.STYLE, "padding: 15,15,15,15; background: #fdfdfd");
        pnlBottom.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        JPanel pnlCust = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlCust.setOpaque(false);
        pnlCust.add(new JLabel("Khách hàng:"));
        pnlCust.add(txtCustomer);

        JPanel pnlSummary = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        pnlSummary.setOpaque(false);
        JLabel lblText = new JLabel("TỔNG TIỀN:");
        lblText.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTotalMoney.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTotalMoney.setForeground(new Color(192, 57, 43));

        JButton btnPay = new JButton("THANH TOÁN (F5)");
        btnPay.setPreferredSize(new Dimension(180, 45));
        btnPay.putClientProperty(FlatClientProperties.STYLE, "background: #27ae60; foreground: #ffffff; font: bold; arc: 15");

        pnlSummary.add(lblText);
        pnlSummary.add(lblTotalMoney);
        pnlSummary.add(btnPay);

        pnlBottom.add(pnlCust, BorderLayout.WEST);
        pnlBottom.add(pnlSummary, BorderLayout.EAST);

        // --- SPLIT & ADD ---
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pnlLeft, pnlRight);
        split.setDividerLocation(550);
        add(split, BorderLayout.CENTER);
        add(pnlBottom, BorderLayout.SOUTH);

        // --- EVENTS ---
        btnAddCart.addActionListener(e -> addToCart());
        btnRemove.addActionListener(e -> removeFromCart());
        btnPay.addActionListener(e -> processCheckout());
    }

    private void addToCart() {
        int row = tableStock.getSelectedRow();
        if (row == -1) return;

        try {
            int id = (int) modelStock.getValueAt(row, 0);
            String name = (String) modelStock.getValueAt(row, 1);
            double price = Double.parseDouble(modelStock.getValueAt(row, 3).toString()); 
            int stock = Integer.parseInt(modelStock.getValueAt(row, 4).toString());    
            
            String input = JOptionPane.showInputDialog(this, "Số lượng mua cho [" + name + "]:", "1");
            if (input == null) return;

            int qty = Integer.parseInt(input);
            if (qty <= 0 || qty > stock) throw new Exception("Số lượng không hợp lệ hoặc vượt tồn kho!");

            // Add to List
            BillDetail detail = new BillDetail();
            Clothes c = new Clothes(); 
            c.setId(id); 
            c.setName(name);
            
            detail.setClothes(c);
            detail.setQuantity(qty);
            detail.setPrice(price);
            currentCart.add(detail);

            // Add to Table View
            modelCart.addRow(new Object[]{name, qty, price, qty * price});
            updateTotal();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng là số nguyên!", "Lỗi", 0);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void removeFromCart() {
        int row = tableCart.getSelectedRow();
        if (row != -1) {
            currentCart.remove(row);
            modelCart.removeRow(row);
            updateTotal();
        }
    }

    private void processCheckout() {
        if (currentCart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng trống!");
            return;
        }

        Bill bill = new Bill();
        bill.setCustomerName(txtCustomer.getText().trim());
        bill.setDetails(new ArrayList<>(currentCart));

        try {
            billController.checkout(bill, (JFrame) SwingUtilities.getWindowAncestor(this));
            resetBill();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Thanh toán thất bại: " + ex.getMessage());
        }
    }

    private void updateTotal() {
        totalAmount = currentCart.stream().mapToDouble(d -> d.getQuantity() * d.getPrice()).sum();
        lblTotalMoney.setText(String.format("%,.0f VNĐ", totalAmount));
    }

    private void resetBill() {
        currentCart.clear();
        modelCart.setRowCount(0);
        txtCustomer.setText("");
        updateTotal();
        refreshStockTable();
    }

    public void refreshStockTable() {
        clothesController.refreshTable(modelStock);
    }
}