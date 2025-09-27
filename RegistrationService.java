package com.eventmanagement.services;

import com.eventmanagement.models.Registration;
import com.eventmanagement.models.Student;
import com.eventmanagement.utils.CsvUtils;
import com.eventmanagement.Constants;

import java.util.List;
import java.util.ArrayList;

/**
 * Service class for registration-related operations
 */
public class RegistrationService {

    /**
     * Registers a student for an event
     * @param student The student to register
     * @param eventId The event ID
     * @return true if registration successful, false otherwise
     */
    public static boolean registerForEvent(Student student, String eventId) {
        // Check if already registered
        if (isStudentRegistered(student.getUsername(), eventId)) {
            return false;
        }

        Registration registration = new Registration(
            student.getUsername(),
            student.getEmail(),
            student.getRollNo(),
            eventId
        );

        return CsvUtils.appendToCsv(Constants.REGISTRATIONS_CSV, registration.toCsvRow());
    }

    /**
     * Cancels a student's registration for an event
     */
    public static boolean cancelRegistration(String username, String eventId) {
        List<String[]> registrations = CsvUtils.readCsv(Constants.REGISTRATIONS_CSV);
        boolean removed = registrations.removeIf(row -> 
            row.length >= 6 && row[1].equals(username) && row[4].equals(eventId));

        if (removed) {
            return CsvUtils.writeCsv(Constants.REGISTRATIONS_CSV, registrations);
        }
        return false;
    }

    /**
     * Checks if a student is already registered for an event
     */
    public static boolean isStudentRegistered(String username, String eventId) {
        List<String[]> registrations = CsvUtils.readCsv(Constants.REGISTRATIONS_CSV);
        for (String[] row : registrations) {
            if (row.length >= 6 && row[1].equals(username) && row[4].equals(eventId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets all registrations for a student
     */
    public static List<Registration> getRegistrationsByStudent(String username) {
        List<String[]> registrationRows = CsvUtils.readCsv(Constants.REGISTRATIONS_CSV);
        List<Registration> registrations = new ArrayList<>();

        for (String[] row : registrationRows) {
            if (row.length >= 6 && row[1].equals(username)) {
                Registration registration = Registration.fromCsvRow(row);
                if (registration != null) {
                    registrations.add(registration);
                }
            }
        }
        return registrations;
    }

    /**
     * Gets all registrations for an event
     */
    public static List<Registration> getRegistrationsByEvent(String eventId) {
        List<String[]> registrationRows = CsvUtils.readCsv(Constants.REGISTRATIONS_CSV);
        List<Registration> registrations = new ArrayList<>();

        for (String[] row : registrationRows) {
            if (row.length >= 6 && row[4].equals(eventId)) {
                Registration registration = Registration.fromCsvRow(row);
                if (registration != null) {
                    registrations.add(registration);
                }
            }
        }
        return registrations;
    }

    /**
     * Deletes all registrations for an event
     */
    public static boolean deleteRegistrationsByEventId(String eventId) {
        List<String[]> registrations = CsvUtils.readCsv(Constants.REGISTRATIONS_CSV);
        boolean removed = registrations.removeIf(row -> 
            row.length >= 6 && row[4].equals(eventId));

        if (removed) {
            return CsvUtils.writeCsv(Constants.REGISTRATIONS_CSV, registrations);
        }
        return true; // No registrations to remove is also success
    }
}