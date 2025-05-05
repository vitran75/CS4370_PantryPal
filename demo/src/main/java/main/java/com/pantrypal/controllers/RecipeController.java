package main.java.com.pantrypal.controllers;

import main.java.com.pantrypal.services.OpenAiService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Handles AI-powered recipe generation from user's available ingredients.
 */
@Controller
@RequestMapping("/ai")
public class RecipeController {

    private final OpenAiService openAIService;

    public RecipeController(OpenAiService openAIService) {
        this.openAIService = openAIService;
    }

    /**
     * Display the AI recipe input form.
     */
    @GetMapping("/generate-recipe")
    public ModelAndView showInputForm(@RequestParam(name = "error", required = false) String error) {
        ModelAndView mv = new ModelAndView("ai_recipe_input");
        if (error != null) {
            mv.addObject("errorMessage", error);
        }
        return mv;
    }

    /**
     * Handle AI recipe generation based on user-input ingredients.
     */
    @PostMapping("/generate-recipe")
    public ModelAndView generateRecipe(@RequestParam("ingredients") String ingredients) {
        ModelAndView mv = new ModelAndView("ai_recipe_result");

        try {
            String recipe = openAIService.generateRecipe(ingredients);
            mv.addObject("generatedRecipe", recipe);
            mv.addObject("inputIngredients", ingredients);
        } catch (Exception e) {
            String errorMessage = URLEncoder.encode("Failed to generate recipe. Please try again.", StandardCharsets.UTF_8);
            return new ModelAndView("redirect:/ai/generate?error=" + errorMessage);
        }

        return mv;
    }
}
