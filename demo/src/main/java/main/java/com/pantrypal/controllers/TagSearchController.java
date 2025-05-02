package main.java.com.pantrypal.controllers;

import main.java.com.pantrypal.models.Recipe;
import main.java.com.pantrypal.services.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.List;

/**
 * Handles /tagsearch?tags=#vegan #glutenfree and returns matching recipes.
 */
@Controller
@RequestMapping("/tagsearch")
public class TagSearchController {

    private final RecipeService recipeService;

    public TagSearchController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    /**
     * Handles /tagsearch?tags=%23vegan+%23dessert style queries.
     */
    @GetMapping
    public ModelAndView searchByTags(@RequestParam(name = "tags") String tags) throws SQLException {
        System.out.println("User is searching recipes by tags: " + tags);

        ModelAndView mv = new ModelAndView("recipe_list");

        try {
            List<Recipe> recipes = recipeService.searchRecipesByTags(tags);
            mv.addObject("recipes", recipes);

            if (recipes.isEmpty()) {
                mv.addObject("isNoContent", true);
            }

        } catch (Exception e) {
            mv.addObject("errorMessage", "An error occurred while searching for recipes.");
        }

        return mv;
    }
}
