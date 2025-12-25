package com.ClothesMangement.controller;

import com.ClothesMangement.entity.User;
import com.ClothesMangement.service.AuthService;
import com.ClothesMangement.view.LoginFrame;
import com.ClothesMangement.view.MenuDashboard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import javax.swing.*;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired @Lazy
    private LoginFrame loginFrame;
    @Autowired @Lazy private MenuDashboard menuDashboard;

    public void handleLogin(String username, String password) {
        if (username.isBlank() || password.isBlank()) {
            JOptionPane.showMessageDialog(loginFrame, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        User user = authService.Login(username, password);
        if (user != null) {
            loginFrame.dispose();
            menuDashboard.initialize(user); // Truyền thông tin user vào dashboard
            menuDashboard.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(loginFrame, "Sai tài khoản hoặc mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void handleRegister(User user) {
        try {
            authService.register(user);
            JOptionPane.showMessageDialog(loginFrame, "Đăng ký thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(loginFrame, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

}
