package com.eventmanagement.ui;

import com.eventmanagement.Constants;
import com.eventmanagement.utils.CsvUtils;
import com.eventmanagement.utils.PasswordUtils;
import com.eventmanagement.utils.UIUtils;
import com.eventmanagement.utils.ValidationUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Admin login page with secure authentication
 */
public class AdminLoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton backButton;

    public AdminLoginPage() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setVisible(true);
    }

    /**
     * Initialize all UI components
     */
    private void initializeComponents() {
        setTitle("Admin Login - " + Constants.APP_TITLE);
        setSize(Constants.SMALL_WINDOW_WIDTH, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = UIUtils.createStyledButton(Constants.LOGIN);
        backButton = UIUtils.createStyledButton(Constants.BACK);

        // Set tooltips
        UIUtils.setTooltip(usernameField, "Enter admin username");
        UIUtils.setTooltip(passwordField, "Enter admin password");
        UIUtils.setTooltip(loginButton, "Login to admin dashboard");
        UIUtils.setTooltip(backButton, "Return to main page");

        // Set default button
        getRootPane().setDefaultButton(loginButton);
    }

    /**
     * Setup the layout of components
     */
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));

        // Header
        JLabel headerLabel = new JLabel("Administrator Login", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        add(headerLabel, BorderLayout.NORTH);

        // Center panel with form
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Username field
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        centerPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        centerPanel.add(usernameField, gbc);

        // Password field
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        centerPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        centerPanel.add(passwordField, gbc);

        // Login button
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        centerPanel.add(loginButton, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // Back button at bottom
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Setup event handlers
     */
    private void setupEventHandlers() {
        loginButton.addActionListener(e -> performLogin());
        backButton.addActionListener(e -> goBack());

        // Enter key triggers login
        passwordField.addActionListener(e -> performLogin());
    }

    /**
     * Performs admin login with authentication
     */
    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        // Input validation
        if (!ValidationUtils.isNotEmpty(username) || !ValidationUtils.isNotEmpty(password)) {
            UIUtils.showErrorMessage(this, "Please enter both username and password.");
            return;
        }

        // Disable login button to prevent multiple clicks
        loginButton.setEnabled(false);

        // Perform authentication in background thread
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return authenticateAdmin(username, password);
            }

            @Override
            protected void done() {
                try {
                    boolean authenticated = get();
                    if (authenticated) {
                        UIUtils.showSuccessMessage(AdminLoginPage.this, Constants.LOGIN_SUCCESS);
                        openAdminDashboard();
                    } else {
                        UIUtils.showErrorMessage(AdminLoginPage.this, Constants.LOGIN_FAILED);
                        clearFields();
                    }
                } catch (Exception e) {
                    UIUtils.showErrorMessage(AdminLoginPage.this, "An error occurred during login. Please try again.");
                } finally {
                    loginButton.setEnabled(true);
                }
            }
        };

        worker.execute();
    }

    /**
     * Authenticates admin credentials
     */
    private boolean authenticateAdmin(String username, String password) {
        // First check default admin (for demo purposes)
        if ("admin".equals(username) && "admin123".equals(password)) {
            return true;
        }

        // Then check admin CSV file
        List<String[]> admins = CsvUtils.readCsv(Constants.ADMINS_CSV);
        for (String[] row : admins) {
            if (row.length >= 2 && row[0].equals(username)) {
                return PasswordUtils.verifyPassword(password, row[1]);
            }
        }

        return false;
    }

    /**
     * Opens admin dashboard after successful login
     */
    private void openAdminDashboard() {
        SwingUtilities.invokeLater(() -> {
            new AdminDashboard();
            dispose();
        });
    }

    /**
     * Returns to home page
     */
    private void goBack() {
        SwingUtilities.invokeLater(() -> {
            new HomePage();
            dispose();
        });
    }

    /**
     * Clears all input fields
     */
    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        usernameField.requestFocus();
    }
}