package com.eventmanagement.ui;

import com.eventmanagement.Constants;
import com.eventmanagement.utils.CsvUtils;
import com.eventmanagement.utils.PasswordUtils;
import com.eventmanagement.utils.UIUtils;
import com.eventmanagement.utils.ValidationUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog for adding new administrators
 */
public class AddAdminDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton addButton;
    private JButton cancelButton;
    private AdminDashboard parentFrame;

    public AddAdminDialog(AdminDashboard parent) {
        super(parent, "Add New Administrator", true);
        this.parentFrame = parent;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setVisible(true);
    }

    /**
     * Initialize all UI components
     */
    private void initializeComponents() {
        setSize(350, 200);
        setLocationRelativeTo(parentFrame);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        addButton = UIUtils.createStyledButton("Add Admin");
        cancelButton = UIUtils.createStyledButton("Cancel");

        // Set tooltips
        UIUtils.setTooltip(usernameField, "Enter admin username (3-20 characters)");
        UIUtils.setTooltip(passwordField, "Enter admin password (minimum 6 characters)");
    }

    /**
     * Setup the layout of components
     */
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(passwordField, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Setup event handlers
     */
    private void setupEventHandlers() {
        addButton.addActionListener(e -> addAdmin());
        cancelButton.addActionListener(e -> dispose());

        // Enter key triggers add
        passwordField.addActionListener(e -> addAdmin());
    }

    /**
     * Adds new admin with validation
     */
    private void addAdmin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        // Validate input
        if (!ValidationUtils.isValidUsername(username)) {
            UIUtils.showErrorMessage(this, "Username must be 3-20 characters (letters, numbers, underscore only).");
            return;
        }

        if (!ValidationUtils.isValidPassword(password)) {
            UIUtils.showErrorMessage(this, "Password must be at least 6 characters.");
            return;
        }

        // Check if username exists
        if (adminExists(username)) {
            UIUtils.showErrorMessage(this, "Admin username already exists.");
            return;
        }

        // Hash password and save
        String hashedPassword = PasswordUtils.hashPassword(password);
        if (CsvUtils.appendToCsv(Constants.ADMINS_CSV, username, hashedPassword)) {
            UIUtils.showSuccessMessage(this, "Admin added successfully!");
            dispose();
        } else {
            UIUtils.showErrorMessage(this, "Failed to add admin. Please try again.");
        }
    }

    /**
     * Checks if admin username already exists
     */
    private boolean adminExists(String username) {
        return CsvUtils.searchCsv(Constants.ADMINS_CSV, 0, username).size() > 0;
    }
}