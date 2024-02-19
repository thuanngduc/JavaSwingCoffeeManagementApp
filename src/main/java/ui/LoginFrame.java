/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import apiclient.LoginApiClient;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author ADMIN
 */
public class LoginFrame extends JFrame{
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        super("Đăng nhập");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);

        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10)); // Sử dụng GridLayout để tổ chức thành phần

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticateUser();
            }
        });

        // Tạo label cho username và password để mô tả ý nghĩa của các trường
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        // Sử dụng JPanel để chứa label và text field tương ứng
        JPanel usernamePanel = new JPanel(new BorderLayout());
        JPanel passwordPanel = new JPanel(new BorderLayout());

        usernamePanel.add(usernameLabel, BorderLayout.WEST);
        usernamePanel.add(usernameField, BorderLayout.CENTER);

        passwordPanel.add(passwordLabel, BorderLayout.WEST);
        passwordPanel.add(passwordField, BorderLayout.CENTER);

        // Thêm các thành phần vào panel chính
        panel.add(usernamePanel);
        panel.add(passwordPanel);
        panel.add(loginButton);

        add(panel);

        // Canh giữa cửa sổ
        setLocationRelativeTo(null);
        setVisible(true);
    }


    private void authenticateUser() {
        try {
            String apiUrl = "http://localhost:8080/api/auth/signin";
            String jsonInputString = "{\"username\":\"" + usernameField.getText() + "\", \"password\":\"" + new String(passwordField.getPassword()) + "\"}";

            String response = apiclient.LoginApiClient.authenticateUser(apiUrl, jsonInputString);

            JOptionPane.showMessageDialog(this, response, "OK", JOptionPane.INFORMATION_MESSAGE);
            if (authenticationSuccessful(response)) {
            // Chuyển hướng sang ứng dụng Quản lý quán cà phê
            new CafeManagementFrame();
            dispose(); // Đóng cửa sổ đăng nhập
        }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi kết nối", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private boolean authenticationSuccessful(String response) {
        return !response.contains("OK");
    }
    public static void main(String[] args) {
        new LoginFrame();
    }
    
}
