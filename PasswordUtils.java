package com.eventmanagement.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.nio.charset.StandardCharsets;

/**
 * Utility class for password hashing and verification
 */
public final class PasswordUtils {
    private static final String ALGORITHM = "SHA-256";
    private static final String SALT = "EventManagement2025"; // In production, use unique salt per password

    /**
     * Hashes a password using SHA-256 with salt
     * @param password The plain text password
     * @return The hashed password
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            String saltedPassword = password + SALT;
            byte[] hash = digest.digest(saltedPassword.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            // Fallback to simple hash for demo purposes
            return Integer.toHexString((password + SALT).hashCode());
        }
    }

    /**
     * Verifies a password against its hash
     * @param password The plain text password to verify
     * @param hash The stored hash to verify against
     * @return true if password matches, false otherwise
     */
    public static boolean verifyPassword(String password, String hash) {
        return hashPassword(password).equals(hash);
    }

    /**
     * Converts byte array to hexadecimal string
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    private PasswordUtils() {
        // Prevent instantiation
    }
}