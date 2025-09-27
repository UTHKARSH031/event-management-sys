package com.eventmanagement.models;

import java.util.UUID;

/**
 * Model class representing an Event
 */
public class Event {
    private String eventId;
    private String eventName;
    private String eventDate;
    private String eventTime;
    private String eventLocation;

    public Event() {}

    public Event(String eventName, String eventDate, String eventTime, String eventLocation) {
        this.eventId = UUID.randomUUID().toString();
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventLocation = eventLocation;
    }

    public Event(String eventId, String eventName, String eventDate, String eventTime, String eventLocation) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventLocation = eventLocation;
    }

    // Getters and Setters
    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }

    public String getEventDate() { return eventDate; }
    public void setEventDate(String eventDate) { this.eventDate = eventDate; }

    public String getEventTime() { return eventTime; }
    public void setEventTime(String eventTime) { this.eventTime = eventTime; }

    public String getEventLocation() { return eventLocation; }
    public void setEventLocation(String eventLocation) { this.eventLocation = eventLocation; }

    /**
     * Converts event object to CSV format
     */
    public String[] toCsvRow() {
        return new String[]{eventId, eventName, eventDate, eventTime, eventLocation};
    }

    /**
     * Creates Event object from CSV row
     */
    public static Event fromCsvRow(String[] csvRow) {
        if (csvRow.length >= 5) {
            return new Event(csvRow[0], csvRow[1], csvRow[2], csvRow[3], csvRow[4]);
        }
        return null;
    }

    @Override
    public String toString() {
        return String.format("Event{id='%s', name='%s', date='%s', time='%s', location='%s'}", 
                           eventId, eventName, eventDate, eventTime, eventLocation);
    }
}