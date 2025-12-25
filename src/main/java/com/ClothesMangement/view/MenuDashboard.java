package com.ClothesMangement.view;

import com.ClothesMangement.entity.User;
import com.formdev.flatlaf.FlatClientProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
public class MenuDashboard extends JFrame {

    @Autowired private ClothesFrame clothesFrame;
    @Autowired private BillFrame billFrame;
    @Autowired @Lazy private LoginFrame loginFrame;
    @Autowired private  BillHistoryFrame  billHistoryFrame;

    private CardLayout cardLayout;
    private JPanel contentPanel;
    private JLabel lbUser;
    private JLabel lbRole;

    public MenuDashboard() {
        setTitle("HỆ THỐNG QUẢN LÝ CỬA HÀNG");
        setSize(1280, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initLayout();
    }

    private void initLayout() {
        setLayout(new BorderLayout());

        // --- SIDEBAR (BÊN TRÁI) ---
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBackground(new Color(33, 37, 41)); // Dark Sidebar
        sidebar.setLayout(new BorderLayout());

        // Header Sidebar
        JPanel sidebarHeader = new JPanel();
        sidebarHeader.setOpaque(false);
        sidebarHeader.setLayout(new BoxLayout(sidebarHeader, BoxLayout.Y_AXIS));
        sidebarHeader.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        JLabel lbLogo = new JLabel("CLOTHES POS");
        lbLogo.setForeground(new Color(13, 110, 253));
        lbLogo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lbLogo.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

        lbUser = new JLabel("Username");
        lbUser.setForeground(Color.WHITE);
        lbUser.setFont(new Font("SansSerif", Font.BOLD, 15));
        lbUser.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

        lbRole = new JLabel("Administrator");
        lbRole.setForeground(Color.GRAY);
        lbRole.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

        sidebarHeader.add(lbLogo);
        sidebarHeader.add(Box.createVerticalStrut(25));
        sidebarHeader.add(lbUser);
        sidebarHeader.add(lbRole);

        // Menu Buttons
        JPanel pnlMenu = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        pnlMenu.setOpaque(false);

        JButton btnClothes = createMenuBtn(" Quản lý Quần áo", "Clothes");
        JButton btnBill = createMenuBtn(" Lập Hóa đơn", "Bill");
        JButton btnHistory = createMenuBtn("Lịch sử hoá đơn", "History");
        JButton btnLogout = createMenuBtn(" Đăng xuất", "Logout");
        

        pnlMenu.add(btnClothes);
        pnlMenu.add(btnBill);
        pnlMenu.add(btnHistory);
        pnlMenu.add(btnLogout);

        sidebar.add(sidebarHeader, BorderLayout.NORTH);
        sidebar.add(pnlMenu, BorderLayout.CENTER);

        // --- CONTENT AREA (CARD LAYOUT) ---
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Màn hình Welcome mặc định
        JPanel pnlHome = new JPanel(new GridBagLayout());
        JLabel lbHome = new JLabel("Chào mừng! Vui lòng chọn chức năng để bắt đầu.");
        lbHome.setFont(new Font("SansSerif", Font.ITALIC, 16));
        lbHome.setForeground(Color.GRAY);
        pnlHome.add(lbHome);
        contentPanel.add(pnlHome, "HOME");

        // --- EVENTS ---
        btnClothes.addActionListener(e -> {
            clothesFrame.btnReloadTable();
            cardLayout.show(contentPanel, "CLOTHES");
        });

        btnBill.addActionListener(e -> {
            billFrame.refreshStockTable();
            cardLayout.show(contentPanel, "BILL");
        });

        btnHistory.addActionListener(e -> {
            billHistoryFrame.refreshHistory();
            cardLayout.show(contentPanel, "HISTORY");
        });

        btnLogout.addActionListener(e -> {
            int res = JOptionPane.showConfirmDialog(this, "Đăng xuất tài khoản?");
            if (res == JOptionPane.YES_OPTION) {
                this.dispose();
                loginFrame.setVisible(true);
            }
        });
        
        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    private JButton createMenuBtn(String text, String type) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(220, 45));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Style FlatLaf
        btn.putClientProperty(FlatClientProperties.STYLE, "" +
                "background: #212529;" +
                "foreground: #ffffff;" +
                "arc: 12;" +
                "borderWidth: 0;" +
                "hoverBackground: #343a40");

        if (type.equals("Logout")) {
            btn.putClientProperty(FlatClientProperties.STYLE, "hoverBackground: #c0392b");
        }
        return btn;
    }

    // Hàm khởi tạo sau khi đăng nhập thành công
    public void initialize(User user) {
        lbUser.setText(user.getUsername().toUpperCase());
        lbRole.setText(user.getRole().toString());

        // Gắn các panel con thực tế vào Content Area
        contentPanel.add(clothesFrame, "CLOTHES");
        contentPanel.add(billFrame, "BILL");
        contentPanel.add(billHistoryFrame, "HISTORY");
        cardLayout.show(contentPanel, "HOME");
    }
}