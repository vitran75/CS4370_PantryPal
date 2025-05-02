package main.java.com.pantrypal.services;

import main.java.com.pantrypal.models.Comment;
import main.java.com.pantrypal.models.ExpandedRecipe;
import main.java.com.pantrypal.models.Recipe;
import main.java.com.pantrypal.models.User;
import main.java.com.pantrypal.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecipeService {

    private final DataSource dataSource;
    private final PeopleService peopleService;
    private final UserService userService;

    @Autowired
    public RecipeService(DataSource dataSource, PeopleService peopleService, UserService userService) {
        this.dataSource = dataSource;
        this.peopleService = peopleService;
        this.userService = userService;
    }

    public boolean createRecipe(String userId, String title, String ingredients, String instructions) throws SQLException {
        String recipeDate = Utility.getCurrentDateAndTime();
        final String insertSQL = "INSERT INTO recipe (userId, title, ingredients, instructions, postDate, heartCount, commentCount) VALUES (?, ?, ?, ?, ?, 0, 0)";
        final String updateUserSQL = "UPDATE user SET mostRecentPostDate = ? WHERE userId = ?";

        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmt = conn.prepareStatement(insertSQL)) {
                stmt.setString(1, userId);
                stmt.setString(2, title);
                stmt.setString(3, ingredients);
                stmt.setString(4, instructions);
                stmt.setString(5, recipeDate);
                stmt.executeUpdate();
            }
            try (PreparedStatement updateStmt = conn.prepareStatement(updateUserSQL)) {
                updateStmt.setString(1, recipeDate);
                updateStmt.setString(2, userId);
                updateStmt.executeUpdate();
            }
            conn.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Recipe> getAllRecipesByMostRecent() throws SQLException {
        final String sql = "SELECT * FROM recipe ORDER BY postDate DESC";
        List<Recipe> recipes = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                User user = peopleService.getUserById(rs.getString("userId"));
                Recipe recipe = new Recipe(
                    rs.getString("recipeId"),
                    rs.getString("title"),
                    rs.getString("ingredients"),
                    rs.getString("instructions"),
                    Utility.convertDateFormat(rs.getString("postDate")),
                    user
                );
                recipes.add(recipe);
            }
        }
        return recipes;
    }

    public List<Recipe> getRecipesByUserId(String userId) throws SQLException {
        final String sql = "SELECT * FROM recipe WHERE userId = ? ORDER BY postDate DESC";
        List<Recipe> recipes = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    User user = peopleService.getUserById(userId);
                    Recipe recipe = new Recipe(
                        rs.getString("recipeId"),
                        rs.getString("title"),
                        rs.getString("ingredients"),
                        rs.getString("instructions"),
                        Utility.convertDateFormat(rs.getString("postDate")),
                        user
                    );
                    recipes.add(recipe);
                }
            }
        }
        return recipes;
    }

    public boolean addCommentToRecipe(String recipeId, String content) throws SQLException {
        final String sql = "INSERT INTO comment (commentId, recipeId, userId, content, postDate) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";
        String commentId = java.util.UUID.randomUUID().toString();
        String userId = userService.getLoggedInUser().getUserId();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, commentId);
            stmt.setString(2, recipeId);
            stmt.setString(3, userId);
            stmt.setString(4, content);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean setBookmark(String userId, String recipeId) throws SQLException {
        final String sql = "INSERT INTO bookmark (userId, recipeId) VALUES (?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            stmt.setString(2, recipeId);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean removeBookmark(String userId, String recipeId) throws SQLException {
        final String sql = "DELETE FROM bookmark WHERE userId = ? AND recipeId = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            stmt.setString(2, recipeId);
            return stmt.executeUpdate() > 0;
        }
    }

    public ExpandedRecipe getExpandedRecipeById(String recipeId) throws SQLException {
        final String recipeSql = "SELECT * FROM recipe WHERE recipeId = ?";
        final String commentSql = "SELECT * FROM comment WHERE recipeId = ? ORDER BY postDate DESC";

        Recipe baseRecipe = null;
        List<Comment> comments = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement recipeStmt = conn.prepareStatement(recipeSql);
             PreparedStatement commentStmt = conn.prepareStatement(commentSql)) {

            recipeStmt.setString(1, recipeId);
            try (ResultSet rs = recipeStmt.executeQuery()) {
                if (rs.next()) {
                    User user = peopleService.getUserById(rs.getString("userId"));
                    baseRecipe = new Recipe(
                        rs.getString("recipeId"),
                        rs.getString("title"),
                        rs.getString("ingredients"),
                        rs.getString("instructions"),
                        Utility.convertDateFormat(rs.getString("postDate")),
                        user
                    );
                }
            }

            commentStmt.setString(1, recipeId);
            try (ResultSet rs = commentStmt.executeQuery()) {
                while (rs.next()) {
                    User user = peopleService.getUserById(rs.getString("userId"));
                    Comment comment = new Comment(
                        rs.getString("commentId"),
                        rs.getString("content"),
                        Utility.convertDateFormat(rs.getString("postDate")),
                        user
                    );
                    comments.add(comment);
                }
            }
        }

        return new ExpandedRecipe(
            baseRecipe.getRecipeId(),
            baseRecipe.getTitle(),
            baseRecipe.getIngredients(),
            baseRecipe.getInstructions(),
            baseRecipe.getPostDate(),
            baseRecipe.getUser(),
            comments
        );
    }

    public boolean addHeart(String recipeId) throws SQLException {
        final String sql = "UPDATE recipe SET heartCount = heartCount + 1 WHERE recipeId = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, recipeId);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean removeHeart(String recipeId) throws SQLException {
        final String sql = "UPDATE recipe SET heartCount = heartCount - 1 WHERE recipeId = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, recipeId);
            return stmt.executeUpdate() > 0;
        }
    }

    public List<Recipe> searchRecipesByTags(String tag) throws SQLException {
        final String sql = "SELECT * FROM recipe WHERE instructions LIKE ? OR title LIKE ? OR ingredients LIKE ? ORDER BY postDate DESC";
        List<Recipe> recipes = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String searchPattern = "%" + tag + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    User user = peopleService.getUserById(rs.getString("userId"));
                    Recipe recipe = new Recipe(
                        rs.getString("recipeId"),
                        rs.getString("title"),
                        rs.getString("ingredients"),
                        rs.getString("instructions"),
                        Utility.convertDateFormat(rs.getString("postDate")),
                        user
                    );
                    recipes.add(recipe);
                }
            }
        }
        return recipes;
    }
}
