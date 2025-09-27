package com.eventmanagement.ui;

import com.eventmanagement.Constants;
import com.eventmanagement.models.Event;
import com.eventmanagement.models.Registration;
import com.eventmanagement.models.Student;
import com.eventmanagement.services.EventService;
import com.eventmanagement.services.RegistrationService;
import com.eventmanagement.utils.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Student dashboard for viewing and managing event registrations
 */
public class StudentDashboard extends JFrame {
    private Student currentStudent;
    private JTabbedPane tabbedPane;
    private JPanel eventsPanel;
    private JPanel registrationsPanel;
    private JButton logoutButton;

    public StudentDashboard(Student student) {
        this.currentStudent = student;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadData();
        setVisible(true);
    }

    /**
     * Initialize all UI components
     */
    private void initializeComponents() {
        setTitle("Student Dashboard - " + currentStudent.getUsername() + " - " + Constants.APP_TITLE);
        setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();
        eventsPanel = new JPanel(new BorderLayout());
        registrationsPanel = new JPanel(new BorderLayout());
        logoutButton = UIUtils.createStyledButton(Constants.LOGOUT);

        UIUtils.setTooltip(logoutButton, "Logout and return to home page");
    }

    /**
     * Setup the layout of components
     */
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel headerLabel = new JLabel("Welcome, " + currentStudent.getUsername() + "!", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        headerPanel.add(headerLabel, BorderLayout.CENTER);

        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.add(logoutButton);
        headerPanel.add(logoutPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Tabbed pane
        tabbedPane.addTab("Available Events", eventsPanel);
        tabbedPane.addTab("My Registrations", registrationsPanel);
        add(tabbedPane, BorderLayout.CENTER);
    }

    /**
     * Setup event handlers
     */
    private void setupEventHandlers() {
        logoutButton.addActionListener(e -> logout());

        // Refresh data when switching tabs
        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            if (selectedIndex == 0) {
                loadAvailableEvents();
            } else if (selectedIndex == 1) {
                loadMyRegistrations();
            }
        });
    }

    /**
     * Load initial data
     */
    private void loadData() {
        loadAvailableEvents();
        loadMyRegistrations();
    }

    /**
     * Load available events tab
     */
    private void loadAvailableEvents() {
        SwingWorker<List<Event>, Void> worker = new SwingWorker<List<Event>, Void>() {
            @Override
            protected List<Event> doInBackground() throws Exception {
                return EventService.getAllEvents();
            }

            @Override
            protected void done() {
                try {
                    List<Event> events = get();
                    displayEventsForRegistration(events);
                } catch (Exception e) {
                    UIUtils.showErrorMessage(StudentDashboard.this, "Error loading events: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    /**
     * Display events in registration tab
     */
    private void displayEventsForRegistration(List<Event> events) {
        eventsPanel.removeAll();

        if (events.isEmpty()) {
            eventsPanel.add(new JLabel("No events available at this time.", SwingConstants.CENTER));
        } else {
            JPanel listPanel = new JPanel();
            listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

            for (Event event : events) {
                JPanel eventCard = createEventCard(event, true);
                listPanel.add(eventCard);
                listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }

            JScrollPane scrollPane = new JScrollPane(listPanel);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            eventsPanel.add(scrollPane, BorderLayout.CENTER);
        }

        eventsPanel.revalidate();
        eventsPanel.repaint();
    }

    /**
     * Load my registrations tab
     */
    private void loadMyRegistrations() {
        SwingWorker<List<Registration>, Void> worker = new SwingWorker<List<Registration>, Void>() {
            @Override
            protected List<Registration> doInBackground() throws Exception {
                return RegistrationService.getRegistrationsByStudent(currentStudent.getUsername());
            }

            @Override
            protected void done() {
                try {
                    List<Registration> registrations = get();
                    displayMyRegistrations(registrations);
                } catch (Exception e) {
                    UIUtils.showErrorMessage(StudentDashboard.this, "Error loading registrations: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    /**
     * Display my registrations
     */
    private void displayMyRegistrations(List<Registration> registrations) {
        registrationsPanel.removeAll();

        if (registrations.isEmpty()) {
            registrationsPanel.add(new JLabel("You haven't registered for any events yet.", SwingConstants.CENTER));
        } else {
            JPanel listPanel = new JPanel();
            listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

            for (Registration registration : registrations) {
                Event event = EventService.getEventById(registration.getEventId());
                if (event != null) {
                    JPanel registrationCard = createRegistrationCard(event, registration);
                    listPanel.add(registrationCard);
                    listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }

            JScrollPane scrollPane = new JScrollPane(listPanel);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            registrationsPanel.add(scrollPane, BorderLayout.CENTER);
        }

        registrationsPanel.revalidate();
        registrationsPanel.repaint();
    }

    /**
     * Create event card for registration
     */
    private JPanel createEventCard(Event event, boolean showRegisterButton) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Event info
        JPanel infoPanel = new JPanel(new GridLayout(4, 1));
        infoPanel.add(new JLabel("Event: " + event.getEventName()));
        infoPanel.add(new JLabel("Date: " + event.getEventDate()));
        infoPanel.add(new JLabel("Time: " + event.getEventTime()));
        infoPanel.add(new JLabel("Location: " + event.getEventLocation()));

        card.add(infoPanel, BorderLayout.CENTER);

        // Register button
        if (showRegisterButton) {
            JButton registerButton = UIUtils.createStyledButton("Register");

            // Check if already registered
            boolean alreadyRegistered = RegistrationService.isStudentRegistered(
                currentStudent.getUsername(), event.getEventId());

            if (alreadyRegistered) {
                registerButton.setText("Registered");
                registerButton.setEnabled(false);
            } else {
                registerButton.addActionListener(e -> registerForEvent(event));
            }

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.add(registerButton);
            card.add(buttonPanel, BorderLayout.EAST);
        }

        return card;
    }

    /**
     * Create registration card with cancel option
     */
    private JPanel createRegistrationCard(Event event, Registration registration) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Event info
        JPanel infoPanel = new JPanel(new GridLayout(5, 1));
        infoPanel.add(new JLabel("Event: " + event.getEventName()));
        infoPanel.add(new JLabel("Date: " + event.getEventDate()));
        infoPanel.add(new JLabel("Time: " + event.getEventTime()));
        infoPanel.add(new JLabel("Location: " + event.getEventLocation()));
        infoPanel.add(new JLabel("Registered: " + registration.getRegistrationTime()));

        card.add(infoPanel, BorderLayout.CENTER);

        // Cancel button
        JButton cancelButton = UIUtils.createStyledButton("Cancel");
        cancelButton.addActionListener(e -> cancelRegistration(event, registration));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(cancelButton);
        card.add(buttonPanel, BorderLayout.EAST);

        return card;
    }

    /**
     * Register for an event
     */
    private void registerForEvent(Event event) {
        if (UIUtils.showConfirmDialog(this, 
            "Are you sure you want to register for '" + event.getEventName() + "'?")) {

            SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    return RegistrationService.registerForEvent(currentStudent, event.getEventId());
                }

                @Override
                protected void done() {
                    try {
                        boolean success = get();
                        if (success) {
                            UIUtils.showSuccessMessage(StudentDashboard.this, "Registration successful!");
                            loadAvailableEvents(); // Refresh to update button states
                        } else {
                            UIUtils.showErrorMessage(StudentDashboard.this, "Registration failed. You may already be registered.");
                        }
                    } catch (Exception e) {
                        UIUtils.showErrorMessage(StudentDashboard.this, "Error during registration: " + e.getMessage());
                    }
                }
            };
            worker.execute();
        }
    }

    /**
     * Cancel registration for an event
     */
    private void cancelRegistration(Event event, Registration registration) {
        if (UIUtils.showConfirmDialog(this, 
            "Are you sure you want to cancel registration for '" + event.getEventName() + "'?")) {

            SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    return RegistrationService.cancelRegistration(
                        currentStudent.getUsername(), event.getEventId());
                }

                @Override
                protected void done() {
                    try {
                        boolean success = get();
                        if (success) {
                            UIUtils.showSuccessMessage(StudentDashboard.this, "Registration cancelled successfully!");
                            loadMyRegistrations(); // Refresh registrations
                            loadAvailableEvents(); // Refresh to update button states
                        } else {
                            UIUtils.showErrorMessage(StudentDashboard.this, "Failed to cancel registration.");
                        }
                    } catch (Exception e) {
                        UIUtils.showErrorMessage(StudentDashboard.this, "Error cancelling registration: " + e.getMessage());
                    }
                }
            };
            worker.execute();
        }
    }

    /**
     * Logout and return to home page
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