package com.eventmanagement.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Model class representing an Event Registration
 */
public class Registration {
    private String registrationId;
    private String username;
    private String email;
    private String rollNo;
    private String eventId;
    private String registrationTime;

    public Registration() {}

    public Registration(String username, String email, String rollNo, String eventId) {
        this.registrationId = UUID.randomUUID().toString();
        this.username = username;
        this.email = email;
        this.rollNo = rollNo;
        this.eventId = eventId;
        this.registrationTime = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

    public Registration(String registrationId, String username, String email, 
                       String rollNo, String eventId, String registrationTime) {
        this.registrationId = registrationId;
        this.username = username;
        this.email = email;
        this.rollNo = rollNo;
        this.eventId = eventId;
        this.registrationTime = registrationTime;
    }

    // Getters and Setters
    public String getRegistrationId() { return registrationId; }
    public void setRegistrationId(String registrationId) { this.registrationId = registrationId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRollNo() { return rollNo; }
    public void setRollNo(String rollNo) { this.rollNo = rollNo; }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getRegistrationTime() { return registrationTime; }
    public void setRegistrationTime(String registrationTime) { this.registrationTime = registrationTime; }

    /**
     * Converts registration object to CSV format
     */
    public String[] toCsvRow() {
        return new String[]{registrationId, username, email, rollNo, eventId, registrationTime};
    }

    /**
     * Creates Registration object from CSV row
     */
    public static Registration fromCsvRow(String[] csvRow) {
        if (csvRow.length >= 6) {
            return new Registration(csvRow[0], csvRow[1], csvRow[2], csvRow[3], csvRow[4], csvRow[5]);
        }
        return null;
    }

    @Override
    public String toString() {
        return String.format("Registration{id='%s', username='%s', eventId='%s', time='%s'}", 
                           registrationId, username, eventId, registrationTime);
    }
}