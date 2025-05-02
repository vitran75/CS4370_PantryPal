package main.java.com.pantrypal.models;

/**
 * Represents a recipe in the PantryPal app,
 * including metadata like favorites, comments, and bookmarks.
 */
public class Recipe extends BasicRecipe {

    /**
     * The number of hearts (likes) this recipe has received.
     */
    private final int heartsCount;

    /**
     * The number of comments this recipe has received.
     */
    private final int commentsCount;

    /**
     * Whether the recipe is liked by the current user.
     */
    private final boolean isHearted;

    /**
     * Whether the recipe is bookmarked by the current user.
     */
    private final boolean isBookmarked;

    /**
     * Whether to display comments or not (optional view logic).
     */
    protected boolean isShowComments;

    /**
     * Title of the recipe (extra field beyond BasicPost).
     */
    private final String title;

    /**
     * Ingredient list for the recipe.
     */
    private final String ingredients;

    /**
     * Constructs a Recipe with all metadata and content.
     *
     * @param recipeId       Unique ID of the recipe
     * @param title          Title of the recipe
     * @param ingredients    List of ingredients (string)
     * @param instructions   Cooking instructions
     * @param postDate       Date of submission
     * @param user           The user who posted the recipe
     * @param heartsCount    Number of likes
     * @param commentsCount  Number of comments
     * @param isHearted      Whether current user liked it
     * @param isBookmarked   Whether current user bookmarked it
     */
    public Recipe(String recipeId, String title, String ingredients, String instructions, String postDate,
                  User user, int heartsCount, int commentsCount, boolean isHearted, boolean isBookmarked) {
        super(recipeId, instructions, postDate, user);
        this.title = title;
        this.ingredients = ingredients;
        this.heartsCount = heartsCount;
        this.commentsCount = commentsCount;
        this.isHearted = isHearted;
        this.isBookmarked = isBookmarked;
        this.isShowComments = false;
    }

    // Basic version for simpler constructors
    public Recipe(String recipeId, String title, String ingredients, String instructions, String postDate,
                  User user) {
        this(recipeId, title, ingredients, instructions, postDate, user, 0, 0, false, false);
    }

    public String getRecipeId() {
        return getRecipeId();
    }

    public String getTitle() {
        return title;
    }

    public String getIngredients() {
        return ingredients;
    }

    public int getHeartsCount() {
        return heartsCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public boolean getHearted() {
        return isHearted;
    }

    public boolean isBookmarked() {
        return isBookmarked;
    }

    public boolean isShowComments() {
        return isShowComments;
    }

    public void setShowComments(boolean showComments) {
        this.isShowComments = showComments;
    }

    public String getInstructions() {
        return getContent(); 
    }
}
