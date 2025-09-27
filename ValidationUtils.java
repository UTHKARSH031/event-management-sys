package com.eventmanagement.utils;

import java.util.regex.Pattern;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Utility class for input validation
 */
public final class ValidationUtils {
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern USERNAME_PATTERN = 
        Pattern.compile("^[a-zA-Z0-9_]{3,20}$");
    private static final Pattern TIME_PATTERN = 
        Pattern.compile("^([01]?[0-9]|2[0-3]):[0-5][0-9]$");

    /**
     * Validates if a string is not null or empty
     */
    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    /**
     * Validates email format
     */
    public static boolean isValidEmail(String email) {
        return isNotEmpty(email) && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validates username format (3-20 characters, alphanumeric and underscore only)
     */
    public static boolean isValidUsername(String username) {
        return isNotEmpty(username) && USERNAME_PATTERN.matcher(username).matches();
    }

    /**
     * Validates password strength (minimum 6 characters)
     */
    public static boolean isValidPassword(String password) {
        return isNotEmpty(password) && password.length() >= 6;
    }

    /**
     * Validates date format (DD-MM-YYYY)
     */
    public static boolean isValidDate(String date) {
        if (!isNotEmpty(date)) return false;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate.parse(date, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Validates time format (HH:MM)
     */
    public static boolean isValidTime(String time) {
        return isNotEmpty(time) && TIME_PATTERN.matcher(time).matches();
    }

    /**
     * Validates if date is not in the past
     */
    public static boolean isDateNotPast(String date) {
        if (!isValidDate(date)) return false;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate eventDate = LocalDate.parse(date, formatter);
            return !eventDate.isBefore(LocalDate.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Validates multiple fields are not empty
     */
    public static boolean areAllFieldsValid(String... fields) {
        for (String field : fields) {
            if (!isNotEmpty(field)) {
                return false;
            }
        }
        return true;
    }

    private ValidationUtils() {
        // Prevent instantiation
    }
}