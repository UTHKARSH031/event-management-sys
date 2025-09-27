package com.eventmanagement.ui;

import com.eventmanagement.Constants;
import com.eventmanagement.models.Student;
import com.eventmanagement.services.StudentService;
import com.eventmanagement.utils.UIUtils;
import com.eventmanagement.utils.ValidationUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Student signup page with comprehensive validation
 */
public class StudentSignupPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JTextField rollNoField;
    private JTextField branchField;
    private JTextField universityField;
    private JButton signupButton;
    private JButton backButton;

    public StudentSignupPage() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setVisible(true);
    }

    /**
     * Initialize all UI components
     */
    private void initializeComponents() {
        setTitle("Student Registration - " + Constants.APP_TITLE);
        setSize(450, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        emailField = new JTextField(20);
        rollNoField = new JTextField(20);
        branchField = new JTextField(20);
        universityField = new JTextField(20);
        signupButton = UIUtils.createStyledButton(Constants.SIGNUP);
        backButton = UIUtils.createStyledButton(Constants.BACK);

        // Set tooltips with validation requirements
        UIUtils.setTooltip(usernameField, "3-20 characters, letters, numbers and underscore only");
        UIUtils.setTooltip(passwordField, "Minimum 6 characters");
        UIUtils.setTooltip(emailField, "Valid email address required");
        UIUtils.setTooltip(rollNoField, "Your student roll number");
        UIUtils.setTooltip(branchField, "Your academic branch/department");
        UIUtils.setTooltip(universityField, "Your university name");

        // Set default button
        getRootPane().setDefaultButton(signupButton);
    }

    /**
     * Setup the layout of components
     */
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));

        // Header
        JLabel headerLabel = new JLabel("Create Student Account", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        add(headerLabel, BorderLayout.NORTH);

        // Center panel with form
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);

        // Add form fields
        addFormField(centerPanel, gbc, "Username:", usernameField, 0);
        addFormField(centerPanel, gbc, "Password:", passwordField, 1);
        addFormField(centerPanel, gbc, "Email:", emailField, 2);
        addFormField(centerPanel, gbc, "Roll Number:", rollNoField, 3);
        addFormField(centerPanel, gbc, "Branch:", branchField, 4);
        addFormField(centerPanel, gbc, "University:", universityField, 5);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(signupButton);

        gbc.gridx = 0; gbc.gridy = 6;
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
     * Helper method to add form fields
     */
    private void addFormField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent field, int row) {
        gbc.gridx = 0; gbc.gridy = row;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1; gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(field, gbc);
    }

    /**
     * Setup event handlers
     */
    private void setupEventHandlers() {
        signupButton.addActionListener(e -> performSignup());
        backButton.addActionListener(e -> goBack());

        // Enter key on last field triggers signup
        universityField.addActionListener(e -> performSignup());
    }

    /**
     * Performs student registration with comprehensive validation
     */
    private void performSignup() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String email = emailField.getText().trim();
        String rollNo = rollNoField.getText().trim();
        String branch = branchField.getText().trim();
        String university = universityField.getText().trim();

        // Validate all fields
        StringBuilder errors = new StringBuilder();

        if (!ValidationUtils.isValidUsername(username)) {
            errors.append("• Username must be 3-20 characters (letters, numbers, underscore only)\n");
        }

        if (!ValidationUtils.isValidPassword(password)) {
            errors.append("• Password must be at least 6 characters\n");
        }

        if (!ValidationUtils.isValidEmail(email)) {
            errors.append("• Please enter a valid email address\n");
        }

        if (!ValidationUtils.areAllFieldsValid(rollNo, branch, university)) {
            errors.append("• All fields are required\n");
        }

        if (errors.length() > 0) {
            UIUtils.showErrorMessage(this, "Please correct the following errors:\n" + errors.toString());
            return;
        }

        // Disable signup button to prevent multiple submissions
        signupButton.setEnabled(false);

        // Perform registration in background thread
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                Student student = new Student(username, password, email, rollNo, branch, university);
                return StudentService.register(student);
            }

            @Override
            protected void done() {
                try {
                    boolean success = get();
                    if (success) {
                        UIUtils.showSuccessMessage(StudentSignupPage.this, Constants.SIGNUP_SUCCESS);
                        goToLogin();
                    } else {
                        UIUtils.showErrorMessage(StudentSignupPage.this, 
                            "Registration failed. Username might already exist or there was an error.");
                        clearFields();
                    }
                } catch (Exception e) {
                    UIUtils.showErrorMessage(StudentSignupPage.this, 
                        "An error occurred during registration. Please try again.");
                } finally {
                    signupButton.setEnabled(true);
                }
            }
        };

        worker.execute();
    }

    /**
     * Goes to login page after successful registration
     */
    private void goToLogin() {
        SwingUtilities.invokeLater(() -> {
            new StudentLoginPage();
            dispose();
        });
    }

    /**
     * Returns to student login page
     */
    private void goBack() {
        SwingUtilities.invokeLater(() -> {
            new StudentLoginPage();
            dispose();
        });
    }

    /**
     * Clears all input fields
     */
    private void clearFields() {
        UIUtils.clearTextFields(this);
        usernameField.requestFocus();
    }
}