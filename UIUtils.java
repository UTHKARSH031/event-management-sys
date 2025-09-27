package com.eventmanagement.utils;

import javax.swing.*;
import java.awt.*;

/**
 * Utility class for common UI operations
 */
public final class UIUtils {

    /**
     * Creates a standardized frame with common settings
     */
    public static JFrame createFrame(String title, int width, int height) {
        JFrame frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        return frame;
    }

    /**
     * Creates a labeled text field panel
     */
    public static JPanel createLabeledField(String labelText, JTextField textField) {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 5));
        panel.add(new JLabel(labelText + ":"));
        panel.add(textField);
        return panel;
    }

    /**
     * Creates a labeled password field panel
     */
    public static JPanel createLabeledField(String labelText, JPasswordField passwordField) {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 5));
        panel.add(new JLabel(labelText + ":"));
        panel.add(passwordField);
        return panel;
    }

    /**
     * Creates a button with standard styling
     */
    public static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(120, 30));
        return button;
    }

    /**
     * Shows success message
     */
    public static void showSuccessMessage(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows error message
     */
    public static void showErrorMessage(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows warning message
     */
    public static void showWarningMessage(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Shows confirmation dialog
     */
    public static boolean showConfirmDialog(Component parent, String message) {
        int result = JOptionPane.showConfirmDialog(parent, message, "Confirmation", 
                                                 JOptionPane.YES_NO_OPTION, 
                                                 JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }

    /**
     * Gets input from user
     */
    public static String showInputDialog(Component parent, String message, String title) {
        return JOptionPane.showInputDialog(parent, message, title, JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Clears all text fields in a container
     */
    public static void clearTextFields(Container container) {
        for (Component component : container.getComponents()) {
            if (component instanceof JTextField) {
                ((JTextField) component).setText("");
            } else if (component instanceof JPasswordField) {
                ((JPasswordField) component).setText("");
            } else if (component instanceof Container) {
                clearTextFields((Container) component);
            }
        }
    }

    /**
     * Sets tooltip for components
     */
    public static void setTooltip(JComponent component, String tooltipText) {
        component.setToolTipText(tooltipText);
    }

    private UIUtils() {
        // Prevent instantiation
    }
}