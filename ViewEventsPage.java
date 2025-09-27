package com.eventmanagement.ui;

import com.eventmanagement.Constants;
import com.eventmanagement.models.Event;
import com.eventmanagement.models.Registration;
import com.eventmanagement.services.EventService;
import com.eventmanagement.services.RegistrationService;
import com.eventmanagement.utils.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Admin view for managing events and viewing registrations
 */
public class ViewEventsPage extends JFrame {
    private JPanel eventsListPanel;
    private JScrollPane scrollPane;
    private JButton backButton;
    private JButton refreshButton;

    public ViewEventsPage() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadEvents();
        setVisible(true);
    }

    /**
     * Initialize all UI components
     */
    private void initializeComponents() {
        setTitle("Manage Events - " + Constants.APP_TITLE);
        setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        eventsListPanel = new JPanel();
        eventsListPanel.setLayout(new BoxLayout(eventsListPanel, BoxLayout.Y_AXIS));

        scrollPane = new JScrollPane(eventsListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        backButton = UIUtils.createStyledButton(Constants.BACK);
        refreshButton = UIUtils.createStyledButton("Refresh");

        UIUtils.setTooltip(backButton, "Return to admin dashboard");
        UIUtils.setTooltip(refreshButton, "Refresh events list");
    }

    /**
     * Setup the layout of components
     */
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));

        // Header
        JLabel headerLabel = new JLabel("Event Management", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        add(headerLabel, BorderLayout.NORTH);

        // Events list in center
        add(scrollPane, BorderLayout.CENTER);

        // Bottom panel with buttons
        JPanel bottomPanel = new JPanel(new BorderLayout());

        JPanel leftButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftButtonPanel.add(backButton);

        JPanel rightButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightButtonPanel.add(refreshButton);

        bottomPanel.add(leftButtonPanel, BorderLayout.WEST);
        bottomPanel.add(rightButtonPanel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Setup event handlers
     */
    private void setupEventHandlers() {
        backButton.addActionListener(e -> goBack());
        refreshButton.addActionListener(e -> loadEvents());
    }

    /**
     * Load and display all events
     */
    private void loadEvents() {
        // Show loading message
        eventsListPanel.removeAll();
        eventsListPanel.add(new JLabel("Loading events...", SwingConstants.CENTER));
        eventsListPanel.revalidate();
        eventsListPanel.repaint();

        SwingWorker<List<Event>, Void> worker = new SwingWorker<List<Event>, Void>() {
            @Override
            protected List<Event> doInBackground() throws Exception {
                return EventService.getAllEvents();
            }

            @Override
            protected void done() {
                try {
                    List<Event> events = get();
                    displayEvents(events);
                } catch (Exception e) {
                    UIUtils.showErrorMessage(ViewEventsPage.this, "Error loading events: " + e.getMessage());
                    eventsListPanel.removeAll();
                    eventsListPanel.add(new JLabel("Error loading events. Please try again.", SwingConstants.CENTER));
                    eventsListPanel.revalidate();
                    eventsListPanel.repaint();
                }
            }
        };
        worker.execute();
    }

    /**
     * Display events in the panel
     */
    private void displayEvents(List<Event> events) {
        eventsListPanel.removeAll();

        if (events.isEmpty()) {
            JLabel noEventsLabel = new JLabel("No events found. Create some events first!", SwingConstants.CENTER);
            noEventsLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            eventsListPanel.add(noEventsLabel);
        } else {
            for (Event event : events) {
                JPanel eventCard = createEventManagementCard(event);
                eventsListPanel.add(eventCard);
                eventsListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        eventsListPanel.revalidate();
        eventsListPanel.repaint();
    }

    /**
     * Create event management card with admin actions
     */
    private JPanel createEventManagementCard(Event event) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Event ID: " + event.getEventId()),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Event information panel
        JPanel infoPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        infoPanel.add(new JLabel("Name:", SwingConstants.RIGHT));
        infoPanel.add(new JLabel(event.getEventName()));
        infoPanel.add(new JLabel("Date:", SwingConstants.RIGHT));
        infoPanel.add(new JLabel(event.getEventDate()));
        infoPanel.add(new JLabel("Time:", SwingConstants.RIGHT));
        infoPanel.add(new JLabel(event.getEventTime()));
        infoPanel.add(new JLabel("Location:", SwingConstants.RIGHT));
        infoPanel.add(new JLabel(event.getEventLocation()));

        card.add(infoPanel, BorderLayout.CENTER);

        // Action buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton viewRegistrationsButton = UIUtils.createStyledButton("View Registrations");
        JButton deleteEventButton = UIUtils.createStyledButton("Delete Event");

        // Style delete button as dangerous action
        deleteEventButton.setBackground(new Color(220, 53, 69));
        deleteEventButton.setForeground(Color.WHITE);

        UIUtils.setTooltip(viewRegistrationsButton, "View all registrations for this event");
        UIUtils.setTooltip(deleteEventButton, "Delete this event (WARNING: Cannot be undone!)");

        viewRegistrationsButton.addActionListener(e -> viewEventRegistrations(event));
        deleteEventButton.addActionListener(e -> deleteEvent(event));

        buttonPanel.add(viewRegistrationsButton);
        buttonPanel.add(deleteEventButton);

        card.add(buttonPanel, BorderLayout.SOUTH);

        return card;
    }

    /**
     * View registrations for a specific event
     */
    private void viewEventRegistrations(Event event) {
        SwingWorker<List<Registration>, Void> worker = new SwingWorker<List<Registration>, Void>() {
            @Override
            protected List<Registration> doInBackground() throws Exception {
                return RegistrationService.getRegistrationsByEvent(event.getEventId());
            }

            @Override
            protected void done() {
                try {
                    List<Registration> registrations = get();
                    showRegistrationsDialog(event, registrations);
                } catch (Exception e) {
                    UIUtils.showErrorMessage(ViewEventsPage.this, 
                        "Error loading registrations: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    /**
     * Show registrations in a dialog
     */
    private void showRegistrationsDialog(Event event, List<Registration> registrations) {
        JDialog dialog = new JDialog(this, "Registrations for " + event.getEventName(), true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);

        if (registrations.isEmpty()) {
            JLabel noRegLabel = new JLabel("No registrations for this event yet.", SwingConstants.CENTER);
            dialog.add(noRegLabel);
        } else {
            // Create table with registration data
            String[] columnNames = {"Registration ID", "Student", "Email", "Roll No", "Registration Time"};
            String[][] data = new String[registrations.size()][5];

            for (int i = 0; i < registrations.size(); i++) {
                Registration reg = registrations.get(i);
                data[i][0] = reg.getRegistrationId();
                data[i][1] = reg.getUsername();
                data[i][2] = reg.getEmail();
                data[i][3] = reg.getRollNo();
                data[i][4] = reg.getRegistrationTime();
            }

            JTable table = new JTable(data, columnNames);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            JScrollPane tableScrollPane = new JScrollPane(table);
            dialog.add(tableScrollPane, BorderLayout.CENTER);

            // Summary label
            JLabel summaryLabel = new JLabel("Total Registrations: " + registrations.size(), SwingConstants.CENTER);
            summaryLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            dialog.add(summaryLabel, BorderLayout.SOUTH);
        }

        dialog.setVisible(true);
    }

    /**
     * Delete an event with confirmation
     */
    private void deleteEvent(Event event) {
        String message = String.format(
            "Are you sure you want to delete the event '%s'?\n\n" +
            "This action cannot be undone and will also delete all registrations for this event.",
            event.getEventName()
        );

        if (UIUtils.showConfirmDialog(this, message)) {
            SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    return EventService.deleteEvent(event.getEventId());
                }

                @Override
                protected void done() {
                    try {
                        boolean success = get();
                        if (success) {
                            UIUtils.showSuccessMessage(ViewEventsPage.this, 
                                "Event '" + event.getEventName() + "' deleted successfully!");
                            loadEvents(); // Refresh the list
                        } else {
                            UIUtils.showErrorMessage(ViewEventsPage.this, 
                                "Failed to delete the event. Please try again.");
                        }
                    } catch (Exception e) {
                        UIUtils.showErrorMessage(ViewEventsPage.this, 
                            "Error deleting event: " + e.getMessage());
                    }
                }
            };
            worker.execute();
        }
    }

    /**
     * Return to admin dashboard
     */
    private void goBack() {
        SwingUtilities.invokeLater(() -> {
            new AdminDashboard();
            dispose();
        });
    }
}