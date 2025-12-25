package com.ClothesMangement;

import com.ClothesMangement.view.LoginFrame;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
// Cấu hình FlatLaf trước khi khởi chạy context
        com.formdev.flatlaf.FlatLightLaf.setup();

        ConfigurableApplicationContext ctx = new SpringApplicationBuilder(Main.class)
                .headless(false)
                .run(args);

        java.awt.EventQueue.invokeLater(() -> {
            ctx.getBean(LoginFrame.class).setVisible(true);
        });
    }
}