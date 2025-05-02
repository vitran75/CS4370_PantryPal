package main.java.com.pantrypal.models;

/**
 * Represents the basic structure of a recipe or comment.
 * Serves as a common superclass for Recipe and Comment.
 */
public class BasicRecipe {

    /**
     * Unique identifier for the recipe or comment.
     */
    private final String recipeId;

    /**
     * The main content: either instructions (for recipes) or comment text.
     */
    private final String content;

    /**
     * The date when the recipe or comment was posted.
     */
    private final String postDate;

    /**
     * The user who created the recipe or comment.
     */
    private final User user;

    /**
     * Constructs a BasicRecipe with all base fields.
     *
     * @param recipeId   Unique identifier
     * @param content    Instructions or comment body
     * @param postDate   Date of creation
     * @param user       Author of the content
     */
    public BasicRecipe(String recipeId, String content, String postDate, User user) {
        this.recipeId = recipeId;
        this.content = content;
        this.postDate = postDate == null ? ": No date" : postDate;
        this.user = user;
    }

    /**
     * Returns the recipe or comment ID.
     */
    public String getRecipeId() {
        return recipeId;
    }

    /**
     * Returns the instructions or comment content.
     */
    public String getContent() {
        return content;
    }

    /**
     * Returns the post date.
     */
    public String getPostDate() {
        return postDate;
    }

    /**
     * Returns the user who created the content.
     */
    public User getUser() {
        return user;
    }
}
