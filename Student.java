package com.eventmanagement.models;

/**
 * Model class representing a Student
 */
public class Student {
    private String username;
    private String hashedPassword;
    private String email;
    private String rollNo;
    private String branch;
    private String university;

    public Student() {}

    public Student(String username, String hashedPassword, String email, 
                  String rollNo, String branch, String university) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.email = email;
        this.rollNo = rollNo;
        this.branch = branch;
        this.university = university;
    }

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getHashedPassword() { return hashedPassword; }
    public void setHashedPassword(String hashedPassword) { this.hashedPassword = hashedPassword; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRollNo() { return rollNo; }
    public void setRollNo(String rollNo) { this.rollNo = rollNo; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public String getUniversity() { return university; }
    public void setUniversity(String university) { this.university = university; }

    /**
     * Converts student object to CSV format
     */
    public String[] toCsvRow() {
        return new String[]{username, hashedPassword, email, rollNo, branch, university};
    }

    /**
     * Creates Student object from CSV row
     */
    public static Student fromCsvRow(String[] csvRow) {
        if (csvRow.length >= 6) {
            return new Student(csvRow[0], csvRow[1], csvRow[2], csvRow[3], csvRow[4], csvRow[5]);
        }
        return null;
    }

    @Override
    public String toString() {
        return String.format("Student{username='%s', email='%s', rollNo='%s', branch='%s', university='%s'}", 
                           username, email, rollNo, branch, university);
    }
}