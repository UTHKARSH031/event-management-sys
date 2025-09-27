package com.eventmanagement.services;

import com.eventmanagement.models.Student;
import com.eventmanagement.utils.CsvUtils;
import com.eventmanagement.utils.PasswordUtils;
import com.eventmanagement.utils.ValidationUtils;
import com.eventmanagement.Constants;

import java.util.List;

/**
 * Service class for student-related operations
 */
public class StudentService {

    /**
     * Authenticates a student
     * @param username The username
     * @param password The plain text password
     * @return Student object if authentication successful, null otherwise
     */
    public static Student authenticate(String username, String password) {
        if (!ValidationUtils.isNotEmpty(username) || !ValidationUtils.isNotEmpty(password)) {
            return null;
        }

        List<String[]> students = CsvUtils.readCsv(Constants.STUDENTS_CSV);
        for (String[] row : students) {
            if (row.length >= 6 && row[0].equals(username)) {
                if (PasswordUtils.verifyPassword(password, row[1])) {
                    return Student.fromCsvRow(row);
                }
                break; // Username found but password incorrect
            }
        }
        return null;
    }

    /**
     * Registers a new student
     * @param student The student to register
     * @return true if registration successful, false otherwise
     */
    public static boolean register(Student student) {
        if (!validateStudent(student)) {
            return false;
        }

        // Check if username already exists
        if (usernameExists(student.getUsername())) {
            return false;
        }

        // Hash password before storing
        student.setHashedPassword(PasswordUtils.hashPassword(student.getHashedPassword()));

        return CsvUtils.appendToCsv(Constants.STUDENTS_CSV, student.toCsvRow());
    }

    /**
     * Checks if a username already exists
     */
    public static boolean usernameExists(String username) {
        List<String[]> students = CsvUtils.readCsv(Constants.STUDENTS_CSV);
        for (String[] row : students) {
            if (row.length > 0 && row[0].equals(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets student data by username
     */
    public static Student getStudentByUsername(String username) {
        List<String[]> students = CsvUtils.readCsv(Constants.STUDENTS_CSV);
        for (String[] row : students) {
            if (row.length >= 6 && row[0].equals(username)) {
                return Student.fromCsvRow(row);
            }
        }
        return null;
    }

    /**
     * Validates student data
     */
    private static boolean validateStudent(Student student) {
        return ValidationUtils.isValidUsername(student.getUsername()) &&
               ValidationUtils.isValidPassword(student.getHashedPassword()) &&
               ValidationUtils.isValidEmail(student.getEmail()) &&
               ValidationUtils.areAllFieldsValid(student.getRollNo(), 
                                               student.getBranch(), 
                                               student.getUniversity());
    }
}