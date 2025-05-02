package main.java.com.pantrypal.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import main.java.com.pantrypal.models.User;
import main.java.com.pantrypal.utility.Utility;

/**
 * Handles user management for the Cooking Recipe Sharing App.
 * Provides methods to register, authenticate, and manage session-specific users.
 */
@Service
@SessionScope
public class UserService {

    private final DataSource dataSource;
    private final BCryptPasswordEncoder passwordEncoder;
    private User loggedInUser = null;

    @Autowired
    public UserService(DataSource dataSource) {
        this.dataSource = dataSource;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Authenticates a user with given username and password.
     * Stores the user object in session if successful.
     */
    public boolean authenticate(String username, String password) throws SQLException {
        final String sql = "SELECT * FROM user WHERE username = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    if (passwordEncoder.matches(password, storedHash)) {
                        String userId = rs.getString("userId");
                        String firstName = rs.getString("firstName");
                        String lastName = rs.getString("lastName");

                        loggedInUser = new User(userId, firstName, lastName);

                        // Optional: Update last active timestamp
                        /*try (PreparedStatement updateStmt = conn.prepareStatement(
                            "UPDATE user SET lastActiveDate = ? WHERE userId = ?")) {
                            updateStmt.setString(1, Utility.getCurrentDateAndTime());
                            updateStmt.setString(2, userId);
                            updateStmt.executeUpdate();
                        }*/

                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Logs out the current user by clearing session scope reference.
     */
    public void unAuthenticate() {
        loggedInUser = null;
    }

    /**
     * Returns true if a user is currently logged in.
     */
    public boolean isAuthenticated() {
        return loggedInUser != null;
    }

    /**
     * Gets the currently logged-in user.
     */
    public User getLoggedInUser() {
        return loggedInUser;
    }

    /**
     * Registers a new user.
     * Returns true if successful.
     * Throws SQLException if username already exists.
     */
    public boolean registerUser(String username, String password, String firstName, String lastName)
            throws SQLException {

        final String sql = "INSERT INTO user (username, password, firstName, lastName, mostRecentPostDate) " +
                           "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, passwordEncoder.encode(password));
            stmt.setString(3, firstName);
            stmt.setString(4, lastName);
            stmt.setString(5, null); // Placeholder for future post tracking

            int affected = stmt.executeUpdate();
            return affected > 0;
        }
    }
}
