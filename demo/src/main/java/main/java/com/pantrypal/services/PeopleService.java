package main.java.com.pantrypal.services;

import main.java.com.pantrypal.models.FollowableUser;
import main.java.com.pantrypal.models.User;
import main.java.com.pantrypal.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service to manage user relationships such as follow/unfollow in the PantryPal app.
 */
@Service
public class PeopleService {

    private final DataSource dataSource;

    @Autowired
    public PeopleService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Fetch a user by their ID.
     */
    public User getUserById(String userId) throws SQLException {
        final String sql = "SELECT * FROM user WHERE userId = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            userId,
                            rs.getString("firstName"),
                            rs.getString("lastName")
                    );
                }
            }
        }
        return null;
    }

    /**
     * Return all users except the current user, including follow status and last activity.
     */
    public List<FollowableUser> getFollowableUsers(String currentUserId) throws SQLException {
        final String sql = "SELECT * FROM user WHERE userId <> ?";
        List<FollowableUser> followableUsers = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, currentUserId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String userId = rs.getString("userId");
                    String firstName = rs.getString("firstName");
                    String lastName = rs.getString("lastName");
                    boolean isFollowed = isFollowing(currentUserId, userId);
                    String lastActiveDate = Utility.convertDateFormat(rs.getString("mostRecentPostDate"));

                    followableUsers.add(new FollowableUser(userId, firstName, lastName, isFollowed, lastActiveDate));
                }
            }
        }
        return followableUsers;
    }

    /**
     * Check if the current user is following another user.
     */
    private boolean isFollowing(String followerId, String followeeId) throws SQLException {
        final String sql = "SELECT 1 FROM follow WHERE followerUserId = ? AND followeeUserId = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, followerId);
            stmt.setString(2, followeeId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Follow a user.
     */
    public boolean setFollow(String followerId, String followeeId) throws SQLException {
        final String sql = "INSERT INTO follow (followerUserId, followeeUserId) VALUES (?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, followerId);
            stmt.setString(2, followeeId);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Unfollow a user.
     */
    public boolean setUnfollow(String followerId, String followeeId) throws SQLException {
        final String sql = "DELETE FROM follow WHERE followerUserId = ? AND followeeUserId = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, followerId);
            stmt.setString(2, followeeId);
            return stmt.executeUpdate() > 0;
        }
    }
}
