package main.java.com.pantrypal.controllers;

import main.java.com.pantrypal.models.Recipe;
import main.java.com.pantrypal.services.PeopleService;
import main.java.com.pantrypal.services.RecipeService;
import main.java.com.pantrypal.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.List;

/**
 * Handles /profile URL and sub-URLs for viewing user profiles and their shared recipes.
 */
@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;
    private final PeopleService peopleService;
    private final RecipeService recipeService;

    @Autowired
    public ProfileController(UserService userService, PeopleService peopleService, RecipeService recipeService) {
        this.userService = userService;
        this.peopleService = peopleService;
        this.recipeService = recipeService;
    }

    /**
     * Displays the profile and recipes of the currently logged-in user.
     */
    @GetMapping
    public ModelAndView profileOfLoggedInUser() throws SQLException {
        System.out.println("Viewing profile of logged-in user.");
        return profileOfSpecificUser(userService.getLoggedInUser().getUserId());
    }

    /**
     * Displays the profile and recipes of a specific user.
     */
    @GetMapping("/{userId}")
    public ModelAndView profileOfSpecificUser(@PathVariable("userId") String userId) throws SQLException {
        System.out.println("Viewing profile of user: " + userId);

        ModelAndView mv = new ModelAndView("recipe_list");

        try {
            List<Recipe> recipes = recipeService.getRecipesByUserId(userId);
            mv.addObject("recipes", recipes);

            if (recipes.isEmpty()) {
                mv.addObject("isNoContent", true);
            }
        } catch (Exception e) {
            mv.addObject("errorMessage", "An error occurred while loading the profile.");
        }

        return mv;
    }
}
