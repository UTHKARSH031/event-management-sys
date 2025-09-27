package com.eventmanagement;

/**
 * Constants class containing all string literals and configuration values
 */
public final class Constants {
    // File paths
    public static final String STUDENTS_CSV = "students.csv";
    public static final String EVENTS_CSV = "events.csv";
    public static final String REGISTRATIONS_CSV = "registrations.csv";
    public static final String ADMINS_CSV = "admins.csv";

    // UI Constants
    public static final String APP_TITLE = "Event Management System";
    public static final String WELCOME_MESSAGE = "Welcome to Event Management System";
    public static final int WINDOW_WIDTH = 600;
    public static final int WINDOW_HEIGHT = 500;
    public static final int SMALL_WINDOW_WIDTH = 400;
    public static final int SMALL_WINDOW_HEIGHT = 300;

    // Button Text
    public static final String ADMIN_LOGIN = "Admin Login";
    public static final String STUDENT_LOGIN = "Student Login";
    public static final String LOGIN = "Login";
    public static final String SIGNUP = "Signup";
    public static final String BACK = "Back";
    public static final String LOGOUT = "Logout";

    // Messages
    public static final String LOGIN_SUCCESS = "Login Successful!";
    public static final String LOGIN_FAILED = "Invalid credentials!";
    public static final String SIGNUP_SUCCESS = "Signup Successful! Please login.";
    public static final String SIGNUP_FAILED = "Signup failed. Username might already exist.";
    public static final String VALIDATION_ERROR = "Please fill all fields correctly.";
    public static final String OPERATION_SUCCESS = "Operation completed successfully!";
    public static final String OPERATION_FAILED = "Operation failed. Please try again.";

    // Date and Time formats
    public static final String DATE_FORMAT = "dd-MM-yyyy";
    public static final String TIME_FORMAT = "HH:mm";
    public static final String DATETIME_FORMAT = "dd-MM-yyyy HH:mm:ss";

    private Constants() {
        // Prevent instantiation
    }
}