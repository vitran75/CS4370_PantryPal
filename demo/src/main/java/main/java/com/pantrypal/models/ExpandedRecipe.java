package main.java.com.pantrypal.models;

import java.util.List;

/**
 * Represents a recipe in expanded form, including comments.
 */
public class ExpandedRecipe extends Recipe {

    /**
     * List of comments associated with this recipe.
     */
    private final List<Comment> comments;

    /**
     * Constructs an ExpandedRecipe that includes comments.
     *
     * @param recipeId     the unique identifier of the recipe
     * @param title        the title of the recipe
     * @param ingredients  the ingredients list
     * @param instructions the cooking instructions
     * @param postDate     the date the recipe was posted
     * @param user         the user who posted the recipe
     * @param comments     the list of comments on this recipe
     */
    public ExpandedRecipe(String recipeId, String title, String ingredients, String instructions, String postDate, User user, List<Comment> comments) {
        super(recipeId, title, ingredients, instructions, postDate, user);
        this.comments = comments;
    }

    /**
     * Returns an unmodifiable list of comments to prevent external modification.
     */
    public List<Comment> getComments() {
        return List.copyOf(comments);
    }
}
