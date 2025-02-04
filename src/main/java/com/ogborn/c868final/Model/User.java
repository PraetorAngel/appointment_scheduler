package com.ogborn.c868final.Model;

/**
 * Represents a user with a unique ID, username, and password.
 * Provides methods for accessing and modifying user details.
 * Overrides {@code toString} for meaningful string representation of the user.
 */
public class User {
    //<editor-fold desc="data members">
    /**
     * unique identifier for the user
     */
    private int userId;

    /**
     * username for authentication
     */
    private String username;

    /**
     * password for authentication
     */
    private String password;
    //</editor-fold>

    /**
     * Constructs a new User with the specified ID, username, and password.
     *
     * @param userId   The unique identifier of the user.
     * @param username The username of the user.
     * @param password The password of the user.
     */
    public User(int userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    /**
     * Gets the user's unique identifier.
     *
     * @return The user ID.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Gets the username of the user.
     *
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the password of the user.
     *
     * @return The password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's unique identifier.
     *
     * @param userId The new user ID.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Sets the username of the user.
     *
     * @param username The new username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the password of the user.
     *
     * @param password The new password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the username as the string representation of the user.
     *
     * @return The username.
     */
    @Override
    public String toString() {
        return username;
    }
}
