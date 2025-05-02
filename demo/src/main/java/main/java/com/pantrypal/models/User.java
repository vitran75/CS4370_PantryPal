package main.java.com.pantrypal.models;

/**
 * Represents a user of the cooking recipe sharing platform.
 */
public class User {

    /**
     * Unique identifier for the user.
     */
    private final String userId;

    /**
     * First name of the user.
     */
    private final String firstName;

    /**
     * Last name of the user.
     */
    private final String lastName;

    /**
     * Path to the user's profile picture (e.g., a cooking-themed avatar).
     */
    private final String profileImagePath;

    /**
     * Constructs a User with full details including a custom profile image path.
     *
     * @param userId           Unique identifier for the user
     * @param firstName        First name of the user
     * @param lastName         Last name of the user
     * @param profileImagePath Path to the user's profile image
     */
    public User(String userId, String firstName, String lastName, String profileImagePath) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImagePath = profileImagePath;
    }

    /**
     * Constructs a User using a generated profile image path based on user ID.
     *
     * @param userId    Unique identifier for the user
     * @param firstName First name of the user
     * @param lastName  Last name of the user
     */
    public User(String userId, String firstName, String lastName) {
        this(userId, firstName, lastName, getDefaultAvatarPath(userId));
    }

    /**
     * Generates a default cooking-themed avatar path based on the user ID.
     */
    private static String getDefaultAvatarPath(String userId) {
        int fileNo = Math.abs(userId.hashCode() % 20) + 1;
        String avatarFileName = String.format("chef_avatar_%d.png", fileNo);
        return "/avatars/" + avatarFileName;
    }

    /**
     * Returns the user ID.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Returns the first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns the last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Returns the path to the profile image.
     */
    public String getProfileImagePath() {
        return profileImagePath;
    }
}
