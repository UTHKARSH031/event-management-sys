package com.eventmanagement.ui;

import com.eventmanagement.Constants;
import com.eventmanagement.utils.UIUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Admin dashboard with administrative functions
 */
public class AdminDashboard extends JFrame {
    private JButton addEventButton;
    private JButton viewEventsButton;
    private JButton addAdminButton;
    private JButton logoutButton;

    public AdminDashboard() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setVisible(true);
    }

    /**
     * Initialize all UI components
     */
    private void initializeComponents() {
        setTitle("Admin Dashboard - " + Constants.APP_TITLE);
        setSize(Constants.SMALL_WINDOW_WIDTH, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        addEventButton = UIUtils.createStyledButton("Add New Event");
        viewEventsButton = UIUtils.createStyledButton("View Events");
        addAdminButton = UIUtils.createStyledButton("Add Admin");
        logoutButton = UIUtils.createStyledButton(Constants.LOGOUT);

        // Set tooltips
        UIUtils.setTooltip(addEventButton, "Create a new event");
        UIUtils.setTooltip(viewEventsButton, "View and manage existing events");
        UIUtils.setTooltip(addAdminButton, "Add new administrator");
        UIUtils.setTooltip(logoutButton, "Logout and return to home page");
    }

    /**
     * Setup the layout of components
     */
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));

        // Header
        JLabel headerLabel = new JLabel("Administrator Dashboard", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(headerLabel, BorderLayout.NORTH);

        // Center panel with action buttons
        JPanel centerPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        centerPanel.add(addEventButton);
        centerPanel.add(viewEventsButton);
        centerPanel.add(addAdminButton);
        centerPanel.add(logoutButton);

        add(centerPanel, BorderLayout.CENTER);

        // Footer
        JLabel footerLabel = new JLabel("Welcome, Administrator!", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        footerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 15, 10));
        add(footerLabel, BorderLayout.SOUTH);
    }

    /**
     * Setup event handlers
     */
    private void setupEventHandlers() {
        addEventButton.addActionListener(e -> openAddEventPage());
        viewEventsButton.addActionListener(e -> openViewEventsPage());
        addAdminButton.addActionListener(e -> openAddAdminDialog());
        logoutButton.addActionListener(e -> logout());
    }

    /**
     * Opens add event page
     */
    private void openAddEventPage() {
        SwingUtilities.invokeLater(() -> {
            new AddEventPage();
            dispose();
        });
    }

    /**
     * Opens view events page
     */
    private void openViewEventsPage() {
        SwingUtilities.invokeLater(() -> {
            new ViewEventsPage();
            dispose();
        });
    }

    /**
     * Opens add admin dialog
     */
    private void openAddAdminDialog() {
        SwingUtilities.invokeLater(() -> {
            new AddAdminDialog(this);
        });
    }

    /**
     * Logs out and returns to home page
     */
    private void logout() {
        if (UIUtils.showConfirmDialog(this, "Are you sure you want to logout?")) {
            SwingUtilities.invokeLater(() -> {
                new HomePage();
                dispose();
            });
        }
    }
}