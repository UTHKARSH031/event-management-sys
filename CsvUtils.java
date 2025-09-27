package com.eventmanagement.utils;

import java.io.*;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Thread-safe utility class for CSV file operations
 */
public final class CsvUtils {
    private static final Logger logger = Logger.getLogger(CsvUtils.class.getName());
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * Reads all data from a CSV file
     * @param filePath Path to the CSV file
     * @return List of string arrays representing rows
     */
    public static List<String[]> readCsv(String filePath) {
        lock.readLock().lock();
        try {
            List<String[]> results = new ArrayList<>();
            File file = new File(filePath);

            if (!file.exists()) {
                logger.info("File does not exist: " + filePath);
                return results;
            }

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        results.add(line.split(",", -1)); // -1 to include empty strings
                    }
                }
            }
            return results;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading CSV file: " + filePath, e);
            return new ArrayList<>();
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Appends a row to a CSV file
     * @param filePath Path to the CSV file
     * @param values Array of values to append
     * @return true if successful, false otherwise
     */
    public static boolean appendToCsv(String filePath, String... values) {
        lock.writeLock().lock();
        try {
            // Escape commas in values
            String[] escapedValues = new String[values.length];
            for (int i = 0; i < values.length; i++) {
                escapedValues[i] = values[i].replace(",", "\,");
            }

            try (FileWriter fw = new FileWriter(filePath, true)) {
                fw.write(String.join(",", escapedValues) + "\n");
                return true;
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error writing to CSV file: " + filePath, e);
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Updates a row in CSV file based on a condition
     * @param filePath Path to the CSV file
     * @param columnIndex Index of column to match
     * @param searchValue Value to search for
     * @param newValues New values for the row
     * @return true if successful, false otherwise
     */
    public static boolean updateCsvRow(String filePath, int columnIndex, String searchValue, String... newValues) {
        lock.writeLock().lock();
        try {
            List<String[]> rows = readCsv(filePath);
            boolean updated = false;

            for (int i = 0; i < rows.size(); i++) {
                if (rows.get(i).length > columnIndex && rows.get(i)[columnIndex].equals(searchValue)) {
                    rows.set(i, newValues);
                    updated = true;
                    break;
                }
            }

            if (updated) {
                return writeCsv(filePath, rows);
            }
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Writes all data to a CSV file (overwrites existing content)
     */
    private static boolean writeCsv(String filePath, List<String[]> rows) {
        try (FileWriter fw = new FileWriter(filePath, false)) {
            for (String[] row : rows) {
                fw.write(String.join(",", row) + "\n");
            }
            return true;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error writing CSV file: " + filePath, e);
            return false;
        }
    }

    /**
     * Searches for rows matching a condition
     * @param filePath Path to the CSV file
     * @param columnIndex Index of column to search
     * @param searchValue Value to search for
     * @return List of matching rows
     */
    public static List<String[]> searchCsv(String filePath, int columnIndex, String searchValue) {
        List<String[]> allRows = readCsv(filePath);
        List<String[]> matchingRows = new ArrayList<>();

        for (String[] row : allRows) {
            if (row.length > columnIndex && row[columnIndex].equals(searchValue)) {
                matchingRows.add(row);
            }
        }
        return matchingRows;
    }


    /**
     * Helper method needed by service classes - writes CSV data (overwrites file)
     */
    public static boolean writeCsv(String filePath, java.util.List<String[]> rows) {
        lock.writeLock().lock();
        try (FileWriter fw = new FileWriter(filePath, false)) {
            for (String[] row : rows) {
                // Escape commas in values
                String[] escapedValues = new String[row.length];
                for (int i = 0; i < row.length; i++) {
                    escapedValues[i] = row[i].replace(",", "\\,");
                }
                fw.write(String.join(",", escapedValues) + "\n");
            }
            return true;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error writing CSV file: " + filePath, e);
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    private CsvUtils() {
        // Prevent instantiation
    }
}