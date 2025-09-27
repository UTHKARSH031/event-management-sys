package com.eventmanagement.services;

import com.eventmanagement.models.Event;
import com.eventmanagement.models.Registration;
import com.eventmanagement.utils.CsvUtils;
import com.eventmanagement.utils.ValidationUtils;
import com.eventmanagement.Constants;

import java.util.List;
import java.util.ArrayList;

/**
 * Service class for event-related operations
 */
public class EventService {

    /**
     * Creates a new event
     * @param event The event to create
     * @return true if creation successful, false otherwise
     */
    public static boolean createEvent(Event event) {
        if (!validateEvent(event)) {
            return false;
        }

        return CsvUtils.appendToCsv(Constants.EVENTS_CSV, event.toCsvRow());
    }

    /**
     * Gets all events
     * @return List of all events
     */
    public static List<Event> getAllEvents() {
        List<String[]> eventRows = CsvUtils.readCsv(Constants.EVENTS_CSV);
        List<Event> events = new ArrayList<>();

        for (String[] row : eventRows) {
            Event event = Event.fromCsvRow(row);
            if (event != null) {
                events.add(event);
            }
        }
        return events;
    }

    /**
     * Gets event by ID
     */
    public static Event getEventById(String eventId) {
        List<String[]> events = CsvUtils.readCsv(Constants.EVENTS_CSV);
        for (String[] row : events) {
            if (row.length >= 5 && row[0].equals(eventId)) {
                return Event.fromCsvRow(row);
            }
        }
        return null;
    }

    /**
     * Gets event name by ID
     */
    public static String getEventNameById(String eventId) {
        Event event = getEventById(eventId);
        return event != null ? event.getEventName() : "Unknown Event";
    }

    /**
     * Deletes an event and its registrations
     */
    public static boolean deleteEvent(String eventId) {
        // First, delete all registrations for this event
        RegistrationService.deleteRegistrationsByEventId(eventId);

        // Then delete the event itself
        List<String[]> events = CsvUtils.readCsv(Constants.EVENTS_CSV);
        events.removeIf(row -> row.length > 0 && row[0].equals(eventId));

        return CsvUtils.writeCsv(Constants.EVENTS_CSV, events);
    }

    /**
     * Validates event data
     */
    private static boolean validateEvent(Event event) {
        return ValidationUtils.areAllFieldsValid(event.getEventName(), 
                                               event.getEventDate(), 
                                               event.getEventTime(), 
                                               event.getEventLocation()) &&
               ValidationUtils.isValidDate(event.getEventDate()) &&
               ValidationUtils.isValidTime(event.getEventTime()) &&
               ValidationUtils.isDateNotPast(event.getEventDate());
    }
}