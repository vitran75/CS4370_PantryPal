package main.java.com.pantrypal.controllers;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import main.java.com.pantrypal.models.Recipe;
import main.java.com.pantrypal.services.RecipeService;
import main.java.com.pantrypal.services.UserService;

/**
 * Handles /saved-recipes and shows all recipes the user has bookmarked.
 */
@Controller
@RequestMapping("/saved-recipes")
public class SavedRecipesController {

    private final RecipeService recipeService;
    private final UserService userService;

    public SavedRecipesController(RecipeService recipeService, UserService userService) {
        this.recipeService = recipeService;
        this.userService = userService;
    }

    /**
     * Loads and displays recipes bookmarked by the logged-in user.
     */
    @GetMapping
    public ModelAndView showSavedRecipes() throws SQLException {
        ModelAndView mv = new ModelAndView("recipe_list");

        try {
            String userId = userService.getLoggedInUser().getUserId();
            List<Recipe> savedRecipes = recipeService.getRecipesByUserId(userId);
            mv.addObject("recipes", savedRecipes);

            if (savedRecipes.isEmpty()) {
                mv.addObject("isNoContent", true);
            }

        } catch (Exception e) {
            mv.addObject("errorMessage", "Failed to load saved recipes.");
        }

        return mv;
    }
}
