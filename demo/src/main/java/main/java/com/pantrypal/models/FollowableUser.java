package main.java.com.pantrypal.models;

/**
 * Represents a user in the cooking app who can be followed.
 * Extends the base User class by adding follow status and last recipe post date.
 */
public class FollowableUser extends User {

    /**
     * Whether the current logged-in user follows this user.
     */
    private final boolean isFollowed;

    /**
     * The date this user last posted a recipe.
     */
    private final String lastActiveDate;

    /**
     * Constructs a FollowableUser with a custom profile image.
     */
    public FollowableUser(String userId, String firstName, String lastName,
                          String profileImageName, boolean isFollowed, String lastActiveDate) {
        super(userId, firstName, lastName, profileImageName);
        this.isFollowed = isFollowed;
        this.lastActiveDate = (lastActiveDate == null) ? ": No recipes yet" : lastActiveDate;
    }

    /**
     * Constructs a FollowableUser with default-generated avatar.
     */
    public FollowableUser(String userId, String firstName, String lastName,
                          boolean isFollowed, String lastActiveDate) {
        super(userId, firstName, lastName);
        this.isFollowed = isFollowed;
        this.lastActiveDate = (lastActiveDate == null) ? ": No recipes yet" : lastActiveDate;
    }

    /**
     * Returns whether the current session user follows this user.
     */
    public boolean isFollowed() {
        return isFollowed;
    }

    /**
     * Returns the last date this user shared a recipe.
     */
    public String getLastActiveDate() {
        return lastActiveDate;
    }
}
