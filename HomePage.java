package com.eventmanagement.ui;

import com.eventmanagement.Constants;
import com.eventmanagement.utils.UIUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Main home page of the Event Management System
 * Provides entry point for both admin and student login
 */
public class HomePage extends JFrame {
    private JButton adminLoginButton;
    private JButton studentLoginButton;

    public HomePage() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setVisible(true);
    }

    /**
     * Initialize all UI components
     */
    private void initializeComponents() {
        setTitle(Constants.APP_TITLE);
        setSize(Constants.SMALL_WINDOW_WIDTH, Constants.SMALL_WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        adminLoginButton = UIUtils.createStyledButton(Constants.ADMIN_LOGIN);
        studentLoginButton = UIUtils.createStyledButton(Constants.STUDENT_LOGIN);

        // Set tooltips
        UIUtils.setTooltip(adminLoginButton, "Login as administrator to manage events");
        UIUtils.setTooltip(studentLoginButton, "Login as student to view and register for events");
    }

    /**
     * Setup the layout of components
     */
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));

        // Header
        JLabel headerLabel = new JLabel(Constants.WELCOME_MESSAGE, SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(headerLabel, BorderLayout.NORTH);

        // Center panel with buttons
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        centerPanel.add(adminLoginButton);
        centerPanel.add(studentLoginButton);
        add(centerPanel, BorderLayout.CENTER);

        // Footer
        JLabel footerLabel = new JLabel("© 2025 Event Management System", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        footerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(footerLabel, BorderLayout.SOUTH);
    }

    /**
     * Setup event handlers for buttons
     */
    private void setupEventHandlers() {
        adminLoginButton.addActionListener(e -> openAdminLogin());
        studentLoginButton.addActionListener(e -> openStudentLogin());
    }

    /**
     * Opens admin login page
     */
    private void openAdminLogin() {
        SwingUtilities.invokeLater(() -> {
            new AdminLoginPage();
            dispose();
        });
    }

    /**
     * Opens student login page
     */
    private void openStudentLogin() {
        SwingUtilities.invokeLater(() -> {
            new StudentLoginPage();
            dispose();
        });
    }

    /**
     * Main method - entry point of the application
     */
    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
        } catch (Exception e) {
            // Use default look and feel if system L&F is not available
        }

        SwingUtilities.invokeLater(() -> new HomePage());
    }
}