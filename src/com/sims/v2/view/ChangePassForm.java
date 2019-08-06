package com.sims.v2.view;

import com.sims.v2.controller.LoginController;
import com.sims.v2.model.User;
import com.sims.v2.util.ClickListener;
import com.sims.v2.util.MD5Encrypt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ChangePassForm extends JPanel {
    private User user;
    private LoginController loginController;
    private ClickListener clickListener;
    private JPanel panel;
    private JLabel titleLabel;
    private JLabel passwordLabel;
    private JLabel newPassLabel;
    private JPasswordField passwordField;
    private JPasswordField newPassField;
    private JButton saveButton;
    private JButton resetButton;
    private JLabel messageLabel;

    public ChangePassForm(User user){
        this.user = user;
        initComponents();
        loginController = new LoginController();
        clickListener = new ClickListener();
    }

    private void initComponents() {
        panel = new JPanel();

        titleLabel = new JLabel("ĐỔI MẬT KHẨU");
        titleLabel.setFont(new Font("Arial", 1, 24));
        titleLabel.setForeground(new Color(26316));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        passwordLabel = new JLabel("Mật khẩu cũ");
        newPassLabel = new JLabel("Mật khẩu mới");

        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(250,30));
        passwordField.addKeyListener(enterKeyPress());

        newPassField = new JPasswordField();
        newPassField.setPreferredSize(new Dimension(250,30));
        newPassField.addKeyListener(enterKeyPress());

        saveButton = new JButton("Cập nhật");
        saveButton.addActionListener(e -> save());
        saveButton.addKeyListener(enterKeyPress());

        resetButton = new JButton("Nhập lại");
        resetButton.addActionListener(e->refresh());

        messageLabel = new JLabel();
        messageLabel.setForeground(Color.RED);

        GroupLayout layout = new GroupLayout(panel);
        settingLayout(layout);

        add(panel);
    }

    private void save(){
        String password = new String(passwordField.getPassword());
        String pass = MD5Encrypt.convertHashToString(password);
        String newPass = new String(newPassField.getPassword());

        if (pass.isEmpty()){
            messageLabel.setText("Vui lòng nhập vào mật khẩu cũ!");
            passwordField.requestFocus();
        }
        else if (newPass.isEmpty()){
            messageLabel.setText("Vui lòng nhập vào mật khẩu mới!");
            newPassField.requestFocus();
        }
        else if (!pass.equals(user.getPassword())){
            messageLabel.setText("Mật khẩu cũ không đúng!");
            passwordField.requestFocus();
        }
        else {
            user.setPassword(newPass);
            boolean response = loginController.changePassword(user);
            if (response == true){
                messageLabel.setText("Cập nhật thành công");
            }
            else {
                messageLabel.setText("Cập nhật thất bại");
            }
            refresh();
        }
    }

    private void refresh() {
        passwordField.setText("");
        newPassField.setText("");
        passwordField.requestFocus();
    }

    private KeyAdapter enterKeyPress(){
        return (new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER){
                    save();
                }
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
                                                        .addComponent(passwordLabel)
                                                        .addComponent(passwordField)
                                                        .addComponent(newPassLabel)
                                                        .addComponent(newPassField)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(saveButton)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                                                                .addComponent(resetButton, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
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
                                        .addComponent(passwordLabel)
                                )
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(passwordField)
                                )
                                .addGap(28, 28, 28)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(newPassLabel)
                                )
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(newPassField)
                                )
                                .addGap(28, 28, 28)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(saveButton)
                                        .addComponent(resetButton)
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
