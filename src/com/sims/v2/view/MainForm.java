package com.sims.v2.view;

import com.sims.v2.bean.MenuBean;
import com.sims.v2.controller.ScreenSwitchController;
import com.sims.v2.model.User;
import com.sims.v2.util.ClickListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class MainForm extends JFrame {
    private User userLogined;
    private ClickListener clickListener;
    private JPanel mainPanel = new JPanel();
    private StudentForm studentPanel = new StudentForm();
    private CalendarForm calendarPanel = new CalendarForm();
    private AttendanceForm attendancePanel = new AttendanceForm();
    private ChangePassForm changePassPanel;

    public MainForm(boolean isAdmin, User user){
        super("QUẢN LÝ SINH VIÊN");
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int xSize = ((int) toolkit.getScreenSize().getWidth());
        int ySize = ((int) toolkit.getScreenSize().getHeight());
        int height = (int) (Math.round(ySize * 0.80));
        int width = (int) (Math.round(xSize * 0.80));
        setPreferredSize(new Dimension(width, height));
        this.userLogined = user;
        this.changePassPanel = new ChangePassForm(user);
        addComponentsToPane(isAdmin);
        pack();
        clickListener = new ClickListener();
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        beforeExit();

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

    private void addComponentsToPane(boolean isAdmin){
        if (isAdmin){
            setAdminPanel(getContentPane());
        }
        else {
            setUserPanel(getContentPane());
        }
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu1 = new JMenu("File");
        JMenu menu2 = new JMenu("Setting");
        JMenu menu3 = new JMenu("Help");
        JMenuItem menuItem1 = new JMenuItem("Exit");
        JMenuItem menuItem2 = new JMenuItem("Điểm chuẩn");
        JMenuItem menuItem3 = new JMenuItem("About");
        menu1.setMnemonic('F');
        menu2.setMnemonic('S');
        menu3.setMnemonic('H');
        menuItem1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
        menuItem1.setMnemonic('x');
        menuItem1.addActionListener(e->clickListener.exitClick()); //lambda
        menuItem3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK | ActionEvent.ALT_MASK));
        menuItem3.setMnemonic('A');
        menu1.add(menuItem1);
        menu2.add(menuItem2);
        menu3.add(menuItem3);
        menuBar.add(menu1);
        menuBar.add(menu2);
        menuBar.add(menu3);
        return menuBar;
    }

    private void setAdminPanel(Container contentPane) {
        BorderLayout layout = new BorderLayout();

        JButton studentButton = new JButton("Quản lý sinh viên");
        JButton calendarButton = new JButton("Thời khóa biểu");
        JButton attendanceButton = new JButton("Danh sách lớp");
        JButton transcriptButton = new JButton("Bảng điểm");
        JButton changePassButton = new JButton("Đổi mật khẩu");

        String lg = "[ " + userLogined.getUsername() + " ] Đăng xuất";
        JButton loginButton = new JButton(lg);

        JToolBar toolBar = new JToolBar();
        toolBar.add(studentButton);
        toolBar.add(calendarButton);
        toolBar.add(attendanceButton);
        toolBar.add(transcriptButton);
        toolBar.add(changePassButton);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(loginButton);
        toolBar.setFloatable(false);

        mainPanel.setLayout(layout);
        mainPanel.add(toolBar, BorderLayout.PAGE_START);

        List<MenuBean> menuList = new ArrayList<>();
        menuList.add(new MenuBean(studentPanel, studentButton, "student"));
        menuList.add(new MenuBean(calendarPanel, calendarButton, "calendar"));
        menuList.add(new MenuBean(attendancePanel, attendanceButton, "attendance"));
        menuList.add(new MenuBean(changePassPanel, changePassButton, "changepass"));
        menuList.add(new MenuBean(null, loginButton, "login"));

        ScreenSwitchController controller = new ScreenSwitchController(this, mainPanel, toolBar);
        controller.setFirstPanel(studentPanel, studentButton);
        controller.setEvent(menuList);

        contentPane.add(mainPanel);
        setJMenuBar(createMenuBar());
    }
    private void setUserPanel(Container contentPane) {
        BorderLayout layout = new BorderLayout();

        JButton transcriptButton = new JButton("Bảng điểm");
        JButton changePassButton = new JButton("Đổi mật khẩu");

        String lg = "[ " + userLogined.getUsername() + " ] Đăng xuất";
        JButton loginButton = new JButton(lg);

        JToolBar toolBar = new JToolBar();
        toolBar.add(transcriptButton);
        toolBar.add(changePassButton);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(loginButton);
        toolBar.setFloatable(false);

        mainPanel.setLayout(layout);
        mainPanel.add(toolBar, BorderLayout.PAGE_START);

        List<MenuBean> menuList = new ArrayList<>();
        menuList.add(new MenuBean(changePassPanel, changePassButton, "changepass"));
        menuList.add(new MenuBean(null, loginButton, "login"));

        ScreenSwitchController controller = new ScreenSwitchController(this, mainPanel, toolBar);
        controller.setEvent(menuList);

        contentPane.add(mainPanel);
    }

}
