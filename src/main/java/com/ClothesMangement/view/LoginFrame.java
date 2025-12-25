package com.ClothesMangement.view;

import com.ClothesMangement.controller.AuthController;
import com.ClothesMangement.entity.User;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatDarkLaf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
public class LoginFrame extends JFrame {

    @Autowired
    private AuthController authController;

    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);

    // Components cho Login
    private JTextField txtLoginUser = new JTextField();
    private JPasswordField txtLoginPass = new JPasswordField();

    // Components cho Register
    private JTextField txtRegUser = new JTextField();
    private JPasswordField txtRegPass = new JPasswordField();
    private JComboBox<User.Role> cbRole = new JComboBox<>(User.Role.values());

    public LoginFrame() {
        setTitle("Hệ Thống Quản Lý");
        setSize(400, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel.add(createLoginPanel(), "LOGIN");
        mainPanel.add(createRegisterPanel(), "REGISTER");

        add(mainPanel);
    }

    private JPanel createLoginPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.putClientProperty(FlatClientProperties.STYLE, "padding: 20,20,20,20");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.insets = new Insets(10, 5, 10, 5);

        JLabel title = new JLabel("ĐĂNG NHẬP", SwingConstants.CENTER);
        title.putClientProperty(FlatClientProperties.STYLE, "font: h2.font; foreground: #007bff");

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; p.add(title, gbc);
        gbc.gridy = 1; gbc.gridwidth = 1; p.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; p.add(txtLoginUser, gbc);
        gbc.gridx = 0; gbc.gridy = 2; p.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; p.add(txtLoginPass, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JButton btnLogin = new JButton("Đăng nhập");
        btnLogin.addActionListener(e -> authController.handleLogin(txtLoginUser.getText(), new String(txtLoginPass.getPassword())));
        p.add(btnLogin, gbc);

        gbc.gridy = 4;
        JButton btnToReg = new JButton("Chưa có tài khoản? Đăng ký ngay");
        btnToReg.addActionListener(e -> cardLayout.show(mainPanel, "REGISTER"));
        btnToReg.putClientProperty(FlatClientProperties.STYLE, "variant: link");
        p.add(btnToReg, gbc);

        return p;
    }

    private JPanel createRegisterPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.putClientProperty(FlatClientProperties.STYLE, "padding: 20,20,20,20");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.insets = new Insets(10, 5, 10, 5);

        JLabel title = new JLabel("ĐĂNG KÝ", SwingConstants.CENTER);
        title.putClientProperty(FlatClientProperties.STYLE, "font: h2.font; foreground: #28a745");

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; p.add(title, gbc);
        gbc.gridy = 1; gbc.gridwidth = 1; p.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; p.add(txtRegUser, gbc);
        gbc.gridx = 0; gbc.gridy = 2; p.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; p.add(txtRegPass, gbc);
        gbc.gridx = 0; gbc.gridy = 3; p.add(new JLabel("Quyền:"), gbc);
        gbc.gridx = 1; p.add(cbRole, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        JButton btnReg = new JButton("Xác nhận đăng ký");
        btnReg.addActionListener(e -> {
            User newUser = new User();
            newUser.setUsername(txtRegUser.getText());
            newUser.setPassword(new String(txtRegPass.getPassword()));
            newUser.setRole((User.Role) cbRole.getSelectedItem());
            authController.handleRegister(newUser);
        });
        p.add(btnReg, gbc);

        gbc.gridy = 5;
        JButton btnToLogin = new JButton("Đã có tài khoản? Quay lại");
        btnToLogin.addActionListener(e -> cardLayout.show(mainPanel, "LOGIN"));
        btnToLogin.putClientProperty(FlatClientProperties.STYLE, "variant: link");
        p.add(btnToLogin, gbc);

        return p;
    }
    
}
