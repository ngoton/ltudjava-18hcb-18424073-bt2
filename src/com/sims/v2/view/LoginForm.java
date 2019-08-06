package com.sims.v2.view;

import com.sims.v2.controller.LoginController;
import com.sims.v2.util.ClickListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LoginForm extends JFrame {
    private LoginController loginController;
    private ClickListener clickListener;
    private JPanel panel;
    private JLabel titleLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton cancelButton;
    private JLabel messageLabel;

    public LoginForm(){
        super("QUẢN LÝ SINH VIÊN");

        initComponents();
        getContentPane().add(panel);
        pack();
        clickListener = new ClickListener();
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        usernameField.requestFocusInWindow();
        beforeExit();
        loginController = new LoginController(this, messageLabel);
    }

    private void initComponents() {
        panel = new JPanel();

        titleLabel = new JLabel("ĐĂNG NHẬP");
        titleLabel.setFont(new Font("Arial", 1, 24));
        titleLabel.setForeground(new Color(26316));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        usernameLabel = new JLabel("Tên đăng nhập");
        passwordLabel = new JLabel("Mật khẩu");

        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(250,30));
        usernameField.addKeyListener(enterKeyPress());

        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(250,30));
        passwordField.addKeyListener(enterKeyPress());

        loginButton = new JButton("Đăng nhập");
        loginButton.addActionListener(e->loginButtonClick());
        loginButton.addKeyListener(enterKeyPress());

        cancelButton = new JButton("Thoát");
        cancelButton.addActionListener(e->cancelButtonClick());

        messageLabel = new JLabel();
        messageLabel.setForeground(Color.RED);

        GroupLayout layout = new GroupLayout(panel);
        settingLayout(layout);

        add(panel);
    }

    private void loginButtonClick(){
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        if (username.isEmpty() || password.isEmpty()){
            messageLabel.setText("Vui lòng nhập vào Username & Password!");
            usernameField.requestFocus();
        }
        else {
            loginController.login(username, password);
        }

    }

    private void cancelButtonClick(){
        clickListener.exitClick();
    }

    private KeyAdapter enterKeyPress(){
        return (new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER){
                    loginButtonClick();
                }
            }
        });
    }

    private void beforeExit() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we)
            {
                clickListener.exitClick();
            }
        });
    }

    private void settingLayout(GroupLayout layout) {
        panel.setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(titleLabel)
                                        )
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(usernameLabel)
                                                        .addComponent(usernameField)
                                                        .addComponent(passwordLabel)
                                                        .addComponent(passwordField)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(loginButton)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                                                                .addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
                                                        )
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(messageLabel)
                                                        )
                                                )
                                        )
                                )
                                .addContainerGap()
                        )
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(titleLabel)
                                )
                                .addGap(34, 34, 34)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(usernameLabel)
                                )
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(usernameField)
                                )
                                .addGap(28, 28, 28)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(passwordLabel)
                                )
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(passwordField)
                                )
                                .addGap(28, 28, 28)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(loginButton)
                                        .addComponent(cancelButton)
                                )
                                .addGap(28, 28, 28)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(messageLabel)
                                )
                                .addGap(28, 28, 28)
                        )
        );
    }
}
