package main.java.com.pantrypal.controllers;

import main.java.com.pantrypal.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Handles user registration at /register
 */
@Controller
@RequestMapping("/register")
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Displays the registration page.
     */
    @GetMapping
    public ModelAndView showRegistrationPage(@RequestParam(name = "error", required = false) String error) {
        ModelAndView mv = new ModelAndView("registration_page");
        mv.addObject("errorMessage", error);
        return mv;
    }

    /**
     * Processes registration form submission.
     */
    @PostMapping
    public String registerUser(@RequestParam("username") String username,
                                @RequestParam("password") String password,
                                @RequestParam("passwordRepeat") String passwordRepeat,
                                @RequestParam("firstName") String firstName,
                                @RequestParam("lastName") String lastName) throws UnsupportedEncodingException {

        if (password.trim().length() < 3) {
            String message = URLEncoder.encode("Passwords should have at least 3 non-empty characters.", "UTF-8");
            return "redirect:/register?error=" + message;
        }

        if (!password.equals(passwordRepeat)) {
            String message = URLEncoder.encode("Passwords do not match.", "UTF-8");
            return "redirect:/register?error=" + message;
        }

        try {
            boolean success = userService.registerUser(username, password, firstName, lastName);
            if (success) {
                return "redirect:/login";
            } else {
                String message = URLEncoder.encode("Registration failed. Username might already be taken.", "UTF-8");
                return "redirect:/register?error=" + message;
            }
        } catch (Exception e) {
            String message = URLEncoder.encode("An error occurred: " + e.getMessage(), "UTF-8");
            return "redirect:/register?error=" + message;
        }
    }
}
