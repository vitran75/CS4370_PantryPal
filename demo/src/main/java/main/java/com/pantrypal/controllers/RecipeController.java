package main.java.com.pantrypal.controllers;

import main.java.com.pantrypal.models.ExpandedRecipe;
import main.java.com.pantrypal.services.RecipeService;
import main.java.com.pantrypal.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

/**
 * Handles /recipe URL and sub-URLs (viewing, commenting, liking, bookmarking).
 */
@Controller
@RequestMapping("/recipe")
public class RecipeController {

    private final RecipeService recipeService;
    private final UserService userService;

    public RecipeController(RecipeService recipeService, UserService userService) {
        this.recipeService = recipeService;
        this.userService = userService;
    }

    /**
     * Displays a specific recipe and its comments.
     */
    @GetMapping("/{recipeId}")
    public ModelAndView viewRecipe(@PathVariable("recipeId") String recipeId,
                                   @RequestParam(name = "error", required = false) String error) {
        ModelAndView mv = new ModelAndView("recipe_page");

        try {
            var recipe = recipeService.getExpandedRecipeById(recipeId);
            mv.addObject("recipe", recipe);

            if (recipe.getComments().isEmpty()) {
                mv.addObject("isNoComments", true);
            }
        } catch (Exception e) {
            mv.addObject("errorMessage", error != null ? error : "Error loading recipe.");
        }

        return mv;
    }

    /**
     * Submits a comment on a recipe.
     */
    @PostMapping("/{recipeId}/comment")
    public String postComment(@PathVariable("recipeId") String recipeId,
                              @RequestParam(name = "comment") String commentText) throws SQLException {

        boolean success = recipeService.addCommentToRecipe(recipeId, commentText);

        if (success) {
            return "redirect:/recipe/" + recipeId;
        }

        String message = URLEncoder.encode("Failed to post comment. Please try again.", StandardCharsets.UTF_8);
        return "redirect:/recipe/" + recipeId + "?error=" + message;
    }

    /**
     * Handles like/unlike (heart) on a recipe.
     */
    @GetMapping("/{recipeId}/heart/{isAdd}")
    public String likeOrUnlikeRecipe(@PathVariable("recipeId") String recipeId,
                                     @PathVariable("isAdd") Boolean isAdd) throws SQLException {

        boolean success = isAdd
                ? recipeService.addHeart(recipeId)
                : recipeService.removeHeart(recipeId);

        if (success) {
            return "redirect:/recipe/" + recipeId;
        }

        String message = URLEncoder.encode("Failed to (un)like the recipe. Please try again.", StandardCharsets.UTF_8);
        return "redirect:/recipe/" + recipeId + "?error=" + message;
    }

    /**
     * Handles bookmarking/unbookmarking a recipe.
     */
    @GetMapping("/{recipeId}/bookmark/{isAdd}")
    public String bookmarkOrUnbookmarkRecipe(@PathVariable("recipeId") String recipeId,
                                             @PathVariable("isAdd") Boolean isAdd) throws SQLException {

        String userId = userService.getLoggedInUser().getUserId();
        boolean success = isAdd
                ? recipeService.setBookmark(userId, recipeId)
                : recipeService.removeBookmark(userId, recipeId);

        if (success) {
            return "redirect:/recipe/" + recipeId;
        }

        String message = URLEncoder.encode("Failed to (un)bookmark the recipe. Please try again.", StandardCharsets.UTF_8);
        return "redirect:/recipe/" + recipeId + "?error=" + message;
    }
}
