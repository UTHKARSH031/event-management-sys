package com.eventmanagement.ui;

import com.eventmanagement.Constants;
import com.eventmanagement.models.Student;
import com.eventmanagement.services.StudentService;
import com.eventmanagement.utils.UIUtils;
import com.eventmanagement.utils.ValidationUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Student login page with secure authentication
 */
public class StudentLoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signupButton;
    private JButton backButton;

    public StudentLoginPage() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setVisible(true);
    }

    /**
     * Initialize all UI components
     */
    private void initializeComponents() {
        setTitle("Student Login - " + Constants.APP_TITLE);
        setSize(Constants.SMALL_WINDOW_WIDTH, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = UIUtils.createStyledButton(Constants.LOGIN);
        signupButton = UIUtils.createStyledButton(Constants.SIGNUP);
        backButton = UIUtils.createStyledButton(Constants.BACK);

        // Set tooltips
        UIUtils.setTooltip(usernameField, "Enter your username (3-20 characters, alphanumeric)");
        UIUtils.setTooltip(passwordField, "Enter your password (minimum 6 characters)");
        UIUtils.setTooltip(loginButton, "Click to login with your credentials");
        UIUtils.setTooltip(signupButton, "Don't have an account? Click to register");
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
        JLabel headerLabel = new JLabel("Student Login", SwingConstants.CENTER);
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

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(loginButton);
        buttonPanel.add(signupButton);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        centerPanel.add(buttonPanel, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // Back button at bottom
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Setup event handlers for buttons and keyboard actions
     */
    private void setupEventHandlers() {
        loginButton.addActionListener(e -> performLogin());
        signupButton.addActionListener(e -> openSignupPage());
        backButton.addActionListener(e -> goBack());

        // Enter key on password field triggers login
        passwordField.addActionListener(e -> performLogin());
    }

    /**
     * Performs student login with validation and authentication
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
        SwingWorker<Student, Void> worker = new SwingWorker<Student, Void>() {
            @Override
            protected Student doInBackground() throws Exception {
                return StudentService.authenticate(username, password);
            }

            @Override
            protected void done() {
                try {
                    Student student = get();
                    if (student != null) {
                        UIUtils.showSuccessMessage(StudentLoginPage.this, Constants.LOGIN_SUCCESS);
                        openStudentDashboard(student);
                    } else {
                        UIUtils.showErrorMessage(StudentLoginPage.this, Constants.LOGIN_FAILED);
                        clearFields();
                    }
                } catch (Exception e) {
                    UIUtils.showErrorMessage(StudentLoginPage.this, "An error occurred during login. Please try again.");
                } finally {
                    loginButton.setEnabled(true);
                }
            }
        };

        worker.execute();
    }

    /**
     * Opens student dashboard after successful login
     */
    private void openStudentDashboard(Student student) {
        SwingUtilities.invokeLater(() -> {
            new StudentDashboard(student);
            dispose();
        });
    }

    /**
     * Opens student signup page
     */
    private void openSignupPage() {
        SwingUtilities.invokeLater(() -> {
            new StudentSignupPage();
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