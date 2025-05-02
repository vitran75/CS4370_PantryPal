package main.java.com.pantrypal.utility;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import main.java.com.pantrypal.models.Comment;
import main.java.com.pantrypal.models.ExpandedRecipe;
import main.java.com.pantrypal.models.Recipe;
import main.java.com.pantrypal.models.User;

public class Utility {

    /**
     * Creates a list of sample users for the cooking app.
     */
    public static List<User> createSampleUserList() {
        List<User> users = new ArrayList<>();
        users.add(new User("1", "Sophie", "Nguyen"));
        users.add(new User("2", "Liam", "Chen"));
        users.add(new User("3", "Ava", "Patel"));
        users.add(new User("4", "Noah", "Kim"));
        return users;
    }

    /**
     * Creates sample recipes without comments.
     */
    public static List<Recipe> createSampleRecipesList() {
        User user1 = new User("1", "Sophie", "Nguyen");
        User user2 = new User("2", "Liam", "Chen");
        User user3 = new User("3", "Ava", "Patel");

        List<Recipe> recipes = new ArrayList<>();
        recipes.add(new Recipe("1", "Vietnamese Pho",
                "Beef bones, rice noodles, herbs, fish sauce",
                "Boil bones, simmer broth for 6 hours, add noodles and garnish.",
                convertDateFormat("2024-03-07 22:54:00"), user1));
        recipes.add(new Recipe("2", "Spaghetti Carbonara",
                "Spaghetti, eggs, pancetta, parmesan cheese",
                "Cook pasta, fry pancetta, mix with eggs and cheese off-heat.",
                convertDateFormat("2024-03-08 11:00:00"), user2));
        recipes.add(new Recipe("3", "Chicken Tikka Masala",
                "Chicken, yogurt, tomato sauce, spices",
                "Marinate chicken, grill, then simmer in sauce.",
                convertDateFormat("2024-03-09 09:30:00"), user3));

        return recipes;
    }

    /**
     * Creates a sample expanded recipe with associated comments.
     */
    public static ExpandedRecipe createSampleExpandedRecipeWithComments() {
        User recipeAuthor = new User("1", "Sophie", "Nguyen");
        User commenter1 = new User("2", "Liam", "Chen");
        User commenter2 = new User("3", "Ava", "Patel");

        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment("1", "Tried this last night and it was amazing!",
                "Mar 07, 2024, 10:54 PM", commenter1));
        comments.add(new Comment("2", "Can I substitute beef with chicken?",
                "Mar 08, 2024, 11:00 AM", commenter2));

        return new ExpandedRecipe(
                "10",
                "Classic Bánh Mì Sandwich",
                "Baguette, pork, pickled veggies, cilantro, chili",
                "Grill pork, assemble sandwich with toppings.",
                convertDateFormat("2024-03-10 08:15:00"),
                recipeAuthor,
                comments
        );
    }

    /**
     * Returns the current timestamp in standard format.
     */
    public static String getCurrentDateAndTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return Timestamp.valueOf(currentDateTime).toString();
    }

    /**
     * Converts a timestamp string into a readable format.
     */
    public static String convertDateFormat(String date) {
        if (date == null) {
            return ": No recipes yet";
        }
        Timestamp timestamp = Timestamp.valueOf(date);
        LocalDateTime dateTime = timestamp.toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy, hh:mm a");

        return dateTime.format(formatter);
    }
}
