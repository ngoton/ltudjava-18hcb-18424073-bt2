package com.sims.v2.bean;

import javax.swing.*;

public class MenuBean {
    private JPanel panel;
    private JButton button;
    private String name;

    public MenuBean(){}
    public MenuBean(JPanel panel, JButton button, String name){
        this.panel = panel;
        this.button = button;
        this.name = name;
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    public JButton getButton() {
        return button;
    }

    public void setButton(JButton button) {
        this.button = button;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
