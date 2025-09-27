package com.eventmanagement.ui;

import com.eventmanagement.Constants;
import com.eventmanagement.models.Event;
import com.eventmanagement.services.EventService;
import com.eventmanagement.utils.UIUtils;
import com.eventmanagement.utils.ValidationUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Page for adding new events with comprehensive validation
 */
public class AddEventPage extends JFrame {
    private JTextField eventNameField;
    private JTextField eventDateField;
    private JTextField eventTimeField;
    private JTextField eventLocationField;
    private JButton addButton;
    private JButton backButton;

    public AddEventPage() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setVisible(true);
    }

    /**
     * Initialize all UI components
     */
    private void initializeComponents() {
        setTitle("Add New Event - " + Constants.APP_TITLE);
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        eventNameField = new JTextField(20);
        eventDateField = new JTextField(20);
        eventTimeField = new JTextField(20);
        eventLocationField = new JTextField(20);
        addButton = UIUtils.createStyledButton("Add Event");
        backButton = UIUtils.createStyledButton(Constants.BACK);

        // Set tooltips with format requirements
        UIUtils.setTooltip(eventNameField, "Enter the event name");
        UIUtils.setTooltip(eventDateField, "Enter date in DD-MM-YYYY format (e.g., 25-12-2025)");
        UIUtils.setTooltip(eventTimeField, "Enter time in HH:MM format (e.g., 14:30)");
        UIUtils.setTooltip(eventLocationField, "Enter the event location/venue");

        // Set default button
        getRootPane().setDefaultButton(addButton);
    }

    /**
     * Setup the layout of components
     */
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));

        // Header
        JLabel headerLabel = new JLabel("Create New Event", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        add(headerLabel, BorderLayout.NORTH);

        // Center panel with form
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Event name
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        centerPanel.add(new JLabel("Event Name:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        centerPanel.add(eventNameField, gbc);

        // Date
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        centerPanel.add(new JLabel("Date (DD-MM-YYYY):"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        centerPanel.add(eventDateField, gbc);

        // Time
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        centerPanel.add(new JLabel("Time (HH:MM):"), gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        centerPanel.add(eventTimeField, gbc);

        // Location
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        centerPanel.add(new JLabel("Location:"), gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        centerPanel.add(eventLocationField, gbc);

        // Add button
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        centerPanel.add(addButton, gbc);

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
        addButton.addActionListener(e -> addEvent());
        backButton.addActionListener(e -> goBack());

        // Enter key on last field triggers add
        eventLocationField.addActionListener(e -> addEvent());
    }

    /**
     * Adds new event with comprehensive validation
     */
    private void addEvent() {
        String eventName = eventNameField.getText().trim();
        String eventDate = eventDateField.getText().trim();
        String eventTime = eventTimeField.getText().trim();
        String eventLocation = eventLocationField.getText().trim();

        // Validate all fields
        StringBuilder errors = new StringBuilder();

        if (!ValidationUtils.areAllFieldsValid(eventName, eventDate, eventTime, eventLocation)) {
            errors.append("• All fields are required\n");
        }

        if (!ValidationUtils.isValidDate(eventDate)) {
            errors.append("• Date must be in DD-MM-YYYY format\n");
        }

        if (!ValidationUtils.isValidTime(eventTime)) {
            errors.append("• Time must be in HH:MM format (24-hour)\n");
        }

        if (ValidationUtils.isValidDate(eventDate) && !ValidationUtils.isDateNotPast(eventDate)) {
            errors.append("• Event date cannot be in the past\n");
        }

        if (errors.length() > 0) {
            UIUtils.showErrorMessage(this, "Please correct the following errors:\n" + errors.toString());
            return;
        }

        // Disable add button to prevent multiple submissions
        addButton.setEnabled(false);

        // Create event in background thread
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                Event event = new Event(eventName, eventDate, eventTime, eventLocation);
                return EventService.createEvent(event);
            }

            @Override
            protected void done() {
                try {
                    boolean success = get();
                    if (success) {
                        UIUtils.showSuccessMessage(AddEventPage.this, "Event created successfully!");
                        clearFields();
                        // Option to go back to admin dashboard
                        if (UIUtils.showConfirmDialog(AddEventPage.this, 
                            "Event created successfully! Would you like to return to the admin dashboard?")) {
                            goBack();
                        }
                    } else {
                        UIUtils.showErrorMessage(AddEventPage.this, "Failed to create event. Please try again.");
                    }
                } catch (Exception e) {
                    UIUtils.showErrorMessage(AddEventPage.this, "An error occurred while creating the event.");
                } finally {
                    addButton.setEnabled(true);
                }
            }
        };

        worker.execute();
    }

    /**
     * Returns to admin dashboard
     */
    private void goBack() {
        SwingUtilities.invokeLater(() -> {
            new AdminDashboard();
            dispose();
        });
    }

    /**
     * Clears all input fields
     */
    private void clearFields() {
        UIUtils.clearTextFields(this);
        eventNameField.requestFocus();
    }
}