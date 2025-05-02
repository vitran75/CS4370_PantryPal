package main.java.com.pantrypal.controllers;

import main.java.com.pantrypal.models.Recipe;
import main.java.com.pantrypal.services.RecipeService;
import main.java.com.pantrypal.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

/**
 * Handles the home page (/) and recipe creation.
 */
@Controller
@RequestMapping("/")
public class HomeController {

    private final RecipeService recipeService;
    private final UserService userService;

    public HomeController(RecipeService recipeService, UserService userService) {
        this.recipeService = recipeService;
        this.userService = userService;
    }

    /**
     * Displays the homepage with a list of recent recipes.
     */
    @GetMapping
    public ModelAndView homepage(@RequestParam(name = "error", required = false) String error) throws SQLException {
        ModelAndView mv = new ModelAndView("home_page");

        try {
            List<Recipe> recipes = recipeService.getAllRecipesByMostRecent();
            mv.addObject("recipes", recipes);

            if (recipes.isEmpty()) {
                mv.addObject("isNoContent", true);
            }
        } catch (Exception e) {
            mv.addObject("errorMessage", error != null ? error : "Something went wrong while loading recipes.");
        }

        return mv;
    }

    /**
     * Handles recipe submission form POST to /createrecipe.
     */
    @PostMapping("/createrecipe")
    public String createRecipe(@RequestParam("title") String title,
                               @RequestParam("ingredients") String ingredients,
                               @RequestParam("instructions") String instructions) throws SQLException {
        System.out.println("Submitting new recipe: " + title);

        boolean success = recipeService.createRecipe(
                userService.getLoggedInUser().getUserId(),
                title,
                ingredients,
                instructions
        );

        if (success) {
            return "redirect:/";
        }

        String errorMessage = URLEncoder.encode("Failed to submit recipe. Please try again.", StandardCharsets.UTF_8);
        return "redirect:/?error=" + errorMessage;
    }
}
