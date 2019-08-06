package com.sims.v2;

import com.sims.v2.view.LoginForm;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater( () -> new LoginForm() );
    }
}
